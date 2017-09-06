package com.asiainfo.mybatis.po;

import java.io.Serializable;
import java.math.BigDecimal;

public class PmScreenIndexListPo implements Serializable {
    private BigDecimal listCode;

    private BigDecimal objectId;

    private static final long serialVersionUID = 1L;

    public BigDecimal getListCode() {
        return listCode;
    }

    public void setListCode(BigDecimal listCode) {
        this.listCode = listCode;
    }

    public BigDecimal getObjectId() {
        return objectId;
    }

    public void setObjectId(BigDecimal objectId) {
        this.objectId = objectId;
    }
}