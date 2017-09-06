package com.asiainfo.mybatis.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PmScreenIndexManagerPo implements Serializable {
    private BigDecimal indexCode;

    private String companyCode;

    private String indexName;

    private BigDecimal tempListCode;

    private BigDecimal listCode;

    private String screenDesp;

    private String dataDesp;

    private String getDataSql;

    private Long collectRate;

    private Date collectLastTime;

    private String collectSwitch;

    private static final long serialVersionUID = 1L;

    public BigDecimal getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(BigDecimal indexCode) {
        this.indexCode = indexCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName == null ? null : indexName.trim();
    }

    public BigDecimal getTempListCode() {
        return tempListCode;
    }

    public void setTempListCode(BigDecimal tempListCode) {
        this.tempListCode = tempListCode;
    }

    public BigDecimal getListCode() {
        return listCode;
    }

    public void setListCode(BigDecimal listCode) {
        this.listCode = listCode;
    }

    public String getScreenDesp() {
        return screenDesp;
    }

    public void setScreenDesp(String screenDesp) {
        this.screenDesp = screenDesp == null ? null : screenDesp.trim();
    }

    public String getDataDesp() {
        return dataDesp;
    }

    public void setDataDesp(String dataDesp) {
        this.dataDesp = dataDesp == null ? null : dataDesp.trim();
    }

    public String getGetDataSql() {
        return getDataSql;
    }

    public void setGetDataSql(String getDataSql) {
        this.getDataSql = getDataSql == null ? null : getDataSql.trim();
    }

    public Long getCollectRate() {
        return collectRate;
    }

    public void setCollectRate(Long collectRate) {
        this.collectRate = collectRate;
    }

    public Date getCollectLastTime() {
        return collectLastTime;
    }

    public void setCollectLastTime(Date collectLastTime) {
        this.collectLastTime = collectLastTime;
    }

    public String getCollectSwitch() {
        return collectSwitch;
    }

    public void setCollectSwitch(String collectSwitch) {
        this.collectSwitch = collectSwitch == null ? null : collectSwitch.trim();
    }

	@Override
	public String toString() {
		return "PmScreenIndexManagerPo [indexCode=" + indexCode + ", companyCode=" + companyCode + ", indexName="
				+ indexName + ", tempListCode=" + tempListCode + ", listCode=" + listCode + ", screenDesp=" + screenDesp
				+ ", dataDesp=" + dataDesp + ", getDataSql=" + getDataSql + ", collectRate=" + collectRate
				+ ", collectLastTime=" + collectLastTime + ", collectSwitch=" + collectSwitch + "]";
	}
}