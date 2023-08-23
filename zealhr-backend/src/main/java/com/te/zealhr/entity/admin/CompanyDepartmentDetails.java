package com.te.zealhr.entity.admin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.zealhr.audit.Audit;
import com.te.zealhr.util.JsonToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_department_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "companyDepartmentId")
public class CompanyDepartmentDetails extends Audit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_company_department_id", unique = true, nullable = false, precision = 19)
	private Long companyDepartmentId;
	@Column(name = "cd_company_department_name", length = 50)
	private String companyDepartmentName;
	@Convert(converter = JsonToStringConverter.class)
	@Column(name = "cd_company_department_roles"  ,columnDefinition = "TEXT")
	private Object companyDepartmentRoles;
	@ManyToOne
	@JoinColumn(name = "cd_company_id")
	private CompanyInfo companyInfo;
	@ManyToOne
	@JoinColumn(name = "cd_parent_department_id")
	private CompanyDepartmentDetails parentDepartmentInfo;
	
	@PreRemove
	public void remove() {
		companyInfo=null;
	}

}
