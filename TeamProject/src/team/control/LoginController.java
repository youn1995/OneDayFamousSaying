package team.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.connect.ConnectionDAO;

public class LoginController implements Initializable
{
	@FXML
	Button buttonSignin, buttonSignup, buttonCancel, buttonSignupComp;
	@FXML
	TextField textEmail, textName, textSignId, textid;
	@FXML
	PasswordField textSignPassword, textPassword;
	@FXML
	Label labelErrors;
	ConnectionDAO dao = new ConnectionDAO();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		if (dao == null)
		{
			labelErrors.setTextFill(Color.RED);
			labelErrors.setText("아이디나 비밀번호를 입력하세요");
		} else
		{
			labelErrors.setTextFill(Color.RED);
			labelErrors.setText("좋은하루 되세요");
		}

	}

	@FXML
	public void handleButtonAction(MouseEvent ff)
	{
		if (ff.getSource() == buttonSignin)
		{
			if (logIn().equals("Success"))
			{
				try
				{
					Node node = (Node) ff.getSource();
					Stage stage = (Stage) node.getScene().getWindow();

					Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/team/ui/Signup.fxml")));
					stage.setScene(scene);
					stage.show();

				}
				catch (Exception ee)
				{
					System.err.println(ee.getMessage());
				}
			}
		} else if (ff.getSource() == buttonSignup)
		{
			//signUp메소드 불러서 새로운창 띄워주기
		}
	}

	private String signUp()
	{
		String status = "Success";
		return status;
	}

	private String logIn()
	{
		String status = "Success";
		
		return status;
	}

	private void setlabelError(Color color, String text)
	{
		labelErrors.setTextFill(color);
		labelErrors.setText(text);
		System.out.println(text);
	}
}
