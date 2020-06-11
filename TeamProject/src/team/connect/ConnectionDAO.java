package team.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import team.data.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import team.data.Diary;

public class ConnectionDAO {

	

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

	public int login(String loginid, String password)
	{
		try
		{

			String sql = "select count(*) as check " + " from diary_user "
					+ " where login_id ='" + loginid + "'" + " and password= '"
					+ password + "'";
			conn = getConnect();
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


	public ObservableList<Diary> getUserDiaryList(int userId) {
		ObservableList<Diary> list = FXCollections.observableArrayList();
		String sql = String.format(
				"select list_id, title, content, list_date from diary_list where user_id = %d order by 4 desc", userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String listDateSubString = rs.getString("list_date").substring(0, 10);
				Diary diary = new Diary(rs.getInt("list_id"), rs.getString("title"), rs.getString("content"),
						listDateSubString);
				list.add(diary);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return list;
	}

	public void insertUserDiary(int userId, Diary diary) {
		String sql = String.format("insert into diary_list values(DIARY_USER_ID_SEQ.nextval, '%s', '%s', '%s', %d)",
				diary.getTitle(), diary.getContent(), diary.getListDate(), userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}