package in.sampleweb.domain.data.dto;

import in.sampleweb.domain.data.entity.Order;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class OrderItemDTO {

    private String imageUrl;
    private double unitPrice;
    private int quantity;
    private String productName;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
   
}
