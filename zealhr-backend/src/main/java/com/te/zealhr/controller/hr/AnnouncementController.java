package com.te.zealhr.controller.hr;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.dto.hr.AnnouncementDetailsDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.hr.AnnouncementService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/hr/announcement")
@RequiredArgsConstructor
@RestController
public class AnnouncementController extends BaseConfigController {

	private final AnnouncementService announcementService;

	@PostMapping
	public ResponseEntity<SuccessResponse> saveAnnouncement(
			@RequestBody AnnouncementDetailsDTO announcementDetailsDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Announcement Saved Successfully")
						.data(announcementService.saveAnnouncement(announcementDetailsDTO, getCompanyId())).build());
	}

	@PutMapping
	public ResponseEntity<SuccessResponse> updateAnnouncement(
			@RequestBody AnnouncementDetailsDTO announcementDetailsDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Announcement Updated Successfully")
						.data(announcementService.updateAnnouncement(announcementDetailsDTO)).build());
	}

	@GetMapping
	public ResponseEntity<SuccessResponse> getAnnouncements() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Announcement Details Fetched Successfully")
						.data(announcementService.getAnnouncements(getCompanyId())).build());
	}

	@DeleteMapping("/{announcementId}")
	public ResponseEntity<SuccessResponse> deleteAnnouncement(@PathVariable Long announcementId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Announcement Details Deleted Successfully")
						.data(announcementService.deleteAnnouncement(announcementId)).build());
	}

}
