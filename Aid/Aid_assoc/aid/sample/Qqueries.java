package sample;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import org.postgresql.util.PSQLException;

import javafx.scene.control.Alert;

public class Qqueries {
	private Connection conn;
	private static String user = "postgres";
	private static String pass = "faaltih1223";

	//employee_volunteer----------------------------------
	private static String listAllemp_vol = "SELECT * FROM employee_volunteer";							//+
	private static String listOne = "SELECT ssn,e_name,e_lastname FROM employee_volunteer WHERE ssn = ?";					//+
	private static String listVolunteers = "SELECT ssn,e_name,e_lastname FROM employee_volunteer WHERE is_emp = false";		//+
	private static String listManagers = "SELECT ssn,e_name,e_lastname FROM employee_volunteer WHERE is_manager = true";	//+
	private static String updateEmp = "UPDATE employee_volunteer SET is_emp = ? WHERE ssn = ?";								//+
	private static String updateManager = "UPDATE employee_volunteer SET is_manager = ? WHERE ssn = ?";						//+
	private static String updateMssn = "UPDATE employee_volunteer SET mssn = ? WHERE ssn = ?";								//+
	private static String updateName_Surname = "UPDATE employee_volunteer SET e_name = ?, e_lastname = ? WHERE ssn = ?";	//+
	private static String addEmployee = "INSERT INTO employee_volunteer VALUES (? , ? , ? , ? , ?)";						//+
	private static String deleteEmployee = "DELETE FROM employee_volunteer WHERE ssn = ?";									//+
	private static String isManager = "SELECT is_manager FROM employee_volunteer WHERE ssn = ?";
	//----------------------------------------------------
	//logistics-------------------------------------------
	private static String listAllLogistics = "SELECT * FROM logistics";														//+
	private static String getTheTruck = "SELECT * FROM logistics WHERE trucks_plate = ?";									//+
	private static String getTheCoFrom = "SELECT * FROM logistics WHERE co_from = ?";										//+
	private static String getTheDest = "SELECT * FROM logistics WHERE destination = ?";										//+
	private static String groupCoFrom = "SELECT co_from,count(*) FROM logistics GROUP BY co_from ORDER BY count(*)";		//+
	private static String groupCoFromWithTime = "SELECT co_from,count(*) FROM logistics GROUP BY co_from,departure_time HAVING departure_time < ? ORDER BY count(*); ";		//+
	private static String groupDest = "SELECT destination,count(*) FROM logistics GROUP BY destination ORDER BY count(*)";	//+
	private static String groupDestWithTime = "SELECT count(*) FROM logistics WHERE departure_time > ? AND departure_time < ? GROUP BY destination HAVING destination = ? ";//+
	private static String getTheTruckBeforeTime = "SELECT co_from,destination FROM logistics WHERE departure_time < ?";		//+
	private static String getTheTruckBetweenTime = "SELECT co_from,destination FROM logistics WHERE departure_time < ? and departure_time > ?";								//+
	private static String addLogistic = "INSERT INTO logistics VALUES (? , ? , ?, ?)";										//+
	private static String deleteLogistic = "DELETE FROM logistics WHERE trucks_plate = ?";
	private static String setItemcarry = "UPDATE logistics SET item_carry = ? WHERE trucks_plate = ?";
	private static String checkifItemexi = "SELECT item_carry FROM logistics WHERE trucks_plate = ?";
	//----------------------------------------------------
	//login_info------------------------------------------
	private static String listAllloginInfo = "SELECT * FROM login_info";													//+
	private static String givePwGivenID = "SELECT psswd FROM login_info WHERE id = ?";										//+
	private static String isInEmp = "SELECT count(*) FROM login_info GROUP BY id HAVING id = ?";
	private static String changePwGivenID = "UPDATE login_info SET psswd = ? WHERE id = ?";									//+
	//----------------------------------------------------
	//stok------------------------------------------------
	private static String list_all_stok="SELECT * FROM stok ORDER BY category";												//
	private static String list_all_items = "SELECT item_name FROM stok";
	private static String find_quantity="SELECT item_quantity FROM stok WHERE item_name=?";									//
	private static String isInStok = "SELECT count(*) FROM stok GROUP BY item_name HAVING item_name = ?";					//
	private static String addToStok = "INSERT INTO stok VALUES(?,?,?)";														//
	//----------------------------------------------------
	//donor_info------------------------------------------
	private static String list_all_donorinfo="SELECT * FROM donor_info ORDER BY d_lastname";								//
	private static String list_one_tc="SELECT d_name , d_last name, contact_number FROM donor_info WHERE tc_no=?";			//
	private static String list_one_number="SELECT d_name , d_last name, tc_no FROM donor_info WHERE contact_number=?";		//
	private static String list_date="SELECT d_name , d_lastname FROM donor_info WHERE last_time_donate =?";					//
	private static String find_tc="SELECT tc_no FROM donor_info WHERE d_name=? and d_lastname=? and contact_number=?";		//
	private static String isInDonor = "SELECT count(*) FROM donor_info GROUP BY tc_no HAVING tc_no = ?";					//
	private static String addToDonor = "INSERT INTO donor_info VALUES (?,?,?,?,?)";											//
	private static String change_donor= "UPDATE donor_info SET d_name=?, d_lastname=?, contact_number=? WHERE tc_no=?";		//
	private static String deleteDonor = "DELETE FROM donor_info WHERE tc_no=?";												//
	//----------------------------------------------------
	//donor_donation--------------------------------------
	private static String list_donor_donation="SELECT * FROM donor_donation ORDER BY item_description";						//
	private static String list_onetc="SELECT item_name,item_description,quantity,donate_date , d_last name FROM donor_donation WHERE donar_tcno=?";							//
	private static String list_date_d_d="SELECT item_description,item_name,quantity,donar_tcno FROM donor_donation WHERE donate_date=?";									//
	private static String find_tc_date="SELECT item_description,item_name,quantity FROM donor_donation WHERE donate_date=? and donar_tcno=?";								//
	private static String insertDonation = "INSERT INTO donor_donation VALUES(nextval('donor_donation_id_seq'),?,?,?,?,?)";
	//intersect-view--------------------------------------
	private static String intersectQu = "SELECT * FROM log_view";
	
