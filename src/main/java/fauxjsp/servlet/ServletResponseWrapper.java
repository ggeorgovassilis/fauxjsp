package fauxjsp.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;

import fauxjsp.impl.Utils;

/**
 * Injects new {@link OutputStream}s into a {@link ServletResponse}
 * @author George Georgovassilis
 *
 */
public class ServletResponseWrapper extends javax.servlet.ServletResponseWrapper{

	protected ServletOutputStream servletOutputStream;
	protected PrintWriter printWriter;

	public ServletResponseWrapper(ServletResponse response, OutputStream out) {
		super(response);
		this.printWriter = new PrintWriter(out, true);
		servletOutputStream = new ServletOutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				out.write(b);
			}
			
			@Override
			public void setWriteListener(WriteListener writeListener) {
			}
			
			@Override
			public boolean isReady() {
				return true;
			}
		};
	}

	public ServletResponseWrapper(ServletResponse response) {
		super(response);
		try {
			this.servletOutputStream = response.getOutputStream();
		} catch (IOException e) {
			throw Utils.softenException(e);
		}
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return servletOutputStream;
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		if (printWriter==null) {
			printWriter = new PrintWriter(servletOutputStream);
		}
		return printWriter;
	}
	
	public void write(String content) {
		try {
			byte[] b = content.getBytes(getCharacterEncoding());
			servletOutputStream.write(b);
		} catch (Exception e) {
			throw Utils.softenException(e);
		}
	}
	
	public void commit() {
		try {
			servletOutputStream.flush();
		} catch (IOException e) {
			throw Utils.softenException(e);
		}
	}
}
