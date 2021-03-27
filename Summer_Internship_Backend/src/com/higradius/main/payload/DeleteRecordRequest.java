package com.higradius.main.payload;

import java.util.List;

public class DeleteRecordRequest {
	
	String docIds;	
	
	DeleteRecordRequest()
	{
		
	}

	public String getDocIds() {
		return docIds;
	}

	/**
	 * @param docIds the docIds to set
	 */
	public void setDocIds(String docIds) {
		this.docIds = docIds;
	}
}
