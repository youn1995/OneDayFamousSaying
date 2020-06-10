package saying;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ODFSDAO2 {

	@FXML TextField idfield;
	@FXML TextField passwordifeld;
	Connection conn = null;
	PreparedStatement pstmt = null;

	public Connection getConnect() {

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

	public List<Member2> getMemberList() { //관리자용 멤버리스트
		List<Member2> list = new ArrayList<Member2>();
		String sql = "select user_id, login_id, name, email from odfs_users";
		conn = getConnect();
		
		conn = getConnect();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Member2 member = new Member2();
				member.setUserId(rs.getInt("user_id"));
				member.setLoginId(rs.getString("login_id"));
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
				list.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<FamousSay2> getFamousSayingList() { // 관리자용 명언리스트
		List<FamousSay2> list = new ArrayList<FamousSay2>();
		String sql = "select f.list_id, f.name, f.content, count(*)"
				+ "  ";
		
		return null;
	}
	
	public List<FamousSay2> getLikeList() { // 사용자용 좋야요 리스트
		
		return null;
	}
	
	
	public void insertMember() { //회원가입
		
	}
	
	public idButton(ActionEvnet event) throw Exception{//로그인버튼
		if(txtUserName.getText().equals("user") && txtPassword.getText().equals("pass")){
            lblStatus.setText("Login Success");
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/saying/OdfsMain.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        }else{
            lblStatus.setText("Login Failed");
        }
    }

