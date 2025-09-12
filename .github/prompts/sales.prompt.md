# 銷售微服務需求

## 功能概述
銷售微服務負責處理訂單的建立、管理與追蹤，連接商品、付款與物流服務。

## 主要功能

1. 銷售訂單處理
   - 從購物車建立銷售訂單
   - 銷售訂單狀態追蹤與管理
   - 銷售訂單項目與金額計算

2. 訂單管理
   - 銷售訂單查詢與搜尋
   - 銷售訂單修改與取消
   - 銷售訂單歷史與統計

3. 銷售整合
   - 與商品服務整合確認庫存
   - 與付款服務整合處理結帳
   - 與物流服務整合安排出貨

## 技術需求
- 遵循六角形架構設計
- 使用 OpenAPI 定義所有 API 介面
- 透過 HTTP Client 與其他微服務互動
- 實作 CQRS 命令模式處理業務邏輯

## Domain Entity 範例

```java
// Sale.java - 銷售訂單實體
public class Sale {
    private final SaleId id;
    private final UserId userId;
    private List<SaleItem> items;
    private Address shippingAddress;
    private Amount subtotal;
    private Amount tax;
    private Amount shippingCost;
    private Amount total;
    private SaleStatus status;
    private PaymentStatus paymentStatus;
    private ShipmentStatus shipmentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor, getters, domain methods
    public Sale(SaleId id, UserId userId, List<SaleItem> items, 
                Address shippingAddress, Amount subtotal, Amount tax, 
                Amount shippingCost, Amount total) {
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>(items);
        this.shippingAddress = shippingAddress;
        this.subtotal = subtotal;
        this.tax = tax;
        this.shippingCost = shippingCost;
        this.total = total;
        this.status = SaleStatus.CREATED;
        this.paymentStatus = PaymentStatus.PENDING;
        this.shipmentStatus = ShipmentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    
    public void updateStatus(SaleStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updatePaymentStatus(PaymentStatus newPaymentStatus) {
        this.paymentStatus = newPaymentStatus;
        this.updatedAt = LocalDateTime.now();
        
        // 若付款完成且出貨準備完成，則銷售訂單進入處理中狀態
        if (newPaymentStatus == PaymentStatus.COMPLETED && 
            this.shipmentStatus == ShipmentStatus.READY) {
            this.status = SaleStatus.PROCESSING;
        }
    }
    
    public void updateShipmentStatus(ShipmentStatus newShipmentStatus) {
        this.shipmentStatus = newShipmentStatus;
        this.updatedAt = LocalDateTime.now();
        
        // 若出貨已送達，則銷售訂單完成
        if (newShipmentStatus == ShipmentStatus.DELIVERED) {
            this.status = SaleStatus.COMPLETED;
        }
        
        // 若出貨準備完成且付款已完成，則銷售訂單進入處理中狀態
        else if (newShipmentStatus == ShipmentStatus.READY && 
                this.paymentStatus == PaymentStatus.COMPLETED) {
            this.status = SaleStatus.PROCESSING;
        }
    }
    
    public void cancel() {
        if (this.status == SaleStatus.COMPLETED || 
            this.status == SaleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel sale in current state");
        }
        
        this.status = SaleStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Value Objects
    public enum SaleStatus {
        CREATED, CONFIRMED, PROCESSING, SHIPPED, COMPLETED, CANCELLED
    }
    
    public enum PaymentStatus {
        PENDING, AUTHORIZED, COMPLETED, FAILED, REFUNDED
    }
    
    public enum ShipmentStatus {
        PENDING, READY, SHIPPED, DELIVERED, RETURNED
    }
}

// SaleItem.java - 銷售訂單項目值對象
public class SaleItem {
    private final ProductId productId;
    private final String productName;
    private final int quantity;
    private final Amount unitPrice;
    private final Amount subtotal;
    
    // Constructor, getters
    public SaleItem(ProductId productId, String productName, 
                    int quantity, Amount unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        
        // Calculate subtotal based on quantity and unit price
        BigDecimal subtotalValue = unitPrice.getValue().multiply(BigDecimal.valueOf(quantity));
        this.subtotal = new Amount(subtotalValue, unitPrice.getCurrency());
    }
}

// SaleSummary.java - 銷售訂單摘要值對象 (用於查詢)
public class SaleSummary {
    private final SaleId id;
    private final UserId userId;
    private final int itemCount;
    private final Amount total;
    private final SaleStatus status;
    private final LocalDateTime createdAt;
    
    // Constructor, getters
    public SaleSummary(Sale sale) {
        this.id = sale.getId();
        this.userId = sale.getUserId();
        this.itemCount = sale.getItems().size();
        this.total = sale.getTotal();
        this.status = sale.getStatus();
        this.createdAt = sale.getCreatedAt();
    }
}
```

---

請依上述需求設計銷售微服務的領域模型、使用案例及 API 介面。
