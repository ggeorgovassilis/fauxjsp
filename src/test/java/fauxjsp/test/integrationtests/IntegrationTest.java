package fauxjsp.test.integrationtests;

import org.junit.Before;
import org.junit.Test;

import fauxjsp.impl.Utils;
import fauxjsp.test.unittests.BaseTest;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.apache.catalina.startup.Tomcat;

public class IntegrationTest extends BaseTest{

	private Tomcat server;
	private final String webapp="fauxjsp";
	private int port = 13766;
	
	protected void copy(String cpResource, File target) throws Exception{
		InputStream in = getClass().getResourceAsStream(cpResource);
		FileOutputStream fos = new FileOutputStream(target);
		Utils.copy(in, fos);
		fos.flush();
		fos.close();
		in.close();
	}
	
	protected File createWorkingDirectory() throws Exception{
		File workingDir = new File(new File(System.getProperty("java.io.tmpdir")), webapp+"-"+UUID.randomUUID());
		workingDir.deleteOnExit();
		workingDir.delete();
		workingDir.mkdir();
		return workingDir;
	}
	
	protected File assembleWebApplication(File workingDir) throws Exception{
		File webappsDir = new File(workingDir, "webapps");
		webappsDir.mkdir();
		File webappDir = new File(webappsDir, webapp);
		webappDir.mkdir();
		org.apache.commons.io.FileUtils.copyDirectory(new File("src/test/resources/webapp"), webappDir);
		return webappDir;
	}

	@Before
	public void setup() throws Exception {
		File workingDir = createWorkingDirectory();
		File webappDir = assembleWebApplication(workingDir);
		server = new Tomcat();
		server.setPort(port);
		server.setBaseDir(workingDir.getAbsolutePath());
		server.getHost().setAppBase(webappDir.getAbsolutePath());
		server.getHost().setAutoDeploy(true);
		server.getHost().setDeployOnStartup(true);
		String contextPath = "/" + webapp;
		server.addWebapp(server.getHost(), contextPath, webappDir.getAbsolutePath());
		server.init();
		server.start();
		server.getConnector(); //required, because initialises lazily
	}

	@Test
	public void testNewsPage() throws Exception{
		URL url = new URL("http://localhost:"+port+"/"+webapp+"/news");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoInput(true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Utils.copy(connection.getInputStream(), baos);
		String actualNewsPage = sanitize(new String(baos.toByteArray()));
		connection.disconnect();

		String expected = sanitize(read("/expected/newspage3.html"));
		assertEquals(expected, actualNewsPage);
	}
}
