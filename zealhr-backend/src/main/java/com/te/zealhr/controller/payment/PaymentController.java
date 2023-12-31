package com.te.zealhr.controller.payment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.PlanDTO;
import com.te.zealhr.dto.payment.PaymentRequestDTO;
import com.te.zealhr.dto.payment.RazorpayResponseDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.payment.PaymentService;
import com.te.zealhr.audit.BaseConfigController;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/payment")
@RestController
public class PaymentController extends BaseConfigController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping
	public ResponseEntity<SuccessResponse> calculateAmount(@RequestBody(required = false) PlanDTO planDTO,
			HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder().error(Boolean.FALSE)
				.data(paymentService.calculateAmount(getTerminalId(), planDTO, request)).build());
	}

	@PostMapping("/order")
	public ResponseEntity<SuccessResponse> getOrderId(@RequestBody PaymentRequestDTO paymentRequestDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("Order Id Generated").data(paymentService.getOrderId(paymentRequestDTO)).build());

	}

	@PostMapping("/verify")
	public ResponseEntity<SuccessResponse> verifySignature(@RequestBody RazorpayResponseDTO razorpayResponseDTO,
			HttpServletRequest request) {
		boolean verifySignature = paymentService.verifySignature(razorpayResponseDTO, request);
		if (verifySignature) {
			return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
					.message("Verifing Signature Successful").data(verifySignature).build());
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
					.message("Verifing Signature Incorrect").data(verifySignature).build());
		}

	}

}
