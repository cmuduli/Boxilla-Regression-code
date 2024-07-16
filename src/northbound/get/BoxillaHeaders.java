package northbound.get;

import java.util.ArrayList;
import java.util.List;

import io.restassured.http.Header;
import io.restassured.http.Headers;

public class BoxillaHeaders {

	private  static Header myHead = new Header("accept", "application/json");
	private static Header versionHead = new Header("Accept-version", "v1.1");
	private static Headers headers;
	private static List<Header> headList = new ArrayList<Header>();
	
	public static Headers getBoxillaHeaders() {
		headList.clear();
		headList.add(myHead);
		headList.add(versionHead);
		headers = new Headers(headList);
		return headers;
	}
	
}
