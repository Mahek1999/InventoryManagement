package com.higradius.main.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import com.higradius.main.DAO.TemplateDTO;
import com.higradius.main.payload.CorrespondenceTemplateResponse;
import com.higradius.main.service.InvoiceService;
import com.higradius.main.service.TemplateService;

public class TemplateServiceImpl_1 implements TemplateService {

	private final InvoiceService invoiceService;
	
	public TemplateServiceImpl_1()
	{
		invoiceService = new InvoiceServiceImpl();
	}
	
	@Override
	public Optional<CorrespondenceTemplateResponse> getTemplate(String docIds) {
		 
		Optional<List<TemplateDTO>> templateDAOOptional = invoiceService.getInvoiceRecordsByDocIds(docIds);
		List<TemplateDTO> templateDAOs = null;
		
		if(templateDAOOptional.isPresent())
			templateDAOs = templateDAOOptional.get();
		 
		Double sumTotalOpenAmount = 0.0;
		
		for(TemplateDTO element : templateDAOs)
		{
			sumTotalOpenAmount+=element.getTotalOpenAmount();
		}
		
		CorrespondenceTemplateResponse ctr = new CorrespondenceTemplateResponse();
		
		String header = "<div> Subject <span style = \"color:white\">Invoice Details - Account Name </span>\r\n" + 
				"<p>Dear Sir/Madam,</p>\r\n" + 
				"<p>Greetings!</p>\r\n" + 
				"<p>This is to remind you that there are one or more invoices open in your account.Please provide at your earliest convenience an update on the payment details or clarify the reason for the delay. If you have specific issues with your invoice(s), please let us know so that we can address it to the correct department.</p>\r\n" + 
				"<p>Please find the details of the invoic below</p>\r\n" + 
				"</div>";
		
		String footer = "<div>\r\n" + 
				"    <p> Total amount to be paid <span style = \"color:white\" >$(?)</span></p>\r\n" + 
				"    <p> In case you have already made a payment for the above items, please send us the details to ensure the payment is posted.</p>\r\n" + 
				"    <p>Let us know if we can be of any further assistance. Looking forward to hearing from you</p>\r\n" + 
				"    \r\n" + 
				"    <p>Kind Regards,</p>\r\n" + 
				"    <p>Aditya Shukla</p>\r\n" + 
				"</div>\r\n" + 
				"";
		
		footer = footer.replace("(?)",formatter(sumTotalOpenAmount));
	
		ctr.setHeader(header);
		ctr.setFooter(footer);
		ctr.setTemplateDAOs(templateDAOs);
		
		return Optional.of(ctr);
	}	
	
	private String formatter(double num) {
	    if (num < 1000) return "" + num;
	    int exp = (int) (Math.log(num) / Math.log(1000));
	    return String.format("%.2f %c",
	                         num / Math.pow(1000, exp),
	                         "KMGTPE".charAt(exp-1));
	}

}
