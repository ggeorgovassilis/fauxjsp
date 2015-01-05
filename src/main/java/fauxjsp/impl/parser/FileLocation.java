package fauxjsp.impl.parser;

import java.io.File;

import fauxjsp.api.parser.ResourceResolver;
import fauxjsp.impl.Utils;

/**
 * Resource resolver for files
 * @author George Georgovassilis
 *
 */
//TODO: rename to FileResolver
public class FileLocation implements ResourceResolver{

	protected File baseFile;
	
	public FileLocation(File baseFile) {
		this.baseFile = baseFile;
	}
	
	@Override
	public byte[] getContents(String path) {
		return Utils.readFile(new File(baseFile, path));
	}

}
