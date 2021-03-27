package com.higradius.main.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import com.higradius.main.DAO.TemplateDTO;
import com.higradius.main.payload.CorrespondenceTemplateResponse;
import com.higradius.main.service.InvoiceService;
import com.higradius.main.service.TemplateService;

public class TemplateServiceImpl_2 implements TemplateService{

		private final InvoiceService invoiceService;
		
		public TemplateServiceImpl_2()
		{
			invoiceService = new InvoiceServiceImpl();
		}
		
		@Override
		public Optional<CorrespondenceTemplateResponse> getTemplate(String docIds) {
			 
			Optional<List<TemplateDTO>> templateDAOOptional = invoiceService.getInvoiceRecordsByDocIds(docIds);
			List<TemplateDTO> templateDAOs = null;
			
			if(templateDAOOptional.isPresent())
				templateDAOs = templateDAOOptional.get();
			 
			
			CorrespondenceTemplateResponse ctr = new CorrespondenceTemplateResponse();
			
			String header = "<div> Subject <span style = \"color:white\">Invoice Details - Account Name </span>\r\n" + 
					"<p>Dear Sir/Madam,</p>\r\n" + 
					"<p>Greetings!</p>\r\n" + 
					"<p> Gentle reminder that you have one or more open invoices on your account.</p>\r\n"
					+"<p>Please get back to us with an expected date of payment.</p>\r\n" + 
					"<p>Please find the details of the invoic below</p>\r\n" + 
					"</div>";
			
			String footer = "<div>\r\n" + 
					"    <p> In case you have already made a payment for the above items, please send us the details to ensure the payment is posted.</p>\r\n" + 
					"    <p>Let us know if we can be of any further assistance. Looking forward to hearing from you</p>\r\n" + 
					"    \r\n" + 
					"    <p>Kind Regards,</p>\r\n" + 
					"    <p>Aditya Shukla</p>\r\n" + 
					"</div>\r\n" + 
					"";
		
			ctr.setHeader(header);
			ctr.setFooter(footer);
			ctr.setTemplateDAOs(templateDAOs);
			
			return Optional.of(ctr);
		}	

	}


