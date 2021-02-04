package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class second_c {

    String pw1;
    String pw2;
    @FXML
    private TextField name_field;
    @FXML
    private TextField surname_field;
    @FXML
    private TextField contact_field;
    @FXML
    private Label Donator_name;
    @FXML
    private Label Donater_Surname;
    @FXML
    private Label Contact_number;
    @FXML
    private TextField ssn;
    @FXML
    private TextField item_name;
    @FXML
    private TextField item_description;
    @FXML
    private TextField amount;
    @FXML
    private Button donator_button;

    @FXML
    private MenuItem list_shipment;
    @FXML
    private Button req_pw;

    @FXML
    private TextField first_pw;

    @FXML
    private TextField repe_pw;

    @FXML
    private Button change_pw;
    @FXML
    private Button log_out;
    @FXML
    private Menu Employee;
    @FXML
    private MenuItem emp_hire;
    @FXML
    private Menu Donor;

    @FXML
    private MenuItem donor_list;

    static String string_ssn;
    String dona_name;
    String dona_surname;
    String dona_contact;
    String it_name;
    String it_description;
    String it_contact;

    @FXML
    private TextField xd;

    String username;
    String password;
    String user_type;
    
    Qqueries quer = new Qqueries();
    Connection con = quer.getConn();



    @FXML
    public void set_xd(String u_name, String u_pass, String u_type) {
        username = u_name;
        password = u_pass;
        user_type = u_type;
    }

    @FXML
    public void set_manager() {
        Employee.setVisible(true);
        Employee.setDisable(false);
        emp_hire.setVisible(true);
        emp_hire.setDisable(false);
        Donor.setVisible(true);
        donor_list.setVisible(true);
        Donor.setDisable(false);
        donor_list.setDisable(false);
    }

    @FXML
    private void donation_add(ActionEvent event) {

        string_ssn = ssn.getText();
 
        if (string_ssn.length() == 0) {

            new Alert(Alert.AlertType.ERROR, "Did not enter any SSN !").showAndWait();
        } else if (string_ssn.length() != 11) {

            new Alert(Alert.AlertType.ERROR, "SSN is not long enough !").showAndWait();
        } else
            for (int i = 0; i < string_ssn.length(); i++) {
               
                if (string_ssn.charAt(i) < '0' || string_ssn.charAt(i) > '9') {
             

                    new Alert(Alert.AlertType.ERROR, "Not valid SSN").showAndWait();
                    break;
                }
            }
        
        
        it_name = item_name.getText();
        it_description = item_description.getText();
        it_contact = amount.getText();

        if (quer.print_1_INTEGER(quer.In_1_STRING_startQuery(quer.getIsInDonor(), quer.getConn(), string_ssn)) == 0) {
            name_field.setDisable(false);
            surname_field.setDisable(false);
            contact_field.setDisable(false);
            Donater_Surname.setDisable(false);
            Donator_name.setDisable(false);
            Contact_number.setDisable(false);
            donator_button.setDisable(false);          
            
        }else {
        	Calendar calendar = Calendar.getInstance();
        	java.util.Date currentTime = calendar.getTime();
        	long time = currentTime.getTime();
        	
        	Timestamp stp = new Timestamp(time);
        	java.sql.Date dt = new java.sql.Date(stp.getTime());
             
        	try {
        		
        		int isinstok = quer.print_1_INTEGER(quer.In_1_STRING_startQuery(quer.getIsInStok(), quer.getConn(), it_name));
        		if(isinstok == 0) {
        			TextInputDialog dialog = new TextInputDialog("Category");
        			dialog.setTitle("Text Category");
        			Optional<String> result = dialog.showAndWait();
        			quer.NoPrint(quer.In_2_STRING_1_INTEGER_startQuery(quer.getAddToStok(), quer.getConn(), result.get(), it_name, 0));
        		}
        		
        		quer.NoPrint(quer.In_1_DATE_3_STRING_1_INTEGER_startQuery(quer.getInsertDonation(), quer.getConn(), dt, string_ssn, it_name, it_description, Integer.parseInt(amount.getText())));
        	}catch(NumberFormatException en) {
        		
        	}catch(Exception ex) {
        		
        	}

        } 
        
        
    }

    @FXML
    void donator_add(ActionEvent event) {
        dona_name = name_field.getText();
        dona_surname = surname_field.getText();
        dona_contact = contact_field.getText();
        name_field.setDisable(true);
        surname_field.setDisable(true);
        contact_field.setDisable(true);
        Donater_Surname.setDisable(true);
        Donator_name.setDisable(true);
        Contact_number.setDisable(true);
        donator_button.setDisable(true);
        name_field.clear();
        surname_field.clear();
        contact_field.clear();
        
        
        Calendar calendar = Calendar.getInstance();
    	java.util.Date currentTime = calendar.getTime();
    	long time = currentTime.getTime();
    	
    	Timestamp stp = new Timestamp(time);
    	java.sql.Date dt = new java.sql.Date(stp.getTime());
        
        quer.NoPrint(quer.In_4_STRING_1_DATE_startQuery(quer.getAddToDonor(), quer.getConn(), dona_name, dona_surname, dona_contact, string_ssn,dt));
        quer.NoPrint(quer.In_1_DATE_3_STRING_1_INTEGER_startQuery(quer.getInsertDonation(), quer.getConn(), dt, string_ssn, it_name, it_description, Integer.parseInt(amount.getText())));
        
        item_name.clear();
        ssn.clear();
        item_description.clear();
        amount.clear();
        
        new Alert(Alert.AlertType.CONFIRMATION, "Donater Added !").showAndWait();
        
    }
    @FXML
    void show_donation_list(ActionEvent event) throws IOException {
        Stage another_stage = new Stage();
        Parent another_root = FXMLLoader.load(getClass().getResource("donation_l.fxml"));
        Scene scene = new Scene(another_root);
        another_stage.setScene(scene);
        another_stage.show();
    }
    @FXML
    void show_shipment_list(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shipment.fxml"));
		Parent root = (Parent)fxmlLoader.load();
    	if(user_type.equals("Manager"))
    	{
    		ship_basic_controller controller = fxmlLoader.<ship_basic_controller>getController();
    		controller.set_enables();
    	}
        Stage another_stage = new Stage();   
        Scene scene = new Scene(root);
        another_stage.setScene(scene);
      
     
        another_stage.show();
    }

    @FXML
    void pw_change(ActionEvent event) {
            pw1 = first_pw.getText();
            pw2 = repe_pw.getText();
        if(pw1.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Do not leave empty").showAndWait();
            first_pw.clear();
            repe_pw.clear();
        }
        else if(pw1.equals(pw2)){
            new Alert(Alert.AlertType.CONFIRMATION, "Password changed successfully !").showAndWait();
            first_pw.setDisable(true);
            first_pw.setVisible(false);
            repe_pw.setDisable(true);
            repe_pw.setVisible(false);
            change_pw.setVisible(false);
            change_pw.setDisable(true);
            first_pw.clear();
            repe_pw.clear();
            
            quer.NoPrint(quer.In_2_STRING_startQuery(quer.getChangePwGivenID(), quer.getConn(), pw1,username));
            
        }
        else{
            new Alert(Alert.AlertType.ERROR, "Enter same new password").showAndWait();
            first_pw.clear();
            repe_pw.clear();
        }
    }

    @FXML
    void req_pw_func(ActionEvent event) {
        first_pw.setDisable(false);
        first_pw.setVisible(true);
        repe_pw.setDisable(false);
        repe_pw.setVisible(true);
        change_pw.setVisible(true);
        change_pw.setDisable(false);
        
   
        
    }
    @FXML
    void log_out_function(ActionEvent event) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        Scene new_scene = new Scene(root);
        window.setScene(new_scene);
    }
    @FXML
    void employee_scene(ActionEvent event) throws IOException {
        Stage another_stage = new Stage();
        Parent another_root = FXMLLoader.load(getClass().getResource("emp_hire.fxml"));
        Scene scene = new Scene(another_root);
        another_stage.setScene(scene);
        another_stage.show();
    }
    @FXML
    void show_donor_list(ActionEvent event) throws IOException {
        Stage another_stage = new Stage();
        Parent another_root = FXMLLoader.load(getClass().getResource("donor.fxml"));
        Scene scene = new Scene(another_root);
        another_stage.setScene(scene);
        another_stage.show();

    }
}
