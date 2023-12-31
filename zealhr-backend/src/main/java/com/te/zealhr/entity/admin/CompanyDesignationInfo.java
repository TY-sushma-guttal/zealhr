package com.te.zealhr.entity.admin;

import java.io.Serializable;
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
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.zealhr.audit.Audit;
import com.te.zealhr.util.JsonToStringConverter;
import com.te.zealhr.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_company_designation_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "designationId")
public class CompanyDesignationInfo extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cdi_designation_id", unique = true, nullable = false, precision = 19)
	private Long designationId;
	@Column(name = "cdi_designation_name", length = 50)
	private String designationName;
	@Column(name = "cdi_department", length = 50)
	private String department;
	@Convert(converter = JsonToStringConverter.class)
	@Column(name = "cdi_roles"  ,columnDefinition = "TEXT")
	private Object roles;
	@ManyToOne
	@JoinColumn(name = "cdi_parent_designation_id")
	private CompanyDesignationInfo parentDesignationInfo;
	@ManyToOne
	@JoinColumn(name = "cdi_company_id")
	private CompanyInfo companyInfo;

	@PreRemove
	public void remove() {
		companyInfo=null;
	}
	
}
