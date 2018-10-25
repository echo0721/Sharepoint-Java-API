package com.sysware.service.office;


import com.sysware.entity.vo.OfficeRestVo;

import java.util.List;

public interface OfficeRestService {

    /**
     * 查看文件信息
     * @param vo
     * @return
     */
    String viewFileInfo(OfficeRestVo vo);

    /**
     * 查询文件或文件夹在 lists 中的item属性
     * @param vo
     * @return
     */
    String getListItemAllFields(OfficeRestVo vo);
    /**
     * 查询文件或文件夹在 lists 中的itemId
     * @param vo
     * @return
     */
    String getListItemId(OfficeRestVo vo);


    /**
     * 创建文件夹
     * @param vo
     * @return
     */
    String createFloder(OfficeRestVo vo);

    /**
     * 新建文档
     * 删除权限集成，只保留创建人所有权
     * @return
     */
    String addFile(OfficeRestVo vo);

    /**
     *  导入新文档
     *  删除权限继承，只保留导入人所有权
     * @return
     */
    String importNewFile(OfficeRestVo vo);

    /**
     * 导入文档，覆盖当前存在文档
     * 生成一个新版本
     * @return
     */
    String importFile(OfficeRestVo vo);

    /**
     * 设置文档权限
     * 包括 查看，编辑，完全控制
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

    /**
     * 停止权限继承
     * @param vo
     * @return
     */
    String breakPermission(OfficeRestVo vo);

    /**
     * 查询文件所有权限
     * @param vo
     * @return
     */
    String getAllPermission(OfficeRestVo vo);

    /**
     * 获取文件上的权限id
     * @param vo
     * @return
     */
    List<String> getPrincipalIds(OfficeRestVo vo);

    /**
     * 获取SharePoint 用户的信息。
     * 主要用来获取ID,设置权限
     * @param vo
     * @return
     */
    String getUserInfo(OfficeRestVo vo);

    /**
     * 获取角色定义
     * @param vo
     * @return
     */
    String getTargetRoleDefinition(OfficeRestVo vo );
    /**
     * 获取角色定义id
     * @param vo
     * @return
     */
    String getTargetRoleDefinitionId(OfficeRestVo vo );

    String getRoleDefinitions(OfficeRestVo vo );

}
