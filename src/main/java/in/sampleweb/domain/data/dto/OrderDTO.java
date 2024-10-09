package in.sampleweb.domain.data.dto;

import java.util.ArrayList;
import java.util.List;

import in.sampleweb.domain.data.entity.Address;
import in.sampleweb.domain.data.entity.Customer;
import in.sampleweb.domain.data.entity.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class OrderDTO {

    private int totalquantity;
    private double totalprice;
    private String razorPayOrderId;
    private String orderStatus;
//    private String razorpayPaymentId;
    private String orderTrackingNumber;
    
    @ManyToOne
    private Customer customer;
    
    @ManyToOne
    private Address address;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();; 
}
