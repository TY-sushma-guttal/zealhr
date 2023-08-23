package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.hr.AnnouncementDetailsDTO;

public interface AnnouncementService {

	AnnouncementDetailsDTO saveAnnouncement(AnnouncementDetailsDTO announcementDetailsDTO, Long companyId);

	AnnouncementDetailsDTO updateAnnouncement(AnnouncementDetailsDTO announcementDetailsDTO);

	List<AnnouncementDetailsDTO> getAnnouncements(Long companyId);

	String deleteAnnouncement(Long announcementId);

}
