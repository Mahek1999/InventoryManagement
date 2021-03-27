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
import com.higradius.main.payload.EditRecordRequest;
import com.higradius.main.service.InvoiceService;
import com.higradius.main.service.serviceImpl.InvoiceServiceImpl;

@WebServlet("/EditInvoiceRecordServlet")
public class EditInvoiceRecordServlet extends HttpServlet {

	private final InvoiceService invoiceService;

	public EditInvoiceRecordServlet() {
		invoiceService = new InvoiceServiceImpl();
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

		BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream()));

		String jsonString = "";
		if (requestReader != null) {
			jsonString = requestReader.readLine();
		}

		System.out.println(jsonString);
		Gson gson = new GsonBuilder().serializeNulls().create();

		EditRecordRequest editRecordRequest = gson.fromJson(jsonString, EditRecordRequest.class);
		invoiceService.updateInvoiceRecord(editRecordRequest);

	}

}
