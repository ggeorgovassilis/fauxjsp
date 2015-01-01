package fauxjsp.impl.parser;

import java.util.HashMap;
import java.util.Map;

import fauxjsp.api.nodes.TaglibDefinition;

public class TaglibDefinitionCache {

	protected Map<String, TaglibDefinition> path2definition = new HashMap<String, TaglibDefinition>();
	
	public TaglibDefinition getDefinition(String path){
		return path2definition.get(path);
	}
	
	public void setDefinition(String path, TaglibDefinition definition){
		path2definition.put(path, definition);
	}
	
	public TaglibDefinitionCache(){
	}

}
