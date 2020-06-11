package team.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import team.data.Diary;

public class ConnectionDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;

	public Connection getConnect() { // 세션접속

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
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