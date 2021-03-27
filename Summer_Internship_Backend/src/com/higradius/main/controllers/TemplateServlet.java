package com.higradius.main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.higradius.main.payload.CorrespondenceTemplateRequest;
import com.higradius.main.payload.CorrespondenceTemplateResponse;
import com.higradius.main.service.TemplateService;
import com.higradius.main.service.serviceImpl.TemplateServiceImpl_1;
import com.higradius.main.service.serviceImpl.TemplateServiceImpl_2;

@WebServlet("/TemplateServlet")
public class TemplateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TemplateService templateService = null;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream()));

		String jsonString = "";
		if (requestReader != null) {
			jsonString = requestReader.readLine();
		}
		System.out.println(jsonString);
		PrintWriter out = response.getWriter();

		Gson gson = new Gson();

		CorrespondenceTemplateRequest ctr = gson.fromJson(jsonString, CorrespondenceTemplateRequest.class);

		String temp = ctr.getDocIds();

		String docIdString = temp.substring(1, temp.length() - 1);

		int id = ctr.getId();

		if (id == 1)
			templateService = new TemplateServiceImpl_1();
		else if(id == 2)
			templateService = new TemplateServiceImpl_2();

		Optional<CorrespondenceTemplateResponse> resultSetListOptional = templateService.getTemplate(docIdString);
		CorrespondenceTemplateResponse resultSetList = null;
		if (resultSetListOptional.isPresent())
			resultSetList = resultSetListOptional.get();
		else
			throw new RuntimeException("Couldn't fetch the data as no data is present");

		Gson gsonCreate = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

		jsonString = gsonCreate.toJson(resultSetList);
		out.write(jsonString);
		out.flush();

	}

}
