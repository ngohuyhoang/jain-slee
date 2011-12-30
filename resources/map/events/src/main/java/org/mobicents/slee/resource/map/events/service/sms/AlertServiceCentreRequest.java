/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.map.events.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.slee.resource.map.events.MAPEvent;

/**
 * @author baranowb
 * 
 */
public class AlertServiceCentreRequest extends MAPEvent<MAPDialogSms> {

	private final AlertServiceCentreRequestIndication wrapped;

	/**
	 * @param mAPDialog
	 * @param alertServiCecentreRequest
	 */
	public AlertServiceCentreRequest(MAPDialogSms mAPDialog, AlertServiceCentreRequestIndication alertServiCecentreRequest) {
		super(mAPDialog);
		this.wrapped = alertServiCecentreRequest;
		
	}

	public long getInvokeId() {
		return wrapped.getInvokeId();
	}

	public ISDNAddressString getMsisdn() {
		return wrapped.getMsisdn();
	}

	public AddressString getServiceCentreAddress() {
		return wrapped.getServiceCentreAddress();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlertServiceCentreRequest [wrapped=" + wrapped + "]";
	}

}