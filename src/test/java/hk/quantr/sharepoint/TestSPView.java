package hk.quantr.sharepoint;

import com.peterswing.CommonLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author Peter <peter@quantr.hk>
 */
public class TestSPView {

	@Test
	public void test1() {
		try {
			Logger.getLogger(TestSPOnline.class).info("test1");
			List<String> lines = IOUtils.readLines(new FileReader(System.getProperty("user.home") + File.separator + "password.txt"));
			String password = lines.get(0);
			String domain = "quantr";
			Pair<String, String> token = SPOnline.login("wordpress@quantr.hk", password, domain);
			if (token != null) {
				String jsonString = SPOnline.post(token, domain, "/_api/contextinfo", null, null);
				System.out.println(CommonLib.prettyFormatJson(jsonString));
				JSONObject json = new JSONObject(jsonString);
				String formDigestValue = json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue");
				System.out.println("FormDigestValue=" + formDigestValue);

//				// print all view names
//				jsonString = SPOnline.get(token, domain, "/test/_api/web/lists/GetByTitle('doclib1')/views");
//				if (jsonString != null) {
//					json = new JSONObject(jsonString);
//					JSONArray arr = json.getJSONObject("d").getJSONArray("results");
//					for (int x = 0; x < arr.length(); x++) {
//						System.out.println(arr.getJSONObject(x).getString("Title"));
//					}
//				}
//
				// get specific view
				jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/views/getbytitle('" + SPOnline.escapeSharePointUrl("peter view呀") + "')");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
				}
//
//				// get all fields of a specific view
//				jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/views/getbytitle('" + SPOnline.escapeSharePointUrl("peter view呀") + "')/ViewFields");
//				if (jsonString != null) {
//					System.out.println(CommonLib.prettyFormatJson(jsonString));
//					json = new JSONObject(jsonString);
//					JSONArray arr = json.getJSONObject("d").getJSONObject("Items").getJSONArray("results");
//					for (int x = 0; x < arr.length(); x++) {
//						System.out.println(arr.getString(x));
//					}
//				}

				// get items of a specific view
				jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/views/getbytitle('" + SPOnline.escapeSharePointUrl("peter view呀") + "')/items");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
//					json = new JSONObject(jsonString);
//					JSONArray arr = json.getJSONObject("d").getJSONObject("Items").getJSONArray("results");
//					for (int x = 0; x < arr.length(); x++) {
//						System.out.println(arr.getString(x));
//					}
				} else {
					System.out.println("fuck");
				}
			} else {
				System.err.println("Login failed");
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TestSPOnline.class.getName()).error(ex);
		} catch (IOException ex) {
			Logger.getLogger(TestSPOnline.class.getName()).error(ex);
		}
	}
}
