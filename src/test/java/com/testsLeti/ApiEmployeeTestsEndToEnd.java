package com.testsLeti;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.POJOsLeti.CreateEmployeeResponsePOJO;
import com.POJOsLeti.CreateEmployeeResponsePOJO.EmployeeData;
import com.POJOsLeti.GetEmployeePOJO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utility.Constants;
import com.utility.ExtentReportsUtility;

public class ApiEmployeeTestsEndToEnd {
    protected static Logger logger;
	protected static ExtentReportsUtility extentreport;
	private BaseMethods base;
	private int employeeId; //variable from TC2
	
	@BeforeClass
	public void init() {
		String uri = Constants.getUri(); //uri placed in Constants
		RestAssured.baseURI=uri;
		base = new BaseMethods();
	}
	
	@BeforeMethod
	public void setUpBeforeMethod() {
        logger = LogManager.getLogger(getClass().getName());//log4j
        extentreport = new ExtentReportsUtility();
        extentreport.startExtentReport();
	}
		
		@Test(priority = 1)
		public void TC1GetEmployeeList() throws Exception, Exception {
			System.out.println("Inside test TC1 getEmployeeList");
			logger.info("Logger : Inside test TC1 getEmployeeList");
			Response res = RestAssured
					.given()
					.when()
					.get("/employees");
			
			res.then().statusCode(200); // one way
			assertEquals(base.getStatusCode(res), 200);//other way
			
			int statuscode=res.statusCode();
			System.out.println("status code="+statuscode); // 200 
			
			res.then().contentType(ContentType.JSON); //one way
			assertEquals(base.getContentType(res), "application/json"); //other way
			
			res.then().body("status",is("success")); // Hamcrest matchers to validate		

			// Getting the records different ways
			// res.prettyPrint();// print all records  Yes
			System.out.println(res.getBody().asString()); //String results of all records 
					
			// Deserialize the response body into a GetEmployeePOJO object
			ObjectMapper objectMapper = new ObjectMapper();
			GetEmployeePOJO responsePOJO = objectMapper.readValue(res.getBody().asString(), GetEmployeePOJO.class);//import com.fasterxml.jackson.databind.ObjectMapper;

			// Assertion using POJO
			Assert.assertEquals(responsePOJO.getStatus().toString().toLowerCase(), "success"); 
			
			//Get the number of data records and print it to the console >> from the responsePOJO
			int numOfDataRecords = responsePOJO.getData().size();
			System.out.println("number of records using responsePOJO: " + numOfDataRecords); //24
		}
		
		@Test(priority = 2)
		public void TC2CreateEmployee() throws Exception, Exception {
			System.out.println("Inside test TC2 CreateEmployee");
			logger.info("Logger : Inside test TC2 CreateEmployee");
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.body("{\"name\":\"test\",\"salary\":\"123\",\"age\":\"23\"}")
					.when()
					.post("/create");
			
			res.then().statusCode(200);
			int statuscode=res.statusCode();
			System.out.println("status code="+statuscode); // 200 
			res.then().body("status",is("success")); // Hamcrest matchers to validate
			
			res.prettyPrint(); // we print the response to be able to create -> CreateEmployeeResponsePOJO file
			
			// Deserialize the response body into a createEmployeeResponsePOJO object
			ObjectMapper objectMapper = new ObjectMapper();
			CreateEmployeeResponsePOJO createEmployeeResponsePOJO = objectMapper.readValue(res.getBody().asString(), CreateEmployeeResponsePOJO.class);//import com.fasterxml.jackson.databind.ObjectMapper;

			//Extract the id,print it to console and store it to send to next requests.
			EmployeeData employeeData = createEmployeeResponsePOJO.getData();
			int employeeId = employeeData.getId(); 
			System.out.println("Id extracted from responsePOJO: " + employeeId); // 1559 - we then keep employeeId as a class-level variable
			
			// verify the name,salary and age data from response is as same as in the request
			Assert.assertEquals(employeeData.getName(), "test"); 
			Assert.assertEquals(employeeData.getSalary(), "123");
			Assert.assertEquals(employeeData.getAge(), "23");
			// assertions pass
			
			}
		
