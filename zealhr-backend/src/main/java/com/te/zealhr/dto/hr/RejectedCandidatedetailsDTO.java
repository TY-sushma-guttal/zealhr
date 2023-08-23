package com.te.zealhr.dto.hr;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.hr.CandidateInterviewInfo;

public class RejectedCandidatedetailsDTO {
	private Long candidateId;
	private String firstName;
	private String emailId;
	private Long mobileNumber;
	private String department;
	private String designationName;
	private String employementStatus;
	private BigDecimal yearOfExperience;
	private String referencePersonName;
	private String highestDegree;
	private String averageGrade;
	private String highestDegreeAndAverageGrade;
	private int roundNumber;
	private Map<String, String> feedback;
	private String resumeUrl;
	private List<CandidateInterviewInfo> candidateInterviewInfoList;
	private List<String> others;
	private Long requirementId;
	private String jobRole;
	private String jobDescription;
	private List<String> skills;
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public void setEmployementStatus(String employementStatus) {
		this.employementStatus = employementStatus;
	}
	public void setYearOfExperience(BigDecimal yearOfExperience) {
		this.yearOfExperience = yearOfExperience;
	}
	public void setReferencePersonName(String referencePersonName) {
		this.referencePersonName = referencePersonName;
	}
	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}
	public void setAverageGrade(String averageGrade) {
		this.averageGrade = averageGrade;
	}
	public void setHighestDegreeAndAverageGrade(String highestDegreeAndAverageGrade) {
		this.highestDegreeAndAverageGrade = highestDegreeAndAverageGrade;
	}
	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}
	public void setFeedback(Map<String, String> feedback) {
		this.feedback = feedback;
	}
	public void setResumeUrl(String resumeUrl) {
		this.resumeUrl = resumeUrl;
	}
	public void setCandidateInterviewInfoList(List<CandidateInterviewInfo> candidateInterviewInfoList) {
		this.candidateInterviewInfoList = candidateInterviewInfoList;
	}
	public String getEmailId() {
		return emailId;
	}
	public Long getMobileNumber() {
		return mobileNumber;
	}
	public String getDepartment() {
		return department;
	}
	public String getDesignationName() {
		return designationName;
	}
	public String getEmployementStatus() {
		return employementStatus;
	}
	public BigDecimal getYearOfExperience() {
		return yearOfExperience;
	}
	public String getReferencePersonName() {
		return referencePersonName;
	}
	public String getHighestDegreeAndAverageGrade() {
		return this.highestDegree+" "+this.averageGrade;
	}
	
	public int getRoundNumber() {
		return roundNumber;
	}
	public Map<String, String> getFeedback() {
		return feedback;
	}
	public String getResumeUrl() {
		return resumeUrl;
	}
	public Long getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}
	public List<String> getOthers() {
		return others;
	}
	public void setOthers(List<String> others) {
		this.others = others;
	}
	public Long getRequirementId() {
		return requirementId;
	}
	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
	}
	public String getJobRole() {
		return jobRole;
	}
	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public List<String> getSkills() {
		return skills;
	}
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	public String getFirstName() {
		return firstName;
	}
	
	
}