	//----------------------------------------------------
	//functions-------------------------------------------
	private static String function_listeleAll = "SELECT listele_logistics(?)";
	private static String function_listeTop = "SELECT liste_top_log(?)";
	private static String function_listeTopDonator = "SELECT listele_top_donator(?)";
	//----------------------------------------------------
	
	//Connection----------------------------------
	public Qqueries() {
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/depremYardim",user,pass);
		}catch(SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
	}	
	public Connection getConn() {
		return conn;
	}
	public void closeConnection(Connection conn) {
		try{
			conn.close();
		}catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
	}
	//----------------------------------------------

	
	// ResultSet -----------------------------------
	public ResultSet startQuery(String query,Connection conn) {
		ResultSet r;
		Statement s;
		try {	
			s = conn.createStatement();
			r = s.executeQuery(query);
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	
	public ResultSet In_1_TIME_startQuery(String query,Connection conn,Time input) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setTime(1, input);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	
	public ResultSet In_2_TIME_startQuery(String query,Connection conn,Time input1,Time input2) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setTime(1, input1);
			s.setTime(2, input2);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_1_STRING_2_TIME_startQuery(String query,Connection conn,String input1,Time input2,Time input3) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setTime(2, input2);
			s.setTime(3, input3);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_1_STRING_startQuery(String query,Connection conn,String input) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_1_INTEGER_startQuery(String query,Connection conn,Integer input) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setInt(1, input);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_2_STRING_startQuery(String query,Connection conn,String input1,String input2) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_2_STRING_1_INTEGER_startQuery(String query,Connection conn,String input1,String input2,int input3) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			s.setInt(3,input3);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_2_DATE_1_STRING_startQuery(String query,Connection conn,Date input1,Date input2,String input3) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setDate(1, input1);
			s.setDate(2, input2);
			s.setString(3,input3);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	
	public ResultSet In_3_STRING_startQuery(String query,Connection conn,String input1,String input2,String input3) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			s.setString(3, input3);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public void DELETE_In_1_STRING_startQuery(String query,Connection conn,String input) {
        PreparedStatement s;
        try {
            s = conn.prepareStatement(query);
            s.clearParameters();
            s.setString(1, input);
            s.executeUpdate();
            s.close();

        } catch (SQLException e) {
        	new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
        }
    }
	public void DELETE_In_2_STRING_startQuery(String query,Connection conn,String input1,String input2) {
        PreparedStatement s;
        try {
            s = conn.prepareStatement(query);
            s.clearParameters();
            s.setString(1, input1);
            s.setString(2, input2);
            s.executeUpdate();
            s.close();

        } catch (SQLException e) {
        	new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
        }
    }
	public ResultSet In_1_DATE_3_STRING_1_INTEGER_startQuery(String query,Connection conn,Date input1,String input2,String input3,String input4,int input5) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setDate(1, input1);
			s.setString(2, input2);
			s.setString(3, input3);
			s.setString(4, input4);
			s.setInt(5, input5);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	
	public ResultSet In_4_STRING_1_BOOL_startQuery(String query,Connection conn,String input1,String input2,String input3,String input4,boolean input5) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			s.setString(3, input3);
			s.setString(4, input4);
			s.setBoolean(5, input5);
			r = s.executeQuery();
			s.close();
			return r;	
		}
		catch (SQLException ex) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	 public void Add_4_STRING_1_BOOL_startQuery(String query,Connection conn,String input1,String input2,String input3,String input4,boolean input5) {
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			s.setString(3, input3);
			s.setString(4, input4);
			s.setBoolean(5, input5);
			s.executeUpdate();
			s.close();
		}catch(PSQLException ep) {
			 new Alert(Alert.AlertType.ERROR, "This SSN already in the system" ).showAndWait();
			
		}
		catch (SQLException ex) {
			 new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}		
	}
	public ResultSet In_3_STRING_1_TIME_startQuery(String query,Connection conn,String input1,String input2,String input3,Time input4) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			s.setString(3, input3);
			s.setTime(4, input4);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_3_STRING_1_DATE_startQuery(String query,Connection conn,String input1,String input2,String input3,Date input4) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			s.setString(3, input3);
			s.setDate(4, input4);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_4_STRING_1_DATE_startQuery(String query,Connection conn,String input1,String input2,String input3,String input4,Date input5) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setString(1, input1);
			s.setString(2, input2);
			s.setString(3, input3);
			s.setString(4, input4);
			s.setDate(5, input5);
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	public ResultSet In_1_BOOL_1_STRING_startQuery(String query,Connection conn,boolean input1,String input2) {
		ResultSet r;
		PreparedStatement s;
		try {	
			s = conn.prepareStatement(query);
			s.clearParameters();
			s.setBoolean(1, input1);
			s.setString(2, input2);
		
			r = s.executeQuery();
			s.close();
			return r;
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR, "Not sure but something definetly happened" ).showAndWait();
		}
		return null;		
	}
	// -----------------------------------------------
	// String Getters---------------------------------
	public static String getListAllemp_vol() {
		return listAllemp_vol;
	}
	public static String getListOne() {
		return listOne;
	}
	public static String getListVolunteers() {
		return listVolunteers;
	}
	public static String getListManagers() {
		return listManagers;
	}
	public static String getListAllLogistics() {
		return listAllLogistics;
	}
	public static String getUpdateEmp() {
		return updateEmp;
	}
	public static String getUpdateManager() {
		return updateManager;
	}
	public static String getUpdateMssn() {
		return updateMssn;
	}
	public static String getUpdateName_Surname() {
		return updateName_Surname;
	}
	public static String getGetTheTruck() {
		return getTheTruck;
	}
	public static String getGetTheCoFrom() {
		return getTheCoFrom;
	}
	public static String getGetTheDest() {
		return getTheDest;
	}
	public static String getListAllloginInfo() {
		return listAllloginInfo;
	}
	public static String getGivePwGivenID() {
		return givePwGivenID;
	}
	public static String getChangePwGivenID() {
		return changePwGivenID;
	}
	public static String getGroupCoFrom() {
		return groupCoFrom;
	}
	public static String getGroupCoFromWithTime() {
		return groupCoFromWithTime;
	}
	public static String getAddEmployee() {
		return addEmployee;
	}
	public static String getGroupDest() {
		return groupDest;
	}
	public static String getGroupDestWithTime() {
		return groupDestWithTime;
	}
	public static String getGetTheTruckBeforeTime() {
		return getTheTruckBeforeTime;
	}
	public static String getGetTheTruckBetweenTime() {
		return getTheTruckBetweenTime;
	}
	public static String getAddLogistic() {
		return addLogistic;
	}
	public static String getDeleteEmployee() {
		return deleteEmployee;
	}
	public static String getIsInStok() {
		return isInStok;
	}
	public static String getAddToStok() {
		return addToStok;
	}
	public static String getIsInDonor() {
		return isInDonor;
	}
	public static String getAddToDonor() {
		return addToDonor;
	}
	public static String getIsInEmp() {
		return isInEmp;
	}
	//------------------------------------------------
	//Functions---------------------------------------
	
	public void print_3_STRING(ResultSet r) {
		try {
			if(r != null) {
				while(r.next()) {
					String str1 = r.getString(1);
					String str2 = r.getString(2);
					String str3 = r.getString(3);
		
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void print_2_STRING_1_INT_1_DATE(ResultSet r) {
		try {
			if(r != null) {
				while(r.next()) {
					String str1 = r.getString(1);
					String str2 = r.getString(2);
					Integer str3 = r.getInt(3);
					Date str4 = r.getDate(4);
			
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void print_2_STRING(ResultSet r) {
		try {
			if(r != null) {
				while(r.next()) {
					String str1 = r.getString(1);
					String str2 = r.getString(2);
			
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String print_1_STRING(ResultSet r) {
		try {
			if(r != null) {
				while(r.next()) {
					String str1 = r.getString(1);
					return str1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			new Alert(Alert.AlertType.ERROR,"There are no shipments for that destination" ).showAndWait();
		}
		return null;
		
	}
	public boolean print_1_BOOL(ResultSet r) {
		try {
			if(r != null) {
				while(r.next()) {
					boolean b1 = r.getBoolean(1);
	
					return b1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	public int print_1_INTEGER(ResultSet r) {
		try {
			if(r != null) {
				while(r.next()) {
					Integer int1 = r.getInt(1);
					return int1;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	public void print_3_STRING_1_TIME(ResultSet r) {
		try {
			while(r.next()) {
				String str1 = r.getString(1);
				String str2 = r.getString(2);
				String str3 = r.getString(3);
				Time time1 = r.getTime(4);
	
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void print_1_STRING_1_INTEGER(ResultSet r) {
		try {
			while(r.next()) {
				String str1 = r.getString(1);
				Integer int1 = r.getInt(2);
			
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void NoPrint(ResultSet r) {
		try {
			while(r.next()) {
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	//------------------------------------------------
	public static String getCheckifItemexi() {
		return checkifItemexi;
	}
	public static String getIsManager() {
		return isManager;
	}
	public static String getInsertDonation() {
		return insertDonation;
	}
	public static String getList_all_donorinfo() {
		return list_all_donorinfo;
	}
	public static String getList_all_stok() {
		return list_all_stok;
	}
	public static String getDeleteLogistic() {
		return deleteLogistic;
	}
	public static String getIntersectQu() {
		return intersectQu;
	}
	public static String getSetItemcarry() {
		return setItemcarry;
	}
	public static String getFunction_listeleAll() {
		return function_listeleAll;
	}
	public static String getFunction_listeTop() {
		return function_listeTop;
	}
	public static String getList_all_items() {
		return list_all_items;
	}
	public static String getFunction_listeTopDonator() {
		return function_listeTopDonator;
	}
	
	
	
	
	
}
