# 供應商微服務需求

## 功能概述
供應商微服務負責管理供應商資料與採購流程，提供註冊、資料管理與採購確認等功能。

## 主要功能

1. 供應商建立與管理
   - 供應商註冊並建立基本資料（名稱、地址、聯絡人、稅務資訊等）
   - 供應商資料更新與查詢功能
   - 供應商狀態管理（啟用/停用）

2. 採購相關功能
   - 採購人員建立採購單，選擇供應商及商品
   - 供應商查看並確認/拒絕採購單
   - 採購單狀態追蹤與更新

## 技術需求
- 遵循六角形架構設計
- 使用 OpenAPI 定義所有 API 介面
- 透過 HTTP Client 與其他微服務互動
- 實作 CQRS 命令模式處理業務邏輯

---

請依上述需求設計供應商微服務的領域模型、使用案例及 API 介面。

## Domain Entity 範例

```java
// Supplier.java - 供應商實體
public class Supplier {
    private final SupplierId id;
    private SupplierName name;
    private ContactInformation contactInfo;
    private Address address;
    private TaxInformation taxInfo;
    private SupplierStatus status;
    
    // Constructor, getters, domain methods
    public Supplier(SupplierId id, SupplierName name, ContactInformation contactInfo, 
                   Address address, TaxInformation taxInfo) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
        this.taxInfo = taxInfo;
        this.status = SupplierStatus.ACTIVE;
    }
    
    public void deactivate() {
        this.status = SupplierStatus.INACTIVE;
    }
    
    public void updateContactInformation(ContactInformation newContactInfo) {
        this.contactInfo = newContactInfo;
    }
    
    public void updateAddress(Address newAddress) {
        this.address = newAddress;
    }
    
    // Value Objects
    public enum SupplierStatus {
        ACTIVE, INACTIVE
    }
}

// PurchaseOrder.java - 採購單實體
public class PurchaseOrder {
    private final PurchaseOrderId id;
    private final SupplierId supplierId;
    private Set<PurchaseOrderItem> items;
    private PurchaseOrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor, getters, domain methods
    public PurchaseOrder(PurchaseOrderId id, SupplierId supplierId, Set<PurchaseOrderItem> items) {
        this.id = id;
        this.supplierId = supplierId;
        this.items = items;
        this.status = PurchaseOrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    
    public void confirm() {
        if (this.status != PurchaseOrderStatus.CREATED) {
            throw new IllegalStateException("Cannot confirm order that is not in CREATED state");
        }
        this.status = PurchaseOrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reject(String reason) {
        if (this.status != PurchaseOrderStatus.CREATED) {
            throw new IllegalStateException("Cannot reject order that is not in CREATED state");
        }
        this.status = PurchaseOrderStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Value Objects
    public enum PurchaseOrderStatus {
        CREATED, CONFIRMED, REJECTED, COMPLETED
    }
}

// PurchaseOrderItem.java - 採購單項目值對象
public class PurchaseOrderItem {
    private final ProductId productId;
    private final ProductName productName;
    private final Quantity quantity;
    private final Price unitPrice;
    
    // Constructor, getters
    public PurchaseOrderItem(ProductId productId, ProductName productName, 
                            Quantity quantity, Price unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    public Price calculateTotal() {
        return unitPrice.multiply(quantity.getValue());
    }
}
```
