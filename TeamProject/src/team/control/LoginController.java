package team.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import team.connect.ConnectionDAO;

public class LoginController implements Initializable
{
	@FXML Button buttonSignin, buttonSignup, buttonCancel, buttonSignupComp;
	@FXML TextField textEmail, textName, textSignId, textid;
	@FXML PasswordField textSignPassword, textPassword;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		buttonSignin.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0)
			{
				buttonSigninAction(arg0);

			}
		});
		
	}
	
	public void buttonSigninAction(ActionEvent aa) {
		ConnectionDAO idcheck = new ConnectionDAO();
		
		if(idcheck.login(textid.getText(), textPassword.getText()) ==0) {
			System.out.println("잘못입력함");
			// 다이얼창 뜨게하기
			return;
		}
		Platform.exit();
		//signup창으로 넘어가게 만들어주기
		
	}

}
