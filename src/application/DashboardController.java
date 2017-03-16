package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import data.Nurses;
import data.Progress;
import data.Requests;
import data.Scheduler;
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
	private Scheduler scheduler = new Scheduler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		String query1 = "SELECT * FROM Nurses";
		String query2 = "SELECT * FROM Requests";
		String query3 = "SELECT COUNT(nurseID) As noOfBundles FROM Bundles GROUP BY nurseID";
		String query4 = "SELECT * FROM Bundles ORDER BY nurseID, Cost ASC";
		try {
			dashboardModel.fetchNurses(query1);
			dashboardModel.fetchRequests(query2);
			dashboardModel.fetchNoOfBundles(query3);
			dashboardModel.fetchBundles(query4);
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
		long startTime = System.currentTimeMillis();
		int count=0;
		int start = 0;
		int j=0;
		String bundle="";
		double cost = 1000000000;
		int bundleID=0;
		int nurseID=0;
		
		for(int i=0; i<nurses.getNurseList().size(); i++)
		{
			count+=nurses.getNoOfBundles(i);
			for(j=start; j<count; j++)
			{
				String string = nurses.getPoolItem(j);
				String[] split = string.split("-");
				if((Double.parseDouble(split[2]) < cost))
				{
					bundleID = Integer.parseInt(split[0]);
					bundle = split[1];
					cost = Double.parseDouble(split[2]);
					nurseID = Integer.parseInt(split[3]);
				}
			}
			start = j;
			scheduler.createAllocation(bundleID+"-"+bundle+"-"+cost+"-"+nurseID);
			cost=1000000000;
			progress.setProgress(progress.getProgress() + 0.2);
		}
		
		scheduler.extractRequests();
		
		for(int key: requests.getRequestList().keySet())
		{
			String keyString = Integer.toString(key);
			if(!Scheduler.extractedRequests.contains(keyString))
			{
				scheduler.createAllocation("0-"+key+"-"+requests.getRequestList().get(key)+"-Backup Nurse");
			}
		}
		
		long endTime = System.currentTimeMillis();
		Scheduler.computationTime = (endTime - startTime);
		System.out.println(Scheduler.computationTime);
		System.out.print(Scheduler.computationTime/1000);
		
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
