package hk.quantr.sharepoint.data;

import com.google.gson.JsonObject;
import com.peterswing.CommonLib;
import hk.quantr.sharepoint.SPOnline;
import hk.quantr.sharepoint.TestSPOnline;
import hk.quantr.sharepoint.entity.RestOfficeVo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter <peter@quantr.hk>
 */
public class TestData {

	@Test
	public void addFile() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("gruopId","11");
		 json.put("docType","Word");
		 json.put("docName","xxx.docx");
		 json.put("userId","zs");
		System.out.println(json);


		JSONObject result = new JSONObject();
		result.put("success","11");
		result.put("msg","执行成功！");

		JSONObject data = new JSONObject();
		data.put("officeId","123");
		data.put("url","xxx.com");
		result.put("data",data);
		System.out.println(result);
	}
	@Test
	public void importFile() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("gruopId","11");
		 json.put("fileId","sfwer");
		 json.put("userId","zs");
		System.out.println(json);


		JSONObject result = new JSONObject();
		result.put("success","11");
		result.put("msg","执行成功！");

		JSONObject data = new JSONObject();
		data.put("officeId","123");
		data.put("url","xxx.com");
		result.put("data",data);
		System.out.println(result);
	}
	@Test
	public void setPermissions() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("userId","11");
		 json.put("officeId","sfwer");
		 json.put("shareId","zs");
		 json.put("permission","edit");
		System.out.println(json);


		JSONObject result = new JSONObject();
		result.put("success","11");
		result.put("msg","执行成功！");

		JSONObject data = new JSONObject();
		data.put("officeId","123");
		data.put("url","xxx.com");
//		result.put("data",data);
		System.out.println(result);
	}
	@Test
	public void queryFileUpdateTime() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("userId","11");
		 json.put("officeId","sfwer");
		System.out.println(json);
		JSONObject result = new JSONObject();
		result.put("updateTime","2018-10-22 16:22:22");
		System.out.println(result);
	}

	@Test
	public void renameFile() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("userId","11");
		 json.put("officeId","sfwer");
		 json.put("newName","yyy.docx");
		System.out.println(json);
		JSONObject result = new JSONObject();

	}
	@Test
	public void queryHistory() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("userId","11");
		 json.put("officeId","sfwer");

		System.out.println(json);
		JSONObject result = new JSONObject();
		result.put("success","11");
		result.put("msg","执行成功！");

		JSONObject data = new JSONObject();
		data.put("Version","1.0");
//		data.put("fileName","yyy.docx");
		data.put("creator","张三");
		data.put("url","sysware.com");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format1 = format.format(new Date());

        data.put("time",format1);

        JSONArray array = new JSONArray();
        array.put(data);
        array.put(data);
        array.put(data);

		result.put("data",array);

        System.out.println(result);
	}
	@Test
	public void getDownloadUrl() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("userId","11");
		 json.put("officeId","sfwer");
        json.put("Version","");
		System.out.println(json);
		JSONObject result = new JSONObject();
		result.put("success","11");
		result.put("msg","执行成功！");
        result.put("data","sysware.com");

		JSONObject data = new JSONObject();
		data.put("Version","1.0");


        System.out.println(result);
	}
	@Test
	public void syncGroup() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("gruopName","11");

	}
	@Test
	public void syncUser() {
		RestOfficeVo vo = new RestOfficeVo("11","Word","xxx.docx","zs");
		JSONObject json = new JSONObject();
		 json.put("username","11");
		 json.put("password","11");
        

	}

}
