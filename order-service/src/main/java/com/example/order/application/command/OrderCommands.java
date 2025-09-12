package com.example.order.application.command;

import com.example.order.domain.Address;
import com.example.order.domain.OrderItem;
import lombok.Value;

import java.util.List;

public class OrderCommands {
    
    @Value
    public static class CreateOrderCommand {
        String customerId;
        List<OrderItem> items;
        Address shippingAddress;
    }
    
    @Value
    public static class UpdateOrderCommand {
        String orderId;
        List<OrderItem> items;
    }
    
    @Value
    public static class CancelOrderCommand {
        String orderId;
        String reason;
    }
    
    @Value
    public static class ConfirmOrderCommand {
        String orderId;
    }
    
    @Value
    public static class MarkOrderAsPaidCommand {
        String orderId;
    }
    
    @Value
    public static class ShipOrderCommand {
        String orderId;
        String trackingNumber;
    }
    
    @Value
    public static class DeliverOrderCommand {
        String orderId;
    }
}
