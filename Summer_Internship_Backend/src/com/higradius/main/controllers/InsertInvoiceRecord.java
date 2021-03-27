package com.higradius.main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.higradius.main.DAO.InvoiceDetailDTO;
import com.higradius.main.service.InvoiceService;
import com.higradius.main.service.serviceImpl.InvoiceServiceImpl;

@WebServlet("/InsertInvoiceRecord")
public class InsertInvoiceRecord extends HttpServlet {

	private final InvoiceService invoiceService;
	 
	public InsertInvoiceRecord()
	{
		this.invoiceService = new InvoiceServiceImpl();
		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Request Recieved");
		// 1. get received JSON data from request
				BufferedReader requestReader = 
				new BufferedReader(new InputStreamReader(req.getInputStream()));
				
				String jsonString = "";
				if(requestReader != null){
					jsonString = requestReader.readLine();
				}
				
				Gson gson = new GsonBuilder()
						 .setDateFormat("yyyy-MM-dd").serializeNulls()
						 .create();
				
				InvoiceDetailDTO invoiceDetailDAO = gson.fromJson(jsonString, InvoiceDetailDTO.class);
				System.out.println(invoiceDetailDAO);
				invoiceService.insertInvoiceRecord(invoiceDetailDAO);
				
	}	

}
