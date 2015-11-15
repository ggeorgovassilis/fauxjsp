package fauxjsp.test.support;

import java.io.File;

import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.Utils;

/**
 * Resource resolver for files
 * @author George Georgovassilis
 *
 */
public class FileResolver implements ResourceResolver{

	protected File baseFile;
	
	public FileResolver(File baseFile) {
		this.baseFile = baseFile;
	}
	
	@Override
	public byte[] getContents(String path) {
		return Utils.readFile(new File(baseFile, path));
	}

}
