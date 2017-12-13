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
 * @author Peter <peter@quantr.hk>
 */
public class TestSPView {
	
	@Test
	public void test1() {
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

				// get fields in a specific
				jsonString = SPOnline.get(token, domain, "/test/_api/web/lists/GetByTitle('doclib1')/views(4DBF612B-E3F9-46F2-87CC-FFC8C16CB943)");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
				}
			} else {
				System.err.println("Login failed");
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TestSPOnline.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TestSPOnline.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
