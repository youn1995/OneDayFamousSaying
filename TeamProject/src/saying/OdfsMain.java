package saying;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OdfsMain extends Application
{

	@Override
	public void start(Stage arg0) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
		Scene scene = new Scene(root);
		
//		scene.getStylesheets().add(getClass().getResource("login.css").toString());
		arg0.show();
		arg0.setScene(scene);
		arg0.setTitle("명언");
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
