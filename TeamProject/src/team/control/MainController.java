package team.control;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import team.connect.ConnectionDAO;
import team.data.Diary;
import team.data.User;

public class MainController implements Initializable {
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

	private Diary updateDiary;
	private User userInfo;
	private TableView<Diary> tableViewUserList;
	private boolean stop;
	private int tableViewPreNextNum = 1;
	private int userListCount = 0;
	private int listPageNum = 1;

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				changeDate();
				ConnectionDAO cDAO = new ConnectionDAO();
				userListCount = cDAO.getUserListCount(userInfo.getUserid());
				btnMyList.setOnAction(e -> userList(e));
				hyLinkLogout.setOnMouseClicked(event -> {
					((Stage) hyLinkLogout.getScene().getWindow()).close();
					// id,비번 초기화
				});
				btnUpload.setOnAction(e -> upLoadDiary(e));
				hyLinkMyPage.setOnMouseClicked(event -> {
					myPage(event);
//					messagePopup(3, "서비스 준비중입니다.");
				});
			}
		});
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
			ObservableList<Diary> userDiaryList = (ObservableList<Diary>) cDAO.getUserDiaryList(userInfo.getUserid(),
					tableViewPreNextNum);
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
						updateDiary = diary;
						addStage.close();
					} else if (event.getButton() == MouseButton.SECONDARY) {
						contextMenu(event);
						tableViewUserList.getSelectionModel().selectedItemProperty().getValue();
					}

				}
			});
			Label labPageNum = (Label) parent.lookup("#labPageNum");
			labPageNum.setText("< " + listPageNum + " >");
			Hyperlink hyLinkPre = (Hyperlink) parent.lookup("#hyLinkPre");
			hyLinkPre.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (listPageNum > 1) {
						listPageNum--;
						Label labPageNum = (Label) parent.lookup("#labPageNum");
						labPageNum.setText("< " + listPageNum + " >");
						tableViewPreNextNum = tableViewPreNextNum - 15;
						ConnectionDAO cDAO = new ConnectionDAO();
						ObservableList<Diary> userDiaryList = (ObservableList<Diary>) cDAO
								.getUserDiaryList(userInfo.getUserid(), tableViewPreNextNum);
						TableColumn<Diary, String> tcTitle = (TableColumn<Diary, String>) tableViewUserList.getColumns()
								.get(0);
						tcTitle.setCellValueFactory(new PropertyValueFactory<Diary, String>("title"));
						TableColumn<Diary, String> tcListDate = (TableColumn<Diary, String>) tableViewUserList
								.getColumns().get(1);
						tcListDate.setCellValueFactory(new PropertyValueFactory<Diary, String>("listDate"));
						tableViewUserList.setItems(userDiaryList);
					}
				}
			});
			Hyperlink hyLinkNext = (Hyperlink) parent.lookup("#hyLinkNext");
			hyLinkNext.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (listPageNum <= userListCount / 16) {
						tableViewPreNextNum = tableViewPreNextNum + 15;
						listPageNum++;
						Label labPageNum = (Label) parent.lookup("#labPageNum");
						labPageNum.setText("< " + listPageNum + " >");
						ConnectionDAO cDAO = new ConnectionDAO();
						ObservableList<Diary> userDiaryList = (ObservableList<Diary>) cDAO
								.getUserDiaryList(userInfo.getUserid(), tableViewPreNextNum);
						TableColumn<Diary, String> tcTitle = (TableColumn<Diary, String>) tableViewUserList.getColumns()
								.get(0);
						tcTitle.setCellValueFactory(new PropertyValueFactory<Diary, String>("title"));
						TableColumn<Diary, String> tcListDate = (TableColumn<Diary, String>) tableViewUserList
								.getColumns().get(1);
						tcListDate.setCellValueFactory(new PropertyValueFactory<Diary, String>("listDate"));
						tableViewUserList.setItems(userDiaryList);
					}
				}
			});

			Hyperlink hyLinkReturn = (Hyperlink) parent.lookup("#hyLinkReturn");
			hyLinkReturn.setOnMouseClicked(event -> addStage.close());
		} catch (IOException e1) {
			messagePopup(1, "리스트를 불러올 수 없습니다. 관리자에게 문의하세요.");
		}
	}

	public void upLoadDiary(ActionEvent e) {
		stop = true;
		String title = txtFieldTitle.getText();
		String content = txtAreaContent.getText();
		if (title == null || content == null || title.equals("") || content.equals("")) {
			messagePopup(4, "일기를 작성하고 업로드를 해주세요");
		} else {
			if (updateDiary == null) {
				Diary diary = new Diary(-1, title, content, labDate.getText());
				ConnectionDAO cDAO = new ConnectionDAO();
				try {
					cDAO.insertUserDiary(userInfo.getUserid(), diary);
					messagePopup(3, "업로드 완료");
					changeDate();
					userListCount++;
					txtFieldTitle.setText(null);
					txtAreaContent.setText(null);
				} catch (Exception e2) {
					messagePopup(1, "업로드 실패 관리자에게 문의하세요.");
				}

			} else {
				try {
					updateDiary.setTitle(title);
					updateDiary.setContent(content);
					ConnectionDAO cDAO = new ConnectionDAO();
					cDAO.updateUserDiary(userInfo.getUserid(), updateDiary);
					messagePopup(3, "업로드 완료");
					changeDate();
					txtFieldTitle.setText(null);
					txtAreaContent.setText(null);
					updateDiary = null;
				} catch (Exception e2) {
					messagePopup(1, "업로드 실패 관리자에게 문의하세요.");
				}
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
				if (tableViewUserList.getSelectionModel().selectedItemProperty().getValue() == null) {
					messagePopup(1, "수정할 일기가 선택되지 않았습니다.");
				} else {
					Diary diary = tableViewUserList.getSelectionModel().selectedItemProperty().getValue();
					txtFieldTitle.setText(diary.getTitle());
					txtAreaContent.setText(diary.getContent());
					stop = true;
					labDate.setText(diary.getListDate());
					updateDiary = diary;
					Stage listStage = (Stage) tableViewUserList.getScene().getWindow();
					listStage.close();
				}

			}
		});
		MenuItem item2 = new MenuItem("삭제");
		item2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (tableViewUserList.getSelectionModel().selectedItemProperty().getValue() == null) {
					messagePopup(1, "삭제할 일기가 선택되지 않았습니다.");
				} else {
					ConnectionDAO cDAO = new ConnectionDAO();
					cDAO.deleteUserDiary(userInfo.getUserid(),
							tableViewUserList.getSelectionModel().selectedItemProperty().getValue().getListId());
					tableViewUserList.setItems((ObservableList<Diary>) cDAO.getUserDiaryList(userInfo.getUserid(), 1));
					userListCount--;
				}
			}
		});

		MenuItem item3 = new MenuItem("뒤로가기");
		item3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage listStage = (Stage) tableViewUserList.getScene().getWindow();
				listStage.close();

			}
		});

		contextMenu.getItems().addAll(item1, item2, item3);
		contextMenu.show(tableViewUserList.getScene().getWindow(), event.getScreenX(), event.getScreenY());

	}
	public void myPage(MouseEvent event) {
		
		Stage myPage = new Stage(StageStyle.UTILITY);
		myPage.initModality(Modality.WINDOW_MODAL);
		myPage.initOwner(btnMyList.getScene().getWindow());
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/team/ui/Mypage.fxml"));
			Scene scene = new Scene(parent);
			myPage.setScene(scene);
			myPage.setResizable(false);
			myPage.show();
			
			TextField mypageid = (TextField) parent.lookup("#mypageid");
			TextField mypagename = (TextField) parent.lookup("#mypagename");
			TextField mypageemail = (TextField) parent.lookup("#mypageemail");
			
			mypageid.setText(userInfo.getLoginid());
			mypagename.setText(userInfo.getName());
			mypageemail.setText(userInfo.getEmail());
			
			
		
			
	}catch(Exception ff){
		ff.printStackTrace();
	}
	}
}
