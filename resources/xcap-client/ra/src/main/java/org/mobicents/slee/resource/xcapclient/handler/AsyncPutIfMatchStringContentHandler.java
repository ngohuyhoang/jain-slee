package org.mobicents.slee.resource.xcapclient.handler;

import java.net.URI;

import org.apache.http.Header;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;

/**
 * Handles an async put if ETag match request, using String content.
 * 
 * @author emmartins
 * 
 */
public class AsyncPutIfMatchStringContentHandler extends AbstractAsyncHandler {

	protected String mimetype;
	protected String content;
	protected String eTag;

	public AsyncPutIfMatchStringContentHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle,URI uri,
			String eTag, String mimetype, String content, Header[] additionalRequestHeaders) {
		super(ra, handle, uri, additionalRequestHeaders);
		this.mimetype = mimetype;
		this.content = content;
		this.eTag = eTag;
	}

	@Override
	protected XcapResponse doRequest() throws Exception {
		return ra.getClient().putIfMatch(uri, eTag, mimetype, content, additionalRequestHeaders);
	}

}