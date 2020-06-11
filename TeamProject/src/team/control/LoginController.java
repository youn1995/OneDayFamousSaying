package team.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import team.connect.ConnectionDAO;



public class LoginController implements Initializable
{

	@FXML
	Button buttonSignin, buttonSignup, buttonCancel, buttonSignupComp;
	@FXML
	TextField textEmail, textName, textSignId, textid;
	@FXML
	PasswordField textSignPassword, textPassword;


	ConnectionDAO dao = new ConnectionDAO();
	PreparedStatement prmt = null;
	

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
			messageDialog("아이디/비밀번호 입력 오류");
			return;
		}
		
		
		Stage nextStage = new Stage(StageStyle.UTILITY);
		nextStage.initModality(Modality.WINDOW_MODAL);
		nextStage.initOwner(buttonSignin.getScene().getWindow());
		
//		try
//		{
//			try
//			{
//				Parent parent = FXMLLoader.load(getClass().getResource("ui/Main.fxml"));
//				Scene scene = new Scene(parent);
//				nextStage.setScene(scene);
//				nextStage.setResizable(false);
//				nextStage.show();
//			} catch (IOException e)
//			{
//				
//				e.printStackTrace();
//			}
//		}catch(NullPointerException e){
//			e.printStackTrace();
//		}
//		
//		
		
	}
	public void messageDialog(String message) {
		Stage customStage = new Stage(StageStyle.UTILITY);
		customStage.initModality(Modality.WINDOW_MODAL);
		customStage.initOwner(buttonSignin.getScene().getWindow());
		customStage.setTitle("오류");
		
		AnchorPane ap = new AnchorPane();
		ap.setPrefSize(400, 150);
		
		Button button = new Button("확인");
		button.setLayoutX(336);
		button.setLayoutY(104);
		button.setOnAction(e->customStage.close());
		
		Label label = new Label(message);
		
		ap.getChildren().add(button);
		ap.getChildren().add(label);
		
		Scene scene = new Scene(ap);
		customStage.setScene(scene);
		customStage.show();
	}
}

