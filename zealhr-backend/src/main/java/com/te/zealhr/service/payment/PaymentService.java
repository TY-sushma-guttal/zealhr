package com.te.zealhr.service.payment;

import javax.servlet.http.HttpServletRequest;

import com.te.zealhr.dto.admin.PlanDTO;
import com.te.zealhr.dto.payment.PaymentRequestDTO;
import com.te.zealhr.dto.payment.RazorpayResponseDTO;

public interface PaymentService {
	
	String getOrderId(PaymentRequestDTO paymentRequestDTO);
	
	PlanDTO calculateAmount(String termonalId, PlanDTO planDto,HttpServletRequest request);
	
	boolean verifySignature(RazorpayResponseDTO razorpayResponseDTO,HttpServletRequest request);

}
