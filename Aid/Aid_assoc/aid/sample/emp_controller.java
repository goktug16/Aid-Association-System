package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class emp_controller implements Initializable {
    @FXML
    private TableView<Employee> employee_table;
    @FXML
    private TableColumn<Employee, String> ssn;

    @FXML
    private TableColumn<Employee, String> mssn;

    @FXML
    private TableColumn<Employee, String> emp_name;

    @FXML
    private TableColumn<Employee, String> emp_surname;

    @FXML
    private TableColumn<Employee, String> employment;
    @FXML
    private RadioButton employee;

    @FXML
    private RadioButton volunteer;
    @FXML
    private TextField ssn_text;

    @FXML
    private TextField mssn_text;

    @FXML
    private TextField emp_name_text;

    @FXML
    private TextField emp_surname_text;
    String selected = "Volunteer";
    @FXML
    private Button fire_emp;
     ObservableList<Employee>  people = FXCollections.observableArrayList();
     
     Qqueries quer = new Qqueries();
     Connection con = quer.getConn();
     ResultSet rsl;
    
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ssn.setCellValueFactory(new PropertyValueFactory<Employee, String>("ssn"));

        mssn.setCellValueFactory(new PropertyValueFactory<Employee,String>("mssn"));

        emp_name.setCellValueFactory(new PropertyValueFactory<Employee,String>("emp_name"));

        emp_surname.setCellValueFactory(new PropertyValueFactory<Employee,String>("emp_surname"));

        employment.setCellValueFactory(new PropertyValueFactory<Employee,String>("emp_type"));
        
        volunteer.setSelected(true);
        
        employee_table.setItems(getPeople());
        

    }



    @FXML
    void fire_emp_function(ActionEvent event) {
        Employee selected_item = employee_table.getSelectionModel().getSelectedItem();
        
        if(!quer.print_1_BOOL(quer.In_1_STRING_startQuery(quer.getIsManager(), quer.getConn(), selected_item.getSsn()))) {
        	quer.DELETE_In_1_STRING_startQuery(quer.getDeleteEmployee(), quer.getConn(), selected_item.getSsn());
	        employee_table.getItems().remove(selected_item);
	        new Alert(Alert.AlertType.CONFIRMATION, selected_item.getEmp_name() + " employee fired ").showAndWait();
        }else {
        	new Alert(Alert.AlertType.ERROR, "Can not fire manager").showAndWait();
        }
    }
    @FXML
    void hire_emp_function(ActionEvent event) {
    		if(ssn_text.getText().isEmpty())
    		{
    			 new Alert(Alert.AlertType.ERROR, "Do not leave empty ssn section").showAndWait();
    		}
    		else
    		{
            Employee emp =new Employee(ssn_text.getText(),mssn_text.getText(),emp_name_text.getText(),emp_surname_text.getText(),selected);
            people.add(emp);
            quer.Add_4_STRING_1_BOOL_startQuery(quer.getAddEmployee(), quer.getConn(), ssn_text.getText(), mssn_text.getText(), emp_name_text.getText(), emp_surname_text.getText(), selected.equals("Employee"));
            ssn_text.clear();
            mssn_text.clear();
            emp_name_text.clear();
            emp_surname_text.clear();
            employee_table.getItems().clear();
            employee_table.setItems(getPeople());
            new Alert(Alert.AlertType.CONFIRMATION, "Employee " + emp.getEmp_name() + "'s login infos has created with trigger" ).showAndWait();
    		}
    }
    @FXML
    void select_type(ActionEvent event) {
        ToggleGroup group = new ToggleGroup();
        volunteer.setToggleGroup(group);
        volunteer.setSelected(true);
        employee.setToggleGroup(group);
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        selected = selectedRadioButton.getText();
    }
    @FXML
    void ship_activities(ActionEvent event) throws IOException {
        Stage another_stage = new Stage();
        Parent another_root = FXMLLoader.load(getClass().getResource("shipment_add_delete.fxml"));
        Scene scene = new Scene(another_root);
        another_stage.setScene(scene);
        another_stage.show();
    }
    
    
    public ObservableList<Employee>  getPeople()
    {
        ObservableList<Employee> people = FXCollections.observableArrayList();
        rsl = quer.startQuery(quer.getListAllemp_vol(), quer.getConn());
        try{while(rsl.next()) {
        	String ssn = rsl.getString(1);
        	String mssn = rsl.getString(2);
        	String e_name = rsl.getString(3);
        	String e_lastname = rsl.getString(4);
        	boolean is_emp = rsl.getBoolean(5);
        	
        	people.add(new Employee(ssn,mssn,e_name,e_lastname,String.valueOf(is_emp)));
        	
        }}catch(SQLException e) {
        	new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
        }
        

        return people;
    }
 
}
