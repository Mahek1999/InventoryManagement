package com.higradius.main.payload;

import java.util.ArrayList;
import java.util.List;

import com.higradius.main.DAO.TemplateDTO;

public class CorrespondenceTemplateResponse {

	private String header;
	private String footer;
	private List<TemplateDTO> listOfUser = new ArrayList<>();

	public CorrespondenceTemplateResponse() {

	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return the templateDAOs
	 */
	public List<TemplateDTO> getTemplateDAOs() {
		return listOfUser;
	}

	/**
	 * @param templateDAOs the templateDAOs to set
	 */
	public void setTemplateDAOs(List<TemplateDTO> templateDAOs) {
		this.listOfUser = templateDAOs;
	}

}
