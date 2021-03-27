package com.higradius.main.DAO;

import java.sql.Date;

public class TemplateDTO {

	 private Long invoiceId;
	 private Long poNumber;
	 private Date dueInDate;
	 private Date invoiceDate;
	 private String currency;
	 private Double totalOpenAmount;
	 
	 public TemplateDTO()
	 {
		 
	 }

	/**
	 * @return the invoiceId
	 */
	public Long getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param invoiceId the invoiceId to set
	 */
	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * @return the poNumber
	 */
	public Long getPoNumber() {
		return poNumber;
	}

	/**
	 * @param poNumber the poNumber to set
	 */
	public void setPoNumber(Long poNumber) {
		this.poNumber = poNumber;
	}



	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the totalOpenAmount
	 */
	public Double getTotalOpenAmount() {
		return totalOpenAmount;
	}

	/**
	 * @param totalOpenAmount the totalOpenAmount to set
	 */
	public void setTotalOpenAmount(Double totalOpenAmount) {
		this.totalOpenAmount = totalOpenAmount;
	}

	/**
	 * @return the dueInDate
	 */
	public Date getDueInDate() {
		return dueInDate;
	}

	/**
	 * @param dueInDate the dueInDate to set
	 */
	public void setDueInDate(Date dueInDate) {
		this.dueInDate = dueInDate;
	}

	/**
	 * @return the invoiceDate
	 */
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	 
	 
}
