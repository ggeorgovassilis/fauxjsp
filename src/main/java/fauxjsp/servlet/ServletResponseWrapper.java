package fauxjsp.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;

/**
 * Injects new {@link OutputStream}s into a {@link ServletResponse}
 * @author George Georgovassilis
 *
 */
public class ServletResponseWrapper extends javax.servlet.ServletResponseWrapper{

	protected ServletOutputStream servletOutputStream;
	protected WriteListener writeListener;
	protected PrintWriter printWriter;

	public ServletResponseWrapper(ServletResponse response, OutputStream out) {
		super(response);
		this.printWriter = new PrintWriter(out, true);
	}

	public ServletResponseWrapper(ServletResponse response) {
		super(response);
		try {
			this.printWriter = response.getWriter();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (servletOutputStream == null){
			servletOutputStream = new ServletOutputStream() {
				
				@Override
				public void write(int b) throws IOException {
					printWriter.write(b);
				}
				
				@Override
				public void setWriteListener(WriteListener wl) {
					writeListener = wl;
				}
				
				@Override
				public boolean isReady() {
					return true;
				}
			};
		}
		return servletOutputStream;
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return printWriter;
	}
	
	public void commit() {
		try {
			getWriter().flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