		@Test (priority = 3)
		public void TC3DeleteEmployee() throws Exception, Exception {
			System.out.println("Inside test TC3 DeleteEmployee");
			logger.info("Logger : Inside test TC3 DeleteEmployee");
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.when()
					.delete("/delete/employeeId"); //variable from TC2 // 5819
			
			
			res.then().statusCode(200);
			int statuscode=res.statusCode();
			System.out.println("status code="+statuscode); // 200 
			res.then().body("status",is("success")); // Hamcrest matchers to validate
			res.prettyPrint(); // fetch the message data and print it to console + we get the following response and create the DeleteEmployeeResponsePOJO file
									
			/*// CONSOLE EXAMPLE:==============
			Inside test TC3 DeleteEmployee
			status code=200
			{
			    "status": "success",
			    "data": "1559",
			    "message": "Successfully! Record has been deleted"
			}
			====================================
			*/
			
			// verify id which you have entered in the path parameter (employeeId) is same as in the response
			// We fetch the actualDeletedId 
			String actualDeletedId = res.jsonPath().getString("data");// we get the actualDeletedId from the response 
			int actualDeletedIdInt = Integer.parseInt(actualDeletedId);
			System.out.println(" EmployeeId deleted is: "+ actualDeletedIdInt);
		    Assert.assertEquals(actualDeletedIdInt, employeeId);
		 // assertion pass
		}
		    
		    
		    @Test (priority = 4)
			public void TC4DeleteEmployeeWithParam0() throws Exception, Exception {
				System.out.println("Inside test TC4 DeleteEmployeeWithParam0");
				logger.info("Logger: Inside test TC4 DeleteEmployeeWithParam0");
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.when()
						.delete("/delete/0"); 
						
				res.then().statusCode(400);
				int statuscode=res.statusCode();
				System.out.println("status code="+statuscode); // 400 
				res.then().body("status",is("error")); // assertion pass
				res.prettyPrint(); // fetch the message data and print it to console 
										
				/*//===================== CONSOLE EXAMPLE:
				status code=400
				{
    			"status": "error",
    			"message": "Not found record",
    			"code": 400,
    			"errors": "id is empty"
				}
				=====================
				 */			
		    }
		    
			@Test(priority = 5)
			public void TC5GetEmployeeInfo() throws Exception, Exception {
				System.out.println("Inside test TC5 getEmployeeInfo");
				logger.info("Logger: Inside test TC5 getEmployeeInfo");
				Response res = RestAssured
						.given()
						.when()
						.get("/employee/2");
				res.then().statusCode(200);
				int statuscode=res.statusCode();
				System.out.println("status code="+statuscode); 
				res.then().contentType(ContentType.JSON); 
				res.then().body("status",is("success"));  
				res.prettyPrint();// print details for employee with id=2
				
				/*//CONSOLE:=================				
				{
				    "status": "success",
				    "data": {
				        "id": 2,
				        "employee_name": "Garrett Winters",
				        "employee_salary": 170750,
				        "employee_age": 63,
				        "profile_image": ""
				    },
				    "message": "Successfully! Record has been fetched."
				}
				===============================	
				*/
	
				// We fetch the employee_name using Rest Assured jsonPath()
				String actualEmployeeName = res.jsonPath().getString("data.employee_name");
				System.out.println("Employee Name for id number 2 is: " + actualEmployeeName);
				String actualEmployeeSalary = res.jsonPath().getString("data.employee_salary");//String (getInt is not needed)
				System.out.println("Employee Salary for id number 2 is: " + actualEmployeeSalary);
				String actualEmployeeAge = res.jsonPath().getString("data.employee_age");//String (getInt is not needed)
				System.out.println("Employee Age for id number 2 is: " + actualEmployeeAge);
				
				Assert.assertEquals(actualEmployeeName,"Garrett Winters");
				Assert.assertEquals(actualEmployeeSalary, "170750");
				Assert.assertEquals(actualEmployeeAge, "63");
				// assertions pass
			}
		
}
