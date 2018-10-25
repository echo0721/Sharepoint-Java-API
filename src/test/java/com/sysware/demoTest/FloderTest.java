package com.sysware.demoTest;

import com.sysware.entity.vo.OfficeRestVo;
import com.sysware.service.office.OfficeRestService;
import com.sysware.service.office.OfficeRestServiceImpl;


import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;


public class FloderTest {
    OfficeRestVo vo = new OfficeRestVo();
    OfficeRestService service = new OfficeRestServiceImpl();
    @Before
    public void setUp() throws Exception {

        vo.setGroupId("20181023");
    }

    @Test
    public void viewFile() {

        vo.setDocName("sysware1.docx");
        vo.setUrlStuff("/ListItemAllFields/ID");
        String fileInfo = service.viewFileInfo(vo);
        System.out.println(fileInfo);
        JSONObject json = JSONObject.fromObject(fileInfo);
        String id = json.getJSONObject("d").getString("Id");
        System.out.println(id);

    }

    @Test
    public void testJson() {
        String fileInfo = "{\"d\":{\"Id\":27}}";
        JSONObject json = JSONObject.fromObject(fileInfo);
        String string = json.getJSONObject("d").getString("Id");
        System.out.println(string);
    }

    @Test
    public void createFloder() {
        OfficeRestService service = new OfficeRestServiceImpl();

        vo.setGroupId("20181024");
        service.createFloder(vo);
    }

    @Test
    public void createFile() {

        Double random = Math.random()*50;
        vo.setDocName("sysware"+ random.intValue()+".docx");

        service.addFile(vo);
    }
    @Test
    public void uploadFile() {
        OfficeRestService service = new OfficeRestServiceImpl();
        String filepath = "F:/sharePointTestFile/test001.docx";
//        String filepath = "F:/sharePointTestFile/test002.doc";
        vo.setData(filepath);

        Double random = Math.random()*50;
        vo.setDocName("sysware"+ random.intValue()+".docx");
        service.addFile(vo);

    }
    @Test
    public void importFile() {
        OfficeRestService service = new OfficeRestServiceImpl();
        String filepath = "F:/sharePointTestFile/test001.docx";
        vo.setData(filepath);
        vo.setDocName("sysware1.docx");
        service.addFile(vo);

    }
    @Test
    public void delPermission() {

        vo.setDocName("sysware1.docx");
        service.delPermissions(vo);
    }
}
