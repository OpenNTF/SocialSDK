/*
 * � Copyright IBM Corp. 2013
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


package com.ibm.sbt.services.client.connections.activitystreams;

public enum ASVerb {
	
	ACCEPT("ACCEPT"),
	ACCESS("ACCESS"),
	ACKNOWLEDGE("ACKNOWLEDGE"),
	ADD("ADD"),
	AGREE("AGREE"),
	APPEND("APPEND"),
	ARCHIVE("ARCHIVE"),
	AT("AT"),
	ATTACH("ATTACH"),
	ATTEND("ATTEND"),
	AUTHOR("AUTHOR"),
	AUTHORIZE("AUTHORIZE"),
	BORROW("BORROW"),
	BUILD("BUILD"),
	CANCEL("CANCEL"),
	COMMENT("COMMENT"),
	COMPLETE("COMPLETE"),
	CONFIRM("CONFIRM"),
	CONSUME("CONSUME"),
	CHECKIN("CHECKIN"),
	CREATE("CREATE"),
	DELETE("DELETE"),
	DELIVER("DELIVER"),
	DENY("DENY"),
	DISAGREE("DISAGREE"),
	DISLIKE("DISLIKE"),
	EXPERIENCE("EXPERIENCE"),
	FAVORITE("FAVORITE"),
	FIND("FIND"),
	FLAG_AS_INAPPROPRIATE("FLAG_AS_INAPPROPRIATE"),
	FOLLOW("FOLLOW"),
	GIVE("GIVE"),
	HOST("HOST"),
	IGNORE("IGNORE"),
	INSERT("INSERT"),
	INSTALL("INSTALL"),
	INTERACT("INTERACT"),
	INVITE("INVITE"),
	JOIN("JOIN"),
	LEAVE("LEAVE"),
	LIKE("LIKE"),
	LISTEN("LISTEN"),
	MAKE_FRIEND("MAKE_FRIEND"),
	OPEN("OPEN"),
	POST("POST"),
	PLAY("PLAY"),
	PRESENT("PRESENT"),
	QUALIFY("QUALIFY"),
	READ("READ"),
	RECEIVE("RECEIVE"),
	REJECT("REJECT"),
	REMOVE("REMOVE"),
	REMOVE_FRIEND("REMOVE_FRIEND"),
	REPLACE("REPLACE"),
	REQUEST_FRIEND("REQUEST_FRIEND"),
	REQUEST("REQUEST"),
	RESOLVE("RESOLVE"),
	RETURN("RETURN"),
	RETRACT("RETRACT"),
	RSVP_MAYBE("RSVP_MAYBE"),
	RSVP_NO("RSVP_NO"),
	RSVP_YES("RSVP_YES"),
	SATISFY("SATISFY"),
	SAVE("SAVE"),
	SCHEDULE("SCHEDULE"),
	SEARCH("SEARCH"),
	SELL("SELL"),
	SEND("SEND"),
	SHARE("SHARE"),
	SPONSOR("SPONSOR"),
	START("START"),
	STOP_FOLLOWING("STOP_FOLLOWING"),
	SUBMIT("SUBMIT"),
	TAG("TAG"),
	TERMINATE("TERMINATE"),
	TIE("TIE"),
	UNFAVORITE("UNFAVORITE"),
	UNLIKE("UNLIKE"),
	UNSAVE("UNSAVE"),
	UNSATISFY("UNSATISFY"),
	UNSHARE("UNSHARE"),
	UPDATE("UPDATE"),
	USE("USE"),
	WATCH("WATCH"),
	WIN("WIN");
	
	
	String verbType;
	ASVerb(String verbType){
		this.verbType = verbType;
	}
	
	/**
	 * Wrapper method to return verb type
	 * <p>
	 */
	public String getVerbType(){return verbType;}

}
