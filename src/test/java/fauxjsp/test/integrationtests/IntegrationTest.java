package fauxjsp.test.integrationtests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.IOUtils;

public class IntegrationTest {

	private Tomcat server;
	private final String webapp="fauxjsp";
	
	protected void copy(String cpResource, File target) throws Exception{
		InputStream in = getClass().getResourceAsStream(cpResource);
		FileOutputStream fos = new FileOutputStream(target);
		IOUtils.copy(in, fos);
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
		server.setPort(7777);
		server.setBaseDir(workingDir.getAbsolutePath());
		server.getHost().setAppBase(webappDir.getAbsolutePath());
		server.getHost().setAutoDeploy(true);
		server.getHost().setDeployOnStartup(true);
		String contextPath = "/" + webapp;
		server.addWebapp(server.getHost(), contextPath, webappDir.getAbsolutePath());
		server.init();
		server.start();
	}

	@Test
	public void testNewsPage() throws Exception{
		URL url = new URL("http://localhost:7777/"+webapp+"/news");
		System.out.println("Connecting to embedded server at "+url);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setDoInput(true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(connection.getInputStream(), baos);
		String actualNewsPage = new String(baos.toByteArray()).replace("\r", "");
		connection.disconnect();

		baos = new ByteArrayOutputStream();
		IOUtils.copy(getClass().getResourceAsStream("/expected/newspage.html"), baos);
		String expectedNewsPage = new String(baos.toByteArray());
		
		assertEquals(expectedNewsPage, actualNewsPage);
	}
}
