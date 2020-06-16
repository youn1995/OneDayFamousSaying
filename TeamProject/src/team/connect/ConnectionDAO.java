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
		} catch (ClassNotFoundException e) {}
		return conn;
	}
//로그인 id,password check , db비교 후 로그인
	public User login(String loginid, String password) {
		User userinfo = null;
		try {
			String sql = "select count(*) as CNT from diary_user where login_id ='" + loginid + "'"
					+ " and user_password= '" + password + "'";
			conn = getConnect();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("CNT") == 0) 
				{
					return null;
				}
			}
			sql = "select * from diary_user where login_id ='" + loginid + "'" + " and user_password= '"
					+ password + "'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) 
			{
				User user = new User(rs.getInt("user_id"), rs.getString("login_id"), rs.getString("user_name"),
						rs.getString("email"));
				userinfo = user;
			}
			return userinfo;

		} catch (SQLException e) {
			return null;
		}
	}


//회원가입 insert
	public void SignUp(String signid, String username, String userpassword, String email)
	{

		try
		{
			conn = getConnect();
			String sql = String.format("insert into diary_user values (diary_user_id_seq.nextval, " + "'%s', " + "'%s',"
					+ " '%s'," + " '%s')", signid, username, userpassword, email);
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	// 유저다이어리 목록 호출
	public ObservableList<Diary> getUserDiaryList(int userId, int startNum) {

		ObservableList<Diary> list = FXCollections.observableArrayList();
		String sql = String.format(
				"WITH E AS (SELECT ROWNUM as RO, X.* FROM ( SELECT A.* FROM DIARY_LIST A where A.user_id = %d ORDER BY A.list_date DESC) X) select * from E where E.RO BETWEEN %d AND %d",
				userId, startNum, startNum + 15);
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
		} catch (SQLException e) {}

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
		} catch (SQLException e) {}
	}

	// 유저 일기 삭제
	public void deleteUserDiary(int userId, int listId) {
		String sql = String.format("delete diary_list where list_id = %d and user_id = %d", listId, userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {}

	}

	// 유저 일기 수정
	public void updateUserDiary(int userId, Diary diary) {
		String sqlContent = String.format("update diary_list set content = '%s' where list_id = %d and user_id = %d",
				diary.getContent(), diary.getListId(), userId);
		String sqlTitle = String.format("update diary_list set title = '%s' where list_id = %d and user_id = %d",
				diary.getTitle(), diary.getListId(), userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sqlContent);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sqlTitle);
			pstmt.executeUpdate();
		} catch (SQLException e) {}
	}

	public int getUserListCount(int userId) {
		int userListCount = 0;
		String sql = String.format("select count(*) as count from  diary_list where user_id = %d", userId);
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				userListCount = rs.getInt("count");
			}
		} catch (SQLException e) {}

		return userListCount;
	}

}