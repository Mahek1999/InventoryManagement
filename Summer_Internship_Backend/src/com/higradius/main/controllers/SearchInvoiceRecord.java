package com.higradius.main.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

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

@WebServlet("/SearchInvoiceRecord")

public class SearchInvoiceRecord extends HttpServlet {

	private final InvoiceService invoiceService;

	public SearchInvoiceRecord() {
		invoiceService = new InvoiceServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String toSearch = request.getParameter("toSearch");
		
		PrintWriter out = response.getWriter();
		String jsonString = "";
		
		Optional<List<InvoiceDetailDTO>> resultSetListOptional = invoiceService.searchByInvoiceId(toSearch);
		List<InvoiceDetailDTO> resultSetList = null;

		if (resultSetListOptional.isPresent())
			resultSetList = resultSetListOptional.get();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

		jsonString = gson.toJson(resultSetList);

		out.write(jsonString);
		out.flush();

	}
}
