// License : Apache License Version 2.0  https://www.apache.org/licenses/LICENSE-2.0
package hk.quantr.sharepoint;

import com.google.common.net.UrlEscapers;
import com.peterswing.CommonLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author Peter <mcheung63@hotmail.com>
 */
public class TestUploadFile {

	@Test
	public void testUpload() {
		try {
			List<String> lines = IOUtils.readLines(new FileReader(System.getProperty("user.home") + File.separator + "password.txt"));
			String password = lines.get(0);
			String domain = "237226835";
			Pair<String, String> token = SPOnline.login("zengq@237226835.onmicrosoft.com", password, domain);
			if (token != null) {
				String jsonString = SPOnline.post(token, domain, "/_api/contextinfo", null, null);
				System.out.println(CommonLib.prettyFormatJson(jsonString));
				JSONObject json = new JSONObject(jsonString);
				String formDigestValue = json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue");
				System.out.println("FormDigestValue=" + formDigestValue);

				//jsonString = SPOnline.post(token, domain, "/_api/web/lists/GetByTitle('20181014')/rootfolder/files/add(overwrite=true,url='filename.txt')", "fuck", formDigestValue);
				String filepath = "F:/sharePointTestFile/firecloud.jpg";
				String content = FileUtils.readFileToString(new File(filepath), "utf-8");
				jsonString = SPOnline.post(token, domain, "/_api/web/getfolderbyserverrelativeurl('" + UrlEscapers.urlFragmentEscaper().escape("/20181014") + "')/files/add(overwrite=true,url='" + UrlEscapers.urlFragmentEscaper().escape("我515.jpg") + "')", content, formDigestValue);
				if (jsonString != null) {
					System.out.println(jsonString);
					System.out.println(CommonLib.prettyFormatJson(jsonString));
				}
			} else {
				System.err.println("Login failed");
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TestUploadFile.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TestUploadFile.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
