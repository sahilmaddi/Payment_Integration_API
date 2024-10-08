package in.sampleweb.domain.data.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import in.sampleweb.domain.data.dto.OrderResponse;
import in.sampleweb.domain.data.dto.PaymentCallbackDTO;
import in.sampleweb.domain.data.dto.PurchaseDTO;
import in.sampleweb.domain.data.entity.Order;
import in.sampleweb.domain.data.repository.OrderRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    private RazorpayClient client;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public OrderResponse createOrder(PurchaseDTO purchaseDto) throws Exception {

        // Create a Razorpay order
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", purchaseDto.getOrder().getTotalprice() * 100);  // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", purchaseDto.getCustomer().getEmail());

        // Initialize Razorpay client
        this.client = new RazorpayClient(keyId, keySecret);
        com.razorpay.Order razorPayOrder = client.Orders.create(orderRequest);

        // Save the order to the database
        Order newOrder = new Order();
        newOrder.setRazorPayOrderId(razorPayOrder.get("id"));
        newOrder.setOrderStatus(razorPayOrder.get("status"));
        newOrder.setTotalPrice(purchaseDto.getOrder().getTotalprice());
        newOrder.setEmail(purchaseDto.getCustomer().getEmail());
        orderRepo.save(newOrder);

        // Create and return the OrderResponse
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setRazorpayOrderId(razorPayOrder.get("id"));
        orderResponse.setOrderStatus(razorPayOrder.get("status"));

        return orderResponse;
    }

    public Order verifyPaymentAndUpdateOrderStatus(PaymentCallbackDTO paymentCallbackDTO) {
        Order order = orderRepo.findByRazorPayOrderId(paymentCallbackDTO.getRazorpayOrderId());
        if (order != null) {
            try {
                // Verify the payment signature
                boolean isValid = verifySignature(paymentCallbackDTO);

                if (isValid) {
                    // Update order status and save the order
                    order.setOrderStatus("PAID");
                    order.setRazorPayPaymentId(paymentCallbackDTO.getRazorpayPaymentId());
                    orderRepo.save(order);  // Save updated order to the database
                    return order;
                } else {
                    System.out.println("Invalid payment signature.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean verifySignature(PaymentCallbackDTO paymentCallbackDTO) throws RazorpayException {
        String generatedSignature = HmacSHA256(
            paymentCallbackDTO.getRazorpayOrderId() + "|" + paymentCallbackDTO.getRazorpayPaymentId(),
            keySecret
        );
        return generatedSignature.equals(paymentCallbackDTO.getRazorpaySignature());
    }

    private String HmacSHA256(String data, String key) throws RazorpayException {
        try {
            Mac mac =Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            return new String(org.apache.commons.codec.binary.Hex.encodeHex(hash));
        } catch (Exception e) {
            throw new RazorpayException("Failed to calculate signature.", e);
        }
    }
}
