// License : Apache License Version 2.0  https://www.apache.org/licenses/LICENSE-2.0
package hk.quantr.sharepoint;

import com.google.common.net.UrlEscapers;
import com.peterswing.CommonLib;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter <mcheung63@hotmail.com>
 */
public class TestPermissions {

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
			String domain = "237226835";
			Pair<String, String> token = SPOnline.login("zhangfei@237226835.onmicrosoft.com", password, domain);
			if (token != null) {
				String jsonString = SPOnline.post(token, domain, "_api/contextinfo", null, null);
				JSONObject json = new JSONObject(jsonString);
				String formDigestValue = json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue");


//				String endpointUri =  "_api/lists(guid'64981c55-5be3-47b7-af08-a90cf0e16034')/Items?$top=5";
				//获取组id
				//				String endpointUri = "_api/web/sitegroups/getbyname('20181014')";
				//查询可编辑的id  Edit =1073741830 ,完全控制 Contribute =1073741827   仅查看'View Only' =1073741925
//				String endpointUri = "/_api/web/roledefinitions/getbyname('View')";
//				查看权限
//				String endpointUri =  "_api/lists(guid'64981c55-5be3-47b7-af08-a90cf0e16034')/Items(13)/roleassignments/GetByPrincipalId(12)/RoleDefinitionBindings";
//				数据权限
//				/GetByPrincipalId('16')/RoleDefinitionBindings
//				删除默认组  '16','6','7'

				//4D1068B4-5D00-45D6-BF8C-46CADE6CDF8A
				String endpointUri =  "_api/lists(guid'64981c55-5be3-47b7-af08-a90cf0e16034')/Items(13)/roleassignments/GetByPrincipalId(12)";
//				String endpointUri =  "_api/lists(guid'64981c55-5be3-47b7-af08-a90cf0e16034')/Items(13)/breakroleinheritance(true)";
        jsonString = SPOnline.delete(token, domain, endpointUri,  formDigestValue);
		 //   /roleassignments/addroleassignment(principalid='id')

				//通过登录名获取用户
			//	"LoginName": "i:0#.f|membership|zhangfei@237226835.onmicrosoft.com",
				String loginHead = "i%3A0%23.f%7Cmembership%7C";
				String site ="%40"; //@符号
				String loginName =loginHead +"icecream"+ site ; //zhangfei
//				String endpointUri =  "_api/web/siteusers(@v)?@v='"+loginName+"237226835.onmicrosoft.com'";

//				给冰冰赋予编辑权限
//				用户 "Id": 12,  权限 Edit =1073741830     仅查看 1073741925
//				String endpointUri =  "_api/lists(guid'64981c55-5be3-47b7-af08-a90cf0e16034')/Items(13)/roleassignments/addroleassignment(principalid='12',roleDefid='1073741925')";

//				String endpointUri = "_api/web/getuserbyid(33)";
// 			/roleassignments/getbyprincipalid
				System.out.println("formDigestValue:"+formDigestValue);


//				在线编辑连接测试
				//4D1068B4-5D00-45D6-BF8C-46CADE6CDF8A
//				  4d1068b4-5d00-45d6-bf8c-46cade6cdf8a
//				UniqueId  4d1068b4-5d00-45d6-bf8c-46cade6cdf8a
//																				%7B={				UniqueId   %7D =}
	//			https://237226835.sharepoint.com/:w:/r/_layouts/15/Doc.aspx?sourcedoc=%7B4D1068B4-5D00-45D6-BF8C-46CADE6CDF8A%7D&file=YYY.docx&action=default&mobileredirect=true
//				String endpointUri = "_api/web/getfilebyserverrelativeurl('/20181014/YYY.docx')";
// 				String endpointUri =  "_api/lists(guid'64981c55-5be3-47b7-af08-a90cf0e16034')/Items(13)";

//                jsonString = SPOnline.post(token, domain, endpointUri, null, formDigestValue);

//				jsonString = SPOnline.get(token, domain, endpointUri );
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
				} else {
					System.out.println("null");
				}
			} else {
				System.err.println("Login failed");
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TestPermissions.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TestPermissions.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
