package fauxjsp.test.unittests;

import org.junit.Before;
import org.junit.Test;

import fauxjsp.api.renderer.JspPageContextImpl;
import fauxjsp.servlet.JspServlet;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;

import static org.junit.Assert.*;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

/**
 * Test for {@link JspPageContextImpl}. I'm not happy with the test because it doesn't really
 * test that {@link JspPageContextImpl} works in the spirit of a {@link PageContext}; it just
 * is a regression test to make sure the implementation doesn't change without me noticing side-effects.
 * @author George Georgovassilis
 *
 */

public class TestJspContextImpl {

	JspPageContextImpl jsp;
	Servlet servlet;
	ServletRequest request;
	ServletResponse response;
	String errorPageURL;
	final String testAttribute="testattribute";
	final String testAttributeValue="testattributevalue";
	
	@Before
	public void setup() throws Exception{
		jsp = new JspPageContextImpl();
		
		servlet = new JspServlet();
		
		request = new MockHttpServletRequest();
		request.setAttribute(testAttribute, testAttributeValue);
		response = new MockHttpServletResponse();
		errorPageURL = "http:/error.page";
		
		jsp.initialize(servlet, request, response, errorPageURL, false, 0, false);
	}
	
	@Test(expected=RuntimeException.class)
	public void test_release(){
		jsp.release();
	}
	
	@Test
	public void test_getSession(){
		HttpSession session = jsp.getSession();
		assertEquals(((HttpServletRequest)request).getSession(),session);
	}
	
	@Test(expected=RuntimeException.class)
	public void test_getPage(){
		jsp.getPage();
	}
	
	@Test
	public void test_getRequest(){
		assertEquals(request, jsp.getRequest());
	}
	
	@Test
	public void test_getResponse(){
		assertEquals(response, jsp.getResponse());
	}
	
	@Test(expected=RuntimeException.class)
	public void test_getException(){
		jsp.getException();
	}
	
	@Test(expected=RuntimeException.class)
	public void test_getServletConfig(){
		assertEquals(null,jsp.getServletConfig());
	}

	@Test
	public void test_getServletContext(){
		assertEquals(request.getServletContext(),jsp.getServletContext());
	}

	@Test(expected=RuntimeException.class)
	public void test_forward() throws Exception{
		jsp.forward("someurl");
	}

	@Test(expected=RuntimeException.class)
	public void test_include() throws Exception{
		jsp.include("someurl");
	}

	@Test(expected=RuntimeException.class)
	public void test_include_flush() throws Exception{
		jsp.include("someurl", false);
	}

	@Test(expected=RuntimeException.class)
	public void test_handlePageException() throws Exception{
		jsp.handlePageException(new Exception());
	}

	@Test(expected=RuntimeException.class)
	public void test_handlePageException_throwable() throws Exception{
		jsp.handlePageException(new Throwable());
	}

	@Test
	public void test_getAttribute_setAttribute() throws Exception{
		jsp.setAttribute("attr", "val");
		assertEquals("val", jsp.getAttribute("attr"));
	}

	@Test(expected=RuntimeException.class)
	public void test_setAttribute_scope() throws Exception{
		jsp.setAttribute("attr", "val", 1);
	}

	@Test(expected=RuntimeException.class)
	public void test_getAttribute_scope() throws Exception{
		jsp.getAttribute("attr", 1);
	}

	@Test
	public void test_findAttribute() throws Exception{
		assertEquals(testAttributeValue, jsp.findAttribute(testAttribute));
	}

	@Test
	public void test_removeAttribute() throws Exception{
		assertNotNull(jsp.getAttribute(testAttribute));
		jsp.removeAttribute(testAttribute);
		assertNull(jsp.getAttribute(testAttribute));
	}
	
	@Test(expected=RuntimeException.class)
	public void test_getAttributeScope() throws Exception{
		jsp.getAttributesScope(testAttribute);
	}

	@Test(expected=RuntimeException.class)
	public void test_getAttributeNamesInScope() throws Exception{
		jsp.getAttributeNamesInScope(1);
	}

	@Test(expected=RuntimeException.class)
	public void test_getOut() throws Exception{
		jsp.getOut();
	}
	
	
	@Test(expected=RuntimeException.class)
	public void getExpressionEvaluator() {
		jsp.getExpressionEvaluator();
	}

	@Test(expected=RuntimeException.class)
	public void getVariableResolver() {
		jsp.getVariableResolver();
	}

	@Test(expected=RuntimeException.class)
	public void getELContext() {
		jsp.getELContext();
	}



}
