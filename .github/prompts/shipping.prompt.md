# 物流微服務需求

## 功能概述
物流微服務負責管理倉儲、出貨與配送流程，確保訂單能夠準確且及時送達客戶手中。

## 主要功能

1. 倉儲管理功能
   - 接收出貨通知與處理
   - 倉庫庫存確認與更新
   - 備貨流程與包裝管理
   - 多倉庫調度與最佳化

2. 出貨處理
   - 出貨單生成與處理
   - 揀貨與包裝指示
   - 出貨狀態追蹤與更新
   - 出貨異常處理機制

3. 物流追蹤
   - 物流商整合與選擇
   - 物流追蹤碼生成與管理
   - 即時物流狀態更新
   - 配送完成確認與回報

## 技術需求
- 遵循六角形架構設計
- 使用 OpenAPI 定義所有 API 介面
- 透過 HTTP Client 與其他微服務互動
- 實作 CQRS 命令模式處理業務邏輯
- 提供物流狀態實時更新機制

---

請依上述需求設計物流微服務的領域模型、使用案例及 API 介面。

## Domain Entity 範例

```java
// Shipment.java - 物流運送實體
public class Shipment {
    private final ShipmentId id;
    private final OrderId orderId;
    private final UserId userId;
    private final Address shippingAddress;
    private final List<ShipmentItem> items;
    private ShipmentStatus status;
    private String trackingNumber;
    private CarrierId carrierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    
    // Constructor, getters, domain methods
    public Shipment(ShipmentId id, OrderId orderId, UserId userId,
                   Address shippingAddress, List<ShipmentItem> items) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>(items);
        this.status = ShipmentStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    
    public void assignCarrier(CarrierId carrierId, String trackingNumber) {
        this.carrierId = carrierId;
        this.trackingNumber = trackingNumber;
        this.updateStatus(ShipmentStatus.READY_FOR_PICKUP);
    }
    
    public void pickUp() {
        this.updateStatus(ShipmentStatus.IN_TRANSIT);
    }
    
    public void deliver() {
        this.updateStatus(ShipmentStatus.DELIVERED);
        this.deliveredAt = LocalDateTime.now();
    }
    
    public void fail(String reason) {
        this.updateStatus(ShipmentStatus.FAILED);
    }
    
    public void returnToSender() {
        this.updateStatus(ShipmentStatus.RETURNED);
    }
    
    private void updateStatus(ShipmentStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
        if (newStatus == ShipmentStatus.IN_TRANSIT && this.shippedAt == null) {
            this.shippedAt = this.updatedAt;
        }
    }
    
    // Value Objects
    public enum ShipmentStatus {
        CREATED, PROCESSING, READY_FOR_PICKUP, IN_TRANSIT, DELIVERED, FAILED, RETURNED
    }
}

// ShipmentItem.java - 物流運送項目值對象
public class ShipmentItem {
    private final ProductId productId;
    private final String productName;
    private final int quantity;
    private final Weight weight;
    private final Dimensions dimensions;
    
    // Constructor, getters
    public ShipmentItem(ProductId productId, String productName, 
                       int quantity, Weight weight, Dimensions dimensions) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.weight = weight;
        this.dimensions = dimensions;
    }
    
    public Weight getTotalWeight() {
        return weight.multiply(quantity);
    }
}

// Warehouse.java - 倉庫實體
public class Warehouse {
    private final WarehouseId id;
    private WarehouseName name;
    private Address address;
    private Map<ProductId, InventoryItem> inventory;
    private boolean isActive;
    
    // Constructor, getters, domain methods
    public Warehouse(WarehouseId id, WarehouseName name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.inventory = new HashMap<>();
        this.isActive = true;
    }
    
    public void addInventory(ProductId productId, int quantity) {
        if (inventory.containsKey(productId)) {
            InventoryItem item = inventory.get(productId);
            inventory.put(productId, item.addQuantity(quantity));
        } else {
            inventory.put(productId, new InventoryItem(productId, quantity));
        }
    }
    
    public void removeInventory(ProductId productId, int quantity) {
        if (!inventory.containsKey(productId)) {
            throw new IllegalArgumentException("Product not in inventory");
        }
        
        InventoryItem item = inventory.get(productId);
        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough inventory");
        }
        
        if (item.getQuantity() == quantity) {
            inventory.remove(productId);
        } else {
            inventory.put(productId, item.removeQuantity(quantity));
        }
    }
    
    public boolean hasAvailableInventory(ProductId productId, int quantity) {
        return inventory.containsKey(productId) && 
               inventory.get(productId).getQuantity() >= quantity;
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void activate() {
        this.isActive = true;
    }
}

// InventoryItem.java - 庫存項目值對象
public class InventoryItem {
    private final ProductId productId;
    private final int quantity;
    private final LocalDateTime updatedAt;
    
    // Constructor, getters
    public InventoryItem(ProductId productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public InventoryItem addQuantity(int quantityToAdd) {
        return new InventoryItem(productId, quantity + quantityToAdd);
    }
    
    public InventoryItem removeQuantity(int quantityToRemove) {
        if (quantityToRemove > quantity) {
            throw new IllegalArgumentException("Cannot remove more than available quantity");
        }
        return new InventoryItem(productId, quantity - quantityToRemove);
    }
    
    public int getQuantity() {
        return quantity;
    }
}

// Carrier.java - 物流商實體
public class Carrier {
    private final CarrierId id;
    private CarrierName name;
    private String apiKey;
    private String apiEndpoint;
    private boolean isActive;
    
    // Constructor, getters, domain methods
    public Carrier(CarrierId id, CarrierName name, String apiKey, String apiEndpoint) {
        this.id = id;
        this.name = name;
        this.apiKey = apiKey;
        this.apiEndpoint = apiEndpoint;
        this.isActive = true;
    }
    
    public void updateApiDetails(String apiKey, String apiEndpoint) {
        this.apiKey = apiKey;
        this.apiEndpoint = apiEndpoint;
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void activate() {
        this.isActive = true;
    }
}
```
