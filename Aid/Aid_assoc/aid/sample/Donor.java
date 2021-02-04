package sample;

import javafx.beans.property.SimpleStringProperty;

public class Donor {
	SimpleStringProperty donor_name,donor_lastname,contact,donor_ssn,last_time_donate;
	
	 public Donor(String a, String b, String c, String d, String e)
	    {
	        this.donor_name =new SimpleStringProperty(a);
	        this.donor_lastname =new SimpleStringProperty(b);
	        this.contact = new SimpleStringProperty(c);
	        this.donor_ssn = new SimpleStringProperty(d);
	        this.last_time_donate = new SimpleStringProperty(e);
	    }

	public String getDonor_name() {
		return donor_name.get();
	}

	public String getDonor_lastname() {
		return donor_lastname.get();
	}

	public String getContact() {
		return contact.get();
	}

	public String getDonor_ssn() {
		return donor_ssn.get();
	}

	public String getLast_time_donate() {
		return last_time_donate.get();
	}


	 

}
