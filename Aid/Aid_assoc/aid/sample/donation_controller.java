package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class donation_controller implements Initializable {
    @FXML
    private TableView<Person> table_donat;
    @FXML
    private TableColumn<Person, String> item_name_col;
    @FXML
    private TableColumn<Person, String> item_desc_col;
    @FXML
    private TableColumn<Person, String> item_quantity_col;
   
    
    Qqueries quer = new Qqueries();
    Connection con = quer.getConn();
    ResultSet rsl;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        item_name_col.setCellValueFactory(new PropertyValueFactory<Person, String>("item_name"));

        item_desc_col.setCellValueFactory(new PropertyValueFactory<Person,String>("item_description"));

        item_quantity_col.setCellValueFactory(new PropertyValueFactory<Person,String>("item_quantity"));

        table_donat.setItems(getPeople());

    }

    public ObservableList<Person>  getPeople()
    {
        ObservableList<Person> people = FXCollections.observableArrayList();
        rsl = quer.startQuery(quer.getList_all_stok(), quer.getConn());
        
        try{while(rsl.next()) {
        	String item_category = rsl.getString(1);
        	String item_name = rsl.getString(2);
        	int quantity = rsl.getInt(3);
        	
        	people.add(new Person(item_category,item_name,String.valueOf(quantity)));
        }}catch(SQLException ex) {
        	new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
        }
     
        return people;
    }
}
