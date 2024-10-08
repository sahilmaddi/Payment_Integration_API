package in.sampleweb.domain.data.controller;

import java.util.Map;

import in.sampleweb.domain.data.dto.OrderResponse;
import in.sampleweb.domain.data.dto.PaymentCallbackDTO;
import in.sampleweb.domain.data.dto.PruchaseDto;
import in.sampleweb.domain.data.dto.PurchaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import in.sampleweb.domain.data.entity.Order;
import in.sampleweb.domain.data.service.OrderService;
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/create-order", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<OrderResponse> purchaseCourse(@RequestBody PurchaseDTO purchaseDto) throws Exception {
        System.out.println("Getting into purchaseCourse...");
        OrderResponse orderResp = orderService.createOrder(purchaseDto);
	return new ResponseEntity<>(orderResp, HttpStatus.OK);
    }

    @PostMapping("/payment-callback")
    public String handlePaymentCallback(@RequestBody PaymentCallbackDTO paymentCallbackDTO, Model model) {
	System.out.println("Payload is :" + paymentCallbackDTO.getRazorpayOrderId() +" ," + paymentCallbackDTO.getRazorpayPaymentId()
    + "," + paymentCallbackDTO.getRazorpaySignature());
	Order updatedOrder = orderService.verifyPaymentAndUpdateOrderStatus(paymentCallbackDTO);
	//model.addAttribute("order", updatedOrder);
	return "success";
    }
}
