package hk.quantr.sharepoint;

import com.peterswing.CommonLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class TestDeleteFileInDocLib {

	@Test
	public void getItemsFromDocLib() {

		try {
			Logger.getLogger(TestSPOnline.class).info("get all files with associated items from document library");
			//String view = "peter view呀";
			String site = "dev";
			String docLib = "TestDocLib";

			String uniqueId ="ee201bc0-93c9-4ee9-8f74-38a89e68f88d";

			List<String> lines = IOUtils.readLines(new FileReader(System.getProperty("user.home") + File.separator + "password.txt"));
			String password = lines.get(0);
			String domain = "237226835";
			Pair<String, String> token = SPOnline.login("zengq@237226835.onmicrosoft.com", password, domain);
			if (token != null) {
				JSONObject json;
				String jsonString;

				// get FormDigestValue
				jsonString = SPOnline.post(token, domain, "/_api/contextinfo", null, null);
				System.out.println(CommonLib.prettyFormatJson(jsonString));
				json = new JSONObject(jsonString);
				String formDigestValue = json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue");
				System.out.println("FormDigestValue=" + formDigestValue);

				// delete a file in doc lib
				String path = "_api/web/GetFileByServerRelativeUrl('/20181014/QQQ.docx')" ;
				jsonString = SPOnline.delete(token, domain,path  , formDigestValue);
				if (jsonString != null) {
					System.out.println(123213);
					//System.out.println(CommonLib.prettyFormatJson(jsonString));
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
