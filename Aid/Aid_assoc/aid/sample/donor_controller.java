package sample;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class donor_controller implements Initializable {

	    @FXML
	    private TableView<Donor> donor_table;

	    @FXML
	    private TableColumn<Donor, String> donor_name;

	    @FXML
	    private TableColumn<Donor, String> donor_lastname;

	    @FXML
	    private TableColumn<Donor, String> donor_contact;

	    @FXML
	    private TableColumn<Donor, String> donor_ssn;

	    @FXML
	    private TableColumn<Donor, String> last_donation;
	    @FXML
	    private ComboBox<String> Top;
	    
	    Qqueries quer = new Qqueries();
	    Connection con = quer.getConn();
	    ResultSet rsl;

	    
	    @Override
	    public void initialize(URL url, ResourceBundle resourceBundle) {

	        donor_name.setCellValueFactory(new PropertyValueFactory<Donor, String>("donor_name"));

	        donor_lastname.setCellValueFactory(new PropertyValueFactory<Donor,String>("donor_lastname"));

	        donor_contact.setCellValueFactory(new PropertyValueFactory<Donor,String>("contact"));

	        donor_ssn.setCellValueFactory(new PropertyValueFactory<Donor,String>("donor_ssn"));

	        last_donation.setCellValueFactory(new PropertyValueFactory<Donor,String>("last_time_donate"));
	    
	        Top.getItems().addAll("1","2","3","4","5");
	      
	        donor_table.setItems(getDonor());
	        
	        	
	    }
	    public ObservableList<Donor>  getDonor()
	    {
	        ObservableList<Donor> people = FXCollections.observableArrayList();	 
	        rsl = quer.startQuery(quer.getList_all_donorinfo(), quer.getConn());
	        try{while(rsl.next()) {
	        	people.add(new Donor(rsl.getString(1),rsl.getString(2),rsl.getString(3),rsl.getString(4),rsl.getDate(5).toString()));
	        }}catch(SQLException ex) {
	        	new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
	        }

	        return people;
	    }
	    @FXML
	    void show_top_donators(ActionEvent event) {	
	    	if(Top.getValue() != null)
	    	{
	    	String str = quer.print_1_STRING(quer.In_1_INTEGER_startQuery(quer.getFunction_listeTopDonator(), quer.getConn(), Integer.parseInt(Top.getValue())));
	    	ObservableList<Donor> find =  FXCollections.observableArrayList();
	     	String [] strs = str.split(",");
	     	ArrayList<String> list = new ArrayList<String>();
	   
	    	for(String t : strs)
	    	{
	    		t= t.replaceAll("[^0-9a-zA-Z_\s ^-]", "");
	    		list.add(t);
	    	}
	    	
	    	for(int i=0; i<list.size() ; i= i+5)
	    		find.add(new Donor(list.get(i),list.get(i+1),list.get(i+2),list.get(i+3),list.get(i+4)));
	    	donor_table.getItems().clear();
	    	donor_table.setItems(find);
	    	}
	    	else
	    		new Alert(Alert.AlertType.ERROR,"Select top value").showAndWait();
	    }

}
