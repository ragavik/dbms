package users;

import app.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Pattern;

public class Employee extends User {

    static Scanner scanner = new Scanner(System.in);
    public static ResultSet rs2 = null;
    public static Statement stmt2 = null;

    public Employee(String userID, String userType) {
        super(userID, userType);
    }

    // Employee: Profile
    public void profile() throws Exception {
        System.out.println("\nPROFILE:");
        System.out.println("\t1. View Profile");
        System.out.println("\t2. Update Profile");
        System.out.println("\t3. Go Back");
        System.out.println("Enter option: ");
        int option = scanner.nextInt();
        switch(option) {
            case 1:
                viewProfile();
                break;
            case 2:
                updateProfile();
                break;
            case 3:
                this.landingPage();
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    // Employee: View Profile
    // TODO: query db & display following details
    //A. Employee ID
    //B. Name
    //C. Address
    //D. Email Address
    //E. Phone Number
  //H. Start Date
    //F. Service Center
  //I. Compensation ($)
    //G. Role
    //J. Compensation Frequency (monthly/hourly)
    private void viewProfile() throws Exception {
        System.out.println("\nVIEW PROFILE:");
        // TODO: display profile details
       String id=Application.currentUser.userID;
       System.out.println("Employee ID: "+id);
        String utype=Application.currentUser.userType;
      
       if(utype.equalsIgnoreCase("receptionist"))
       	{ Application.rs=Application.stmt.executeQuery("select receptionistat.sc_id from receptionist, employee, receptionistat where employee.emp_id='"+id+"' AND employee.emp_id=receptionist.emp_id AND receptionist.receptionist_id=receptionistat.receptionist_id");
        	while(Application.rs.next())
         	{
        	
        	String service_center_id=Application.rs.getString("sc_id");
        	System.out.println("The Receptionist is at service center: "+service_center_id);
        		}
       	}	
       if(utype.equalsIgnoreCase("manager"))
      	{ Application.rs=Application.stmt.executeQuery("select manages.sc_id from manager, employee, manages where employee.emp_id='"+id+"' AND employee.emp_id=manager.emp_id AND manager.manager_id=manages.manager_id");
       	while(Application.rs.next())
        	{
       	
       	String service_center_id=Application.rs.getString("sc_id");
       	System.out.println("The Manager manages the service center: "+service_center_id);
       		}
      	}	
        Application.rs = Application.stmt.executeQuery("select employee.emp_id,employee.emp_name,employee.emp_location,employee.addr,employee.phone,employee.email,payroll.start_date,payroll.wages,payroll.frequency from employee,payroll where employee.emp_id="+id+"AND employee.emp_id=payroll.emp_id");
        while (Application.rs.next()) {
		    String eid= Application.rs.getString("emp_id");
		    String name = Application.rs.getString("emp_name");
		    String addr = Application.rs.getString("addr");
		    String phone = Application.rs.getString("phone");
		    String email= Application.rs.getString("email");
		    float wages=Application.rs.getFloat("wages");
		    String location=Application.rs.getString("emp_location");
		    Date startdate=Application.rs.getDate("start_date");
		    String frequency= Application.rs.getString("frequency");
		    
		    System.out.println("Employee ID: "+eid);
		    System.out.println("Employee Name: " +name);
		    System.out.println("Address: " +addr);
		    System.out.println("Phone: " +phone);
		    System.out.println("email: " +email);
		    System.out.println("Wages: " +wages);
		    System.out.println("Location:" +location);
		    System.out.println("Start Date: " +startdate);
		    System.out.println("frequency: " +frequency);
		    System.out.println("Role: " +utype);
		
		    if (utype.equalsIgnoreCase("receptionist"))
			   { 
			    
			}

        System.out.println("MENU:");
        System.out.println("\t1. Go back");
        System.out.println("Enter option: ");
        int option = scanner.nextInt();
        if(option == 1) {
            profile();
        } else {
            System.out.println("Invalid option. Try again.");
            viewProfile();
        }
		} 
		
		
    }

    // Employee: Update Profile
    private void updateProfile() throws Exception {
        System.out.println("\nUPDATE PROFILE:");
        System.out.println("\t1. Name");
        System.out.println("\t2. Address");
        System.out.println("\t3. Email Address");
        System.out.println("\t4. Phone Number");
        System.out.println("\t5. Password");
        System.out.println("\t6. Go Back");
        System.out.println("Enter option: ");
        int option = scanner.nextInt();
        if(option >= 1 && option <= 5) {
            System.out.println("Enter new value: ");
            String newValue = scanner.next();
            updateProfileField(option, newValue);
            profile();
        } else if(option == 6) {
            profile();
        } else {
            System.out.println("Invalid option. Try again.");
            updateProfile();
        }
    }

    // TODO: update employee information in db for selected field with given value
    // (For Employee: Update Profile)
    private void updateProfileField(int field, String newValue) throws Exception {
        // TODO: validate value based on field type
        // TODO: update in db
    	String id=Application.currentUser.userID;
    	if(field==1)
    	{ if(isWord(newValue))
    	 {Application.stmt.executeUpdate("update employee set emp_name='"+newValue+"' where emp_id="+id);
    		System.out.println("Profile Details Successfully updated");
    	 }
    	else {
    		System.out.println("Invalid Data");
    		updateProfile();
    	}
    	}
    	if(field==2) { if(newValue == null || newValue.isEmpty()) {
            System.out.println("Address cannot be empty.");}
    	{Application.stmt.executeUpdate("update employee set addr='"+newValue+"' where emp_id="+id);
    		System.out.println("Profile Details Successfully Updated");
    	
    	}
    	}
    	if (field==3) { if (validateEmail(newValue))
    	{ if(newValue == null || newValue.isEmpty()) {
            System.out.println("Email cannot be empty.");
    	    }
    		Application.stmt.executeUpdate("update employee set email='"+newValue+"' where emp_id="+id);
    		System.out.println("Profile Details Successfully Updated");
    	}else {
    		System.out.println("Invalid Data");
    	}
    		
    	}
    	if(field==4) {if(validatePhone(newValue))
    	{
    		Application.stmt.executeUpdate("update employee set phone='"+newValue+"' where emp_id="+id);
    		System.out.println("Profile Details Successfully Updated");
    	}
    	else {
    		System.out.println("Invalid Data");
    	}
    		
    	}
    	if(field==5) {if(newValue.length()<8) {
    		System.out.println("Password cannot be less than 8 characters");
        	} else {Application.stmt.executeUpdate("update login set password='"+newValue+"' where id="+id);
        	System.out.println("Profile Details Updated");
        		
        	}
    		
    	}
    	
    	
    }
    public static boolean validatePhone(String phone)
    {
    	return Pattern.matches("\\d{3}-\\d{3}-\\d{4}",phone);
    }
    public static boolean validateEmail(String email)
    {
    	return Pattern.matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$",email);
    }
    
    public static boolean isWord(String in)
    {
    	return Pattern.matches("[a-zA-z]+", in);
    }

    // Employee: View Customer Profile
    public void viewCustomerProfile() throws Exception {
        System.out.println("\nVIEW CUSTOMER PROFILE:");
        System.out.println("Enter customer email address: ");
        String customerEmail = scanner.next();
        displayCustomerProfile(customerEmail);
        System.out.println("MENU:");
        System.out.println("\t1. Go back");
        int option = scanner.nextInt();
        
        if(option == 1) {
            this.landingPage();
        } else {
            System.out.println("Invalid option. Try again.");
            viewCustomerProfile();
        }
    }

    // (For Employee: View Customer Profile)
    // TODO: Query db & display following customer details
    //A. Customer ID
    //B. Name
    //C. Address
    //D. Email Address
    //E. Phone Number
    //F. List of All Cars (and their details)
    private void displayCustomerProfile(String customerEmail) throws Exception {
    	Application.rs = Application.stmt.executeQuery("select customer.customer_id,customer.customer_name,customer.email,customer.address,customer.telephone_no, car.license_no, car.car_make,car.car_model,car.car_year,car.customer_id, car.date_of_purchase,car.last_recorded_mileage,car.recent_service_date,recent_service_type from customer,car where customer.email='"+customerEmail+"' AND customer.customer_id=car.customer_id");
    	//String Query="select customer.customer_id,customer.customer_name,customer.email,customer.address,customer.telephone_no, car.license_no, car.car_make,car.car_model,car.car_year,car.customer_id, car.date_of_purchase,car.last_recorded_mileage,car.recent_service_date,recent_service_type from customer,car where customer.email='"+customerEmail +"' AND customer.customer_id=car.customer_id";
    	//System.out.println(Query);
    	while (Application.rs.next()) {
		    int cid= Application.rs.getInt("customer_id");
		    String name = Application.rs.getString("customer_name");
		    String addr = Application.rs.getString("address");
		    String phone = Application.rs.getString("telephone_no");
		    String license_no = Application.rs.getString("license_no");
		    String email= Application.rs.getString("email");
		    String car_make = Application.rs.getString("car_make");
		    String car_year = Application.rs.getString("car_year");
		    Date purchasedate=Application.rs.getDate("date_of_purchase");
		    String car_model = Application.rs.getString("car_model");
		    int mileage = Application.rs.getInt("last_recorded_mileage");
		    Date latest_service_date = Application.rs.getDate("recent_service_date");
		    String recent_service_type = Application.rs.getString("recent_service_type");
		    
		    
		    System.out.println("Customer ID: "+cid);
		    System.out.println("Customer Name: " +name);
		    System.out.println("Address: " +addr);
		    System.out.println("Phone: " +phone);
		    System.out.println("email: " +email);
		    System.out.println("\n\nCar Details");
		    System.out.println("license no: " +license_no);
		    System.out.println("Car_Make:  " +car_make);
		    System.out.println("Car Year " +car_year);
		    System.out.println("Purchase Date:" +purchasedate);
		    System.out.println("Car Model: " +car_model);
		    System.out.println("Last recorder mileage: " +mileage);
		    System.out.println("latest_service_date: " +latest_service_date);
		    System.out.println("Recent Service Type: " +recent_service_type);
		}
    }
    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }
}
