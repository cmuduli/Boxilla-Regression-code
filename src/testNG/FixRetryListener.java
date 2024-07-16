package testNG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class FixRetryListener extends TestListenerAdapter {

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        System.out.println("****************************** GETTING HERE ***************************");
        // List of test results which we will delete later
        List<ITestResult> testsToBeRemoved = new ArrayList<>();
        
        //get all skipped tests
        Set <Integer> skippedTestIds = new HashSet<>();
      for (ITestResult skippedTest : testContext.getSkippedTests().getAllResults()) {
      	skippedTestIds.add(TestUtil.getId(skippedTest));
      }
      
      //get all passed tests
      Set <Integer> passedTestTestIds = new HashSet<>();
      for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
    	  passedTestTestIds.add(TestUtil.getId(passedTest));
    }
      
      
      //get all failed tests
      Set <Integer> failedTestTestIds = new HashSet<>();
      for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
    	  failedTestTestIds.add(TestUtil.getId(failedTest));
    }
      
      //compare skipped to passed. If match take out the skipped version
      for (ITestResult skippedTest : testContext.getSkippedTests().getAllResults()) {
    	  int skippedTestId = TestUtil.getId(skippedTest);
    	  if(passedTestTestIds.contains(skippedTestId)) {
    		  testsToBeRemoved.add(skippedTest);
    	  }
      }
      
      //compare skipped to failed. If match take out the skipped version
      for (ITestResult skippedTest : testContext.getSkippedTests().getAllResults()) {
    	  int skippedTestId = TestUtil.getId(skippedTest);
    	  if(failedTestTestIds.contains(skippedTestId)) {
    		  testsToBeRemoved.add(skippedTest);
    	  }
      }
      
      //now remove all the tests
      
      
        
//        Set <Integer> skippedTestIds = new HashSet<>();
//        for (ITestResult passedTest : testContext.getSkippedTests().getAllResults()) {
//        	skippedTestIds.add(TestUtil.getId(passedTest));
//        }
//        // collect all id's from passed test
//        Set <Integer> passedTestIds = new HashSet<>();
//        for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
//            passedTestIds.add(TestUtil.getId(passedTest));
////            int test = TestUtil.getId(passedTest);
////            for(Integer x : skippedTestIds) {
////            	if(x == test) {
////            		testsToBeRemoved.add(passedTest);
////            	}
////            }
//        }
//
//
//
//        Set <Integer> failedTestIds = new HashSet<>();
//        for (ITestResult skippedTest : testContext.getSkippedTests().getAllResults()) {
//
//            // id = class + method + dataprovider
//            int failedTestId = TestUtil.getId(skippedTest);
//
//            // if we saw this test as a failed test before we mark as to be deleted
//            // or delete this failed test if there is at least one passed version
//            if ( passedTestIds.contains(failedTestId)) {
//                testsToBeRemoved.add(skippedTest);
//            } 
//        }
//        for (ITestResult skippedTest : testContext.getFailedTests().getAllResults()) {
//
//            // id = class + method + dataprovider
//            int failedTestId = TestUtil.getId(skippedTest);
//
//            // if we saw this test as a failed test before we mark as to be deleted
//            // or delete this failed test if there is at least one passed version
//            if ( failedTestIds.contains(failedTestId)) {
//                testsToBeRemoved.add(skippedTest);
//            } 
//        }
//        
//        
//        
//        for(Integer x : failedTestIds) {
//        	System.out.println("Passed test ids:" + x);
//        }
//
//        // finally delete all tests that are marked
        for (Iterator<ITestResult> iterator = testContext.getSkippedTests().getAllResults().iterator(); iterator.hasNext(); ) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                iterator.remove();
            }
        }
        

    }

}