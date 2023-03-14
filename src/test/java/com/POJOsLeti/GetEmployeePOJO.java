package com.POJOsLeti;
import java.util.List;

//https://jsoneditoronline.org/ 
public class GetEmployeePOJO {

	    private String status;
	    private List<Employee> data;
	    private String message;

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public List<Employee> getData() {
	        return data;
	    }

	    public void setData(List<Employee> data) {
	        this.data = data;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public static class Employee { 
	        private int id;
	        private String employee_name;
	        private int employee_salary;
	        private int employee_age;
	        private String profile_image;

	        public int getId() {
	            return id;
	        }

	        public void setId(int id) {
	            this.id = id;
	        }

	        public String getEmployee_name() {
	            return employee_name;
	        }

	        public void setEmployee_name(String employee_name) {
	            this.employee_name = employee_name;
	        }

	        public int getEmployee_salary() {
	            return employee_salary;
	        }

	        public void setEmployee_salary(int employee_salary) {
	            this.employee_salary = employee_salary;
	        }

	        public int getEmployee_age() {
	            return employee_age;
	        }

	        public void setEmployee_age(int employee_age) {
	            this.employee_age = employee_age;
	        }

	        public String getProfile_image() {
	            return profile_image;
	        }

	        public void setProfile_image(String profile_image) {
	            this.profile_image = profile_image;
	        }
	    }
	}
	