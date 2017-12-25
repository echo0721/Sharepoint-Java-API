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
public class TestDocLib {

	@Test
	public void getItemsFromDocLib() {
		try {
			Logger.getLogger(TestSPOnline.class).info("get all items from document library");
			//String view = "peter view呀";
			String view = "view1";

			List<String> lines = IOUtils.readLines(new FileReader(System.getProperty("user.home") + File.separator + "password.txt"));
			String password = lines.get(0);
			String domain = "quantr";
			Pair<String, String> token = SPOnline.login("wordpress@quantr.hk", password, domain);
			if (token != null) {
				JSONObject json;
				String jsonString;

				// get folder by specific an uniqueId
				ArrayList<String> uniqueIds = new ArrayList();
				jsonString = SPOnline.get(token, domain, "test/_api/web/getfolderbyserverrelativeurl('/test/doclib1')/folders");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
					json = new JSONObject(jsonString);
					JSONArray arr = json.getJSONObject("d").getJSONArray("results");
					for (int x = 0; x < arr.length(); x++) {
						uniqueIds.add(arr.getJSONObject(x).getString("UniqueId"));
					}
				}

				// get file by specific an uniqueId
				jsonString = SPOnline.get(token, domain, "test/_api/web/getfolderbyserverrelativeurl('/test/doclib1')/files");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
					json = new JSONObject(jsonString);
					JSONArray arr = json.getJSONObject("d").getJSONArray("results");
					for (int x = 0; x < arr.length(); x++) {
						uniqueIds.add(arr.getJSONObject(x).getString("UniqueId"));
					}
				}

				// get all fields
				HashMap<String, Integer> fieldtypes = new HashMap<String, Integer>();
				jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/fields");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
					json = new JSONObject(jsonString);
					JSONArray arr = json.getJSONObject("d").getJSONArray("results");
					for (int x = 0; x < arr.length(); x++) {
						System.out.println(arr.getJSONObject(x).getString("Title") + "\t\t\t" + arr.getJSONObject(x).getString("InternalName") + "\t\t\t" + arr.getJSONObject(x).getInt("FieldTypeKind"));
						fieldtypes.put(arr.getJSONObject(x).getString("InternalName"), arr.getJSONObject(x).getInt("FieldTypeKind"));
					}
				}

				String query = "";
				String expand = "&$expand=";

				// get all fields of a specific view
				jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/views/getbytitle('" + SPOnline.escapeSharePointUrl(view) + "')/ViewFields");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
					json = new JSONObject(jsonString);
					JSONArray arr = json.getJSONObject("d").getJSONObject("Items").getJSONArray("results");
					for (int x = 0; x < arr.length(); x++) {
						String fieldName = arr.getString(x);
						if (fieldtypes.get(fieldName) == 20) {
							query += fieldName + "/Title,";
							expand += fieldName + ",";
						} else {
							query += fieldName + ",";
						}
					}
				}
				query += "UniqueId,";
				query += "GUID,";

				if (query.endsWith(",")) {
					query = query.substring(0, query.length() - 1);
				}
				if (expand.endsWith(",")) {
					expand = expand.substring(0, expand.length() - 1);
				}

				// get items of a specific view
				String filters = "";
				String url = "test/_api/web/lists/GetByTitle('doclib1')/items?$filter=" + URLEncoder.encode("GUID eq guid'47e1eab5-c06e-46a3-a100-ab0fa3874416'", "utf8");
//				url = "test/_api/web/lists/GetByTitle('doclib1')/items?$filter=" + URLEncoder.encode("ID eq 13", "utf8");
				url = "test/_api/web/lists/GetByTitle('doclib1')/items?$select=LinkFilename,GUID,UniqueId&$filter=" + URLEncoder.encode("AuthorId eq 11", "utf8");
				url = "test/_api/web/lists/GetByTitle('doclib1')/items?$select=LinkFilename,GUID,UniqueId&$filter=" + URLEncoder.encode("UniqueId eq guid'e7ac2b9b-d5e3-4257-a21c-0e2951e83887'", "utf8");
//				url = "test/_api/web/lists/GetByTitle('doclib1')/items;
				System.out.println(url);
				jsonString = SPOnline.get(token, domain, url);
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
