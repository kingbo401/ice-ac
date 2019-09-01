package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.DataPropertyDTO;
import com.kingbo401.acl.model.entity.param.DataPropertyQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface DataPropertyManager {
	DataPropertyDTO createDataProperty(DataPropertyDTO dataPropertyDTO);
	
	DataPropertyDTO updateDataProperty(DataPropertyDTO dataPropertyDTO);
	
	boolean removeDataProperty(DataPropertyDTO dataPropertyDTO);
	
	boolean freezeDataProperty(DataPropertyDTO dataPropertyDTO);
	
	boolean unfreezeDataProperty(DataPropertyDTO dataPropertyDTO);

	List<DataPropertyDTO> getDataProperties(List<Long> ids);

	List<DataPropertyDTO> getDataProperties(String appKey, List<String> propertyCodes);
	
	DataPropertyDTO getDataProperty(String appKey, String propertyCode);
	
	List<DataPropertyDTO> listDataProperty(DataPropertyQueryParam param);
	
	PageVO<DataPropertyDTO> pageDataProperty(DataPropertyQueryParam param);
}
