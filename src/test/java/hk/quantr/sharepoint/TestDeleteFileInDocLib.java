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

			List<String> lines = IOUtils.readLines(new FileReader(System.getProperty("user.home") + File.separator + "password.txt"));
			String password = lines.get(0);
			String domain = "quantr";
			Pair<String, String> token = SPOnline.login("wordpress@quantr.hk", password, domain);
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
				jsonString = SPOnline.delete(token, domain, "/dev/_api/web/GetFolderByServerRelativeUrl('/" + site + "/" + docLib + SPOnline.escapeSharePointUrl("/folder 1/Screen Shot 2018-08-31 at 6.55.08 PM.png") + "')", formDigestValue);
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
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
