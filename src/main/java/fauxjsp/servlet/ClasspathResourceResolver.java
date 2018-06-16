package fauxjsp.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import fauxjsp.api.parser.ResourceResolver;

public class ClasspathResourceResolver implements ResourceResolver {

	@Override
	public byte[] getContents(String path) {
		path = path.substring("classpath:".length());
		InputStream in = getClass().getResourceAsStream(path);
		if (in == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			IOUtils.copy(in, baos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return baos.toByteArray();
	}

	@Override
	public boolean canHandle(String path) {
		return path.startsWith("classpath:");
	}

}
