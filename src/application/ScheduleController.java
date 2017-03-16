package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import data.Allocation;
import data.Scheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ScheduleController implements Initializable {
	
	@FXML private TableView<Allocation> allocationTable;
	@FXML private TableColumn<Allocation, Integer> id;
	@FXML private TableColumn<Allocation, String> bundle;
	@FXML private TableColumn<Allocation, String> cost;
	@FXML private TableColumn<Allocation, String> nurseId;
	@FXML private Label time;
	@FXML private Label totalCostLabel;
	
	private Scheduler scheduler = new Scheduler();
	
	private ObservableList<Allocation> allocation = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		long startTime = System.currentTimeMillis();
		float totalCost=0;
		List<String> tableData = scheduler.getAllocation();
		for(int i=0; i<tableData.size(); i++)
		{
			String row = tableData.get(i);
			String[] split = row.split("-");
			totalCost += Float.parseFloat(split[2]);
			allocation.add(new Allocation(i+1,"Requests: "+split[1], "$"+split[2] ,"Nurse "+split[3]));
		}
		long endTime = System.currentTimeMillis();
		id.setCellValueFactory(new PropertyValueFactory<Allocation, Integer>("id"));
		bundle.setCellValueFactory(new PropertyValueFactory<Allocation, String>("bundle"));
		cost.setCellValueFactory(new PropertyValueFactory<Allocation, String>("cost"));
		nurseId.setCellValueFactory(new PropertyValueFactory<Allocation, String>("nurseId"));
		allocationTable.setItems(allocation);
		
		Scheduler.computationTime += (endTime - startTime) / 1000;
		
		totalCostLabel.setText("Total Cost: $"+totalCost);
		time.setText("Computation TIme: "+Scheduler.computationTime+" seconds");
	}

}
