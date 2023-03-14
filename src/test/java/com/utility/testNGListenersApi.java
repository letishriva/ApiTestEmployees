package com.utility;
import org.testng.IClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class testNGListenersApi implements ITestListener {
	protected static ExtentReportsUtility extentreport=null;

	
	@Override
	public void onTestStart(ITestResult Result) {
	System.out.println(Result.getTestClass()+" Test Case started");	
	System.out.println(Result.getName()+" Method started");	
	IClass classInfo =  Result.getTestClass();
	extentreport.startSingleTestReport(classInfo.toString());

	}
	
    // When Test case get failed, this method is called.		
    @Override		
    public void onTestFailure(ITestResult Result) 					
    {	
	    extentreport.logTestFailed("from testNGListenersApi : Test Case Failed " +Result.getTestClass());// in extent report, we print the name of the test failed
	    System.out.println("The name of the Method failed is :"+Result.getName());

    }
	    	    

 
		

    // When Test case get Skipped, this method is called.		
    @Override		
    public void onTestSkipped(ITestResult Result)					
    {	
    System.out.println("The name of the Method skipped is :"+Result.getName());
    System.out.println("The name of the Test Case skipped is :"+Result.getTestClass());	
    }		

    // When Test case get passed, this method is called.		
    @Override		
    public void onTestSuccess(ITestResult Result)					
    {
    extentreport.logTestpassed("from testNGListenersApi : Test Case PASSED " +Result.getTestClass());
    System.out.println("The name of the Method passed is :"+Result.getName());		
    System.out.println("The name of the Test Case passed is :"+Result.getTestClass());	
    }
    
    @Override		
    public void onStart(ITestContext Context)					
    {		
    extentreport=new ExtentReportsUtility();
    extentreport.startExtentReport();
				
    }	

    @Override		
    public void onFinish(ITestContext Context)					
    {		
      extentreport.endReport();
				
    }	
}