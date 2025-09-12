package com.example.order.application.query;

import com.example.order.domain.OrderStatus;
import lombok.Value;

public class OrderQueries {
    
    @Value
    public static class GetOrderQuery {
        String orderId;
    }
    
    @Value
    public static class GetCustomerOrdersQuery {
        String customerId;
        OrderStatus status;
        int page;
        int size;
    }
    
    @Value
    public static class SearchOrdersQuery {
        String keyword;
        OrderStatus status;
        String startDate;
        String endDate;
        int page;
        int size;
    }
}
