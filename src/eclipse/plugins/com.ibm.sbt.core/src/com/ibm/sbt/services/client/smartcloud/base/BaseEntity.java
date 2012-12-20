/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client.smartcloud.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import com.ibm.commons.util.NotImplementedException;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.BaseService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.util.extractor.field.DataExtractor;
import com.ibm.sbt.services.util.extractor.field.JSONFieldExtractor;
import com.ibm.sbt.services.util.extractor.field.XMLFieldExtractor;
import com.ibm.sbt.services.util.navigable.NavigableObject;

/**
 * The BaseEntity encapsulates common functionality for every entity. It contains a {@link NavigableObject}
 * which holds both the data and a field {@link DataExtractor} that can get values from the known fields.
 * 
 * @see NavigableObject NavigableObject
 * @see DataExtractor DataExtractor
 * @author Carlos Manias
 */
public abstract class BaseEntity<DataFormat> {
	private static final String				sourceClass	= BaseEntity.class.getName();
	private static final Logger				logger		= Logger.getLogger(sourceClass);
	protected NavigableObject<DataFormat>	dataNavigator;
	protected Handler						dataFormat;
	protected String						uuid;
	protected BaseService					svc;
	protected String						selfLoadUrl;
	protected String						nameParameterId;

	private enum Format {
		XML, JSON;

		@SuppressWarnings("unused")
		public static Format valueOf(Handler handler) {
			if (handler == ClientService.FORMAT_XML) {
				return XML;
			}
			if (handler == ClientService.FORMAT_JSON) {
				return JSON;
			}
			throw new IllegalArgumentException("Unsupported handler " + handler);

		}

		public static <DataFormat> Format valueOf(Class<DataFormat> k) {
			if (Node.class.isAssignableFrom(k)) {
				return XML;
			}
			if (JsonObject.class.isAssignableFrom(k)) {
				return XML;
			}

			throw new IllegalArgumentException("Unsupported handler " + k);
		}
	};

	protected BaseEntity() {
	}

	protected BaseEntity(String uuid, BaseService svc) {
		this();
		this.uuid = uuid;
		this.svc = svc;
		if (svc != null) {
			this.dataFormat = svc.getDataFormat();
		}
	}

	protected BaseEntity(DataFormat data, BaseService svc) {
		this();
		this.svc = svc;
		if (svc != null) {
			this.dataFormat = svc.getDataFormat();
		}
		this.setData(data);
	}

	/**
	 * This method returns the XML mapping to navigate XML data
	 * 
	 * @return xmlFieldMapping the XML field mapping
	 */
	abstract protected String[][] getXMLFieldMapping();

	/**
	 * This method returns the JSON mapping to navigate JSON data
	 * 
	 * @return jsonFieldMapping the JSON field mapping
	 */
	abstract protected String[][] getJsonFieldMapping();

	/**
	 * @return Service
	 */
	public BaseService getSvc() {
		return svc;
	}

	/**
	 * @return id
	 */
	public String getUniqueId() {
		return this.uuid;
	}

	/**
	 * @return uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @return Data
	 */
	public NavigableObject<DataFormat> getDataNavigator() {
		return dataNavigator;
	}

	/**
	 * This method creates a new {@link NavigableObject} from the data to navigate the data.
	 * 
	 * @set data
	 */
	public void setData(DataFormat data) {
		DataExtractor<DataFormat> ext = getExtractor(data);
		this.dataNavigator = new NavigableObject<DataFormat>(data, ext);
	}

	/**
	 * Query for specified field. Field names follow IBM Connections naming convention
	 * 
	 * @param fieldName
	 * @return value
	 */
	public String get(String fieldName) {
		return dataNavigator.get(fieldName);
	}

	/**
	 * This method makes the entity retrieve and load its own data
	 * 
	 * @throws SBTServiceException
	 * @throws ClientServicesException
	 */
	public void load() throws  ClientServicesException {
		load(getSelfLoadURL(), this.uuid, this.nameParameterId);
	}

	protected String getSelfLoadURL() {
		// TODO Auto-generated method stub
		return selfLoadUrl;
	}

