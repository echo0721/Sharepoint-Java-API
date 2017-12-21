// License : Apache License Version 2.0  https://www.apache.org/licenses/LICENSE-2.0
package hk.quantr.sharepoint;

import com.peterswing.CommonLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author Peter <mcheung63@hotmail.com>
 */
public class TestGetFilesFromDocLib {

	@Test
	public void testUpload() {
		try {
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

				jsonString = SPOnline.get(token, domain, "/test/_api/web/lists/getbytitle('doclib1')/files");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
				}
			} else {
				System.err.println("Login failed");
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TestGetFilesFromDocLib.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TestGetFilesFromDocLib.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
