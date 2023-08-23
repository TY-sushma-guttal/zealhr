package com.te.zealhr.entity.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.te.zealhr.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fa_employee_regularization_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "regularizationId")
public class EmployeeRegularizationDetails extends Audit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "erd_regularization_id", unique = true, nullable = false, precision = 19)
	private Long regularizationId;

	@Column(name = "erd_punch_in")
	private LocalDateTime punchIn;

	@Column(name = "erd_punch_out")
	private LocalDateTime punchOut;

	@Column(name = "erd_reason", length = 999)
	private String reason;
	
	@Column(name = "erd_status")
	private String status;
	
	@Column(name = "erd_rejection_reason")
	private String rejectionReason;

	@ManyToOne
	@JoinColumn(name = "erd_employee_info_id")
	private EmployeePersonalInfo employeePersonalInfo;

}
