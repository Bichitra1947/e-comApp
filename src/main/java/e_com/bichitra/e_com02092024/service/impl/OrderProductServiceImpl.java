package e_com.bichitra.e_com02092024.service.impl;

import e_com.bichitra.e_com02092024.payload.OrderDTO;
import e_com.bichitra.e_com02092024.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderProductImpl implements OrderService {
    @Override
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod,
                               String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {

        return null;
    }
}
