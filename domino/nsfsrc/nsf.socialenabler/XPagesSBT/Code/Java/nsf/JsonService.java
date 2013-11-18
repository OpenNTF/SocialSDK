package nsf;

import java.io.IOException;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;

public class JsonService {
	
	public static String toJson(Object o){
		String json=null;
		try {
			json = JsonGenerator.toJson(JsonJavaFactory.instanceEx, o);
		} catch (JsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
