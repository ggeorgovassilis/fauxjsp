package fauxjsp.servlet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;

import fauxjsp.api.parser.ResourceResolver;

/**
 * Returns files on a server
 * @author George Georgovassilis
 *
 */

public class ServletResourceResolver implements ResourceResolver {

	protected ServletContext context;
	protected String pathBase;
	
	public ServletResourceResolver(String pathBase, ServletContext context) {
		this.context = context;
		this.pathBase = pathBase;
	}

	@Override
	public byte[] getContents(String path) {
		String resourcePath = pathBase+path;
		try (InputStream in = context.getResourceAsStream(resourcePath)) {
			if (in == null)
				return null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while (true) {
				int length = in.read(buffer);
				if (length == -1)
					break;
				baos.write(buffer, 0, length);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
