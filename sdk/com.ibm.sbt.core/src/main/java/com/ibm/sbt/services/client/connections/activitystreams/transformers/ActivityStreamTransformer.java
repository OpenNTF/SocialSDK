package com.ibm.sbt.services.client.connections.activitystreams.transformers;

import java.util.Map;

import com.ibm.commons.util.StringUtil;
/*
 * © Copyright IBM Corp. 2013
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
import com.ibm.sbt.services.client.base.transformers.AbstractBaseTransformer;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.activitystreams.ASDataPopulator;

/**
 * Manish Kataria 
 */
public class ActivityStreamTransformer extends AbstractBaseTransformer {

	private final String SOURCE_PATH = "/com/ibm/sbt/services/client/connections/activitystreams/templates/";
	private final String ACTIVITYSTREAM = "as";
	private final String ACTOR = "actor";
	private final String GENERATOR = "generator";
	private final String OBJECT = "object";
	private final String PROVIDER = "provider";
	private final String TARGET = "targetTmpl";
	private final String TO = "to";
	private final String IMAGE = "image";
	private final String CONNSTATE = "connState";
	public  final String DATA_POPULATOR = "dataPopulator";
	
	@Override
	public String transform(Map<String, Object> fieldmap)
			throws TransformerException {
		
		String	asTemplate = getTemplateContent(SOURCE_PATH+ACTIVITYSTREAM);
		String finalPayload = asTemplate;
		ASDataPopulator populator = (ASDataPopulator)fieldmap.get("dataPopulator");
		
		
		// target gets appended if targetid is present
		if(StringUtil.isNotEmpty(populator.getTargetId())){
			String targetTemplate = getTemplateContent(SOURCE_PATH+TARGET);
			String targetPayload = getXMLRep(targetTemplate, "targetId", populator.getTargetId());
			if(StringUtil.isNotEmpty(populator.getTargetConnectionState())){
				String targetconnectionstate = getTemplateContent(SOURCE_PATH+CONNSTATE);
				targetconnectionstate =	getXMLRep(targetconnectionstate, "connState", populator.getTargetConnectionState());
				targetPayload = getXMLRep(targetPayload, "connState", targetconnectionstate);
			}
			targetPayload = getPayload(targetPayload, "targetType", populator.getTargetType());
			targetPayload = getPayload(targetPayload, "targetName", populator.getTargetName());
			targetPayload = getPayload(targetPayload, "targetUrl", populator.getTargetUrl());
			targetPayload = getPayload(targetPayload, "targetSummary", populator.getTargetSummary());
			
			finalPayload = getPayload(finalPayload, "target", targetPayload);
			
		}
		
		// provider gets appended if providerid is present
		
		if(StringUtil.isNotEmpty(populator.getProviderId())){
			String providerTemplate = getTemplateContent(SOURCE_PATH+PROVIDER);
			String providerPayload = getXMLRep(providerTemplate, "providerId", populator.getProviderId());
			providerPayload = getPayload(providerPayload, "providerDisplayName", populator.getProviderDisplayName());
			providerPayload = getPayload(providerPayload, "providerUrl", populator.getProviderUrl());
			finalPayload = getPayload(finalPayload, "provider", providerPayload);
		}
		
		
		// target gets appended if generatorId is present
		if(StringUtil.isNotEmpty(populator.getGeneratorId())){
			String generatorTemplate = getTemplateContent(SOURCE_PATH+GENERATOR);
			String generatorPayload = getXMLRep(generatorTemplate, "generatorId", populator.getGeneratorId());
			if(StringUtil.isNotEmpty(populator.getGeneratorImageUrl())){
				String generatorImgContent = getTemplateContent(SOURCE_PATH+IMAGE);
				generatorImgContent =	getXMLRep(generatorImgContent, "imageUrl", populator.getGeneratorImageUrl());
				generatorPayload = getXMLRep(generatorPayload, "imageUrl", generatorImgContent);
			}
			generatorPayload = getPayload(generatorPayload, "generatorName", populator.getGeneratorName());
			generatorPayload = getPayload(generatorPayload, "generatorUrl", populator.getGeneratorUrl());
			
			finalPayload = getPayload(finalPayload, "generator", generatorPayload);
		}
		
		// actor gets appended if actorId is present
		if(StringUtil.isNotEmpty(populator.getActorId())){
			String actorTemplate = getTemplateContent(SOURCE_PATH+ACTOR);
			String actorPayload = getXMLRep(actorTemplate, "actorId", populator.getActorId());
			
			if(StringUtil.isNotEmpty(populator.getActorConnectionsState())){
				String actorconnectionstate = getTemplateContent(SOURCE_PATH+CONNSTATE);
				actorconnectionstate =	getXMLRep(actorconnectionstate, "connState", populator.getActorConnectionsState());
				actorPayload = getXMLRep(actorPayload, "actorConnectionsState", actorconnectionstate);
			}
			
			actorPayload = getPayload(actorPayload, "actorObjectType", populator.getActorObjectType());
			actorPayload = getPayload(actorPayload, "actorName", populator.getActorName());
			
			finalPayload = getPayload(finalPayload, "actor", actorPayload);
		}
		
		
		// object gets appended if actorId is present
		if(StringUtil.isNotEmpty(populator.getObjectId())){
			String objectTemplate = getTemplateContent(SOURCE_PATH+OBJECT);
			String objectPayload = getXMLRep(objectTemplate, "objectId", populator.getObjectId());
			objectPayload = getPayload(objectPayload, "objectSummary", populator.getObjectSummary());
			objectPayload = getPayload(objectPayload, "objectType", populator.getObjectType());
			objectPayload = getPayload(objectPayload, "objectDisplayName", populator.getObjectDisplayName());
			objectPayload = getPayload(objectPayload, "objectPublished", populator.getObjectPublished());
			objectPayload = getPayload(objectPayload, "objectUrl", populator.getObjectUrl());
			finalPayload = getPayload(finalPayload, "object", objectPayload);
		}
		
		// object gets appended if toId is present
		if(StringUtil.isNotEmpty(populator.getToId())){
			String toTemplate = getTemplateContent(SOURCE_PATH+TO);
			String toPayload = getXMLRep(toTemplate, "toId", populator.getToId());
			toPayload = getPayload(toPayload, "toObjectType", populator.getToObjectType());
			
			finalPayload = getPayload(finalPayload, "to", toPayload);
		}
		
		// independent properties
		finalPayload = getPayload(finalPayload,   "verb", populator.getVerb());
		finalPayload = getPayload(finalPayload, "title",populator.getTitle());
		finalPayload = getPayload(finalPayload, "content", populator.getContent());
		finalPayload = getPayload(finalPayload, "updated", populator.getUpdated());
		finalPayload = getPayload(finalPayload, "published", populator.getPublished());
		finalPayload = getPayload(finalPayload, "url", populator.getUrl());
		finalPayload = getPayload(finalPayload, "id", populator.getId());
		
		
		
		finalPayload = removeExtraPlaceholders(finalPayload);
		
		return finalPayload;
	}
	
	
	public String getPayload(String template, String placeholder, String value){
		if(StringUtil.isEmpty(value)){
			return template;
		}
		return (getXMLRep(template, placeholder, value));
	}

}
