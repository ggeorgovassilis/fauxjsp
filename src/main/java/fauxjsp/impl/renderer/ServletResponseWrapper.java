package fauxjsp.impl.renderer;

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
	protected OutputStream out;
	protected WriteListener writeListener;
	protected PrintWriter printWriter;
	
	public ServletResponseWrapper(ServletResponse response, OutputStream out) {
		super(response);
		this.out = out;
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (servletOutputStream == null){
			servletOutputStream = new ServletOutputStream() {
				
				@Override
				public void write(int b) throws IOException {
					out.write(b);
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
	
	public OutputStream getOut() {
		return out;
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		if (printWriter==null){
			printWriter = new PrintWriter(out);
		}
		return printWriter;
	}
}
