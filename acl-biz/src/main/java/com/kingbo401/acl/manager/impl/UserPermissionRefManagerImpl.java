package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.model.dto.PermissionDTO;
import com.kingbo401.acl.common.model.dto.UserPermissionRefDTO;
import com.kingbo401.acl.common.model.dto.param.UserPermissionRefParam;
import com.kingbo401.acl.dao.UserPermissionRefDAO;
import com.kingbo401.acl.manager.PermissionManager;
import com.kingbo401.acl.manager.UserPermissionRefManager;
import com.kingbo401.acl.model.entity.UserPermissionRefDO;
import com.kingbo401.acl.model.entity.param.UserPermissionRefQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;

@Service
public class UserPermissionRefManagerImpl implements UserPermissionRefManager{
	@Autowired
	private UserPermissionRefDAO userPermissionRefDAO;
	@Autowired
	private PermissionManager permissionManager;
	
	@Override
	public boolean addRef(UserPermissionRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> permissionIds = param.getPermissionIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		BizUtil.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		assertPermissions(appKey, param.getSubgroup(), permissionIds);
		List<UserPermissionRefDO> userPermissionRefDOs = new ArrayList<UserPermissionRefDO>();
		for(Long permissionId : permissionIds){
			UserPermissionRefDO userPermissionRefDO = new UserPermissionRefDO();
			userPermissionRefDO.setUserId(userId);
			userPermissionRefDO.setTenant(tenant);
			userPermissionRefDO.setPermissionId(permissionId);
			userPermissionRefDO.setStatus(AclConstant.STATUS_NORMAL);
			userPermissionRefDO.setEffectiveTime(effectiveTime);
			userPermissionRefDO.setExpireTime(expireTime);
			userPermissionRefDOs.add(userPermissionRefDO);
		}
		userPermissionRefDAO.batchCreate(userPermissionRefDOs);
		return true;
	}

	@Override
	public boolean updateRef(UserPermissionRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> permissionIds = param.getPermissionIds();
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		BizUtil.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		
		UserPermissionRefQueryParam userPermissionRefQueryParam = new UserPermissionRefQueryParam();
		userPermissionRefQueryParam.setAppKey(appKey);
		userPermissionRefQueryParam.setUserId(userId);
		userPermissionRefQueryParam.setTenant(tenant);
		userPermissionRefQueryParam.setReturnNotEffective(true);
		List<UserPermissionRefDTO> userPermissionRefVOs = userPermissionRefDAO.listUserPermissionRef(userPermissionRefQueryParam);
		if(CollectionUtil.isNotEmpty(userPermissionRefVOs)){
			List<Long> permissionIdsRemove = userPermissionRefVOs.stream().map(UserPermissionRefDTO::getPermissionId).collect(Collectors.toList());
			userPermissionRefDAO.updateRefsStatus(userId, tenant, permissionIdsRemove, AclConstant.STATUS_REMOVE);
		}
		assertPermissions(appKey, param.getSubgroup(), permissionIds);
		List<UserPermissionRefDO> userPermissionRefDOs = new ArrayList<UserPermissionRefDO>();
		for(Long permissionId : permissionIds){
			UserPermissionRefDO userPermissionRefDO = new UserPermissionRefDO();
			userPermissionRefDO.setUserId(userId);
			userPermissionRefDO.setTenant(tenant);
			userPermissionRefDO.setPermissionId(permissionId);
			userPermissionRefDO.setStatus(AclConstant.STATUS_NORMAL);
			userPermissionRefDO.setEffectiveTime(effectiveTime);
			userPermissionRefDO.setExpireTime(expireTime);
			userPermissionRefDOs.add(userPermissionRefDO);
		}
		userPermissionRefDAO.batchCreate(userPermissionRefDOs);
		return true;
	}
	
	private boolean updateRefStatus(UserPermissionRefParam param, int status) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> permissionIds = param.getPermissionIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		BizUtil.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		if (CollectionUtil.isEmpty(permissionIds)) {
			UserPermissionRefQueryParam userPermissionRefQueryParam = new UserPermissionRefQueryParam();
			userPermissionRefQueryParam.setAppKey(appKey);
			userPermissionRefQueryParam.setUserId(userId);
			userPermissionRefQueryParam.setTenant(tenant);
			userPermissionRefQueryParam.setReturnNotEffective(true);
			List<UserPermissionRefDTO> userPermissionRefVOs = userPermissionRefDAO.listUserPermissionRef(userPermissionRefQueryParam);
			if(CollectionUtil.isEmpty(userPermissionRefVOs)){
				return true;
			}
			permissionIds = userPermissionRefVOs.stream().map(UserPermissionRefDTO::getPermissionId).collect(Collectors.toList());
		}
		
		userPermissionRefDAO.updateRefsStatus(userId, tenant, permissionIds, status);
		return true;
	}
	
	@Override
	public boolean removeRef(UserPermissionRefParam param) {
		return this.updateRefStatus(param, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeRef(UserPermissionRefParam param) {
		return this.updateRefStatus(param, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeRef(UserPermissionRefParam param) {
		return this.updateRefStatus(param, AclConstant.STATUS_NORMAL);
	}

	@Override
	public List<UserPermissionRefDTO> listUserPermissionRef(UserPermissionRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		Assert.notNull(param.getUserId(), "用户id不能为空");
		Assert.hasText(param.getTenant(), "tenant不能为空");
		return userPermissionRefDAO.listUserPermissionRef(param);
	}

	@Override
	public PageVO<UserPermissionRefDTO> pageUserPermissionRef(UserPermissionRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		Assert.notNull(param.getUserId(), "用户id不能为空");
		Assert.hasText(param.getTenant(), "tenant不能为空");
		PageVO<UserPermissionRefDTO> pageVO = new PageVO<UserPermissionRefDTO>(param);
		if(param.isReturnTotalCount()){
			long total = userPermissionRefDAO.countUserPermissionRef(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		pageVO.setItems(userPermissionRefDAO.pageUserPermissionRef(param));
		return pageVO;
	}

	private void assertPermissions(String appKey, String subgroup, List<Long> permissionIds){
		if(CollectionUtil.isEmpty(permissionIds)){
			return;
		}
		List<PermissionDTO> permissionDTOs = permissionManager.getByIds(appKey, permissionIds);
		Assert.notEmpty(permissionDTOs, "权限不存在");
		Map<Long, PermissionDTO> idMap = permissionDTOs.stream().collect(Collectors.toMap(PermissionDTO::getId, a -> a, (k1, k2) -> k1));
		for(Long permissionId : permissionIds){
			PermissionDTO permissionDTO = idMap.get(permissionId);
			Assert.notNull(permissionDTO, "权限不存在");
			if(StringUtils.isNotBlank(subgroup)){
				Assert.isTrue(subgroup.equals(permissionDTO.getSubgroup()), "权限类型不匹配");
			}
		}
	}

	@Override
	public boolean hasUserUse(long permissionId) {
		return userPermissionRefDAO.hasUserUse(permissionId) != null;
	}
}
