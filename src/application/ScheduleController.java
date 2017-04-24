package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import entities.Allocation;
import entities.Scheduler;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScheduleController implements Initializable {
	
	@FXML private TableView<Allocation> allocationTable;
	@FXML private TableColumn<Allocation, Integer> id;
	@FXML private TableColumn<Allocation, String> bundle;
	@FXML private TableColumn<Allocation, String> cost;
	@FXML private TableColumn<Allocation, String> nurseId;
	@FXML private Label totalCostLabel;
	@FXML private Button btnBack;
	
	private Scheduler scheduler = new Scheduler();
	
	private ObservableList<Allocation> allocation = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		float totalCost=0;
		int count=0;
		List<String> tableData = scheduler.getAllocation();
		for(int i=0; i<tableData.size(); i++)
		{
			String row = tableData.get(i);
			String[] split = row.split("-");
			totalCost += Float.parseFloat(split[2]);
			if(split[3]  == "backup")
			{
				allocation.add(new Allocation(i+1,"Request: "+split[1], "$"+split[2] ,"Backup Nurse "+count++));
			}
			else
			{
				allocation.add(new Allocation(i+1,"Requests: "+split[1], "$"+split[2] ,"Nurse "+split[3]));
			}
		}
		id.setCellValueFactory(new PropertyValueFactory<Allocation, Integer>("id"));
		bundle.setCellValueFactory(new PropertyValueFactory<Allocation, String>("bundle"));
		cost.setCellValueFactory(new PropertyValueFactory<Allocation, String>("cost"));
		nurseId.setCellValueFactory(new PropertyValueFactory<Allocation, String>("nurseId"));
		allocationTable.setItems(allocation);
		
		totalCostLabel.setText("Total Cost: $"+totalCost);
	}
	
	public void goBack(ActionEvent event) throws IOException
	{
		Scheduler.allocation.clear();
		Scheduler.allocation2.clear();
		Scheduler.total1 = 0;
		Scheduler.total2 = 0;
		
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		primaryStage.setTitle("Schedule - Healthcare Scheduling System");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("/img/images.png"));
		Pane root = loader.load(getClass().getResource("/application/Dashboard.fxml").openStream());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
