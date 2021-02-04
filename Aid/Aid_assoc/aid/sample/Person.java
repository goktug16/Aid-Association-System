package sample;

import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {
    SimpleStringProperty item_name,item_description,item_quantity;

    public Person(String a, String b, String c)
    {
        this.item_name =new SimpleStringProperty(a);
        this.item_description =new SimpleStringProperty(b);
        this.item_quantity = new SimpleStringProperty(c);
    }

    public String getItem_name() {
        return item_name.get();
    }

    public void setItem_name(String item_name) {
        this.item_name.set(item_name);
    }

    public String getItem_description() {
        return item_description.get();
    }

    public void setItem_description(String item_description) {
        this.item_description.set(item_description);
    }

    public String getItem_quantity() {
        return item_quantity.get();
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity.set(item_quantity);
    }

    public String toString()
    {
        return String.format("%s %s %s %s", item_description, item_quantity,item_name);
    }
}
