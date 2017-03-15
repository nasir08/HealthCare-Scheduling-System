package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import data.Agent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.LoginModel;

public class LoginController implements Initializable {

	public LoginModel loginModel = new LoginModel();
	
	@FXML private Label connectionStatus;
	@FXML private TextField txtUserName;
	@FXML private PasswordField txtPassword;
	private Agent agent = new Agent();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(loginModel.isDbConnected()) {
			connectionStatus.setText("Database Connection is Active");
		}
		else{
			connectionStatus.setText("Database Connection Not Active");
		}
	}
	
	public void login(ActionEvent event) {
		try {
			if(loginModel.login(txtUserName.getText(), txtPassword.getText())) {
				((Node)event.getSource()).getScene().getWindow().hide();
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				primaryStage.setTitle("Dashboard - Healthcare Scheduling System");
				primaryStage.setResizable(false);
				primaryStage.getIcons().add(new Image("/img/images.png"));
				Pane root = loader.load(getClass().getResource("/application/Dashboard.fxml").openStream());
				DashboardController dashboardController = loader.getController();
				dashboardController.setUserDetails(agent.getAgentName());
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();	
			}
			else {
			connectionStatus.setText("User not found");
			}
		} 
		catch (SQLException e) {
			connectionStatus.setText("User not found");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
