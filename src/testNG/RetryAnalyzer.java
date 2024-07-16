package testNG;

import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	int counter = 0;
	int retryLimit = 1; // Execute test two times before failing

	@Override
	public boolean retry(ITestResult result) {
		 if (!result.isSuccess()) {                      //Check if test not succeed
	            if (counter < retryLimit) {                            //Check if maxtry count is reached
	                counter++;                                     //Increase the maxTry count by 1
	                result.setStatus(ITestResult.FAILURE);  //Mark test as failed
	                return true;                                 //Tells TestNG to re-run the test
	            } else {
	                result.setStatus(ITestResult.FAILURE);  //If maxCount reached,test marked as failed
	            }
	        } else {
	            result.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
	        }
	        return false;
	    }
}
