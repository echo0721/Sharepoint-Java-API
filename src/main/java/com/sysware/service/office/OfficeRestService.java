package com.sysware.service.office;


import com.sysware.entity.vo.OfficeRestVo;

public interface OfficeRestService {

    /**
     * 创建文件夹
     * @param vo
     * @return
     */
    String createFloder(OfficeRestVo vo);

    /**
     * 新建文档
     * @return
     */
    String addFile(OfficeRestVo vo);

    /**
     *  导入新文档
     * @return
     */
    String importNewFile(OfficeRestVo vo);

    /**
     * 导入文档，覆盖当前存在文档
     * @return
     */
    String importFile(OfficeRestVo vo);

    /**
     * 设置文档权限
     * @return
     */
    String setPermissions(OfficeRestVo vo);

    /**
     * 删除文档权限
     * @return
     */
    String delPermissions(OfficeRestVo vo);


    /**
     * 查询更新时间
     * @return
     */
    String getFileUpdateTime(OfficeRestVo vo);

    /**
     * 重命名文件
     * @return
     */
    String renameFile(OfficeRestVo vo);


    /**
     * 查询历史版本
     * @return
     */
    String queryHistory(OfficeRestVo vo );

    /**
     * 下载文件
     * @return
     */
    String getDownloadUrl(OfficeRestVo vo);

}
