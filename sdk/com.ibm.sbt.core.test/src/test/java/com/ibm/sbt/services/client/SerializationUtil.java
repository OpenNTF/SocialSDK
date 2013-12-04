package com.ibm.sbt.services.client;

import java.io.*;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.connections.profiles.Profile;

public abstract class SerializationUtil {
	
	protected String serFile = "serFile.out";
	
	public void isSerializable(BaseEntity basentity){
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile)); 
			oos.writeObject(basentity);
			oos.close();
			validateSerializable();
		} catch (Exception e) { }
	}
	
	public abstract void validateSerializable();

}
