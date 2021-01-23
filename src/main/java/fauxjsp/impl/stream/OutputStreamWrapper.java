package fauxjsp.impl.stream;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamWrapper extends OutputStream{

	protected OutputStream wrapped;
	
	public void setWrappedOutputStream(OutputStream wrapped) {
		this.wrapped = wrapped;
	}
	
	@Override
	public void write(int b) throws IOException {
		wrapped.write(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		wrapped.write(b, off, len);
	}
	
	@Override
	public void flush() throws IOException {
		wrapped.flush();
	}
	
}
