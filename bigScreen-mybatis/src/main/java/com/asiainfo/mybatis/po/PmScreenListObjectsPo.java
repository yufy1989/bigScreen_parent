package com.asiainfo.mybatis.po;

import java.io.Serializable;
import java.math.BigDecimal;

public class PmScreenListObjectsPo implements Serializable {
	
    private BigDecimal objectId;

    private String objectKey;

    private String objectValue;

    private static final long serialVersionUID = 1L;

    public BigDecimal getObjectId() {
        return objectId;
    }

    public void setObjectId(BigDecimal objectId) {
        this.objectId = objectId;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey == null ? null : objectKey.trim();
    }

    public String getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(String objectValue) {
        this.objectValue = objectValue == null ? null : objectValue.trim();
    }
}