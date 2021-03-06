package org.telestax.slee.container.build.as7.service;

import java.util.LinkedList;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.slee.management.AlarmMBean;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.TraceMBean;
import javax.transaction.TransactionManager;

import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.RuntimeConfig;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.mobicents.cache.MobicentsCache;
import org.mobicents.cluster.DefaultMobicentsCluster;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.slee.connector.local.MobicentsSleeConnectionFactory;
import org.mobicents.slee.connector.local.SleeConnectionService;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.component.management.jmx.PolicyMBeanImpl;
import org.mobicents.slee.container.component.management.jmx.PolicyMBeanImplMBean;
import org.mobicents.slee.container.congestion.CongestionControl;
import org.mobicents.slee.container.congestion.CongestionControlImpl;
import org.mobicents.slee.container.deployment.ExternalDeployer;
import org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBeanImplMBean;
import org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBeanImpl;
import org.mobicents.slee.container.deployment.jboss.SleeContainerDeployerImpl;
import org.mobicents.slee.container.event.DefaultEventContextFactoryDataSource;
import org.mobicents.slee.container.event.EventContextFactory;
import org.mobicents.slee.container.event.EventContextFactoryDataSource;
import org.mobicents.slee.container.event.EventContextFactoryImpl;
import org.mobicents.slee.container.eventrouter.EventRouter;
import org.mobicents.slee.container.facilities.ActivityContextNamingFacility;
import org.mobicents.slee.container.facilities.TimerFacility;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityContextInterfaceFactory;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityFactory;
import org.mobicents.slee.container.management.ComponentManagement;
import org.mobicents.slee.container.management.ProfileManagement;
import org.mobicents.slee.container.management.ResourceManagementImpl;
import org.mobicents.slee.container.management.SbbManagement;
import org.mobicents.slee.container.management.SbbManagementImpl;
import org.mobicents.slee.container.management.ServiceManagementImpl;
import org.mobicents.slee.container.management.UsageParametersManagement;
import org.mobicents.slee.container.management.UsageParametersManagementImpl;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.container.management.jmx.CongestionControlConfiguration;
import org.mobicents.slee.container.management.jmx.CongestionControlConfigurationMBean;
import org.mobicents.slee.container.management.jmx.DeploymentMBeanImpl;
import org.mobicents.slee.container.management.jmx.EventContextFactoryConfiguration;
import org.mobicents.slee.container.management.jmx.EventContextFactoryConfigurationMBean;
import org.mobicents.slee.container.management.jmx.EventRouterConfiguration;
import org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean;
import org.mobicents.slee.container.management.jmx.EventRouterStatistics;
import org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.management.jmx.MobicentsManagementMBean;
import org.mobicents.slee.container.management.jmx.ResourceManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.SbbEntitiesMBeanImpl;
import org.mobicents.slee.container.management.jmx.SbbEntitiesMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.ServiceManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImpl;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImplMBean;
import org.mobicents.slee.container.management.jmx.TimerFacilityConfiguration;
import org.mobicents.slee.container.management.jmx.TimerFacilityConfigurationMBean;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.rmi.RmiServerInterface;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityManagementConfiguration;
import org.mobicents.slee.runtime.eventrouter.EventRouterImpl;
import org.mobicents.slee.runtime.eventrouter.mapping.ActivityHashingEventRouterExecutorMapper;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactoryImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManagerImpl;
import org.telestax.slee.container.build.as7.deployment.ExternalDeployerImpl;
import org.telestax.slee.container.build.as7.naming.JndiManagementImpl;

public class SleeContainerService implements Service<SleeContainer> {

	Logger log = Logger.getLogger(SleeContainerService.class);

	// TODO obtain real path through
	// org.jboss.as.controller.services.path.PathManager (see WebServerService)
	// or expression resolve ?
	private static final String TEMP_DIR = "jboss.server.temp.dir";

	private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
	private final InjectedValue<TransactionManager> transactionManager = new InjectedValue<TransactionManager>();
	private final LinkedList<String> registeredMBeans = new LinkedList<String>();
	
	private SleeContainer sleeContainer;

	@Override
	public SleeContainer getValue() throws IllegalStateException,
			IllegalArgumentException {
		return sleeContainer;
	}

