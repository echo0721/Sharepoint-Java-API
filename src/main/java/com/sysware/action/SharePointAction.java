package com.sysware.action;

import com.sysware.service.office.OfficeRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weboffice")
public class SharePointAction {

    @Autowired
    OfficeRestService restService ;

    @RequestMapping("/addFile")
    public String addFile(String jsonString){

          return null;
    }
    @RequestMapping("/importNewFile")
    public String importNewFile(String jsonString){

          return null;
    }
    @RequestMapping("/importFile")
    public String importFile(String importFile){

          return null;
    }
    @RequestMapping("/setPermissions")
    public String setPermissions(String jsonString){

          return null;
    }
    @RequestMapping("/delPermissions")
    public String delPermissions(String jsonString){

          return null;
    }
    @RequestMapping("/getFileUpdateTime")
    public String getFileUpdateTime(String jsonString){

          return null;
    }
    @RequestMapping("/renameFile")
    public String renameFile(String jsonString){

          return null;
    }
    @RequestMapping("/queryHistory")
    public String queryHistory(String jsonString){

          return null;
    }

    @RequestMapping("/getDownloadUrl")
    public String getDownloadUrl(String jsonString){

          return null;
    }

}
