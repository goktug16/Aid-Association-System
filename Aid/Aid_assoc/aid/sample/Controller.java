package sample;

import com.sun.javafx.tk.TKStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.util.logging.ConsoleHandler;

public class Controller {
    String selected = "Employee";
    @FXML
    private second_c second_c_controller;
    @FXML
    private RadioButton manager;
    @FXML
    private RadioButton employee;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
     
    Qqueries quer = new Qqueries();
    Connection con = quer.getConn();
    
    @FXML
    private void deneme(ActionEvent event) throws IOException {
        event.consume();
 
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("yorgun.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        //----------------
        String den = username.getText();
        String den2 = password.getText();
        
       int id = quer.print_1_INTEGER(quer.In_1_STRING_startQuery(quer.getIsInEmp(), quer.getConn(), den));
       if(id == 0) {
    	   new Alert(Alert.AlertType.ERROR, "No such Employee in system").showAndWait();
       }else {
       String pw = quer.print_1_STRING(quer.In_1_STRING_startQuery(quer.getGivePwGivenID(), quer.getConn(),den));
       
       if(!pw.equals(den2)) {
    	   new Alert(Alert.AlertType.ERROR, "Username password mismatch").showAndWait();
       }else {
        
	        second_c controller = fxmlLoader.<second_c>getController();
	        controller.set_xd(den,den2,selected);

	        
	        
	        if(quer.print_1_BOOL(quer.In_1_STRING_startQuery(quer.getIsManager(), quer.getConn(), den))) {
	        	if(selected.equals("Manager")) {
	        		controller.set_manager();
	        		Scene new_scene = new Scene(root);
	    	        window.setScene(new_scene);
	        		window.show();
	        	}
	        	else {
	        		 new Alert(Alert.AlertType.ERROR, "You seem like manager").showAndWait();
	        	}
	        }
	        else {
	        	if(selected.equals("Manager")) {
	        		 new Alert(Alert.AlertType.ERROR, "You are not manager").showAndWait();
	        	}else {
	        		if(employee.isSelected()) {
		        		Scene new_scene = new Scene(root);
		    	        window.setScene(new_scene);
		        		window.show();
	        		}else {
	        			new Alert(Alert.AlertType.ERROR, "Choose a button").showAndWait();
	        		}
	        	}
	        }
	        
        }
       }
    }

    @FXML
    void select(ActionEvent event) {
        ToggleGroup group = new ToggleGroup();
        manager.setToggleGroup(group);    
        employee.setToggleGroup(group);
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        selected = selectedRadioButton.getText();
        selectedRadioButton.setSelected(true);
    }

}
