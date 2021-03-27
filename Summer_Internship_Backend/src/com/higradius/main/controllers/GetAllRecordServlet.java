package com.higradius.main.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.higradius.main.DAO.InvoiceDetailDTO;
import com.higradius.main.model.InvoiceDetail;
import com.higradius.main.service.InvoiceService;
import com.higradius.main.service.serviceImpl.InvoiceServiceImpl;

@WebServlet("/GetAllRecordServlet")
public class GetAllRecordServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final InvoiceService invoiceService;

	public GetAllRecordServlet() {
		super();
		invoiceService = new InvoiceServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String jsonString = "";
		// To convert the result set into JSON.

		Optional<List<InvoiceDetailDTO>> resultSetListOptional = invoiceService.getAllRecords();
		List<InvoiceDetailDTO> resultSetList = null;
		if (resultSetListOptional.isPresent())
			resultSetList = resultSetListOptional.get();
		else
			throw new RuntimeException("Couldn't fetch the data as no data is present");

		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		jsonString = gson.toJson(resultSetList);
		out.write(jsonString);
		out.flush();

	}

}
