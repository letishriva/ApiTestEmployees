package com.POJOsLeti;

public class CreateEmployeeResponsePOJO {
	    private String status;
	    private EmployeeData data;
	    private String message;

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public EmployeeData getData() {
	        return data;
	    }

	    public void setData(EmployeeData data) {
	        this.data = data;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public static class EmployeeData { // used to verify after creation of Employee
	        private String name;
	        private String salary;
	        private String age;
	        private int id;

	        public String getName() {
	            return name;
	        }

	        public void setName(String name) {
	            this.name = name;
	        }

	        public String getSalary() {
	            return salary;
	        }

	        public void setSalary(String salary) {
	            this.salary = salary;
	        }

	        public String getAge() {
	            return age;
	        }

	        public void setAge(String age) {
	            this.age = age;
	        }

	        public int getId() {
	            return id;
	        }

	        public void setId(int id) {
	            this.id = id;
	        }
	    }
	}

