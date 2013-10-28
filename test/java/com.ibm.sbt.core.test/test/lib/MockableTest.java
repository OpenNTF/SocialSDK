package lib;

import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;

public class MockableTest extends TestEndpoint {

	@Override
	protected Response testRequest(String method, Args args, Object content)
			throws ClientServicesException {
		// TODO fill in mocking strategy
		
		return super.testRequest(method, args, content);
	}

}
