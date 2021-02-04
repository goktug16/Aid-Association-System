package sample;

import javafx.beans.property.SimpleStringProperty;

public class Employee {
    SimpleStringProperty ssn,mssn,emp_name,emp_surname,emp_type;
    public Employee(String a, String b, String c, String d,String e)
    {
        this.ssn =new SimpleStringProperty(a);
        this.mssn =new SimpleStringProperty(b);
        this.emp_name = new SimpleStringProperty(c);
        this.emp_surname =new SimpleStringProperty(d);
        this.emp_type = new SimpleStringProperty(e);
    }

    public String getSsn() {
        return ssn.get();
    }

    public SimpleStringProperty ssnProperty() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn.set(ssn);
    }

    public String getMssn() {
        return mssn.get();
    }

    public SimpleStringProperty mssnProperty() {
        return mssn;
    }

    public void setMssn(String mssn) {
        this.mssn.set(mssn);
    }

    public String getEmp_name() {
        return emp_name.get();
    }

    public SimpleStringProperty emp_nameProperty() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name.set(emp_name);
    }

    public String getEmp_surname() {
        return emp_surname.get();
    }

    public SimpleStringProperty emp_surnameProperty() {
        return emp_surname;
    }

    public void setEmp_surname(String emp_surname) {
        this.emp_surname.set(emp_surname);
    }

    public String getEmp_type() {
        return emp_type.get();
    }

    public SimpleStringProperty emp_typeProperty() {
        return emp_type;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type.set(emp_type);
    }
}
