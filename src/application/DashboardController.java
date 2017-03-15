package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import data.Nurses;
import data.Progress;
import data.Requests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.DashboardModel;

public class DashboardController implements Initializable {

	@FXML private Label welcomeMsg;
	@FXML private Button btnGenerate;
	@FXML private ListView<String> nurseListView;
	@FXML private ListView<String> requestListView;
	@FXML private ProgressBar progressBar;
	
	private ObservableList<String> nurseList = FXCollections.observableArrayList();
	private ObservableList<String> requestList = FXCollections.observableArrayList();
	
	private Nurses nurses = new Nurses();
	private Requests requests = new Requests();
	private DashboardModel dashboardModel = new DashboardModel();
	private Progress progress = new Progress();
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		String query1 = "SELECT * FROM Nurses";
		String query2 = "SELECT * FROM Requests";
		try {
			dashboardModel.fetchNurses(query1);
			dashboardModel.fetchRequests(query2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int key : nurses.getNurseList().keySet())
		{
			nurseList.add(key+"-- "+nurses.getNurseList().get(key));
		}
		nurseListView.setItems(nurseList);
		
		
		
		for(int key : requests.getRequestList().keySet())
		{
			requestList.add("Request "+key);
		}
		requestListView.setItems(requestList);
		
		progressBar.setVisible(false);
		progress.setProgress(0);
		progressBar.progressProperty().bind(progress.numberProperty());
	}
	
	
	public void setUserDetails(String name) {
		welcomeMsg.setText("Welcome, "+name);
	}
	
	public void selectNurse(MouseEvent event)
	{
		System.out.print(nurseListView.getSelectionModel().getSelectedItem());
	}
	
	public void selectRequest(MouseEvent event)
	{
		System.out.print(requestListView.getSelectionModel().getSelectedItem());
	}
	
	public void generateSchedule(ActionEvent event) throws InterruptedException, IOException
	{
		progressBar.setVisible(true);
		while(progress.getProgress() < 1)
		{
			progress.setProgress(progress.getProgress() + 0.1);
		}
		
		
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		primaryStage.setTitle("Schedule - Healthcare Scheduling System");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("/img/images.png"));
		Pane root = loader.load(getClass().getResource("/application/Schedule.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void signOut(ActionEvent event) {
		try{
			((Node)event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			primaryStage.setTitle("Login - Healthcare Scheduling System");
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image("/img/images.png"));
			Pane root = loader.load(getClass().getResource("/application/Login.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();	
		}
		catch(Exception e) {
			
		}
		
	}

}
