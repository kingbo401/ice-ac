package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.PageParam;

public class UserPermissionRefQueryParam extends PageParam{
	private String appKey;
    private String tenant;
	private String userId;
	private Integer status;
    private Long permissionId;
    private String permissionKey;
    private String subgroup;
    
    /**
             * 是否返回没有生效的授权
     */
    private boolean returnNotEffective = true;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionKey() {
		return permissionKey;
	}

	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
	}

	public String getSubgroup() {
		return subgroup;
	}

	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}

	public boolean isReturnNotEffective() {
		return returnNotEffective;
	}

	public void setReturnNotEffective(boolean returnNotEffective) {
		this.returnNotEffective = returnNotEffective;
	}
}
