package com.sysware.demoTest;

import com.sysware.entity.vo.OfficeRestVo;
import com.sysware.service.office.OfficeRestService;
import com.sysware.service.office.OfficeRestServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FloderTest {
    OfficeRestVo vo = new OfficeRestVo();
    @Before
    public void setUp() throws Exception {

        vo.setGroupId("20181023");
    }

    @Test
    public void createFloder() {
        OfficeRestService service = new OfficeRestServiceImpl();

        service.createFloder(vo);
    }

    @Test
    public void createFile() {

        Double random = Math.random()*50;
        vo.setDocName("sysware"+ random.intValue()+".docx");
    }
    @Test
    public void uploadFile() {
        OfficeRestService service = new OfficeRestServiceImpl();
//        String filepath = "F:/sharePointTestFile/test001.docx";
        String filepath = "F:/sharePointTestFile/test002.doc";
        vo.setData(filepath);

        Double random = Math.random()*50;
        vo.setDocName("sysware"+ random.intValue()+".doc");
        service.addFile(vo);

    }
}
