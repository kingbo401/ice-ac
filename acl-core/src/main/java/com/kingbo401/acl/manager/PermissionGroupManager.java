package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.entity.PermissionGroupLite;
import com.kingbo401.acl.model.entity.param.PermissionGroupQueryParam;
import com.kingbo401.commons.model.PageVO;

import kingbo401.iceacl.common.model.PermissionGroupTreeNode;

public interface PermissionGroupManager {

    PermissionGroupDTO createPermissionGroup(PermissionGroupDTO permissionGroupDTO);

    boolean updatePermissionGroup(PermissionGroupDTO permissionGroupDTO);

    boolean removePermissionGroup(String appKey, long id);
    
    boolean freezePermissionGroup(String appKey, long id);
    
    boolean unfreezePermissionGroup(String appKey, long id);

    List<PermissionGroupTreeNode> getPermissionGroupTree(PermissionGroupQueryParam permissionGroupQueryParam);

    List<PermissionGroupDTO> listPermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam);

    PageVO<PermissionGroupDTO> pagePermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam);

    List<PermissionGroupDTO> getPermissionGroupByIds(String appKey, List<Long> groupIds);
    
    List<PermissionGroupDTO> getPermissionGroupByIds(List<Long> groupIds);
    
    PermissionGroupDTO getPermissionGroupById(long id);
    
	List<PermissionGroupLite> listPermissionGroupLite(String appKey, String tenant);
}