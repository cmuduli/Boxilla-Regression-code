package user;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import testNG.FixRetryListener;
@Listeners(value = FixRetryListener.class)
public class Testing {

	int x = 0;
	
	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01() {
		x++;
		Assert.assertTrue(x == 2);
		x= 0;	
	}
	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test05() {
		x++;
		Assert.assertTrue(x == 2);	
	}
	
	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test03() {
		Assert.assertTrue(true);
	}
//	
	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test02() {
		Assert.assertTrue(false);
	}
	
	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test09() {
		throw new SkipException("SKIPPING");
	}
	
	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test1() {
		throw new SkipException("SKIPPING");
	}
	
//	
//	@Test(retryAnalyzer = com.testNG.RetryAnalyzer.class)
//	public void test04() {
//		Assert.assertTrue(false);
//	}
//	
}