	@Override
	public void start(StartContext context) throws StartException {
		log.info("Starting SLEE Container service");

		final String deployPath = System.getProperty(TEMP_DIR) + "/slee";

		// inits the SLEE cache and cluster
		final MobicentsCache cache = initCache();
		final MobicentsCluster cluster = new DefaultMobicentsCluster(cache,
				getTransactionManager().getValue(), null);

		// init the tx manager
		final SleeTransactionManager sleeTransactionManager = new SleeTransactionManagerImpl(
				getTransactionManager().getValue());

		final TraceMBeanImpl traceMBean = new TraceMBeanImpl();
		
		final AlarmMBeanImpl alarmMBean = new AlarmMBeanImpl(traceMBean);		
		
		final MobicentsManagement mobicentsManagement = new MobicentsManagement();
		mobicentsManagement.setEntitiesRemovalDelay(1);

		final ComponentManagement componentManagement = new ComponentManagementImpl();

		final SbbManagement sbbManagement = new SbbManagementImpl();

		final ServiceManagementImpl serviceManagement = new ServiceManagementImpl();

		final ResourceManagementImpl resourceManagement = ResourceManagementImpl
				.getInstance();

		// TODO profile management and its config
		final ProfileManagement profileManagement = null;

		final EventRouterConfiguration eventRouterConfiguration = new EventRouterConfiguration();
		eventRouterConfiguration.setEventRouterThreads(8);
		eventRouterConfiguration.setCollectStats(true);
		eventRouterConfiguration.setConfirmSbbEntityAttachement(true);
		try {
			eventRouterConfiguration
					.setExecutorMapperClassName(ActivityHashingEventRouterExecutorMapper.class
							.getName());
		} catch (ClassNotFoundException e) {
			throw new StartException(e);
		}
		final EventRouter eventRouter = new EventRouterImpl(eventRouterConfiguration);
		
		final TimerFacilityConfiguration timerFacilityConfiguration = new TimerFacilityConfiguration();
		timerFacilityConfiguration.setTimerThreads(4);
		timerFacilityConfiguration.setPurgePeriod(0);
		timerFacilityConfiguration
				.setTaskExecutionWaitsForTxCommitConfirmation(true);
		final TimerFacility timerFacility = new TimerFacilityImpl(
				timerFacilityConfiguration);

		final ActivityManagementConfiguration activityManagementConfiguration = new ActivityManagementConfiguration();
		activityManagementConfiguration.setTimeBetweenLivenessQueries(60);
		activityManagementConfiguration.setMaxTimeIdle(60);
		activityManagementConfiguration.setMinTimeBetweenUpdates(15);
		final ActivityContextFactory activityContextFactory = new ActivityContextFactoryImpl(
				activityManagementConfiguration);

		final NullActivityContextInterfaceFactory nullActivityContextInterfaceFactory = new NullActivityContextInterfaceFactoryImpl();
		final NullActivityFactory nullActivityFactory = new NullActivityFactoryImpl();

		final ActivityContextNamingFacility activityContextNamingFacility = new ActivityContextNamingFacilityImpl();

		// TODO SLEE Connection Factory + RMI stuff
		final SleeConnectionService sleeConnectionService = null;
		final MobicentsSleeConnectionFactory sleeConnectionFactory = null;
		final RmiServerInterface rmiServerInterface = null;

		final UsageParametersManagement usageParametersManagement = new UsageParametersManagementImpl();

		final SbbEntityFactory sbbEntityFactory = new SbbEntityFactoryImpl();

		final EventContextFactoryDataSource eventContextFactoryDataSource = new DefaultEventContextFactoryDataSource();
		final EventContextFactoryConfiguration eventContextFactoryConfiguration = new EventContextFactoryConfiguration();
		eventContextFactoryConfiguration
				.setDefaultEventContextSuspensionTimeout(10000);
		final EventContextFactory eventContextFactory = new EventContextFactoryImpl(
				eventContextFactoryDataSource, eventContextFactoryConfiguration);

		final CongestionControlConfiguration congestionControlConfiguration = new CongestionControlConfiguration();
		congestionControlConfiguration.setPeriodBetweenChecks(0);
		congestionControlConfiguration.setMinFreeMemoryToTurnOn(10);
		congestionControlConfiguration.setMinFreeMemoryToTurnOff(20);
		congestionControlConfiguration.setRefuseStartActivity(true);
		congestionControlConfiguration.setRefuseFireEvent(false);
		final CongestionControl congestionControl = new CongestionControlImpl(
				congestionControlConfiguration);

		// FIXME deployer stuff
		final ExternalDeployer externalDeployer = new ExternalDeployerImpl();
		final SleeContainerDeployerImpl internalDeployer = new SleeContainerDeployerImpl();
		internalDeployer.setExternalDeployer(externalDeployer);

		// FIXME this needs further work on dependencies
		// final PolicyMBeanImpl policyMBeanImpl = new PolicyMBeanImpl();
		// policyMBeanImpl.setUseMPolicy(true);
		
		try {
			sleeContainer = new SleeContainer(deployPath, getMbeanServer()
					.getValue(), componentManagement, sbbManagement,
					serviceManagement, resourceManagement, profileManagement,
					eventContextFactory, eventRouter, timerFacility,
					activityContextFactory, activityContextNamingFacility,
					nullActivityContextInterfaceFactory, nullActivityFactory,
					rmiServerInterface, sleeTransactionManager, cluster,
					alarmMBean, traceMBean, usageParametersManagement,
					sbbEntityFactory, congestionControl, sleeConnectionService,
					sleeConnectionFactory, internalDeployer);
		} catch (Throwable e) {
			throw new StartException(e);
		}

		// set AS7+ Jndi Management 
		sleeContainer.setJndiManagement(new JndiManagementImpl());
		
		// register mbeans
		registerMBean(traceMBean, TraceMBean.OBJECT_NAME);
		registerMBean(alarmMBean, AlarmMBean.OBJECT_NAME);
		registerMBean(mobicentsManagement, MobicentsManagementMBean.OBJECT_NAME);
		registerMBean(eventRouterConfiguration, EventRouterConfigurationMBean.OBJECT_NAME);		
		registerMBean(new EventRouterStatistics(eventRouter), EventRouterStatisticsMBean.OBJECT_NAME);
		registerMBean(timerFacilityConfiguration, TimerFacilityConfigurationMBean.OBJECT_NAME);
		registerMBean(eventContextFactoryConfiguration, EventContextFactoryConfigurationMBean.OBJECT_NAME);
		registerMBean(congestionControlConfiguration, CongestionControlConfigurationMBean.OBJECT_NAME);
		registerMBean(new DeploymentManagerMBeanImpl(internalDeployer), DeploymentManagerMBeanImplMBean.OBJECT_NAME);
		registerMBean(new DeploymentMBeanImpl(internalDeployer), DeploymentMBean.OBJECT_NAME);		
		registerMBean(new ServiceManagementMBeanImpl(serviceManagement), ServiceManagementMBean.OBJECT_NAME);		
		// TODO ProfileProvisioningMBeanImpl
		registerMBean(new ResourceManagementMBeanImpl(resourceManagement), ResourceManagementMBean.OBJECT_NAME);
		registerMBean(new SbbEntitiesMBeanImpl(sbbEntityFactory), SbbEntitiesMBeanImplMBean.OBJECT_NAME);
		registerMBean(new ActivityManagementMBeanImpl(sleeContainer), ActivityManagementMBeanImplMBean.OBJECT_NAME);
		//registerMBean(policyMBeanImpl, PolicyMBeanImplMBean.OBJECT_NAME);
		
		// slee management mbean
		registerMBean(new SleeManagementMBeanImpl(sleeContainer), SleeManagementMBeanImplMBean.OBJECT_NAME);

	}

