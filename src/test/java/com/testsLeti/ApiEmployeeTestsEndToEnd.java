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
import com.utility.Endpoints;
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
			logger.info("Logger : Inside test TC1 getEmployeeList");
			
			Response res = BaseMethods.getEmployeeList(); // here example using reusable methods
			BaseMethods.validateStatusCode(res, 200);
			BaseMethods.validateMessage(res,"success");// Hamcrest matchers to validate	
			assertEquals(base.getContentType(res), "application/json"); //one way
			//res.then().contentType(ContentType.JSON); //one way
			
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
			logger.info("Logger : Inside test TC2 CreateEmployee");
			Response res = BaseMethods.createEmployee();
			BaseMethods.validateStatusCode(res, 200);
			BaseMethods.validateMessage(res,"success");
			BaseMethods.printToConsole(res); // we print the response to be able to create -> CreateEmployeeResponsePOJO file
			
			// Deserialize the response body into a createEmployeeResponsePOJO object
			ObjectMapper objectMapper = new ObjectMapper();
			CreateEmployeeResponsePOJO createEmployeeResponsePOJO = objectMapper.readValue(res.getBody().asString(), CreateEmployeeResponsePOJO.class);//import com.fasterxml.jackson.databind.ObjectMapper;

			//Extract the id,print it to console and store it to send to next requests.
			EmployeeData employeeData = createEmployeeResponsePOJO.getData();
			int employeeId = employeeData.getId(); 
			System.out.println("Id extracted from responsePOJO: " + employeeId); // 1559 - we then keep employeeId as a class-level variable
			
			// verify the name,salary and age data from response is as same as in the request
			String nameTest = Constants.getNameTest();
			String salaryTest = Constants.getSalaryTest();
			String ageTest = Constants.getAgeTest();
			Assert.assertEquals(employeeData.getName(), nameTest); 
			Assert.assertEquals(employeeData.getSalary(), salaryTest);
			Assert.assertEquals(employeeData.getAge(), ageTest);
			}
		
		@Test (priority = 3)
		public void TC3DeleteEmployee() throws Exception, Exception { //without using Reusable Methods
			logger.info("Logger : Inside test TC3 DeleteEmployee");
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.when()
					.delete(Endpoints.DELETE_EMP_ID_PATH); //variable from TC2 // 5819
						
			BaseMethods.validateStatusCode(res, 200);
			BaseMethods.validateMessage(res,"success");
			BaseMethods.printToConsole(res); // we get the following response and create the DeleteEmployeeResponsePOJO file
									
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
			}
		    
		    @Test (priority = 4)
			public void TC4DeleteEmployeeWithParam() throws Exception, Exception {
				logger.info("Logger: Inside test TC4 DeleteEmployeeWithParam"); 
				
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.when()
						.delete(Endpoints.DELETE_CHOSEN_ID_PATH); //0
				
				BaseMethods.printToConsole(res);			
				BaseMethods.validateStatusCode(res, 400);
				BaseMethods.validateMessage(res,"error");
				
				//assertEquals(base.getStatusCode(res), 400);//other way -> used in Reusable method
				//res.then().body("status",is("error")); // -> used in Reusable method
				//I am validating the response status code - as I will perform action again? -> reusable method
				//provide input int argument that will be 400 + argument Response										
		    }
		    
			@Test(priority = 5)
			public void TC5GetEmployeeInfo() throws Exception, Exception {
				logger.info("Logger: Inside test TC5 getEmployeeInfo");
				Response res = RestAssured
						.given()
						.when()
						.get(Endpoints.GET_CHOSEN_ID_PATH); //2
				BaseMethods.validateStatusCode(res, 200);
				res.then().contentType(ContentType.JSON); 
				BaseMethods.validateMessage(res,"success");
				BaseMethods.printToConsole(res);;// print details for employee with id=2
				
				// We fetch the employee_name using Rest Assured jsonPath()
				//String actualEmployeeName = res.jsonPath().getString("data.employee_name");
				// System.out.println("Employee Name for id chosen is: " + actualEmployeeName);
				String actualEmployeeName = BaseMethods.getDataEmployee(res,"data.employee_name");
				String actualEmployeeSalary = BaseMethods.getDataEmployee(res,"data.employee_salary");
				String actualEmployeeAge  = BaseMethods.getDataEmployee(res, "data.employee_age");//String (getInt is not needed)
				
				String nameChosen = Constants.getNameChosen();
				String salaryChosen = Constants.getSalaryChosen();
				String ageChosen = Constants.getAgeChosen();
				
				Assert.assertEquals(actualEmployeeName,nameChosen);
				Assert.assertEquals(actualEmployeeSalary, salaryChosen);
				Assert.assertEquals(actualEmployeeAge, ageChosen);
				
				// other way to assess:
				res.then().body("data.employee_age",is(63));
				
				
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
			}
		
}
