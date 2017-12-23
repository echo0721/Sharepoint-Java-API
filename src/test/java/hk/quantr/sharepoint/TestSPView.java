package hk.quantr.sharepoint;

import com.peterswing.CommonLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public class TestSPView {

	@Test
	public void getAllItemsWithFieldsThatSpecificByView() {
		try {
			Logger.getLogger(TestSPOnline.class).info("getAllItemsWithFieldsThatSpecificByView");
			//String view = "peter view呀";
			String view = "view1";

			List<String> lines = IOUtils.readLines(new FileReader(System.getProperty("user.home") + File.separator + "password.txt"));
			String password = lines.get(0);
			String domain = "quantr";
			Pair<String, String> token = SPOnline.login("wordpress@quantr.hk", password, domain);
			if (token != null) {
				JSONObject json;
				String jsonString;

				HashMap<String, Integer> fieldtypes = new HashMap<String, Integer>();
//				HashMap<String, String> fieldInternalName = new HashMap<String, String>();

				// get all fields
				jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/fields");
				if (jsonString != null) {
					System.out.println(CommonLib.prettyFormatJson(jsonString));
					json = new JSONObject(jsonString);
					JSONArray arr = json.getJSONObject("d").getJSONArray("results");
					for (int x = 0; x < arr.length(); x++) {
						System.out.println(arr.getJSONObject(x).getString("Title") + "\t\t\t" + arr.getJSONObject(x).getString("InternalName") + "\t\t\t" + arr.getJSONObject(x).getInt("FieldTypeKind"));
						fieldtypes.put(arr.getJSONObject(x).getString("InternalName"), arr.getJSONObject(x).getInt("FieldTypeKind"));
//						fieldInternalName.put(arr.getJSONObject(x).getString("InternalName"), arr.getJSONObject(x).getString("Title"));
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
//						String internalName = fieldInternalName.get(fieldName);

//						System.out.println(fieldName + " > " + internalName);
						if (fieldtypes.get(fieldName) == 20) {
							query += fieldName + "/Title,";
							expand += fieldName + ",";
						} else {
							query += fieldName + ",";
						}
					}
				}

				if (query.endsWith(",")) {
					query = query.substring(0, query.length() - 1);
				}
				if (expand.endsWith(",")) {
					expand = expand.substring(0, expand.length() - 1);
				}

				// get items of a specific view
				//jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/items?$select=LinkFilename,DocIcon,Modified,Editor/Title&$expand=Editor");
				System.out.println("test/_api/web/lists/GetByTitle('doclib1')/items?$select=" + query + expand);
				jsonString = SPOnline.get(token, domain, "test/_api/web/lists/GetByTitle('doclib1')/items?$select=" + query + expand);
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
