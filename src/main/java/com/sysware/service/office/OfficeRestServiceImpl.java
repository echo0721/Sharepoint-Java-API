package com.sysware.service.office;

import com.peterswing.CommonLib;
import com.sysware.connect.SPOnline;
import com.sysware.conver.SPConver;
import com.sysware.entity.vo.ConnectVo;
import com.sysware.entity.vo.OfficeRestVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * syswareLib 为顶层库目录
 */
public class OfficeRestServiceImpl implements OfficeRestService {


    private ConnectVo getPostParam(OfficeRestVo vo) {
        ConnectVo con = null;
        String domain = "237226835";
        //查询eap获得username，password
        String password = "Me123456";
        String username = vo.getUsername();
        if(StringUtils.isNotBlank( vo.getSiteOwner())){
            username = vo.getSiteOwner();
        }

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

    /**
     * post请求
     * @param postParam
     * @return
     */
    private String post(ConnectVo postParam) {
        String postRes = SPOnline.post(postParam.getToken(), postParam.getDomain(), postParam.getUrl(), postParam.getData(), postParam.getFormDigestValue());
        return postRes;
    }

    /**
     * delete请求
     * @param postParam
     * @return
     */
    private String delete(ConnectVo postParam) {
        String delete = SPOnline.delete(postParam.getToken(), postParam.getDomain(), postParam.getUrl(), postParam.getFormDigestValue());
        return delete;
    }

    private String uploadFile(ConnectVo vo) {

        File file = new File(vo.getData());

        String postRes = SPOnline.uploadFile(vo.getToken(), vo.getDomain(), vo.getUrl(), file, vo.getFormDigestValue());
        return postRes;
    }

    @Override
    public String viewFileInfo(OfficeRestVo vo) {


        String url = "_api/Web/GetFileByServerRelativePath(decodedurl='/syswareLib/" + vo.getGroupId() + "/" + vo.getDocName() + "')";

        if (StringUtils.isNotBlank(vo.getUrlStuff())) {
            url += vo.getUrlStuff(); ///Properties"
        }
//     url = "/_api/Web/GetFileByServerRelativePath(decodedurl='/syswareLib/20181023/sysware1.docx')";

        ConnectVo postParam = this.getPostParam(vo);
        postParam.setUrl(url);
        String jsonString = this.post(postParam);
        if (jsonString != null) {

            System.out.println(CommonLib.prettyFormatJson(jsonString));
            return jsonString;
        }
        return null;
    }

    @Override
    public String getListItemAllFields(OfficeRestVo vo) {

        String realPath = "/syswareLib/" + vo.getGroupId();
        String url = "_api/Web/GetFolderByServerRelativeUrl('" + realPath + "')/ListItemAllFields";
        if (StringUtils.isNotBlank(vo.getDocName())) {
            realPath += "/" + vo.getDocName();
            url = "_api/Web/GetFileByServerRelativePath(decodedurl='" + realPath + "')/ListItemAllFields";

        }

        ConnectVo postParam = this.getPostParam(vo);
        postParam.setUrl(url);
        String jsonString = this.post(postParam);
        if (jsonString != null) {

            return jsonString;
        }
        return null;
    }

    @Override
    public String getListItemId(OfficeRestVo vo) {
        String listItemAllFields = this.getListItemAllFields(vo);
        String id = "";
        if (StringUtils.isNotBlank(listItemAllFields)) {
            JSONObject json = new JSONObject(listItemAllFields);
            JSONObject d = json.getJSONObject("d");

            Integer id1 = d.getInt("Id");

            id = id1.toString();
        }
        return id;
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
        String jsonString = this.post(con);
        if (jsonString != null) {
            System.out.println(CommonLib.prettyFormatJson(jsonString));

            // "UniqueId": "52027068-2d67-46fa-ad9b-16cede5f02dd", SharePoint端文件夹id
//            //    "ServerRelativeUrl": "/syswareLib/20181023/sysware0.docx", 相对路径
//            //  "Name": "sysware0.docx",
////      下载链接    "LinkingUrl": "https://237226835.sharepoint.com/syswareLib/20181023/sysware0.docx?d\u003dw520270682d6746faad9b16cede5f02dd",
////            "UIVersion": 512,
////             "UIVersionLabel": "1.0",
////            Versions  版本查看
////            "UIVersionLabel": "2.0",

        } else {
            System.out.println("null");
        }

        return null;
    }

    @Override
    public String importNewFile(OfficeRestVo vo) {
        String floder = vo.getGroupId();
        String url = "_api/web/GetFolderByServerRelativeUrl('/syswareLib/" + vo.getGroupId() + "')/Files/add(url='" + vo.getDocName() + "',overwrite=true)";
        ConnectVo con = getPostParam(vo);
        con.setUrl(url);
        con.setData(vo.getData());
        String jsonString = this.uploadFile(con);
        if (jsonString != null) {
            System.out.println(CommonLib.prettyFormatJson(jsonString));

        } else {
            System.out.println("null");
        }
        return null;
    }

    @Override
    public String importFile(OfficeRestVo vo) {
        return null;
    }

    @Override
    public String setPermissions(OfficeRestVo vo) {

        String userInfo = this.getUserInfo(vo);
        String principalId = SPConver.getUserId(userInfo);

        String listItemId = this.getListItemId(vo);

        String targetRoleDefinitionId = this.getTargetRoleDefinitionId(vo);

        String url = "_api/Web/Lists/getByTitle('syswareLib')/items("+listItemId+")";
        url += "/roleassignments/addroleassignment";
        ConnectVo postParam = getPostParam(vo);

        String permissionUrl = url +"(principalid='"+principalId+"',roledefid='"+targetRoleDefinitionId+"')";
        postParam.setUrl(permissionUrl);
        String post = this.post(postParam);
        System.out.println("post====="+post);

        return post;
    }

    @Override
    public String delPermissions(OfficeRestVo vo) {

        List<String> principalIds = getPrincipalIds(vo);
        String listItemId = this.getListItemId(vo);

        String url = "_api/Web/Lists/getByTitle('syswareLib')/items("+listItemId+")";
        url += "/roleassignments/getbyprincipalid";
        ConnectVo postParam = getPostParam(vo);

        if(principalIds != null && principalIds.size() >0){

            for (String principalId : principalIds) {

               String permissionUrl = url +"('"+principalId+"')";
                postParam.setUrl(permissionUrl);
                String delete = this.delete(postParam);
                System.out.println("delete===     "+delete);
            }
        }

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

    @Override
    public String breakPermission(OfficeRestVo vo) {
        String listItemId = this.getListItemId(vo);

        String url = "_api/Web/Lists/getByTitle('syswareLib')/items(" + listItemId + ")";
        url += "/breakroleinheritance(true)";
        ConnectVo postParam = getPostParam(vo);
        postParam.setUrl(url);
        String post = this.post(postParam);

        if (StringUtils.isNotBlank(post)) {
            System.out.println(CommonLib.prettyFormatJson(post));

        } else {
            System.out.println("null");
        }

        return post;
    }

    @Override
    public String getAllPermission(OfficeRestVo vo) {

        //      roleassignments
        String listItemId = this.getListItemId(vo);

        String url = "_api/Web/Lists/getByTitle('syswareLib')/items(" + listItemId + ")";
        url += "/roleassignments";
        ConnectVo postParam = getPostParam(vo);
        postParam.setUrl(url);
        String post = this.post(postParam);

        if (StringUtils.isNotBlank(post)) {
            System.out.println(CommonLib.prettyFormatJson(post));
        } else {
            System.out.println("null");
        }

        return post;
    }

    @Override
    public List<String> getPrincipalIds(OfficeRestVo vo) {
        String allPermission = getAllPermission(vo);
        JSONObject jsonObject = new JSONObject(allPermission);

        JSONArray jsonArray = jsonObject.getJSONObject("d").getJSONArray("results");

        List<String> ids = new ArrayList<>();
        List<Object> objects = jsonArray.toList();
        for (Object obj : objects) {
            Map<String, Object> json = (HashMap) obj;
            Object id = json.get("PrincipalId");

            ids.add(id.toString());
        }

        return ids;
    }

    @Override
    public String getUserInfo(OfficeRestVo vo) {
        //        https://237226835.sharepoint.com//_api/web/siteusers(@v)?@v=%27i%3A0%23.f%7Cmembership%7Czengq%40237226835.onmicrosoft.com%27

        String shareId = vo.getShareId();

        //SharePoint Online 获取用户链接
//        …/users(@v)?@v='i%3A05%3At%7Cadfs+with+roles%7Cuser%40domain.com'  本地SharePoint的链接
        String url  = "/_api/web/siteusers(@v)?@v=%27i%3A0%23.f%7Cmembership%7C"+shareId+"%40"+vo.getDomain()+".onmicrosoft.com%27";

        ConnectVo postParam = getPostParam(vo);

        postParam.setUrl(url);
        String post = this.post(postParam);
        if (StringUtils.isNotBlank(post)) {
            System.out.println(CommonLib.prettyFormatJson(post));
        } else {
            System.out.println("null");
        }

        return post;
    }

    @Override
    public String getTargetRoleDefinition(OfficeRestVo vo) {
        String url  = "/_api/web/roledefinitions/getbyname('"+vo.getPermission()+"')";

        ConnectVo postParam = this.getPostParam(vo);
        postParam.setUrl(url);

        String post = this.post(postParam);

        if (StringUtils.isNotBlank(post)) {
            System.out.println(CommonLib.prettyFormatJson(post));
        } else {
            System.out.println("null");
        }
        return post;
    }

    @Override
    public String getTargetRoleDefinitionId(OfficeRestVo vo) {
        String url  = "/_api/web/roledefinitions/getbyname('"+vo.getPermission()+"')/id";
        ConnectVo postParam = this.getPostParam(vo);
        postParam.setUrl(url);
        String post = this.post(postParam);
        if (StringUtils.isNotBlank(post)) {
            System.out.println(CommonLib.prettyFormatJson(post));
        } else {
            System.out.println("null");
        }
        return SPConver.getId(post);
    }
}
