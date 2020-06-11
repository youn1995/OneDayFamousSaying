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
	ResultSet rs = null;
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
	// public void login(String loginid, String password)
	// {
	// String sql = "select *" + " from diary_user " + " where login_id ='"
	// + loginid + "'" + " and password= '" + password + "'";
	// try
	// {
	// pstmt = conn.prepareStatement(sql);
	// rs = pstmt.executeQuery();
	// while (rs.next())
	// {
	//
	// if (rs.getString("login_id") == null)
	// continue;
	// if (rs.getString("user_password") == null)
	// continue;
	// if (rs.getString("user_name") == null)
	// continue;
	// User user = new User(rs.getInt("user_id"),
	// rs.getString("login_id"), rs.getString("user_name"),
	// rs.getString("user_password"));
	//
	// }
	// } catch (SQLException e)
	// {
	// e.printStackTrace();
	// }
	// }
	public int login(String loginid, String password)
	{
		try
		{

			String sql = "select count(*) as check " + " from diary_user "
					+ " where login_id ='" + loginid + "'" + " and password= '"
					+ password + "'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.getInt("check") == 0)
			{
				return 0;
			}
			
			sql = "select *" + " from diary_user " + " where login_id ='"
					+ loginid + "'" + " and password= '" + password + "'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			User user = new User(rs.getInt("user_id"), rs.getString("login_id"),
					rs.getString("user_name"), rs.getString("user_password"));

			return 1;
		} catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}