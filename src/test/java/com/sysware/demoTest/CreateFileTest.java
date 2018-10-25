package com.sysware.demoTest;

import com.sysware.entity.vo.OfficeRestVo;
import com.sysware.service.office.OfficeRestService;
import com.sysware.service.office.OfficeRestServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 创建文件夹20181025
 * 在文件夹下创建新文件 sysware1024.docx
 * 停止文件权限继承
 * 删除文件权限
 * 添加仅查看权限给 zhangfei
 *
 */
public class CreateFileTest {
    OfficeRestVo vo = new OfficeRestVo();
    OfficeRestService service = new OfficeRestServiceImpl();

    String username = "zengq@237226835.onmicrosoft.com";//测试用户
    String domain ="237226835";//测试域
    String shareId = "zhangfei"; //测试要共享的用户名

    String groupId = "20181026"; //测试的团队id
    String docName = "sysware12.docx"; //测试的文档名

    @Before
    public void setUp() throws Exception {

        vo.setGroupId(groupId);
        vo.setDocName(docName);
    }

    @Test
    public void createFloder() {
        service.createFloder(vo);
    }

    @Test
    public void createFile() {
        service.addFile(vo);
    }

    @Test
    public void breakPermission() {
        String res = service.breakPermission(vo);

        System.out.println(res);
    }
    @Test
    public void breakFilePermission() {
        vo.setDocName("");
        String res = service.breakPermission(vo);
        System.out.println(res);
    }

    @Test
    public void getAllPermission() {

        String allPermission = service.getAllPermission(vo);
        System.out.println(allPermission);
    }
    @Test
    public void getPermissionIds() {

        List<String> principalIds = service.getPrincipalIds(vo);

        for (String id: principalIds         ) {
            System.out.println("ID=:"+id);
        }
    }

    @Test
    public void delPermission() {
//        https://237226835.sharepoint.com//_api/web/siteusers(@v)?@v=%27i%3A0%23.f%7Cmembership%7Czengq%40237226835.onmicrosoft.com%27
        service.delPermissions(vo);
    }

    @Test
    public void getUserInfo() {
//        https://237226835.sharepoint.com//_api/web/siteusers(@v)?@v=%27i%3A0%23.f%7Cmembership%7Czengq%40237226835.onmicrosoft.com%27
        vo.setSimpleName(shareId);
        vo.setDomain(domain);
        String userInfo = service.getUserInfo(vo);
        System.out.println(userInfo);

    }

    @Test
    public void getRoledef() {
//        vo.setPermission("Contribute");
        vo.setPermission("完全控制");
        String targetRoleDefinition = service.getTargetRoleDefinition(vo);

    }

    @Test
    public void addViewPermisstion() {
//        https://237226835.sharepoint.com//_api/web/siteusers(@v)?@v=%27i%3A0%23.f%7Cmembership%7Czengq%40237226835.onmicrosoft.com%27
        vo.setShareId(shareId);
        vo.setDomain(domain);
        vo.setPermission("读取");
        service.setPermissions(vo);

    }
}
