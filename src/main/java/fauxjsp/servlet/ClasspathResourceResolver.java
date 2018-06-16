package fauxjsp.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.Utils;

public class ClasspathResourceResolver implements ResourceResolver {

	@Override
	public byte[] getContents(String path) {
		path = path.substring("classpath:".length());
		InputStream in = getClass().getResourceAsStream(path);
		if (in == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Utils.copy(in, baos);
		} finally {
			Utils.close(in);
		}
		return baos.toByteArray();
	}

	@Override
	public boolean canHandle(String path) {
		return path != null && path.startsWith("classpath:");
	}

}
