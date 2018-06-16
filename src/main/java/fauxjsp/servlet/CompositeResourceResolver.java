package fauxjsp.servlet;

import java.util.ArrayList;
import java.util.List;

import fauxjsp.api.parser.ResourceResolver;

public class CompositeResourceResolver implements ResourceResolver{

	protected List<ResourceResolver> resolvers = new ArrayList<>();
	protected Object mutex = new Object();
	
	public void register(ResourceResolver resolver) {
		synchronized (mutex) {
			List<ResourceResolver> copy = new ArrayList<>(resolvers);
			copy.add(resolver);
			resolvers = copy;
		}
	}
	
	@Override
	public byte[] getContents(String path) {
		for (ResourceResolver resolver:resolvers)
			if (resolver.canHandle(path)) {
				byte[] content = resolver.getContents(path);
				if (content!=null)
					return content;
			}
		return null;
	}

	@Override
	public boolean canHandle(String path) {
		for (ResourceResolver resolver:resolvers)
			if (resolver.canHandle(path))
				return true;
		return false;
	}

}
