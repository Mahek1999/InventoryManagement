package com.higradius.main.DAO;

import java.sql.Date;


public class InvoiceDetailDTO {
	
	private String custNumber;
	private String nameCustomer;
	private Date dueInDate;
	private Double totalOpenAmount;
	private Long invoiceId;
	private String notes;
	private Long docId;

	public InvoiceDetailDTO() {
		
	}
	public String getCustNumber() {
		return custNumber;
	}
	/**
	 * @param custNumber the custNumber to set
	 */
	public void setCustNumber(String custNumber) {
		this.custNumber = custNumber;
	}
	/**
	 * @return the nameCustomer
	 */
	public String getNameCustomer() {
		return nameCustomer;
	}
	/**
	 * @param nameCustomer the nameCustomer to set
	 */
	public void setNameCustomer(String nameCustomer) {
		this.nameCustomer = nameCustomer;
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
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public String toString() {
		return "InvoiceDetailDAO [custNumber=" + custNumber + ", nameCustomer=" + nameCustomer + ", dueInDate="
				+ dueInDate + ", totalOpenAmount=" + totalOpenAmount + ", invoiceId=" + invoiceId + ", notes=" + notes
				+ "]";
	}
	/**
	 * @return the docId
	 */
	public Long getDocId() {
		return docId;
	}
	/**
	 * @param docId the docId to set
	 */
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	
}
