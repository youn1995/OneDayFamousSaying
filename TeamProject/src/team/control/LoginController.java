package team.control;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import team.data.User;

public class LoginController implements Initializable
{
	@FXML
	private Button buttonSignin, buttonSignup, buttonCancel, buttonSignupComp;
	@FXML
	private TextField textid;
	@FXML
	private PasswordField textPassword;
	ConnectionDAO dao = new ConnectionDAO();
	private Stage signupStage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
//로그인창 로그인버튼
		buttonSignin.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				buttonSigninAction(arg0);
			}
		});

		buttonSignup.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				buttonSignupAction(arg0);
			}
		});
	}

//로그인창 회원가입버튼
	public void buttonSigninAction(ActionEvent aa)
	{

		User userinfo = dao.login(textid.getText(), textPassword.getText());
		if (userinfo == null)
		{
			messageDialog("아이디/비밀번호 입력 오류");
			return;
		}

		Stage nextStage = new Stage(StageStyle.UTILITY);
		nextStage.initModality(Modality.WINDOW_MODAL);
		nextStage.initOwner(buttonSignin.getScene().getWindow());
		try
		{
			try
			{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/team/ui/Main.fxml"));
				Parent parent = loader.load();
				MainController mainController = loader.getController();
				mainController.setUserInfo(userinfo);
				Scene scene = new Scene(parent);
				nextStage.setScene(scene);
				nextStage.setResizable(false);
				nextStage.show();
				textid.setText(null);
				textPassword.setText(null);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} 
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}//end of buttonSigninAction
//에러오류 다이얼로그창
	public void messageDialog(String message)
	{
		Stage customStage = new Stage(StageStyle.UTILITY);
		customStage.initModality(Modality.WINDOW_MODAL);
		customStage.initOwner(buttonSignin.getScene().getWindow());
		customStage.setTitle("오류");
		
		AnchorPane ap = new AnchorPane();
		ap.setPrefSize(400, 70);
		
		Button button = new Button("확인");
		button.setLayoutX(300);
		button.setLayoutY(30);
		button.setOnAction(e -> customStage.close());

		Label label = new Label(message);

		ap.getChildren().add(button);
		ap.getChildren().add(label);

		Scene scene = new Scene(ap);
		customStage.setScene(scene);
		customStage.show();
	}//end of messageDialog
	
//회원가입창 연결 및 회원가입창 회원가입버튼
	public void buttonSignupAction(ActionEvent gg)
	{
		signupStage = new Stage(StageStyle.UTILITY);
		signupStage.initModality(Modality.WINDOW_MODAL);
		signupStage.initOwner(buttonSignup.getScene().getWindow());
		try
		{
			Parent parent = FXMLLoader.load(getClass().getResource("/team/ui/Signup.fxml"));
			Scene scene = new Scene(parent);
			signupStage.setScene(scene);
			signupStage.setResizable(false);
			signupStage.show();
			TextField textSignId = (TextField) parent.lookup("#textSignId");
			PasswordField textSignPassword = (PasswordField) parent.lookup("#textSignPassword");
			TextField textName = (TextField) parent.lookup("#textName");
			TextField textEmail = (TextField) parent.lookup("#textEmail");
			Button buttonSignupComp = (Button) parent.lookup("#buttonSignupComp");
			Button buttonCancel = (Button) parent.lookup("#buttonCancel");
			buttonSignupComp.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent arg0)
				{
					if (textSignId.getText() == null || textSignId.getText().equals(""))
					{
						messageDialog("아이디를 입력하세요");
					} 
					else if (textSignPassword.getText() == null || textSignPassword.getText().equals(""))
					{
						messageDialog("비밀번호를 입력하세요");
					} 
					else if (textName.getText() == null || textName.getText().equals(""))
					{
						messageDialog("이름 입력하세요");
					} 
					else if (textEmail.getText() == null || textEmail.getText().equals(""))
					{
						messageDialog("이메일을 입력하세요");
					}
					dao.SignUp(textSignId.getText(), textName.getText(), textSignPassword.getText(),
							textEmail.getText());
					signupStage.close();
				}
			});
			buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0)
				{
					signupStage.close();
				}
				
			});
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}//end of buttonSignup Action
}//end of class LoginController
