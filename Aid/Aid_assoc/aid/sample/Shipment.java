package sample;

import javafx.beans.property.SimpleStringProperty;

public class Shipment {
    SimpleStringProperty ship_from,ship_to,ship_item,ship_date,ship_plate;
    public Shipment(String a, String b, String c, String d,String e)
    {
        this.ship_from =new SimpleStringProperty(a);
        this.ship_to =new SimpleStringProperty(b);
        this.ship_item = new SimpleStringProperty(c);
        this.ship_date =new SimpleStringProperty(d);
        this.ship_plate = new SimpleStringProperty(e);
    }

    public String getShip_from() {
        return ship_from.get();
    }

    public SimpleStringProperty ship_fromProperty() {
        return ship_from;
    }

    public void setShip_from(String ship_from) {
        this.ship_from.set(ship_from);
    }

    public String getShip_to() {
        return ship_to.get();
    }

    public SimpleStringProperty ship_toProperty() {
        return ship_to;
    }

    public void setShip_to(String ship_to) {
        this.ship_to.set(ship_to);
    }

    public String getShip_item() {
        return ship_item.get();
    }

    public SimpleStringProperty ship_itemProperty() {
        return ship_item;
    }

    public void setShip_item(String ship_item) {
        this.ship_item.set(ship_item);
    }

    public String getShip_date() {
        return ship_date.get();
    }

    public SimpleStringProperty ship_dateProperty() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date.set(ship_date);
    }

    public String getShip_plate() {
        return ship_plate.get();
    }

    public SimpleStringProperty ship_plateProperty() {
        return ship_plate;
    }

    public void setShip_plate(String ship_plate) {
        this.ship_plate.set(ship_plate);
    }

    public String toString()
    {
        return String.format("%s %s %s %s %s", ship_from, ship_to,ship_item,ship_date,ship_plate);
    }
}
