package com.soflyit.chattask.im.channel.domain.param;

import java.util.HashMap;


public class CardPublicData extends HashMap<String, Object> {

    private Long businessId;

    private Integer businessType;


    private Boolean deleteFlag;

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
        this.put("businessId", businessId);
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
        this.put("businessType", businessType);
    }

    public Long getBusinessId() {
        if (businessId == null) {
            businessId = (Long) get("businessId");
        }
        return businessId;
    }

    public Integer getBusinessType() {
        if (businessType == null) {
            businessType = (Integer) get("businessType");
        }
        return businessType;
    }

    public Boolean getDeleteFlag() {
        if (businessId == null) {
            businessId = (Long) get("deleteFlag");
        }
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        if (deleteFlag == null) {
            deleteFlag = (Boolean) get("deleteFlag");
        }
        this.deleteFlag = deleteFlag;
    }
}
