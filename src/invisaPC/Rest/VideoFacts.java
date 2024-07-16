package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;

/**
 * Class that contains all tests for appliance rest api video_facts
 * @author Boxilla
 *
 */
public class VideoFacts extends StartupTestCase {

	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	
	
	final static Logger log = Logger.getLogger(VideoFacts.class);
	////RECEIVER TESTS ARE NOT APPLICABLE FOR 4K OR > 5.3 3K DEVICES
	
	/**
	 * Will check that the server returned a status of 200
	 */

//	@Test(groups = {"rest", "emerald", "quick"})
//	public void test01_verifyReturnCode () {
//		log.info("***** test01_verifyReturnCode *****");
//		int statusCode = given().header(getHead())
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts")
//		.getStatusCode();
//		log.info("RETURN: " + statusCode);
//		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
//		
//	}
//	
//	//these tests will check the video facts parameters
//	/**
//	 * Checks the rest api returns id
//	 */
//	@Test(groups = {"rest", "smoke", "emerald", "quick"})
//	public void test02_checkKvmVideoFactsIdReceiver () {
//		log.info("***** test02_checkKvmVideoFactsIdReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"id\":"), "Video facts did not contain id");
//	}
//	
//	/**
//	 * Checks the rest api returns head
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test03_checkKvmVideoFactsHeadReceiver () {
//		log.info("***** test03_checkKvmVideoFactsHeadReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"head\":1"), "Video facts did not contain head:1");
//	}
//	
//	/**
//	 * checks the rest api returns mac
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test04_checkKvmVideoFactsMacReceiver () {
//		log.info("***** test04_checkKvmVideoFactsMacReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"mac\":\"" + rxSingle.getMac().toLowerCase()), 
//				"Video facts did not contain \"mac\":\"00:8c:10:1b:08:1e\"");
//	}
//	/**
//	 * Checks the rest api returns min_tiles_per_frame
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test05_checkKvmVideoFactsMinTilesReceiver () {
//		log.info("***** test05_checkKvmVideoFactsMinTilesReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"min_tiles_per_frame\":"), 
//				"Video facts did not contain \"min_tiles_per_frame\":");
//	}
//	/**
//	 * Checks the rest api returns max_tiles_per_frame
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test06_checkKvmVideoFactsMaxTilesReceiver () {
//		log.info("***** test06_checkKvmVideoFactsMaxTilesReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"max_tiles_per_frame\":"), 
//				"Video facts did not contain \"max_tiles_per_frame\":");
//	}
//	/**
//	 * checks the rest api returns avg_tiles_per_frame
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test07_checkKvmVideoFactsAvgTilesReceiver () {
//		log.info("***** test07_checkKvmVideoFactsAvgTilesReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"avg_tiles_per_frame\":"), 
//				"Video facts did not contain \"avg_tiles_per_frame\":");
//	}
//	/**
//	 * check that the rest api returns frames_per_second
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test08_checkKvmVideoFactsFPSReceiver () {
//		log.info("***** test08_checkKvmVideoFactsFPSReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"frames_per_second\":"), 
//				"Video facts did not contain \"frames_per_second\":");
//	}
//	/**
//	 * Checks that the rest api returns min_frames_per_sec
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test09_checkKvmVideoFactsMinFPSReceiver () {
//		log.info("***** test09_checkKvmVideoFactsMinFPSReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"min_frames_per_sec\":"), 
//				"Video facts did not contain \"min_frames_per_sec\":");
//	}
//	/**
//	 * Checks that the rest api returns max_frames_per_sec
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test10_checkKvmVideoFactsMaxFPSReceiver () {
//		log.info("***** test10_checkKvmVideoFactsMaxFPSReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"max_frames_per_sec\":"), 
//				"Video facts did not contain \"max_frames_per_sec\":");
//	}
//	/**
//	 * Checks that the rest api returns min_tile_size
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test11_checkKvmVideoFactsMinTileSizeReceiver () {
//		log.info("***** test11_checkKvmVideoFactsMinTileSizeReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"min_tile_size\":"), 
//				"Video facts did not contain \"min_tile_size\":");
//	}
//	/**
//	 * Checks that the rest api returns max_tile_size
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test12_checkKvmVideoFactsMaxTileSizeReceiver () {
//		log.info("***** test12_checkKvmVideoFactsMaxTileSizeReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"max_tile_size\":"), 
//				"Video facts did not contain \"max_tile_size\":");
//	}
//	/**
//	 * Checks that the rest api returns avg_tile_size
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test13_checkKvmVideoFactsAvgTileSizeReceiver () {
//		log.info("***** test13_checkKvmVideoFactsAvgTileSizeReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"avg_tile_size\":"), 
//				"Video facts did not contain \"avg_tile_size\":");
//	}
//	/**
//	 * Checks that the rest api returns min_video_bw
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test14_checkKvmVideoFactsMinVideoBwReceiver () {
//		log.info("***** test14_checkKvmVideoFactsMinVideoBwReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"min_video_bw\":"), 
//				"Video facts did not contain \"min_video_bw\":");
//	}
//	/**
//	 * Checks that the rest api returns max_video_bw
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test15_checkKvmVideoFactsMaxVideoBwReceiver () {
//		log.info("***** test15_checkKvmVideoFactsMaxVideoBwReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"max_video_bw\":"), 
//				"Video facts did not contain \"max_video_bw\":");
//	}
//	/**
//	 * Checks that the rest api returns avg_video_bw
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test16_checkKvmVideoFactsAvgVideoBwReceiver () {
//		log.info("***** test16_checkKvmVideoFactsAvgVideoBwReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"avg_video_bw\":"), 
//				"Video facts did not contain \"avg_video_bw\":");
//	}
//	/**
//	 * Checks that the rest api returns video_bw
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test17_checkKvmVideoFactsVideoBwReceiver () {
//		log.info("***** test17_checkKvmVideoFactsVideoBwReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"video_bw\":"), 
//				"Video facts did not contain \"video_bw\":");
//	}
//	/**
//	 * Check that the rest api returns min_rtt
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test18_checkKvmVideoFactsMinRttReceiver () {
//		log.info("***** test18_checkKvmVideoFactsMinRttReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"min_rtt\":"), 
//				"Video facts did not contain \"min_rtt\":");
//	}
//	/**
//	 * Checks that the rest api returns max_rtt
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test19_checkKvmVideoFactsMaxRttReceiver () {
//		log.info("***** test19_checkKvmVideoFactsMaxRttReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"max_rtt\":"), 
//				"Video facts did not contain \"max_rtt\":");
//	}
//	/**
//	 * Checks that the rest api returns avg_rtt
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test20_checkKvmVideoFactsAvgRttReceiver () {
//		log.info("***** test20_checkKvmVideoFactsAvgRttReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"avg_rtt\":"), 
//				"Video facts did not contain \"avg_rtt\":");
//	}
//	/**
//	 * checks that the rest api returns rtt
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test21_checkKvmVideoFactsRttReceiver () {
//		log.info("***** test21_checkKvmVideoFactsRttReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"rtt\":"), 
//				"Video facts did not contain \"rtt\":");
//	}
//	/**
//	 * Checks that the test api returns user_response
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test22_checkKvmVideoFactsUserResponseReceiver () {
//		log.info("***** test22_checkKvmVideoFactsUserResponseReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"user_response\":"), 
//				"Video facts did not contain \"user_response\":");
//	}
//	/**
//	 * Checks that the rest api returns resolution
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test23_checkKvmVideoFactsResolutionReceiver () {
//		log.info("***** test23_checkKvmVideoFactsResolutionReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN TEST 23: " + returnValue);
//		Assert.assertTrue( returnValue.contains("\"resolution\""), 
//				"Video facts did not contain \"resolution\"");
//	}
//	/**
//	 * Checks that the rest api returns dropped_frames
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test24_checkKvmVideoFactsDroppedFramesReceiver () {
//		log.info("***** test24_checkKvmVideoFactsDroppedFramesReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"dropped_frames\":"), 
//				"Video facts did not contain \"dropped_frames\":");
//	}
//	/**
//	 * Checks that the rest api returns timestamp
//	 */
//	@Test(groups = {"rest", "emerald"})
//	public void test25_checkKvmVideoFactsTimeStampReceiver () {
//		log.info("***** test25_checkKvmVideoFactsTimeStampReceiver *****");
//		String returnValue = given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/video_facts").asString();
//		log.info("RETURN: " + returnValue);
//		Assert.assertTrue(returnValue.contains("\"timestamp\":"), 
//				"Video facts did not contain \"timestamp\":");
//	}
	
	//////////////////////////////////////////////
	//TRANSMITTOR
	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest", "emerald"})
	public void test01_verifyReturnCodeTransmitter () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts")
		.getStatusCode();
		log.info("RETURN: " + statusCode);
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
	
	//these tests will check the video facts parameters
	/**
	 * Checks the rest api returns id
	 */
	@Test(groups = {"rest", "smoke", "emerald"})
	public void test02_checkKvmVideoFactsIdTransmitter () {
		log.info("***** test02_checkKvmVideoFactsIdReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"id\":"), "Video facts did not contain id");
	}
	
	/**
	 * Checks the rest api returns head
	 */
	@Test(groups = {"rest", "emerald"})
	public void test03_checkKvmVideoFactsHeadTransmitter () {
		log.info("***** test03_checkKvmVideoFactsHeadReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"head\":1"), "Video facts did not contain head:1");
	}
	
	/**
	 * checks the rest api returns mac
	 */
	@Test(groups = {"rest", "emerald"})
	public void test04_checkKvmVideoFactsMacTransmitter () {
		log.info("***** test04_checkKvmVideoFactsMacReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"mac\":\"" + txSingle.getMac().toLowerCase()), 
				"Video facts did not contain \"mac\":\"00:8c:10:1b:08:1e\"");
	}
	/**
	 * Checks the rest api returns min_tiles_per_frame
	 */
	@Test(groups = {"rest", "emerald"})
	public void test05_checkKvmVideoFactsMinTilesTransmitter () {
		log.info("***** test05_checkKvmVideoFactsMinTilesReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"min_tiles_per_frame\":"), 
				"Video facts did not contain \"min_tiles_per_frame\":");
	}
	/**
	 * Checks the rest api returns max_tiles_per_frame
	 */
	@Test(groups = {"rest", "emerald"})
	public void test06_checkKvmVideoFactsMaxTilesTransmitter () {
		log.info("***** test06_checkKvmVideoFactsMaxTilesReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"max_tiles_per_frame\":"), 
				"Video facts did not contain \"max_tiles_per_frame\":");
	}
	/**
	 * checks the rest api returns avg_tiles_per_frame
	 */
	@Test(groups = {"rest", "emerald"})
	public void test07_checkKvmVideoFactsAvgTilesTransmitter () {
		log.info("***** test07_checkKvmVideoFactsAvgTilesReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"avg_tiles_per_frame\":"), 
				"Video facts did not contain \"avg_tiles_per_frame\":");
	}
	/**
	 * check that the rest api returns frames_per_second
	 */
	@Test(groups = {"rest", "emerald"})
	public void test08_checkKvmVideoFactsFPSTransmitter () {
		log.info("***** test08_checkKvmVideoFactsFPSReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"frames_per_second\":"), 
				"Video facts did not contain \"frames_per_second\":");
	}
	/**
	 * Checks that the rest api returns min_frames_per_sec
	 */
	@Test(groups = {"rest", "emerald"})
	public void test09_checkKvmVideoFactsMinFPSTransmitter () {
		log.info("***** test09_checkKvmVideoFactsMinFPSReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"min_frames_per_sec\":"), 
				"Video facts did not contain \"min_frames_per_sec\":");
	}
	/**
	 * Checks that the rest api returns max_frames_per_sec
	 */
	@Test(groups = {"rest", "emerald"})
	public void test10_checkKvmVideoFactsMaxFPSTransmitter () {
		log.info("***** test10_checkKvmVideoFactsMaxFPSReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"max_frames_per_sec\":"), 
				"Video facts did not contain \"max_frames_per_sec\":");
	}
	/**
	 * Checks that the rest api returns min_tile_size
	 */
	@Test(groups = {"rest", "emerald"})
	public void test11_checkKvmVideoFactsMinTileSizeTransmitter () {
		log.info("***** test11_checkKvmVideoFactsMinTileSizeReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"min_tile_size\":"), 
				"Video facts did not contain \"min_tile_size\":");
	}
	/**
	 * Checks that the rest api returns max_tile_size
	 */
	@Test(groups = {"rest", "emerald"})
	public void test12_checkKvmVideoFactsMaxTileSizeTransmitter () {
		log.info("***** test12_checkKvmVideoFactsMaxTileSizeReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"max_tile_size\":"), 
				"Video facts did not contain \"max_tile_size\":");
	}
	/**
	 * Checks that the rest api returns avg_tile_size
	 */
	@Test(groups = {"rest", "emerald"})
	public void test13_checkKvmVideoFactsAvgTileSizeTransmitter () {
		log.info("***** test13_checkKvmVideoFactsAvgTileSizeReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"avg_tile_size\":"), 
				"Video facts did not contain \"avg_tile_size\":");
	}
	/**
	 * Checks that the rest api returns min_video_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test14_checkKvmVideoFactsMinVideoBwTransmitter () {
		log.info("***** test14_checkKvmVideoFactsMinVideoBwReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"min_video_bw\":"), 
				"Video facts did not contain \"min_video_bw\":");
	}
	/**
	 * Checks that the rest api returns max_video_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test15_checkKvmVideoFactsMaxVideoBwTransmitter () {
		log.info("***** test15_checkKvmVideoFactsMaxVideoBwReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"max_video_bw\":"), 
				"Video facts did not contain \"max_video_bw\":");
	}
	/**
	 * Checks that the rest api returns avg_video_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test16_checkKvmVideoFactsAvgVideoBwTransmitter () {
		log.info("***** test16_checkKvmVideoFactsAvgVideoBwReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"avg_video_bw\":"), 
				"Video facts did not contain \"avg_video_bw\":");
	}
	/**
	 * Checks that the rest api returns video_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test17_checkKvmVideoFactsVideoBwTransmitter () {
		log.info("***** test17_checkKvmVideoFactsVideoBwReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"video_bw\":"), 
				"Video facts did not contain \"video_bw\":");
	}
	/**
	 * Check that the rest api returns min_rtt
	 */
	@Test(groups = {"rest", "emerald"})
	public void test18_checkKvmVideoFactsMinRttTransmitter () {
		log.info("***** test18_checkKvmVideoFactsMinRttReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"min_rtt\":"), 
				"Video facts did not contain \"min_rtt\":");
	}
	/**
	 * Checks that the rest api returns max_rtt
	 */
	@Test(groups = {"rest", "emerald"})
	public void test19_checkKvmVideoFactsMaxRttTransmitter () {
		log.info("***** test19_checkKvmVideoFactsMaxRttReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"max_rtt\":"), 
				"Video facts did not contain \"max_rtt\":");
	}
	/**
	 * Checks that the rest api returns avg_rtt
	 */
	@Test(groups = {"rest", "emerald"})
	public void test20_checkKvmVideoFactsAvgRttTransmitter () {
		log.info("***** test20_checkKvmVideoFactsAvgRttReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"avg_rtt\":"), 
				"Video facts did not contain \"avg_rtt\":");
	}
	/**
	 * checks that the rest api returns rtt
	 */
	@Test(groups = {"rest", "emerald"})
	public void test21_checkKvmVideoFactsRttTransmitter () {
		log.info("***** test21_checkKvmVideoFactsRttReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"rtt\":"), 
				"Video facts did not contain \"rtt\":");
	}
	/**
	 * Checks that the test api returns user_response
	 */
	@Test(groups = {"rest", "emerald"})
	public void test22_checkKvmVideoFactsUserResponseTransmitter () {
		log.info("***** test22_checkKvmVideoFactsUserResponseReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"user_response\":"), 
				"Video facts did not contain \"user_response\":");
	}
	/**
	 * Checks that the rest api returns resolution
	 */
	@Test(groups = {"rest", "emerald"})
	public void test23_checkKvmVideoFactsResolutionTransmitter () {
		log.info("***** test23_checkKvmVideoFactsResolutionReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"resolution\":\""), 
				"Video facts did not contain \"resolution\":\"");
	}
	/**
	 * Checks that the rest api returns dropped_frames
	 */
	@Test(groups = {"rest", "emerald"})
	public void test24_checkKvmVideoFactsDroppedFramesTransmitter () {
		log.info("***** test24_checkKvmVideoFactsDroppedFramesReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"dropped_frames\":"), 
				"Video facts did not contain \"dropped_frames\":");
	}
	/**
	 * Checks that the rest api returns timestamp
	 */
	@Test(groups = {"rest", "emerald"})
	public void test25_checkKvmVideoFactsTimeStampTransmitter () {
		log.info("***** test25_checkKvmVideoFactsTimeStampReceiver *****");
		String returnValue = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/video_facts").asString();
		log.info("RETURN: " + returnValue);
		Assert.assertTrue(returnValue.contains("\"timestamp\":"), 
				"Video facts did not contain \"timestamp\":");
	}

	/**
	 * Overrides superclass method to create real connection for tests
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		try {
			log.info("**** Starting test setup for " + this.getClass().getSimpleName() + " ****");
			//cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			//deviceManageTestPrep();
			log.info("Creating connection through SSH");
			conMethods.createRealConnection("", deviceUserName, devicePassword, rxIp, txIp) ;
			log.info("Connection created");
			log.info("Sleeping...");
			Thread.sleep(70000);
			log.info("Waking...setup complete");
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			//cleanUpLogout();
		}
		//cleanUpLogout();
	}

	
	/**
	 * We dont need to log into boxilla after each method in this suite
	 * so override with an empty method
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void login(String browser, Method method) {
		
	}
	
	@Override
	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result) {
		log.info("********* @ After Method Started ************");
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			try {
				//Utilities.captureLog(boxillaManager, prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"),
				//		 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
				}catch(Exception e) {
					System.out.println("Error when trying to capture log file. Catching error and continuing");
					e.printStackTrace();
				}
			try {
				Utilities.captureDeviceLog(rxIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + rxIp);
			}
			try {
				Utilities.captureDeviceLog(txIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + txIp);
			}
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	
//	/**
//	 * Method to manage transmitter and receiver device
//	 * @throws InterruptedException
//	 */
//	public void deviceManageTestPrep() throws InterruptedException {
//		log.info("Test Preparation - Unamage - Manage Device");
//		
//		//RX
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("rxMac"), prop.getProperty("ipCheck"), prop.getProperty("rxIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_RX", prop.getProperty("rxMac"), prop.getProperty("ipCheck"));
//		
//		//TX
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("txMac"), prop.getProperty("ipCheck"), prop.getProperty("txIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_TX", prop.getProperty("txMac"), prop.getProperty("ipCheck"));
//		
//		log.info("Appliance Managed Successfully - Test Preparation Completed");
//		log.info("Sleeping while devices configures");
//		Thread.sleep(100000);
//	}
	
	
}
