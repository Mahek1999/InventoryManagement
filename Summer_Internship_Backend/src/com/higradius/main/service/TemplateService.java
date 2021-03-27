package com.higradius.main.service;

import java.util.Optional;

import com.higradius.main.payload.CorrespondenceTemplateResponse;

public interface TemplateService {
	
	Optional<CorrespondenceTemplateResponse> getTemplate(String docIds);

}
