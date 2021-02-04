package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class shipment_controller implements Initializable {
    @FXML
    private TableView<Shipment> shipment_table;

    @FXML
    private TableColumn<Shipment,String> from;

    @FXML
    private TableColumn<Shipment,String> to;

    @FXML
    private TableColumn<Shipment,String> item;

    @FXML
    private TableColumn<Shipment,String> date;

    @FXML
    private TableColumn<Shipment,String> plate;
    @FXML
    private TableView<Shipment> selected_table;

    @FXML
    private TableColumn<Shipment,String> selected_from;

    @FXML
    private TableColumn<Shipment,String> selected_category;

    @FXML
    private TableColumn<Shipment,String> selected_item;

    @FXML
    private TableColumn<Shipment,String> selected_date;

    @FXML
    private TableColumn<Shipment,String> selected_plate;
    @FXML
    private TextField to_find;
    @FXML
    private TextField from_text;
    @FXML
    private TextField to_text;
    @FXML
    private DatePicker date_text;
    @FXML
    private TextField plate_text;
    @FXML
    private Button add_shipment;
    @FXML
    private Button delete_shipment;
    @FXML
    private DatePicker to_since;
    @FXML
    private DatePicker to_until;
    @FXML
    private TextField count_to;


    
    Qqueries quer = new Qqueries();
    Connection con = quer.getConn();
    ResultSet rsl;

    ObservableList<Shipment> people =  FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        from.setCellValueFactory(new PropertyValueFactory<Shipment, String>("ship_from"));

        to.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_to"));

        item.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_item"));

        date.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_date"));

        plate.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_plate"));
        
        selected_from.setCellValueFactory(new PropertyValueFactory<Shipment, String>("ship_from"));
        selected_item.setCellValueFactory(new PropertyValueFactory<Shipment, String>("ship_item"));
        selected_date.setCellValueFactory(new PropertyValueFactory<Shipment, String>("ship_date"));
        selected_plate.setCellValueFactory(new PropertyValueFactory<Shipment, String>("ship_plate"));
        selected_category.setCellValueFactory(new PropertyValueFactory<Shipment, String>("ship_to"));
        shipment_table.setItems(getShipment());

    }

    public ObservableList<Shipment>  getShipment()
    {
    	rsl = quer.startQuery(quer.getListAllLogistics(), quer.getConn());
    	try{while(rsl.next()) {
    		String nereden = rsl.getString(1);
    		String nereye = rsl.getString(2);
    		String plate = rsl.getString(3);
    		String item_carry = rsl.getString(5);
    		Date dp_time = rsl.getDate(4);
    		
    		people.add(new Shipment(nereden,nereye,item_carry,dp_time.toString(),plate));
    		
    	}}catch(SQLException ex) {
    		
    	}
    	
        return people;
    }
    @FXML
    void add_shipment_func(ActionEvent event) {
    		
    	String date_s = (String) date_text.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Shipment ship = new Shipment(from_text.getText(),to_text.getText(),"",date_s,plate_text.getText());
        people.add(ship);

        quer.In_3_STRING_1_DATE_startQuery(quer.getAddLogistic(), quer.getConn(), ship.getShip_from(), ship.getShip_to(), ship.getShip_plate(),Date.valueOf(ship.getShip_date()));
        
        
        from_text.clear();
        to_text.clear();
        plate_text.clear();
        shipment_table.getItems().clear();
        shipment_table.setItems(people);
    }

    @FXML
    void delete_shipment_func(ActionEvent event) {
        Shipment ship = shipment_table.getSelectionModel().getSelectedItem();
        quer.DELETE_In_1_STRING_startQuery(quer.getDeleteLogistic(), quer.getConn(), ship.getShip_plate());        
        shipment_table.getItems().remove(ship);
    }

    @FXML
    void to_show_list(ActionEvent event) {
      	String to = to_find.getText();
    	if(to.isEmpty())
    		new Alert(Alert.AlertType.ERROR,"Do not leave empty 'to' section ").showAndWait();
    	else
    	{
    		    	String str = quer.print_1_STRING(quer.In_1_STRING_startQuery(quer.getFunction_listeleAll(), quer.getConn(), to));
    		    	ObservableList<Shipment> find =  FXCollections.observableArrayList();
    		    	if(str != null) {
    		    	String [] strs = str.split(",");
    		    	ArrayList<String> list = new ArrayList<String>();
    		    	
    		    	for(String t : strs)
    		    	{
    		    		t= t.replaceAll("[^0-9a-zA-Z_\s ^-]", "");
    		    		list.add(t);
    		    	}
    	
    		    	for(int i=0; i<list.size() ; i= i+5)
    		    		find.add(new Shipment(list.get(i),list.get(i+4),list.get(i+1),list.get(i+2),list.get(i+3)));
    	
    		    	selected_table.getItems().clear();
    		    	selected_table.setItems(find);
    		    	}
    		    	else
    		    	{
    		    		new Alert(Alert.AlertType.ERROR,"There is no shipment to " + to).showAndWait();
    		    	}
    		    		
    	}

    }
  
    
    @FXML
    void show_first_shipment(ActionEvent event) {
      	String to = to_find.getText();
    	if(to.isEmpty())
    		new Alert(Alert.AlertType.ERROR,"Do not leave empty 'to' section ").showAndWait();
    	else {
    		
    	String str = quer.print_1_STRING(quer.In_1_STRING_startQuery(quer.getFunction_listeTop(), quer.getConn(), to));
    		if(str != null) {
    	ObservableList<Shipment> find =  FXCollections.observableArrayList();
     	String [] strs = str.split(",");
     	ArrayList<String> list = new ArrayList<String>();

    	for(String t : strs)
    	{
    		t= t.replaceAll("[^0-9a-zA-Z_\s ^-]", "");
    		System.out.println(t);
    		list.add(t);
    	}
    	
    	find.add(new Shipment(list.get(0),list.get(4),list.get(1),list.get(2),list.get(3)));
    	selected_table.getItems().clear();
    	selected_table.setItems(find);
    		}
    		else
    		{
    			new Alert(Alert.AlertType.ERROR,"There is no shipment to " + to).showAndWait();
    		}
    	}
    }
    @FXML
    void show_count(ActionEvent event) {
    	String since = (String)to_since.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    	String until = (String)to_until.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    	int result = quer.print_1_INTEGER(quer.In_2_DATE_1_STRING_startQuery(quer.getGroupDestWithTime(), quer.getConn(), Date.valueOf(since),Date.valueOf(until) , count_to.getText()));
    	new Alert(Alert.AlertType.CONFIRMATION,"There are " + String.valueOf(result) + " founded.").showAndWait();

    }
}