	/**
	 * This method makes the entity retrieve and load its own data
	 * 
	 * @param selfLoadUrl
	 * @param uuid
	 * @param nameParameterId
	 * @throws ClientServicesException
	 */
	public void load(String selfLoadUrl, String uuid, String nameParameterId) throws
			ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "load");
		}
		Map<String, String> parameters = new HashMap<String, String>();
		if (nameParameterId != null) {
			parameters.put(nameParameterId, uuid);
		}

		@SuppressWarnings("unchecked")
		DataFormat data;

		data = (DataFormat) svc.retrieveData(selfLoadUrl, parameters, nameParameterId);

		DataExtractor<DataFormat> dataExtractor = getExtractor(data);
		List<DataFormat> entries = dataExtractor.getEntitiesFromServiceResult(data);
		if (entries.size() == 0) {
			throw new RuntimeException("No entries loaded");
		}
		setData(entries.get(0));
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "load");
		}
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append(this.getClass().getName()).append("{uuid=").append(this.getUuid()).append("}");
		return res.toString();
	}

	protected String debugInformation() {
		StringBuilder res = new StringBuilder();
		res.append(this.getClass().getName()).append("{");
		boolean first = true;
		for (String field : getDataNavigator().getAllFields()) {
			if (first) {
				first = false;
			} else {
				res.append(",").append('\n');// not useful here to use the line.separator
			}
			res.append(field).append("=").append(getDataNavigator().get(field));
		}
		res.append("}");
		return res.toString();
	}

	public boolean isEmpty() {
		for (String field : getDataNavigator().getAllFields()) {
			String value = getDataNavigator().get(field);
			if (StringUtil.isEmpty(value)) {
				continue;
			}
			return false;
		}
		return true;
	}

	/**
	 * This method allows to override static extractors if needed. works like a visitor, but has a default
	 * implementation
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataExtractor<DataFormat> getExtractor(DataFormat data) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getExtractor");
		}
		Format f = Format.valueOf(data.getClass());
		switch (f) {
			case XML:
				return (DataExtractor<DataFormat>) getXMLFieldExtractor();
			case JSON:
				return (DataExtractor<DataFormat>) getJSONFieldExtractor();
			default:
				throw new IllegalArgumentException("Unsupported handler " + f);
		}
	}

	private XMLFieldExtractor getXMLFieldExtractor() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getXMLFieldExtractor");
		}
		Map<String, XPathExpression> map = new HashMap<String, XPathExpression>();
		String[][] pairs = getXMLFieldMapping();
		for (String[] pair : pairs) {
			XPathExpression expression;
			try {
				expression = DOMUtil.createXPath(pair[1]);
				map.put(pair[0], expression);
			} catch (XMLException e) {
				logger.throwing(sourceClass, "getXMLFieldExtractor", e);
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getXMLFieldExtractor");
		}
		return new XMLFieldExtractor(map);
	}

	private JSONFieldExtractor getJSONFieldExtractor() {
		throw new NotImplementedException();
	}

	protected Collection<DataFormat> getEntities(String field) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getEntities");
		}
		return getDataNavigator().getEntities(field);
	}

	protected Collection<DataFormat> getEntities() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getEntities");
		}
		return getDataNavigator().getEntities();
	}

	/**
	 * This method returns a default xml field extractor which can be used to extract nodes from an xml feed
	 * 
	 * @return xmlFieldExtractor The default XML Field Extractor
	 */
	public static XMLFieldExtractor getDefaultXMLExtractor() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getDefaultXMLExtractor");
		}
		Map<String, XPathExpression> map = new HashMap<String, XPathExpression>();
		return new XMLFieldExtractor(map);
	}

	/**
	 * This method returns a default JSON field extractor which can be used to extract nodes from an json
	 * object
	 * 
	 * @return xmlFieldExtractor The default JSON Field Extractor
	 */
	public static <DataFormat> DataExtractor<DataFormat> getDefaultJsonExtractor() {
		throw new NotImplementedException();
	}

	/**
	 * This method returns a default field extractor which can be used to extract nodes of the given data type
	 * 
	 * @param data
	 *            the raw data
	 * @return dataExtractor the data extractor
	 */
	@SuppressWarnings("unchecked")
	public static <DataFormat> DataExtractor<DataFormat> getNodeExtractorFromData(DataFormat data) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getNodeExtractorFromData");
		}
		Format format = Format.valueOf(data.getClass());
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getNodeExtractorFromData");
		}
		switch (format) {
			case XML:
				return (DataExtractor<DataFormat>) getDefaultXMLExtractor();
			case JSON:
				return getDefaultJsonExtractor();
			default:
				throw new IllegalArgumentException("Unsupported handler " + format);
		}
	}

}
