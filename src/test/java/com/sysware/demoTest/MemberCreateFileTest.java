package com.sysware.demoTest;

import com.sysware.conver.SPConver;
import com.sysware.entity.vo.OfficeRestVo;
import com.sysware.service.office.OfficeRestService;
import com.sysware.service.office.OfficeRestServiceImpl;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

/**
 * 普通用户zhangfei
 * 在文件夹20181026下创建新文件 sysware1025.docx
 * 停止文件权限继承
 * 删除文件权限
 * 添加仅查看权限给 icecream
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemberCreateFileTest {
    OfficeRestVo vo = new OfficeRestVo();
    OfficeRestService service = new OfficeRestServiceImpl();

    String domain ="237226835";//测试域
    String shareId = "icecream"; //测试要共享的用户名
    String username = "zhangfei@237226835.onmicrosoft.com"; //登陆SharePoint用户名
    String groupId = "20181026"; //测试的团队id
    String docName = "sysware1025E.docx"; //测试的文档名
    String permission = "读取";  //这里的权限 ，跟用户设置的语言有关。

    String siteOwner ="zengq@237226835.onmicrosoft.com";
    String site = "https://237226835.sharepoint.com";
    @Before
    public void setUp() throws Exception {

        vo.setUsername(username);
        vo.setGroupId(groupId);
        vo.setDocName(docName);
        vo.setDomain(domain);
    }

    @Test
    public void test000createFloder() {
        vo.setSiteOwner(siteOwner);
        service.createFloder(vo);
    }
    @Test
    public void test001createFile() {
        String fileInfo = service.addFile(vo);
        JSONObject jsonObject = SPConver.getJSONObject(fileInfo);

        String uniqueId = jsonObject.getString("UniqueId");
        String url = site+"/:w:/r/_layouts/15/Doc.aspx?sourcedoc=%7B"+uniqueId+"%7D&file="+docName+"&action=default&mobileredirect=true";
        System.out.println("查看编辑链接"+url);

    }
    @Test
    public void test002breakFilePermission() {
        vo.setSiteOwner(siteOwner);
        String res = service.breakPermission(vo);
        System.out.println(res);
    }
    /**
     * 按目前团队管理逻辑，创建这可能没有完全控制权限。
     * 给创建者完全权限
     */
    @Test
    public void test003Permisstion(){
        vo.setShareId("zhangfei");
        vo.setDomain(domain);
        vo.setPermission("完全控制");
        vo.setSiteOwner(siteOwner);
        service.setPermissions(vo);
    }



    @Test
    public void test004delPermission() {
        service.delPermissions(vo);
    }

    @Test
    public void test005addViewPermisstion() {
        vo.setShareId(shareId);
        vo.setDomain(domain);
        vo.setPermission(permission);
        /**
         * TODO
         * 1.检查用户语言设置。 如果用户语言设置不为中文，permission会调用失败。
         * 2.可以预先加载所有权限设置到本系统中。（系统配置完毕，权限组角色为固定id）
         * 可行的方案为第二种， 直接存储到本地数据库中，权限id保持一致
         */
        service.setPermissions(vo);

    }
}
