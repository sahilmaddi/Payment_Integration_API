package in.sampleweb.domain.data.dto;

import lombok.Data;

@Data
public class OrderResponse {

    private String razorpayOrderId;
    private String orderStatus;

}
