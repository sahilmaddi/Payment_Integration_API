package in.sampleweb.domain.data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.sampleweb.domain.data.dto.OrderResponse;
import in.sampleweb.domain.data.dto.PaymentCallbackDTO;
import in.sampleweb.domain.data.dto.PurchaseDTO;
import in.sampleweb.domain.data.entity.Order;
import in.sampleweb.domain.data.service.OrderService;
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/byEmail/{email}")
    public ResponseEntity<List<Order>> getOrdersByEmail(@PathVariable("email") String email) {
        List<Order> orders = orderService.getOrdersByEmail(email);
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

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
