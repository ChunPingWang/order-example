# 商品微服務需求

## 功能概述
商品微服務負責管理商品資訊、庫存與目錄展示，提供完整的商品瀏覽、搜尋與購物車功能。

## 主要功能

1. 商品管理功能
   - 建立與維護商品基本資料（名稱、描述、價格、圖片等）
   - 商品分類與標籤管理
   - 商品庫存管理與追蹤

2. 購物功能
   - 使用者瀏覽商品目錄
   - 商品搜尋與篩選功能
   - 使用者將商品加入購物車
   - 購物車管理（新增、修改數量、移除）

## 技術需求
- 遵循六角形架構設計
- 使用 OpenAPI 定義所有 API 介面
- 透過 HTTP Client 與其他微服務互動
- 實作 CQRS 命令模式處理業務邏輯

---

請依上述需求設計商品微服務的領域模型、使用案例及 API 介面。

## Domain Entity 範例

```java
// Product.java - 商品實體
public class Product {
    private final ProductId id;
    private ProductName name;
    private ProductDescription description;
    private Price price;
    private Set<CategoryId> categories;
    private Set<ProductImage> images;
    private Inventory inventory;
    private ProductStatus status;
    
    // Constructor, getters, domain methods
    public Product(ProductId id, ProductName name, ProductDescription description,
                  Price price, Set<CategoryId> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.images = new HashSet<>();
        this.inventory = new Inventory(0);
        this.status = ProductStatus.DRAFT;
    }
    
    public void updatePrice(Price newPrice) {
        this.price = newPrice;
    }
    
    public void addToCategory(CategoryId categoryId) {
        this.categories.add(categoryId);
    }
    
    public void addImage(ProductImage image) {
        this.images.add(image);
    }
    
    public void publish() {
        if (inventory.getQuantity() <= 0) {
            throw new IllegalStateException("Cannot publish product with no inventory");
        }
        this.status = ProductStatus.ACTIVE;
    }
    
    public void updateInventory(int quantityChange) {
        this.inventory = this.inventory.updateQuantity(quantityChange);
        if (this.inventory.getQuantity() <= 0 && this.status == ProductStatus.ACTIVE) {
            this.status = ProductStatus.OUT_OF_STOCK;
        } else if (this.inventory.getQuantity() > 0 && this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.ACTIVE;
        }
    }
    
    // Value Objects
    public enum ProductStatus {
        DRAFT, ACTIVE, OUT_OF_STOCK, DISCONTINUED
    }
}

// Category.java - 商品分類實體
public class Category {
    private final CategoryId id;
    private CategoryName name;
    private CategoryDescription description;
    private CategoryId parentId;
    
    // Constructor, getters, domain methods
    public Category(CategoryId id, CategoryName name, CategoryDescription description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = null;
    }
    
    public Category(CategoryId id, CategoryName name, CategoryDescription description, 
                   CategoryId parentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
    }
    
    public void updateName(CategoryName newName) {
        this.name = newName;
    }
    
    public void updateDescription(CategoryDescription newDescription) {
        this.description = newDescription;
    }
    
    public void setParent(CategoryId parentId) {
        this.parentId = parentId;
    }
}

// ShoppingCart.java - 購物車實體
public class ShoppingCart {
    private final CartId id;
    private final UserId userId;
    private Map<ProductId, CartItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor, getters, domain methods
    public ShoppingCart(CartId id, UserId userId) {
        this.id = id;
        this.userId = userId;
        this.items = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
    
    public void addItem(ProductId productId, ProductName productName, Price unitPrice, int quantity) {
        if (items.containsKey(productId)) {
            CartItem existingItem = items.get(productId);
            items.put(productId, existingItem.updateQuantity(existingItem.getQuantity() + quantity));
        } else {
            items.put(productId, new CartItem(productId, productName, unitPrice, quantity));
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateItemQuantity(ProductId productId, int quantity) {
        if (!items.containsKey(productId)) {
            throw new IllegalArgumentException("Product not in cart");
        }
        if (quantity <= 0) {
            items.remove(productId);
        } else {
            CartItem existingItem = items.get(productId);
            items.put(productId, existingItem.updateQuantity(quantity));
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeItem(ProductId productId) {
        items.remove(productId);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void clear() {
        items.clear();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Price calculateTotal() {
        return items.values().stream()
            .map(CartItem::calculateSubtotal)
            .reduce(Price.ZERO, Price::add);
    }
    
    public int getTotalItems() {
        return items.values().stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
}

// CartItem.java - 購物車項目值對象
public class CartItem {
    private final ProductId productId;
    private final ProductName productName;
    private final Price unitPrice;
    private final int quantity;
    
    // Constructor, getters
    public CartItem(ProductId productId, ProductName productName, Price unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
    
    public CartItem updateQuantity(int newQuantity) {
        return new CartItem(this.productId, this.productName, this.unitPrice, newQuantity);
    }
    
    public Price calculateSubtotal() {
        return unitPrice.multiply(quantity);
    }
    
    public int getQuantity() {
        return quantity;
    }
}
```
