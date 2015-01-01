package jspparser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class MockHttpServletResponse implements HttpServletResponse{

	protected PrintWriter writer;
	protected ServletOutputStream out;
	protected ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public ByteArrayOutputStream getBaos(){
		return baos;
	}
	
	@Override
	public String getCharacterEncoding() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getContentType() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (out == null){
			out = new ServletOutputStream() {
				
				@Override
				public void write(int b) throws IOException {
					baos.write(b);
				}
				
				@Override
				public void setWriteListener(WriteListener writeListener) {
				}
				
				@Override
				public boolean isReady() {
					return false;
				}
			};
		}
		return out;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null){
			writer = new PrintWriter(getOutputStream());
		}
		return writer;
	}

	@Override
	public void setCharacterEncoding(String charset) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setContentLength(int len) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setContentLengthLong(long len) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setContentType(String type) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setBufferSize(int size) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getBufferSize() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void flushBuffer() throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void resetBuffer() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isCommitted() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void reset() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setLocale(Locale loc) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Locale getLocale() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void addCookie(Cookie cookie) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean containsHeader(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String encodeURL(String url) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String encodeRedirectURL(String url) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String encodeUrl(String url) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String encodeRedirectUrl(String url) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void sendError(int sc) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setDateHeader(String name, long date) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void addDateHeader(String name, long date) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setHeader(String name, String value) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void addHeader(String name, String value) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setIntHeader(String name, int value) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void addIntHeader(String name, int value) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setStatus(int sc) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void setStatus(int sc, String sm) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int getStatus() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public String getHeader(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Collection<String> getHeaders(String name) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Collection<String> getHeaderNames() {
		throw new RuntimeException("Not implemented");
	}

}
