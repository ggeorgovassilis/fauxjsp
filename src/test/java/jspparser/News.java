package jspparser;

public class News {

	public String headline;
	public String description;
	protected String id;
	protected String fulltext;
	protected boolean important;

	public boolean isImportant() {
		return important;
	}

	public News(String id, String headline, String description, String fulltext, boolean important){
		this.id = id;
		this.headline = headline;
		this.description = description;
		this.fulltext = fulltext;
		this.important = important;
	}
	
	public String getFulltext() {
		return fulltext;
	}

	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


}
