package com.testsLeti;
import static org.testng.Assert.assertEquals;

import com.utility.Endpoints;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;

public class BaseMethods  {
		
	public int getStatusCode (Response response) {
		return response.getStatusCode();
	}
		
	public String getContentType (Response response) {
		return response.getContentType();
	}
	
	public static String getDataEmployee (Response response, String data) {
		String dataEmp = response.jsonPath().getString(data);
		System.out.println("Employee info for id chosen is: " + dataEmp);
		return dataEmp;
	}
	
	
	public static Response getEmployeeList() {
		Response res = RestAssured
				.given()
				.when()
				.get(Endpoints.EMPLOYEES_PATH); //Constants
		return res;
	
	}
	
	public static Response createEmployee() {
		Response res = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body(Endpoints.CREATE_EMPLOYEE_BODY)
				.when()
				.post(Endpoints.CREATE_PATH);
		return res; 
	
	}
	
	public static void validateStatusCode (Response response, int code){
		
		response.then().statusCode(code);
		assertEquals(response.getStatusCode(), code);
		int statuscode=response.statusCode();
		System.out.println("status code is ="+statuscode); // 400 
		
	}
		
	public static void	validateMessage (Response response, String Message) {
			
		response.then().body("status",is(Message));

	}

	public static void printToConsole (Response response) {
	
	response.prettyPrint(); // fetch the message data and print it to console 
	
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
}
	
	

