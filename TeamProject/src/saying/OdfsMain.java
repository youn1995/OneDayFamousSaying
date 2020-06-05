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

		arg0.show();
		arg0.setScene(scene);
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
