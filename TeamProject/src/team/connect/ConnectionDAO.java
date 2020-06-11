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

	public Connection getConnect() { // 세션접속

		String url = "jdbc:oracle:thin:@192.168.0.74:1521:xe";
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, "hr", "hr");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public User login(String loginid, String password) {
		User userinfo = null;
		try {

			String sql = "select count(*) as CNT from diary_user where login_id ='" + loginid + "'"
					+ " and user_password= '" + password + "'";

			conn = getConnect();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("CNT") == 0) {
					return null;
				}
			}
			sql = "select *" + " from diary_user " + " where login_id ='" + loginid + "'" + " and user_password= '"
					+ password + "'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				User user = new User(rs.getInt("user_id"), rs.getString("login_id"), rs.getString("user_name"),
						rs.getString("user_password"));
				userinfo = user;
			}
			return userinfo;

		} catch (SQLException e) {
			e.printStackTrace();

			return null;
		}
	}

	// 유저다이어리 목록 호출
	public ObservableList<Diary> getUserDiaryList(int userId) {
		ObservableList<Diary> list = FXCollections.observableArrayList();
		String sql = String.format(
				"select list_id, title, content, list_date from diary_list where user_id = %d order by 4 desc", userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String listDateSubString = rs.getString("list_date").substring(0, 16);
				Diary diary = new Diary(rs.getInt("list_id"), rs.getString("title"), rs.getString("content"),
						listDateSubString);
				list.add(diary);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return list;
	}

	// 유저가 쓴 일기 INSERT
	public void insertUserDiary(int userId, Diary diary) {
		String sql = String.format(
				"insert into diary_list values(DIARY_LIST_ID_SEQ.nextval, '%s', '%s', to_date('%s', 'YYYY-MM-DD hh24:mi'), %d)",

				diary.getTitle(), diary.getContent(), diary.getListDate(), userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 유저 일기 삭제
	public void deleteUserDiary(int userId, int listId) {
		String sql = String.format("delete diary_list where list_id = %d and user_id = %s", listId, userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}