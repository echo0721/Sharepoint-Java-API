// License : Apache License Version 2.0  https://www.apache.org/licenses/LICENSE-2.0
package hk.quantr.sharepoint;

import com.google.common.net.UrlEscapers;
import com.peterswing.CommonLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
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
public class TestHavePermissions {

	/*
	 * check if the user can access site https://quantr.sharepoint.com/quantr-dev-central, if i remove the user from the owner group, api return false. If the user is in owner group, api return true. "quantr-dev-central" is my subsite name.
	   The high/low values passed to refer to /_api/web/doesuserhavepermissions, please refer https://msdn.microsoft.com/en-us/library/office/ee556747(v=office.14).aspx or below
		
		namespace Microsoft.SharePoint.Client
		{
			public enum PermissionKind
			{
				EmptyMask = 0,
				ViewListItems = 1,
				AddListItems = 2,
				EditListItems = 3,
				DeleteListItems = 4,
				ApproveItems = 5,
				OpenItems = 6,
				ViewVersions = 7,
				DeleteVersions = 8,
				CancelCheckout = 9,
				ManagePersonalViews = 10,
				ManageLists = 12,
				ViewFormPages = 13,
				AnonymousSearchAccessList = 14,
				Open = 17,
				ViewPages = 18,
				AddAndCustomizePages = 19,
				ApplyThemeAndBorder = 20,
				ApplyStyleSheets = 21,
				ViewUsageData = 22,
				CreateSSCSite = 23,
				ManageSubwebs = 24,
				CreateGroups = 25,
				ManagePermissions = 26,
				BrowseDirectories = 27,
				BrowseUserInfo = 28,
				AddDelPrivateWebParts = 29,
				UpdatePersonalWebParts = 30,
				ManageWeb = 31,
				AnonymousSearchAccessWebLists = 32,
				UseClientIntegration = 37,
				UseRemoteAPIs = 38,
				ManageAlerts = 39,
				CreateAlerts = 40,
				EditMyUserInfo = 41,
				EnumeratePermissions = 63,
				FullMask = 65
			}
		}
	 */

	@Test
	public void testHavePermissions() {
		try {
			List<String> lines = IOUtils.readLines(new FileReader(System.getProperty("user.home") + File.separator + "password.txt"));
			String password = lines.get(0);
			String domain = "quantr";
			Pair<String, String> token = SPOnline.login("wordpress@quantr.hk", password, domain);
			if (token != null) {
				String jsonString = SPOnline.post(token, domain, "/_api/contextinfo", null, null);
				JSONObject json = new JSONObject(jsonString);
				String formDigestValue = json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue");

				jsonString = SPOnline.get(token, domain, "/quantr-dev-central/_api/web/doesuserhavepermissions(@v)?@v=" + URLEncoder.encode("{'High':'1', 'Low':'1'}", "utf-8"));
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
				} else {
					System.out.println("null");
				}
			} else {
				System.err.println("Login failed");
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TestHavePermissions.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TestHavePermissions.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
