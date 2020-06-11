package team.control;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import team.connect.ConnectionDAO;
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

	TableView<Diary> tableViewUserList;
	private boolean stop;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				changeDate();

				btnMyList.setOnAction(e -> userList(e));
				hyLinkLogout.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						primaryStage.close();
					}
				});
				btnUpload.setOnAction(e -> upLoadDiary(e));				
			}
		});

	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public void userList(ActionEvent e) {
		Stage addStage = new Stage(StageStyle.UTILITY);
		addStage.initModality(Modality.WINDOW_MODAL);
		addStage.initOwner(btnMyList.getScene().getWindow());
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/team/ui/List.fxml"));
			Scene scene = new Scene(parent);
			addStage.setScene(scene);
			addStage.setResizable(false);
			addStage.show();
			tableViewUserList = (TableView<Diary>) parent.lookup("#tableViewUserList");
			ConnectionDAO cDAO = new ConnectionDAO();
			ObservableList<Diary> userDiaryList = (ObservableList<Diary>) cDAO.getUserDiaryList(1);
			TableColumn<Diary, String> tcTitle = (TableColumn<Diary, String>) tableViewUserList.getColumns().get(0);
			tcTitle.setCellValueFactory(new PropertyValueFactory<Diary, String>("title"));
			TableColumn<Diary, String> tcListDate = (TableColumn<Diary, String>) tableViewUserList.getColumns().get(1);
			tcListDate.setCellValueFactory(new PropertyValueFactory<Diary, String>("listDate"));
			tableViewUserList.setItems(userDiaryList);

			tableViewUserList.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2) {
						Diary diary = tableViewUserList.getSelectionModel().selectedItemProperty().getValue();
						txtFieldTitle.setText(diary.getTitle());
						txtAreaContent.setText(diary.getContent());
						stop = true;
						labDate.setText(diary.getListDate());
						addStage.close();
					} else if (event.getButton() == MouseButton.SECONDARY){
						System.out.println("오른쪽 클릭");
						contextMenu(event);
						tableViewUserList.getSelectionModel().selectedItemProperty().getValue();
						
					}

				}
			});
			
			Hyperlink hyLinkReturn = (Hyperlink) parent.lookup("#hyLinkReturn");
			hyLinkReturn.setOnMouseClicked(event -> addStage.close());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void upLoadDiary(ActionEvent e) {
		stop = true;
		String title = txtFieldTitle.getText();
		String content = txtAreaContent.getText();
		if (title == null || content == null || title.equals("") || content.equals("")) {
			messagePopup(4, "일기를 작성하고 업로드를 해주세요");
		} else {
			Diary diary = new Diary(-1, title, content, labDate.getText());
			ConnectionDAO cDAO = new ConnectionDAO();
			try {
				cDAO.insertUserDiary(1, diary);
				messagePopup(3, "업로드 완료");
				changeDate();
				txtFieldTitle.setText(null);
				txtAreaContent.setText(null);
			} catch (Exception e2) {
				messagePopup(1, "업로드 실패 관리자에게 문의하세요.");
			}
		}
	}

	public void changeDate() {
		stop = false;

		Thread thread = new Thread() {
			@Override
			public void run() {
				while (!stop) {
					LocalDateTime currDateTime = LocalDateTime.now();
					Platform.runLater(() -> {
						labDate.setText(currDateTime.toString().replace('T', ' ').substring(0, 16));
					});
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}

				}
			};
		};
		thread.setDaemon(true);
		thread.start();
	}

	public void messagePopup(int alert, String message) {
		HBox hbox = new HBox();
		hbox.setStyle("-fx-background-color: skyblue; -fx-background-radius: 20;");
		hbox.setAlignment(Pos.CENTER);
		ImageView imageView = new ImageView();
		if (alert == 1) { // 에러
			imageView.setImage(new Image("/team/images/dialog-error.png"));
		} else if (alert == 2) { // 도움
			imageView.setImage(new Image("/team/images/dialog-help.png"));
		} else if (alert == 3) { // 정보
			imageView.setImage(new Image("/team/images/dialog-info.png"));
		} else if (alert == 4) { // 경고
			imageView.setImage(new Image("/team/images/dialog-warning.png"));
		}
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		Label label = new Label();
		label.setText(message);
		label.setStyle("-fx-text-fill: white");
		HBox.setMargin(label, new Insets(0, 5, 0, 5));
		hbox.getChildren().add(imageView);
		hbox.getChildren().add(label);
		Popup popup = new Popup();
		popup.getContent().add(hbox);
		popup.setAutoHide(true);
		popup.show(btnMyList.getScene().getWindow());
	}
	
	public void contextMenu(MouseEvent event) {		  
	        ContextMenu contextMenu = new ContextMenu();
	 
	        MenuItem item1 = new MenuItem("수정");
	        item1.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	            	System.out.println("sss");
	            }
	        });
	        MenuItem item2 = new MenuItem("삭제");
	        item2.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	            	System.out.println("sss");
	            }
	        });
	        
	        MenuItem item3 = new MenuItem("뒤로가기");
	        item2.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	            	System.out.println("sss");
	            }
	        });
	
	        contextMenu.getItems().addAll(item1, item2, item3);
	        contextMenu.show(tableViewUserList.getScene().getWindow(), event.getScreenX(), event.getScreenY());

	}
}
