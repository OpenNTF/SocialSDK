package lib;

import org.junit.After;
import org.junit.Before;

import com.ibm.commons.runtime.Context;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * this class bridges the endpoint as required from the API with a more convenient method to handle the
 * operations as required by the test case
 * 
 * @author Lorenzo Boccaccia
 * @date Nov 22, 2012
 */
public abstract class TestEndpoint extends BasicEndpoint {

	TestRuntimeFactory	runtime;
	TestContext			context;
	private Endpoint ep;

	@Before
	public void setUpEndpoint() {
		// this enables the test to sidestep the default Endpoint initialization and to use local resources
		runtime = new TestRuntimeFactory();

		// this loads the context and inject our own test endpoint to load load
		// resources used within the test and to perform assreion over API usage
		context = (TestContext) Context.init(runtime.getApplicationUnchecked(), null, null);

		
		//currently only forward to connections
		this.ep = (Endpoint)context.getBean("connections");
		
		// the test class injected into the context is our test server and will serve and check
		// all the API calls performed within this test class and this only
		context.addBean("connections", this);
		context.addBean("smartcloud", this);
		setUrl(this.getClass().getResource(".").toString());
		
	}

	@After
	public void tearDownEndpoint() {
		runtime.restore();
	}

	public TestEndpoint() {

	}

	public TestEndpoint(String base) {

		setUrl(base);
	}

	@Override
	public ClientService getClientService() throws ClientServicesException {
		final TestEndpoint testEndpoint = this;
		return new ClientService() {
			@Override
			public Response xhr(String method, Args args, Object content) throws ClientServicesException {
				Response ret = testEndpoint.testRequest(method, args, content);
				if (ret == null) {
					throw new ClientServicesException(new IllegalArgumentException(
							"Testing service only, please implement " + method + " at "
									+ args.getServiceUrl() + " for " + args.getParameters() + " for "
									+ content + " with " + args.getHeaders()));
				}
				return ret;
			}
		};
	}

	protected Response testRequest(String method, Args args, Object content) throws ClientServicesException {
		
		return ep.xhr(method, args, content);
		
	}
}
