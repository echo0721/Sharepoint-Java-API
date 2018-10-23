package com.sysware.service.office;

import com.peterswing.CommonLib;
import com.sysware.connect.SPOnline;
import com.sysware.entity.vo.ConnectVo;
import com.sysware.entity.vo.OfficeRestVo;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import java.io.File;

public class OfficeRestServiceImpl implements OfficeRestService {


    private ConnectVo getPostParam(OfficeRestVo vo) {
        ConnectVo con = null;
        String domain = "237226835";
        //查询eap获得username，password
        String password = "Me123456";
        String username = "zengq@237226835.onmicrosoft.com";

        Pair<String, String> token = SPOnline.login(username, password, domain);

        if (token != null) {
            con = new ConnectVo();
            con.setDomain(domain);
            con.setPassword(password);
            con.setToken(token);
            String jsonString = SPOnline.post(token, domain, "_api/contextinfo", null, null);
            JSONObject json = new JSONObject(jsonString);
            String formDigestValue = json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue");
            con.setFormDigestValue(formDigestValue);
        }
        return con;
    }

    private String post(ConnectVo vo) {

        String postRes = SPOnline.post(vo.getToken(), vo.getDomain(), vo.getUrl(), vo.getData(), vo.getFormDigestValue());
        return postRes;
    }
    private String uploadFile(ConnectVo vo) {

        File file = new File(vo.getData());

        String postRes =  SPOnline.uploadFile(vo.getToken(), vo.getDomain(), vo.getUrl(), file , vo.getFormDigestValue());
        return postRes;
    }

    @Override
    public String createFloder(OfficeRestVo vo) {
//        url: http://site url/_api/web/folders
//        method: POST
//        body: { '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '/document library relative url/folder name'}
//        Headers:
//        Authorization: "Bearer " + accessToken
//        X-RequestDigest: form digest value
//        accept: "application/json;odata=verbose"
//        content-type: "application/json;odata=verbose"
//        content-length:length of post body
        String url = "_api/web/folders";
//        document library relative url
        String data = "{ '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '/syswareLib/" + vo.getGroupId() + "'}";

        ConnectVo con = getPostParam(vo);
        con.setUrl(url);
        con.setData(data);
        String jsonString = this.post(con);
        if (jsonString != null) {
            System.out.println(CommonLib.prettyFormatJson(jsonString));

            // "UniqueId": "ffdecae7-6ad1-4180-ae72-bc7b546d45b8", SharePoint端文件夹id
            //  "ServerRelativeUrl": "/syswareLib/20181023",  相对路径
            // "Name": "20181023",

        } else {
            System.out.println("null");
        }

        return null;
    }

    //    url: http://site url/_api/web/GetFolderByServerRelativeUrl('/Folder Name')/Files/add(url='a.txt',overwrite=true)
//    method: POST
//    body: "Contents of file"
//    Headers:
//    Authorization: "Bearer " + accessToken
//    X-RequestDigest: form digest value
//    content-length:length of post body
    @Override
    public String addFile(OfficeRestVo vo) {

        String floder = vo.getGroupId();
        String url = "_api/web/GetFolderByServerRelativeUrl('/syswareLib/" + vo.getGroupId() + "')/Files/add(url='" + vo.getDocName() + "',overwrite=true)";
        ConnectVo con = getPostParam(vo);
        con.setUrl(url);
        con.setData(vo.getData());
//        con.setData("Contents of file");
        String jsonString = this.uploadFile(con);
        if (jsonString != null) {
            System.out.println(CommonLib.prettyFormatJson(jsonString));

            // "UniqueId": "52027068-2d67-46fa-ad9b-16cede5f02dd", SharePoint端文件夹id
            //    "ServerRelativeUrl": "/syswareLib/20181023/sysware0.docx", 相对路径
            //  "Name": "sysware0.docx",
//      下载链接    "LinkingUrl": "https://237226835.sharepoint.com/syswareLib/20181023/sysware0.docx?d\u003dw520270682d6746faad9b16cede5f02dd",
//            "UIVersion": 512,
//             "UIVersionLabel": "1.0",
//            Versions  版本查看
//            "UIVersionLabel": "2.0",

        } else {
            System.out.println("null");
        }

        return null;
    }

    @Override
    public String importNewFile(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String importFile(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String setPermissions(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String delPermissions(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String getFileUpdateTime(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String renameFile(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String queryHistory(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String getDownloadUrl(OfficeRestVo vo) {
        return null;
    }
}
