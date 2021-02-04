package sample;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ship_basic_controller implements Initializable {
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
    private ComboBox<String> items;

    @FXML
    private Button item_add;
    ObservableList<Shipment> people =  FXCollections.observableArrayList();
    
    
    Qqueries quer = new Qqueries();
    Connection con = quer.getConn();
    ResultSet res;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        from.setCellValueFactory(new PropertyValueFactory<Shipment, String>("ship_from"));

        to.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_to"));

        item.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_item"));

        date.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_date"));

        plate.setCellValueFactory(new PropertyValueFactory<Shipment,String>("ship_plate"));
        
  
        items.getItems().addAll(getItems());
 
        shipment_table.setItems(getShipment());

    }
    
    public void set_enables()
    {
    	items.setDisable(false);
    	item_add.setDisable(false);
    }

    public ObservableList<Shipment>  getShipment()
    {
    	
    	res = quer.startQuery(quer.getListAllLogistics(), quer.getConn());
    	try {
    	while(res.next()) {
    		String nerden = res.getString(1);
    		String nereye = res.getString(2);
    		String item = res.getString(5);
    		if(item == null)
    			item = "null";
    		String plate = res.getString(3);
    		java.sql.Date dt= res.getDate(4);
    		people.add(new Shipment(nerden,nereye,item,dt.toString(),plate));

    	}}catch(SQLException ex) {
    		
    	}
    	
    	return people;
    }
    public ObservableList<String>  getItems()
    {
        ObservableList<String> items =  FXCollections.observableArrayList();
        res = quer.startQuery(quer.getIntersectQu(), quer.getConn());
        try{while(res.next()) {
        	items.add(res.getString(1));
        }}catch(SQLException ex) {
        	ex.printStackTrace();
        }
        return items;	
    }
    @FXML
    void item_add_toshipment(ActionEvent event) {
    	Shipment selected_item = shipment_table.getSelectionModel().getSelectedItem();
    	if(selected_item != null)
    	{
    	quer.DELETE_In_2_STRING_startQuery(quer.getSetItemcarry(), quer.getConn(), items.getValue(),selected_item.getShip_plate());
    	shipment_table.getItems().clear();
    	shipment_table.setItems(getShipment());
    	items.getItems().clear();
    	items.getItems().addAll(getItems());
    	}
    	else
    		new Alert(Alert.AlertType.ERROR,"Select Item").showAndWait();
    }

}
