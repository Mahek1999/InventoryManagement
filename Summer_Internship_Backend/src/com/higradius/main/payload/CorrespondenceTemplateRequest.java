package com.higradius.main.payload;

public class CorrespondenceTemplateRequest {
  int id;
  String docIds;
  public  CorrespondenceTemplateRequest()
  {
	  
  }
/**
 * @return the id
 */
public int getId() {
	return id;
}
/**
 * @param id the id to set
 */
public void setId(int id) {
	this.id = id;
}
/**
 * @return the docIds
 */
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
