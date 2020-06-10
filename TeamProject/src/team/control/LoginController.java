package team.control;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import team.connect.ConnectionDAO;
import team.data.User;

public class LoginController implements Initializable
{
	@FXML
	Button buttonSignin, buttonSignup, buttonCancel, buttonSignupComp;
	@FXML
	TextField textEmail, textName, textSignId, textid;
	@FXML
	PasswordField textSignPassword, textPassword;

	User user = new User(0, null, null, null);
	ConnectionDAO dao = new ConnectionDAO();
	PreparedStatement prmt = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

	}
}