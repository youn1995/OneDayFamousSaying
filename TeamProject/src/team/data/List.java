package team.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class List
{
	private SimpleStringProperty title;
	private SimpleStringProperty content;
	
	//title
	public String getTitle()
	{
		return this.title.get();
	}

	public void setTitle(String title)
	{
		this.title.set(title);
	}

	public SimpleStringProperty titleProperty()
	{
		return this.title;
	}
	//content
	public String getContent()
	{
		return this.content.get();
	}

	public void setContent(String content)
	{
		this.content.set(content);
	}

	public SimpleStringProperty contentProperty()
	{
		return this.content;
	}
}
