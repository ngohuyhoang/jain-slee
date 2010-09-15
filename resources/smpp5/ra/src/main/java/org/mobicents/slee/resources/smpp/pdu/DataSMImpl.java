package org.mobicents.slee.resources.smpp.pdu;

import net.java.slee.resources.smpp.pdu.Address;
import net.java.slee.resources.smpp.pdu.DataSM;
import net.java.slee.resources.smpp.pdu.SmppResponse;
import net.java.slee.resources.smpp.pdu.Tag;

import org.mobicents.protocols.smpp.message.SMPPPacket;
import org.mobicents.slee.resources.smpp.ExtSmppRequest;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DataSMImpl extends PDUImpl implements DataSM, ExtSmppRequest {
	
	public DataSMImpl(org.mobicents.protocols.smpp.message.DataSM dataSm){
		this.smppPacket = dataSm;
	}

	public DataSMImpl(long sequenceNumber) {
		super();
		this.smppPacket = new org.mobicents.protocols.smpp.message.DataSM();
		this.smppPacket.setSequenceNum(sequenceNumber);
	}

	public int getDataCoding() {
		return ((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).getDataCoding();
	}

	public Address getDestAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket)
				.getDestination());
	}

	public int getEsmClass() {
		return ((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).getEsmClass();
	}

	public int getRegisteredDelivery() {
		return ((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).getRegistered();
	}

	public String getServiceType() {
		return ((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).getServiceType();
	}

	public Address getSourceAddress() {
		return this.convertProtoAddress(((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).getSource());
	}

	public void setDataCoding(int dataCoding) {
		((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).setDataCoding(dataCoding);
	}

	public void setDestAddress(Address address) {
		((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).setDestination(((AddressImpl) address)
				.getProtoAddress());
	}

	public void setEsmClass(int esmClass) {
		((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).setEsmClass(esmClass);
	}

	public void setRegisteredDelivery(int registeredDelivery) {
		((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).setRegistered(registeredDelivery);
	}

	public void setServiceType(String serviceType) {
		((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).setServiceType(serviceType);
	}

	public void setSourceAddress(Address address) {
		((org.mobicents.protocols.smpp.message.DataSM) this.smppPacket).setSource(((AddressImpl) address)
				.getProtoAddress());
	}

	public SmppResponse createSmppResponseEvent(int status) {
		DataSMRespImpl dataSMRespImpl = new DataSMRespImpl(status);
		dataSMRespImpl.setSequenceNum(this.getSequenceNum());
		return dataSMRespImpl;
	}

	public boolean isTLVPermitted(Tag tag) {
		return (tag.equals(Tag.ALERT_ON_MESSAGE_DELIVERY) || tag.equals(Tag.BILLING_IDENTIFICATION)
				|| tag.equals(Tag.CALLBACK_NUM) || tag.equals(Tag.CALLBACK_NUM_ATAG)
				|| tag.equals(Tag.CALLBACK_NUM_PRES_IND) || tag.equals(Tag.DEST_ADDR_NP_COUNTRY)
				|| tag.equals(Tag.DEST_ADDR_NP_INFORMATION) || tag.equals(Tag.DEST_ADDR_NP_RESOLUTION)
				|| tag.equals(Tag.DEST_ADDR_SUBUNIT) || tag.equals(Tag.DEST_BEARER_TYPE)
				|| tag.equals(Tag.DEST_NETWORK_ID) || tag.equals(Tag.DEST_NETWORK_TYPE) || tag.equals(Tag.DEST_NODE_ID)
				|| tag.equals(Tag.DEST_SUBADDRESS) || tag.equals(Tag.DEST_TELEMATICS_ID) || tag.equals(Tag.DEST_PORT)
				|| tag.equals(Tag.DISPLAY_TIME) || tag.equals(Tag.ITS_REPLY_TYPE) || tag.equals(Tag.ITS_SESSION_INFO)
				|| tag.equals(Tag.LANGUAGE_INDICATOR) || tag.equals(Tag.MESSAGE_PAYLOAD)
				|| tag.equals(Tag.MORE_MESSAGES_TO_SEND) || tag.equals(Tag.MS_MSG_WAIT_FACILITIES)
				|| tag.equals(Tag.MS_VALIDITY) || tag.equals(Tag.NUMBER_OF_MESSAGES) || tag.equals(Tag.PAYLOAD_TYPE)
				|| tag.equals(Tag.PRIVACY_INDICATOR) || tag.equals(Tag.QOS_TIME_TO_LIVE)
				|| tag.equals(Tag.SAR_MSG_REF_NUM) || tag.equals(Tag.SAR_SEGMENT_SEQNUM)
				|| tag.equals(Tag.SAR_TOTAL_SEGMENTS) || tag.equals(Tag.SET_DPF) || tag.equals(Tag.SMS_SIGNAL)
				|| tag.equals(Tag.SOURCE_ADDR_SUBUNIT) || tag.equals(Tag.SOURCE_BEARER_TYPE)
				|| tag.equals(Tag.SOURCE_NETWORK_ID) || tag.equals(Tag.SOURCE_NETWORK_TYPE)
				|| tag.equals(Tag.SOURCE_NODE_ID) || tag.equals(Tag.SOURCE_PORT) || tag.equals(Tag.SOURCE_SUBADDRESS)
				|| tag.equals(Tag.SOURCE_TELEMATICS_ID) || tag.equals(Tag.USER_MESSAGE_REFERENCE)
				|| tag.equals(Tag.USER_RESPONSE_CODE) || tag.equals(Tag.USSD_SERVICE_OP));
	}

	public SMPPPacket getSMPPPacket() {
		return this.smppPacket;
	}

}
