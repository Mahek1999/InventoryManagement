package com.higradius.main.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.higradius.main.DAO.InvoiceDetailDTO;
import com.higradius.main.DAO.TemplateDTO;
import com.higradius.main.model.InvoiceDetail;
import com.higradius.main.payload.CorrespondenceTemplateResponse;
import com.higradius.main.payload.EditRecordRequest;

public interface InvoiceService {
	// To insert the rows of a file into the database.
	void insertAllInvoiceRecords(File file);

	Optional<List<InvoiceDetailDTO>> getAllRecords();

	void deleteRecord(String docId[]);

	void insertInvoiceRecord(InvoiceDetailDTO invoiceDetail);

	Optional<List<InvoiceDetailDTO>> getRecordByPages(int pageCount, int offset);

	void updateInvoiceRecord(EditRecordRequest invoiceDetail);
	
	Optional<List<TemplateDTO>> getInvoiceRecordsByDocIds(String str);
	
	Optional<List<InvoiceDetailDTO>> searchByInvoiceId(String str);
	
}
