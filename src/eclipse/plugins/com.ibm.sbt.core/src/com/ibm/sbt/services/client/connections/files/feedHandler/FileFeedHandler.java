package com.ibm.sbt.services.client.connections.files.feedHandler;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.files.FileList;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.model.File;

public class FileFeedHandler implements IFeedHandler{

	private FileService service; 
	
	public FileFeedHandler() {
		// TODO Auto-generated constructor stub
	} 
	
	public FileFeedHandler(FileService service) {
		this.service = service;
	}
	
	/**
	 * @param dataHolder
	 * @return file
	 */
	@Override
    public File createEntity(Response dataHolder) {
        return createEntityFromData((Node)dataHolder.getData());
    }

	/**
	 * @param data
	 * @return file
	 */
    @Override
    public File createEntityFromData(Object data) {
    	 XmlDataHandler handler = new XmlDataHandler((Node)data, ConnectionsConstants.nameSpaceCtx);
         File file = new File(service, handler);
         return file;
    }

    /**
	 * @param dataHolder
	 * @return filelist
	 */
    @Override
    public FileList createEntityList(Response dataHolder) {
        return new FileList((Response) dataHolder, this);
    }

    /**
	 * @return service
	 */
    @Override
    public FileService getService() {
        return service;
    }
}
