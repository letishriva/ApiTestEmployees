package com.testsLeti;
import io.restassured.response.Response;

public class BaseMethods {
		
	public int getStatusCode (Response response) {
		return response.getStatusCode();
	}
		
	public String getContentType (Response response) {
		return response.getContentType();
	}
	
}
