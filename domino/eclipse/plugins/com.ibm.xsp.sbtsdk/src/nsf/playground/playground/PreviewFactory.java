package nsf.playground.playground;

import com.ibm.xsp.extlib.servlet.ServletFactory;

public class PreviewFactory extends ServletFactory {

	public PreviewFactory() {
		super("preview",nsf.playground.playground.PreviewServlet.class.getName(),"Playground Preview Servlet");
	}
}
