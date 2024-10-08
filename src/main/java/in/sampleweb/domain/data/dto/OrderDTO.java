package in.sampleweb.domain.data.dto;

import in.sampleweb.domain.data.entity.Address;
import in.sampleweb.domain.data.entity.Customer;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class OrderDTO {

    private int totalquantity;
    private double totalprice;
    private String razorPayOrderId;
    private String orderStatus;
    private String razorpayPaymentId;
    
    @ManyToOne
    private Customer customer;
    
    @ManyToOne
    private Address address;
}
