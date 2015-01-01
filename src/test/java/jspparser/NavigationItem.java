package jspparser;

public class NavigationItem {

	protected String path;
	protected String label;
	
	public NavigationItem(String path, String label){
		this.path = path;
		this.label = label;
	}
	
	public String getPath() {
		return path;
	}
	public String getLabel() {
		return label;
	}
}
