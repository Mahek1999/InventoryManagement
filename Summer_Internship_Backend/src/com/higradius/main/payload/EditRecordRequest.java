package com.higradius.main.payload;

public class EditRecordRequest {

	private Long docId;
	private Double totalOpenAmount;
	private String notes;
	
	public EditRecordRequest()
	{
		
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
	
	
}
