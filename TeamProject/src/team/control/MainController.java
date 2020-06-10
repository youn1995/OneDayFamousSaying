package team.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import team.data.Diary;
import team.data.User;

public class MainController implements Initializable {
	
	User userInfo;
	Stage primaryStage;
	
	@FXML
	Button btnMyList, btnUpload;
	@FXML
	Hyperlink hyLinkMyPage, hyLinkLogout;
	@FXML
	TextArea txtAreaContent;
	@FXML
	Label labDate;
	@FXML
	TextField txtFieldTitle;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		labDate.setText(value);
		btnMyList.setOnAction(e -> userList(e));
		hyLinkLogout.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.close();
			}
		});
		btnUpload.setOnAction(e -> upLoadDiary(e));
		
	}
	
	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}
	
	public void userList(ActionEvent e) {
		Stage addStage = new Stage(StageStyle.UTILITY);
		addStage.initModality(Modality.WINDOW_MODAL);
		addStage.initOwner(btnMyList.getScene().getWindow());
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("AddForm.fxml"));
			Scene scene = new Scene(parent);
			addStage.setScene(scene);
			addStage.setResizable(false);
			addStage.show();
			
			TableView<Diary> tableViewUserList = (TableView<Diary>) parent.lookup("#tableViewUserList");
//			ObservableList<Diary> userDiaryList = (ObservableList<Diary>)
			TableColumn<Diary, String> tcTitle = (TableColumn<Diary, String>) tableViewUserList.getColumns().get(0);
			tcTitle.setCellValueFactory(new PropertyValueFactory<Diary, String>("title"));
			TableColumn<Diary, String> tcContent = (TableColumn<Diary, String>) tableViewUserList.getColumns().get(1);
			tcContent.setCellValueFactory(new PropertyValueFactory<Diary, String>("content"));
			TableColumn<Diary, String> tclistDate = (TableColumn<Diary, String>) tableViewUserList.getColumns().get(2);
			tclistDate.setCellValueFactory(new PropertyValueFactory<Diary, String>("title"));
			tableViewUserList.getColumns().add(tcTitle);
			tableViewUserList.getColumns().add(tcContent);
			tableViewUserList.getColumns().add(tclistDate);
//			tableViewUserList.setItems(userDiaryList);
			
			Hyperlink hyLinkReturn = (Hyperlink) parent.lookup("#hyLinkReturn");
			hyLinkReturn.setOnMouseClicked(event -> addStage.close());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public void upLoadDiary(ActionEvent e) {
		txtFieldTitle.getText();
		txtAreaContent.getText();
		
	}
}
