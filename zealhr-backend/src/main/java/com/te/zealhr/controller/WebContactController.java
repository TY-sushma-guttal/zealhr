package com.te.zealhr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.WebContactDto;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.WebContactService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/web-contact")
public class WebContactController extends BaseConfigController{
	@Autowired
	private WebContactService webContactService;

	@PostMapping
	public ResponseEntity<SuccessResponse> webContact(@RequestBody WebContactDto webContactDto ){
		return ResponseEntity.ok(new SuccessResponse(false, "response added successfully", webContactService.webContact(webContactDto)));
	}
}
