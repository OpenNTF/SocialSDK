package com.ibm.sbt.util;

import java.io.StringWriter;

import org.w3c.dom.Node;

import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

public class XMLEntityInspector {
	
	public static String entityToString(BaseEntity entity){
		String output = "";
		if (entity==null) return output;
		DataHandler<?> dataHandler = entity.getDataHandler();
		if (dataHandler!=null) {
			if (dataHandler instanceof XmlDataHandler) {
				XmlDataHandler handler = (XmlDataHandler)dataHandler;
					output = xmlToString(handler.getData());
			}
		}
		return output;
	}

	public static String xmlToString(Node node){
		String output = "";
		StringWriter writer = new StringWriter();
		try {
			DOMUtil.serialize(writer, node, false, true);
			output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
}
