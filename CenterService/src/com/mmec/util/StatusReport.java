/**
 * StatusReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mmec.util;

public class StatusReport implements java.io.Serializable {
    private java.lang.String errorCode;

    private java.lang.String memo;

    private java.lang.String mobile;

    private java.lang.String receiveDate;

    private int reportStatus;

    private long seqID;

    private java.lang.String serviceCodeAdd;

    private java.lang.String submitDate;

    public StatusReport() {
    }

    public StatusReport(java.lang.String errorCode, java.lang.String memo, java.lang.String mobile, java.lang.String receiveDate, int reportStatus, long seqID, java.lang.String serviceCodeAdd,
	    java.lang.String submitDate) {
	this.errorCode = errorCode;
	this.memo = memo;
	this.mobile = mobile;
	this.receiveDate = receiveDate;
	this.reportStatus = reportStatus;
	this.seqID = seqID;
	this.serviceCodeAdd = serviceCodeAdd;
	this.submitDate = submitDate;
    }

    /**
     * Gets the errorCode value for this StatusReport.
     * 
     * @return errorCode
     */
    public java.lang.String getErrorCode() {
	return errorCode;
    }

    /**
     * Sets the errorCode value for this StatusReport.
     * 
     * @param errorCode
     */
    public void setErrorCode(java.lang.String errorCode) {
	this.errorCode = errorCode;
    }

    /**
     * Gets the memo value for this StatusReport.
     * 
     * @return memo
     */
    public java.lang.String getMemo() {
	return memo;
    }

    /**
     * Sets the memo value for this StatusReport.
     * 
     * @param memo
     */
    public void setMemo(java.lang.String memo) {
	this.memo = memo;
    }

    /**
     * Gets the mobile value for this StatusReport.
     * 
     * @return mobile
     */
    public java.lang.String getMobile() {
	return mobile;
    }

    /**
     * Sets the mobile value for this StatusReport.
     * 
     * @param mobile
     */
    public void setMobile(java.lang.String mobile) {
	this.mobile = mobile;
    }

    /**
     * Gets the receiveDate value for this StatusReport.
     * 
     * @return receiveDate
     */
    public java.lang.String getReceiveDate() {
	return receiveDate;
    }

    /**
     * Sets the receiveDate value for this StatusReport.
     * 
     * @param receiveDate
     */
    public void setReceiveDate(java.lang.String receiveDate) {
	this.receiveDate = receiveDate;
    }

    /**
     * Gets the reportStatus value for this StatusReport.
     * 
     * @return reportStatus
     */
    public int getReportStatus() {
	return reportStatus;
    }

    /**
     * Sets the reportStatus value for this StatusReport.
     * 
     * @param reportStatus
     */
    public void setReportStatus(int reportStatus) {
	this.reportStatus = reportStatus;
    }

    /**
     * Gets the seqID value for this StatusReport.
     * 
     * @return seqID
     */
    public long getSeqID() {
	return seqID;
    }

    /**
     * Sets the seqID value for this StatusReport.
     * 
     * @param seqID
     */
    public void setSeqID(long seqID) {
	this.seqID = seqID;
    }

    /**
     * Gets the serviceCodeAdd value for this StatusReport.
     * 
     * @return serviceCodeAdd
     */
    public java.lang.String getServiceCodeAdd() {
	return serviceCodeAdd;
    }

    /**
     * Sets the serviceCodeAdd value for this StatusReport.
     * 
     * @param serviceCodeAdd
     */
    public void setServiceCodeAdd(java.lang.String serviceCodeAdd) {
	this.serviceCodeAdd = serviceCodeAdd;
    }

    /**
     * Gets the submitDate value for this StatusReport.
     * 
     * @return submitDate
     */
    public java.lang.String getSubmitDate() {
	return submitDate;
    }

    /**
     * Sets the submitDate value for this StatusReport.
     * 
     * @param submitDate
     */
    public void setSubmitDate(java.lang.String submitDate) {
	this.submitDate = submitDate;
    }
}
