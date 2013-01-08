package lib;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;

public class TestRuntimeFactory extends RuntimeFactory {

	private TestContext testingContext;
	private TestApplication app;
	RuntimeFactory prev;

	public TestRuntimeFactory() {
		super();
		prev = get();
		set(this);
		app = new TestApplication();
	}

	@Override
	public TestApplication getApplicationUnchecked() {
		return app;
	}

	@Override
	public Application initApplication(Object servletContext) {
		return app;
	}

	@Override
	public void destroyApplication(Application application) {
		app.cleanServices();
		app = new TestApplication();
		testingContext = new TestContext(app);

	}

	@Override
	public Application createApplication(Object context) {
		return testingContext.getApplication();
	}

	@Override
	public TestContext getContextUnchecked() {
		// TODO Auto-generated method stub

		return testingContext;
	}

	@Override
	public synchronized Context initContext(Application application,
			Object request, Object response) {
		if (testingContext == null) {
			testingContext = new TestContext(app);
		}
		testingContext.setRequest((HttpServletRequest) request);
		testingContext.setResponse((HttpServletResponse) response);
		return testingContext;
	}

	@Override
	public void destroyContext(Context ctx) {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info(
				"Implement this" + Thread.currentThread().getStackTrace()[2]);
	}

	@Override
	public Context createContext(Application application, Object request,
			Object response) {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info(
				"Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	public void restore() {
		set(prev);
	}

	@Override
	public Context initContext(Context context) {
		return context;
	}

}
