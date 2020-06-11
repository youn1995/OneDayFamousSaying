package team.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Diary {
	private SimpleIntegerProperty listId;
	private SimpleStringProperty title;
	private SimpleStringProperty content;
	private SimpleStringProperty listDate;
	
	public Diary() 	{}
	public Diary(int listId, String title, String content, String listDate) {
		this.listId = new SimpleIntegerProperty(listId);
		this.title = new SimpleStringProperty(title);
		this.content = new SimpleStringProperty(content);
		this.listDate = new SimpleStringProperty(listDate);
	}
	

	// listId
	public int getListId() {
		return this.listId.get();
	}
	public void setListId(int listId) {
		this.listId.set(listId);
	}
	public SimpleIntegerProperty listIdProperty() {
		return this.listId;
	}
	
	// title
	public String getTitle() {
		return this.title.get();
	}

	public void setTitle(String title) {
		System.out.println(title+" diary");
		this.title.set(title);
	}

	public SimpleStringProperty titleProperty() {
		return this.title;
	}

	// content
	public String getContent() {
		return this.content.get();
	}

	public void setContent(String content) {
		this.content.set(content);
	}

	public SimpleStringProperty contentProperty() {
		return this.content;
	}
	
	//listDate
	public String getListDate() {
		return this.listDate.get();
	}
	public void setListDate(String listDate) {
		this.listDate.set(listDate);
	}
	public SimpleStringProperty listDateProperty() {
		return this.listDate;
	}
	
}
