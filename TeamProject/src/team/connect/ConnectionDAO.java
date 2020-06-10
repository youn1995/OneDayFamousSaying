package team.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import team.data.User;

public class ConnectionDAO
{
	Connection conn = null;
	PreparedStatement pstmt = null;

	public Connection getConnect()
	{ // 세션접속

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, "hr", "hr");
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return conn;
	}

	public User getUserLogin(String loginid, String password)
	{

		String sql = "select * from diary_user where login_id = ? and password = ?  ";
		try
		{
			pstmt.getConnection().prepareStatement(sql);
			pstmt.setString(1, loginid);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				User userlogin = new User(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("login_id"),rs.getString("email"));
				return userlogin;
			}
			else{
				return null;
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;

	}	
}