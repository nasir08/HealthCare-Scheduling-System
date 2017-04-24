package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Vector;

import entities.Bundle;
import entities.Nurse;
import entities.Patient;
import entities.Progress;
import entities.Scheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.DashboardModel;

public class DashboardController implements Initializable {

	@FXML public Label welcomeMsg;
	@FXML public Label status;
	@FXML public Button btnGenerate;
	@FXML public Button btnReadFile;
	@FXML public ListView<String> nurseListView;
	@FXML public ListView<String> requestListView;
	@FXML public ProgressBar progressBar;
	
	public ObservableList<String> nurseList = FXCollections.observableArrayList();
	public ObservableList<String> requestList = FXCollections.observableArrayList();
	
	public List<Entry<Map<Integer,Bundle>,Double>> pool;
	public HashMap<Integer,Patient> patients;
	public ArrayList<Nurse> nurses;
	public ArrayList<Bundle> bundles;
	int bundlesize;
	public ArrayList<Bundle> potentialOpt;
	public ArrayList<Integer> potentialOpt2;
	public ArrayList<Bundle> overlaps;
	public Map<Integer,Bundle> schedule;
	public Vector<Patient> scheduledPatients;
	public Vector<Patient> scheduledPatients2;
	public double totalSaving;
	public DashboardModel dashboardModel;
	public Scheduler scheduler;
	public double totalCost1;
	public double totalCost2;
	public HashMap<Nurse,Bundle> newPotentialOptimal;
	public HashMap<Nurse,Integer> loopState;
	public HashMap<Nurse,Integer> loopState2;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		btnGenerate.setVisible(false);
		status.setVisible(false);
		totalCost1 = 0;
		totalCost2 = 0;
	}
	
	public void readFile(ActionEvent event) throws SQLException
	{
		status.setVisible(true);
		status.setText("Loading Data...");
		XSSFSheet sheet = null;
		Iterator<Row> rowIterator = null;
		
		dashboardModel = new DashboardModel();
		nurseListView.getItems().clear();
		requestListView.getItems().clear();
		
		String reset1 = "DELETE FROM Requests";
		String reset2 = "DELETE FROM Nurses";
		String reset3 = "DELETE FROM Bundles";
		
		dashboardModel.reset(reset1,reset2,reset3);
		
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("Excel Files", "*.xlsx"),
				new ExtensionFilter("Excel 97-2003 Files", "*.xls"),
				new ExtensionFilter("CSV Files", "*.csv"));
		File selectedFile = fc.showOpenDialog(null);
		
		if(selectedFile != null)
		{
		String filePath = selectedFile.getAbsolutePath().replace("\\", "\\\\");
		
		
		
		try {
	        FileInputStream file = new FileInputStream(new File(filePath));

	        //Create Workbook instance holding reference to .xlsx file
	        XSSFWorkbook workbook = new XSSFWorkbook(file);

	        //Get first/desired sheet from the workbook
	        sheet = workbook.getSheetAt(0);

	        //Iterate through each rows one by one
	        rowIterator = sheet.iterator();
	        while (rowIterator.hasNext())
	        {
	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.getColumnIndex() != 0 && cell.getRowIndex() !=0) 
	                {
	                    String query = "INSERT INTO Requests (StandardPrice) VALUES ("+cell.getNumericCellValue()+")";
	                    dashboardModel.insertRequests(query);	
	                }
	            }
	        }
	        
	        
	        sheet = workbook.getSheetAt(1);

	        //Iterate through each rows one by one
	        rowIterator = sheet.iterator();
	        int nurseCounter = 0;
	        while (rowIterator.hasNext())
	        {
	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                if(cell.getCellType() != Cell.CELL_TYPE_BLANK  && cell.getColumnIndex() != 0 && cell.getRowIndex() == 0) 
	                {
	                	nurseCounter++;
	                    String query = "INSERT INTO Nurses (Name) VALUES (?)";
	                    dashboardModel.insertNurses(query,cell.getStringCellValue());	
	                }
	            }  
	    }
	        System.out.println(nurseCounter);
	        
	        
	        sheet = workbook.getSheetAt(1);
	        //Iterate through each rows one by one
	        rowIterator = sheet.iterator();
	        while (rowIterator.hasNext())
	        {
	        	int cellCounter = 0;
            	int nurseCount = 0;
            	String bundle = "";
            	double cost = 0;
            	
            	int trackNurseCount = 0;
            	String trackBundle = "";
            	double trackCost = 0;
	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                if(nurseCount == nurseCounter) { nurseCount = 0;}
	                if(cellCounter == 2) { cellCounter = 0;}
	                if(cellCounter == 1) { nurseCount++;  }
	                		if(cell.getColumnIndex() != 0 && cell.getRowIndex() > 1) 
	                		{
	                			if(cell.getCellType() == Cell.CELL_TYPE_STRING)
	                			{
	                				bundle = cell.getStringCellValue();
	                				cellCounter++;
	                			}
	                			else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
	                			{
	                				cost = cell.getNumericCellValue();
	                				cellCounter++;
	                			}
	                			
	                			else if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
	                			{
	                				cellCounter++;
	                			}
	                			
	                			if(cellCounter == 2 && bundle!="")
	                			{
	                				if(trackBundle != bundle && trackCost != cost && trackNurseCount != nurseCount)
	                				{
	                					String query = "INSERT INTO Bundles (VisitBundle,Cost,nurseID) VALUES (?,"+cost+","+nurseCount+")";
	                					dashboardModel.insertBundles(query, bundle);
	                					System.out.println(bundle+"-"+cost+"-"+nurseCount);
	                				}
	                				trackBundle = bundle;
	                				trackCost = cost;
	                				trackNurseCount = nurseCount;
	                			}
	                			
	                		}
	             }
	        }
	        
		
		
	        file.close();
		}catch (Exception e) {
	        e.printStackTrace();
	    }
		}
		
		String query1 = "SELECT * FROM Nurses";
		String query2 = "SELECT * FROM Requests";
		try {
			patients = dashboardModel.fetchPatients(query2);
			nurses = dashboardModel.fetchNurses(query1);
			bundles = new ArrayList<Bundle>();
			for(Nurse n : nurses)
			{
				for(Bundle b: n.getBundles())
				{
					bundles.add(b);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int key : patients.keySet())
		{
			requestList.add("Request "+patients.get(key).getID());
		}
		requestListView.setItems(requestList);
		
		
		for(Nurse nurse : nurses)
		{
			nurseList.add(nurse.getID()+"-- "+nurse.getName());
		}
		nurseListView.setItems(nurseList);
		
		status.setText("Loading Complete");
		btnGenerate.setVisible(true);
		bundlesize = bundles.size();
	}
	
	
	public void setUserDetails(String name) {
		welcomeMsg.setText("Welcome, "+name);
	}
	
	public void selectNurse(MouseEvent event)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		String[] split = nurseListView.getSelectionModel().getSelectedItem().split("--");
		int id = Integer.parseInt(split[0]);
		alert.setTitle("N"+id+" Bundles");
		alert.setHeaderText("List of Bundles for N"+id);
		int index = id-1;
		
		Nurse n = nurses.get(index);
		ArrayList<Bundle> list  = n.getBundles();
		String s = "";
		
		for(Bundle b : list)
		{
			for(Patient pa:b.getPatients()) 
			{
				s += pa.getID()+",";
				
			}
			s += " --- Cost: $"+b.price+"\n";
		}
		alert.setContentText(s);
		alert.show();
	}
	
	public void selectRequest(MouseEvent event)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		
		String[] split = requestListView.getSelectionModel().getSelectedItem().split(" ");
		int id = Integer.parseInt(split[1]);
		
		alert.setTitle(requestListView.getSelectionModel().getSelectedItem()+" Information");
		alert.setHeaderText(requestListView.getSelectionModel().getSelectedItem()+" cost");
		
		String s = "";
		
		s = "$"+patients.get(id).PRICE+"";
		
		alert.setContentText(s);
		alert.show();
	}
	
	
	public void doStrategy1(Map<Map<Integer,Bundle>,Double> sortedPool) 
	{
		scheduler = new Scheduler();
		scheduledPatients = new Vector<Patient>();
		int count = 0; // counts the number of assigned nurses
		   
		   while(!nurses.isEmpty() && sortedPool.size() != 0)
		   {
			   
			   @SuppressWarnings("unchecked")
			   Map<Integer,Bundle> m = (Map<Integer,Bundle>) sortedPool.keySet().toArray()[0];
			   int id = (int) m.keySet().toArray()[0];
			    
			    Bundle b = m.get(id);
			    
			    String bundle="";
				for(Patient pa:b.getPatients()) 
			    {
			    	bundle += pa.getID()+",";
			    	scheduledPatients.add(pa);
			    }
			    
				scheduler.createAllocation(count+"-"+bundle+"-"+b.price+"-"+id);
				totalCost1 += b.price;
			    
			    removeOverlaps(sortedPool,b,id);
			    
			    int comp = id-(++count);
			    
			    if(comp >= 0 && !nurses.isEmpty()) nurses.remove(comp);
		   }
		   
		   if(sortedPool.isEmpty())
		   {   
			   for(int key : patients.keySet()){
				   
				   if(!scheduledPatients.contains(patients.get(key)))
				   {
					   Vector<Patient> vec = new Vector<Patient>();
					   vec.add(patients.get(key));
					   scheduler.createAllocation(count+"-"+patients.get(key).getID()+"-"+patients.get(key).PRICE+"-backup");
					   totalCost1 += patients.get(key).PRICE;
				   }
			   }
		   } 
		   scheduler.total1 = totalCost1;
	}
	
	public void doStrategy2()
	{
		potentialOpt = new ArrayList<Bundle>();
		scheduledPatients2 = new Vector<Patient>();
		overlaps = new ArrayList<Bundle>();
		scheduler = new Scheduler();
		int count = 0;
		for(Nurse nurse: nurses)
		{
			Bundle b = nurse.getLowestCostBundle1();
			
			if(!checkOverlap(b))
			{
				potentialOpt.add(b);
				String bundle = "";
				for(Patient pa:b.getPatients()) 
				{
					bundle += pa.getID()+",";
					scheduledPatients2.add(pa);
				}
		    
				count++;
				scheduler.createAllocation2(count+"-"+bundle+"-"+b.price+"-"+nurse.getID());
				totalCost2 += b.price;
			}
		}
		
		for(int key : patients.keySet())
		{
			   
			   if(!scheduledPatients2.contains(patients.get(key)))
			   {
				   Vector<Patient> vec = new Vector<Patient>();
				   vec.add(patients.get(key));
				   scheduler.createAllocation2(count+"-"+patients.get(key).getID()+"-"+patients.get(key).PRICE+"-backup");
				   totalCost2 += patients.get(key).PRICE;
			   }
		}
		scheduler.total2 = totalCost2;
	}
	
	public void generateSchedule(ActionEvent event) throws InterruptedException, IOException
	{
		doStrategy2();
		createPool();
		doStrategy1(sortPool());
		
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
	
	public void createPool()
	{
		Map<Map<Integer,Bundle>,Double> m = new HashMap<Map<Integer,Bundle>,Double>();
		for(Nurse n : nurses)
		{
			for(Bundle b: n.getBundles())
			{
				Map<Integer,Bundle> key = new HashMap<Integer,Bundle>();
				key.put(n.getID(), b);
				m.put(key, b.getSaving());		
			}
			
		}
		pool = new LinkedList<Entry<Map<Integer,Bundle>,Double>>(m.entrySet());
	}
	
	public Map<Map<Integer,Bundle>,Double> sortPool()
	{
		
		 // Sorting the list based on values
      Collections.sort(pool, new Comparator<Entry<Map<Integer,Bundle>,Double>>()
      {
          public int compare(Entry<Map<Integer,Bundle>,Double> o1,
          		Entry<Map<Integer,Bundle>,Double> o2)
          {
          	return o2.getValue().compareTo(o1.getValue()); 
          }
      });
      
      Map<Map<Integer,Bundle>,Double> sortedPool = new LinkedHashMap<Map<Integer,Bundle>,Double>();
      for (Entry<Map<Integer,Bundle>,Double> entry : pool)
      {
      	sortedPool.put(entry.getKey(), entry.getValue());
      }
      
      return sortedPool;
	}
	
	
	public void removeOverlaps(Map<Map<Integer,Bundle>,Double> sortedPool, Bundle b, int id)
	{
		
	Iterator<Entry<Map<Integer,Bundle>,Double>> it = sortedPool.entrySet().iterator();
	while(it.hasNext())
	{
		Map<Integer,Bundle> m1 = it.next().getKey();
		int compID = (int) m1.keySet().toArray()[0];
	    Bundle bd = m1.get(compID);
	    	
	    if(id == compID)
	    {
	    	it.remove();
	    }
	    else if(b.overlap(bd))
	    {
	    	it.remove();
	    }	
	}
	}
	
	public boolean checkOverlap(Bundle b)
	{
		boolean returnStmt = false;
		for(int i=0; i<potentialOpt.size(); i++)
		{
			if(!b.equals(potentialOpt.get(i)) && b.overlap(potentialOpt.get(i)))
			{
				returnStmt = true;
			}
		}
		return returnStmt;
	}
		
		
		/*public Map<Integer,Bundle> makeSchedule(Map<Map<Integer,Bundle>,Double> sortedPool){
			
			   int count = 0; // counts the number of assigned nurses
			   
			   while(!nurses.isEmpty() && sortedPool.size() != 0){
				   
				   @SuppressWarnings("unchecked")
				   Map<Integer,Bundle> m = (Map<Integer,Bundle>) sortedPool.keySet().toArray()[0];
				   int id = (int) m.keySet().toArray()[0];
				   
				   System.out.println("N"+id+" is being scheduled, pool size: " + sortedPool.size());
				   System.out.println("Pool: "+sortedPool);
				    
				    Bundle b = m.get(id);
				    schedule.put(id, b);
				    
				    removeOverlaps(sortedPool,b,id);
				    
				    totalSaving += b.getSaving();
				    
				    int comp = id-(++count);
				    
				    if(comp >= 0 && !nurses.isEmpty()) nurses.remove(comp);
				    
				    for(Patient pa:b.getPatients()) {
				    	scheduledPatients.addElement(pa);
				    }
			   }
			   
			   if(!sortedPool.isEmpty()){
				   
				   scheduleBackupNurses();
			   }  
				
				return schedule;
			}*/
		
}
