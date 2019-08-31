package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.RoleDO;
import com.kingbo401.acl.model.entity.param.RoleQueryParam;

public interface RoleDAO {
	RoleDO getRoleByKey(@Param("appKey")String appKey, @Param("roleKey")String roleKey);
	RoleDO getRoleById(@Param("id")long id);
	int createRole(RoleDO role);
	int updateRole(RoleDO role);
	List<RoleDO> listRole(RoleQueryParam param);
	List<RoleDO> pageRole(RoleQueryParam param);
	long countRole(RoleQueryParam param);
	List<RoleDO> getRoleByIds(@Param("ids") List<Long> ids);
	List<RoleDO> getRoleByKeys(@Param("appKey")String appKey, @Param("roleKeys")List<String> roleKey);
}