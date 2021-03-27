package com.higradius.main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.higradius.main.payload.DeleteRecordRequest;
import com.higradius.main.service.InvoiceService;
import com.higradius.main.service.serviceImpl.InvoiceServiceImpl;

@WebServlet("/DeleteInvoiceRecord")
public class DeleteInvoiceRecord extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final InvoiceService invoiceService;
	
	public DeleteInvoiceRecord()
	{
		this.invoiceService = new InvoiceServiceImpl();
		
	}
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		System.out.println("Request Recieved to delete");
		// 1. get received JSON data from request
		BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream()));

		String jsonString = "";
		if (requestReader != null) {
			jsonString = requestReader.readLine();
		}
		System.out.println(jsonString);

		Gson gson = new Gson();

		DeleteRecordRequest docIds = gson.fromJson(jsonString, DeleteRecordRequest.class);

		String temp = docIds.getDocIds();

		String docIdString[] = temp.substring(1, temp.length() - 1).split(",");
		
		invoiceService.deleteRecord(docIdString);
		

	}

}
