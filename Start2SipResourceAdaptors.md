# How to start 2 SIP resource adaptors #

## Introduction ##
When more then 1 network interface is connected to your server and you want to
`SIP` over multiple interfaces, you need to start a Sip Resource Adaptor for every interface connected.

Here's a recipe on how to do this.

For a resource adaptor, you need to drop a deployable unit in the deploy directory of the Jboss application server
(`$JBOSS_HOME/server/<servertype>/deploy/<anyDU>.jar`)

By default, the jar's contents is something like:
```
- META-INF
  - deployable-unit.xml
  - deploy-config.xml
- jars
  - sip11-ra.jar
  - ...
```

The default deployable unit (`sip11-ra-DU-<version>.jar`) also contains jars with supporting code like
SLEE events, the actual SIP stack implementation etc.

The address to bind to is configured in `resource-adaptor-jar.xml` (within `sip11-ra.jar`) with
the definition  of the `config-property`: `javax.sip.IP_ADDRESS`.

&lt;BR&gt;


The file `deploy-config.xml` for the default reads something like:
```
<deploy-config>
    <ra-entity
        resource-adaptor-id="ResourceAdaptorID[name=JainSipResourceAdaptor,
        vendor=net.java.slee.sip,version=1.2]" entity-name="SipRA">
        <ra-link name="SipRA" />
    </ra-entity>
</deploy-config>
```

## Deploying a second adaptor ##
For deployment of a second RA "**`SipSecondRA`**" with address "**`second.address.net`**" the following changes should be made within the jar:

For `deploy-config.xml` add an `ra-entity` definition:
```
<deploy-config>
  <ra-entity
        resource-adaptor-id="ResourceAdaptorID[name=JainSipSecondResourceAdaptor,
        vendor=net.java.slee.sip,version=1.2]" entity-name="SipRA">
    <properties>
      <property name="javax.sip.IP_ADDRESS" type="java.lang.String"
                value="first.address.net" />
    </properties>
    <ra-link name="SipRA" />
  </ra-entity>

  <ra-entity
        resource-adaptor-id="ResourceAdaptorID[name=JainSipSecondResourceAdaptor,
        vendor=net.java.slee.sip,version=1.2]" entity-name="SipSecondRA">
    <properties>
      <property name="javax.sip.IP_ADDRESS" type="java.lang.String"
                value="second.address.net" />
    </properties>
    <ra-link name="SipSecondRA" />
  </ra-entity>
</deploy-config>
```

From `resource-adaptor-jar.xml` (in `sip11-ra.jar`) remove the property that sets the IP address.

## Using the second adaptor ##
Within an `sbb`, you can now use the second resource adaptor by binding to it within the `sbb-jar.xml`:

```
<sbb-jar>
  <sbb>

  ...

    <resource-adaptor-type-binding>

    ...

      <resource-adaptor-entity-binding>
        <resource-adaptor-object-name>
          slee/resources/jainsip/1.2/secondprovider
        </resource-adaptor-object-name>
        <resource-adaptor-entity-link>SipSecondRA</resource-adaptor-entity-link>
      </resource-adaptor-entity-binding>

      ...
```

and getting the SIP provider (in `setSbbContext()`) with:
```
     sipProvider = (SleeSipProvider) new InitialContext().lookup(
         "java:comp/env/slee/resources/jainsip/1.2/secondprovider");
```