package com.te.zealhr.entity.hr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
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
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_announcement_details")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "announcementId")
public class AnnouncementDetails extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ad_announcement_id", unique = true, nullable = false, precision = 19)
	private Long announcementId;

	@Column(name = "ad_discription", nullable = false, length = 250)
	private String discription;

	@Column(name = "ad_type", nullable = false, length = 250)
	private String type;

	@Column(name = "ad_employees", length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> employees;

	@ManyToOne
	@JoinColumn(name = "ad_company_id")
	private CompanyInfo companyInfo;

	@ManyToOne
	@JoinColumn(name = "ad_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