	private MobicentsCache initCache() {
		RuntimeConfig runtimeConfig = new RuntimeConfig();
		runtimeConfig.setTransactionManager(getTransactionManager().getValue());
		Configuration configuration = new Configuration();
		configuration.setRuntimeConfig(runtimeConfig);
		configuration.setCacheMode("LOCAL");
		configuration.setLockAcquisitionTimeout(3000);
		configuration.setUseLockStriping(false);
		configuration.setExposeManagementStatistics(false);
		configuration.setShutdownHookBehavior("DONT_REGISTER");
		return new MobicentsCache(configuration);
	}	
	
	@Override
	public void stop(StopContext context) {
		// shutdown the SLEE
		while(!registeredMBeans.isEmpty()) {
			unregisterMBean(registeredMBeans.pop());
		}		
		sleeContainer = null;
	}

	private void registerMBean(Object mBean, String name) throws StartException {
		try {
			getMbeanServer().getValue().registerMBean(mBean, new ObjectName(name));
		} catch (Throwable e) {
			throw new StartException(e);
		}
		registeredMBeans.push(name);
	}
	
	private void unregisterMBean(String name) {
		try {
			getMbeanServer().getValue().unregisterMBean(new ObjectName(name));
		} catch (Throwable e) {
			log.error("failed to unregister mbean", e);
		}		
	}
	
	public InjectedValue<MBeanServer> getMbeanServer() {
		return mbeanServer;
	}

	public InjectedValue<TransactionManager> getTransactionManager() {
		return transactionManager;
	}
}
