package fauxjsp.test.unittests;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

import fauxjsp.servlet.ServletResourceResolver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
public class TestServletResourceResolver extends BaseTest{

	ServletResourceResolver resolver;
	final String pathBase="/base";
	ServletContext context;
	
	@Before
	public void setup(){
		context = mock(ServletContext.class);
		resolver = new ServletResourceResolver(pathBase, context);
	}
	
	@Test
	public void testGetContents() throws Exception{
		final byte[] expected = "the resource".getBytes();
		when(context.getResourceAsStream(pathBase+"/path")).thenReturn(new ByteArrayInputStream(expected));
		byte[] b = resolver.getContents("/path");
		assertArrayEquals(expected, b);
	}

	@Test
	public void testGetContentsNotFound() throws Exception{
		when(context.getResourceAsStream(pathBase+"/path")).thenReturn(null);
		byte[] b = resolver.getContents("/path");
		assertNull(b);
	}

	@SuppressWarnings("unchecked")
	@Test(expected=Exception.class)
	public void testGetContentsFailure() throws Exception{
		when(context.getResourceAsStream(pathBase+"/path")).thenThrow(IOException.class);
		resolver.getContents("/path");
	}
}
