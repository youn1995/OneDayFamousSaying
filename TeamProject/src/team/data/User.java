package team.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User
{
	private SimpleIntegerProperty userid;
	private SimpleStringProperty loginid;
	private SimpleStringProperty name;
	private SimpleStringProperty email;
	

	
	//user생성자
	public User(int userid, String loginid, String name, String email)
	{
		this.userid = new SimpleIntegerProperty(userid);
		this.loginid = new SimpleStringProperty(loginid);
		this.name = new SimpleStringProperty(name);
		this.email = new SimpleStringProperty(email);
	}
	
	

	//userid	
	public int getUserid()
	{
		return this.userid.get();
	}

	public void setUserid(int userid)
	{
		this.userid.set(userid);
	}

	public SimpleIntegerProperty useridProperty()
	{
		return this.userid;
	}
	
	//loginid
	
	public String getLoginid()
	{
		return this.loginid.get();
	}

	public void setLoginid(String loginid)
	{
		this.loginid.set(loginid);
	}

	public SimpleStringProperty loginidProperty()
	{
		return this.loginid;
	}
	
	
	//name
	public String getName()
	{
		return this.name.get();
	}

	public void setname(String name)
	{
		this.name.set(name);
	}

	public SimpleStringProperty nameProperty()
	{
		return this.name;
	}
	
	
	//email
	public String getEmail()
	{
		return this.email.get();
	}

	public void setEmail(String email)
	{
		this.email.set(email);
	}

	public SimpleStringProperty emailProperty()
	{
		return this.email;
	}
	
	
}
