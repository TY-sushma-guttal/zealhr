package com.te.zealhr.entity.hr;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.zealhr.audit.Audit;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_job_requirement_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "requirementId")
public class JobRequirementDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "jrd_requirement_id", unique = true, nullable = false, precision = 19)
	private Long requirementId;

	@Column(name = "jrd_job_role", length = 50)
	private String jobRole;

	@Column(name = "jrd_job_description", length = 999)
	private String jobDescription;

	@Column(name = "jrd_no_of_openings")
	private Long noOfOpenings;

	@Column(name = "jrd_closed_slots")
	private Long closedSlots;

	@Column(name = "jrd_skills", length = 999)
	@Convert(converter = ListToStringConverter.class)
	private List<String> skills;

	@ManyToOne
	@JoinColumn(name = "jrd_company_id")
	private CompanyInfo companyInfo;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "jobRequirementDetails")
	private List<CandidateInfo> candidateInfoList;

}
