# 銷售訂單流程情境測試

## 情境概述

本情境測試驗證從使用者選購商品、加入購物車、結帳付款到訂單確認的完整銷售流程，包括庫存檢查、付款處理和訂單生成的全過程。

## 前置條件

1. 已有註冊的客戶帳號
2. 商品目錄中有可購買的商品
3. 付款系統已設置好信用卡支付服務

## 測試步驟

### 1. 使用者瀏覽商品

**請求：**
```http
GET /api/merchandise/products?category=electronics&page=1&size=10
Content-Type: application/json
Authorization: Bearer {token}
```

**預期響應：**
```json
{
  "content": [
    {
      "productId": "PROD001",
      "name": "高效能筆記型電腦",
      "description": "最新款高效能筆記型電腦，適合專業工作使用",
      "price": {
        "value": 35000,
        "currency": "TWD"
      },
      "categoryIds": ["CAT-ELEC", "CAT-COMP"],
      "images": ["url-to-image1", "url-to-image2"],
      "inStock": true,
      "availableQuantity": 15
    },
    {
      "productId": "PROD002",
      "name": "專業級無線滑鼠",
      "description": "人體工學設計，長時間使用不疲勞",
      "price": {
        "value": 1500,
        "currency": "TWD"
      },
      "categoryIds": ["CAT-ELEC", "CAT-ACC"],
      "images": ["url-to-image1"],
      "inStock": true,
      "availableQuantity": 8
    }
  ],
  "totalElements": 42,
  "totalPages": 5,
  "currentPage": 1,
  "pageSize": 10
}
```

**驗證點：**
- 成功獲取商品列表
- 包含商品價格與庫存資訊

### 2. 使用者將商品加入購物車

**請求：**
```http
POST /api/merchandise/carts/current/items
Content-Type: application/json
Authorization: Bearer {token}

{
  "productId": "PROD001",
  "quantity": 1
}
```

**預期響應：**
```json
{
  "cartId": "CART123456",
  "items": [
    {
      "productId": "PROD001",
      "productName": "高效能筆記型電腦",
      "quantity": 1,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 35000,
        "currency": "TWD"
      }
    }
  ],
  "itemCount": 1,
  "totalAmount": {
    "value": 35000,
    "currency": "TWD"
  }
}
```

**驗證點：**
- 商品成功加入購物車
- 購物車總金額正確計算

### 3. 使用者繼續添加商品到購物車

**請求：**
```http
POST /api/merchandise/carts/current/items
Content-Type: application/json
Authorization: Bearer {token}

{
  "productId": "PROD002",
  "quantity": 2
}
```

**預期響應：**
```json
{
  "cartId": "CART123456",
  "items": [
    {
      "productId": "PROD001",
      "productName": "高效能筆記型電腦",
      "quantity": 1,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 35000,
        "currency": "TWD"
      }
    },
    {
      "productId": "PROD002",
      "productName": "專業級無線滑鼠",
      "quantity": 2,
      "unitPrice": {
        "value": 1500,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 3000,
        "currency": "TWD"
      }
    }
  ],
  "itemCount": 3,
  "totalAmount": {
    "value": 38000,
    "currency": "TWD"
  }
}
```

**驗證點：**
- 多個商品在購物車中
- 數量和總金額正確計算

### 4. 使用者調整購物車中商品數量

**請求：**
```http
PUT /api/merchandise/carts/current/items/PROD002
Content-Type: application/json
Authorization: Bearer {token}

{
  "quantity": 1
}
```

**預期響應：**
```json
{
  "cartId": "CART123456",
  "items": [
    {
      "productId": "PROD001",
      "productName": "高效能筆記型電腦",
      "quantity": 1,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 35000,
        "currency": "TWD"
      }
    },
    {
      "productId": "PROD002",
      "productName": "專業級無線滑鼠",
      "quantity": 1,
      "unitPrice": {
        "value": 1500,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 1500,
        "currency": "TWD"
      }
    }
  ],
  "itemCount": 2,
  "totalAmount": {
    "value": 36500,
    "currency": "TWD"
  }
}
```

**驗證點：**
- 商品數量成功更新
- 購物車總金額重新計算

### 5. 使用者前往結帳

**請求：**
```http
POST /api/sales/checkout
Content-Type: application/json
Authorization: Bearer {token}

{
  "cartId": "CART123456",
  "shippingAddress": {
    "street": "台北市信義區信義路五段7號",
    "city": "台北市",
    "state": "台灣",
    "postalCode": "110",
    "country": "TW"
  },
  "shippingMethod": "STANDARD"
}
```

**預期響應：**
```json
{
  "checkoutId": "CHKOUT789012",
  "items": [
    {
      "productId": "PROD001",
      "productName": "高效能筆記型電腦",
      "quantity": 1,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 35000,
        "currency": "TWD"
      }
    },
    {
      "productId": "PROD002",
      "productName": "專業級無線滑鼠",
      "quantity": 1,
      "unitPrice": {
        "value": 1500,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 1500,
        "currency": "TWD"
      }
    }
  ],
  "subtotal": {
    "value": 36500,
    "currency": "TWD"
  },
  "tax": {
    "value": 1825,
    "currency": "TWD"
  },
  "shippingCost": {
    "value": 100,
    "currency": "TWD"
  },
  "total": {
    "value": 38425,
    "currency": "TWD"
  },
  "shippingAddress": {
    "street": "台北市信義區信義路五段7號",
    "city": "台北市",
    "state": "台灣",
    "postalCode": "110",
    "country": "TW"
  },
  "paymentMethods": [
    {
      "id": "CREDIT_CARD",
      "name": "信用卡"
    },
    {
      "id": "BANK_TRANSFER",
      "name": "銀行轉帳"
    }
  ],
  "expiresAt": "2025-09-13T15:30:00Z"
}
```

**驗證點：**
- 結帳資訊成功創建
- 稅金與運費計算正確
- 提供可用的付款方式

### 6. 使用信用卡支付

**請求：**
```http
POST /api/payment/process
Content-Type: application/json
Authorization: Bearer {token}

{
  "checkoutId": "CHKOUT789012",
  "paymentMethod": "CREDIT_CARD",
  "creditCardInfo": {
    "cardHolderName": "王小明",
    "cardNumber": "4111111111111111",
    "expiryMonth": 12,
    "expiryYear": 2027,
    "cvv": "123"
  }
}
```

**預期響應：**
```json
{
  "paymentId": "PAY456789",
  "status": "AUTHORIZED",
  "transactionReference": "TXN987654321",
  "amount": {
    "value": 38425,
    "currency": "TWD"
  },
  "authorizationCode": "AUTH123456"
}
```

**驗證點：**
- 付款授權成功
- 返回交易參考號和授權碼

### 7. 創建銷售訂單

**請求：**
```http
POST /api/sales/orders
Content-Type: application/json
Authorization: Bearer {token}

{
  "checkoutId": "CHKOUT789012",
  "paymentId": "PAY456789"
}
```

**預期響應：**
```json
{
  "saleId": "SALE123456",
  "status": "CONFIRMED",
  "paymentStatus": "COMPLETED",
  "shipmentStatus": "PENDING",
  "items": [
    {
      "productId": "PROD001",
      "productName": "高效能筆記型電腦",
      "quantity": 1,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 35000,
        "currency": "TWD"
      }
    },
    {
      "productId": "PROD002",
      "productName": "專業級無線滑鼠",
      "quantity": 1,
      "unitPrice": {
        "value": 1500,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 1500,
        "currency": "TWD"
      }
    }
  ],
  "subtotal": {
    "value": 36500,
    "currency": "TWD"
  },
  "tax": {
    "value": 1825,
    "currency": "TWD"
  },
  "shippingCost": {
    "value": 100,
    "currency": "TWD"
  },
  "total": {
    "value": 38425,
    "currency": "TWD"
  },
  "shippingAddress": {
    "street": "台北市信義區信義路五段7號",
    "city": "台北市",
    "state": "台灣",
    "postalCode": "110",
    "country": "TW"
  },
  "createdAt": "2025-09-13T14:15:00Z"
}
```

**驗證點：**
- 銷售訂單成功創建
- 訂單狀態為CONFIRMED
- 付款狀態為COMPLETED
- 出貨狀態為PENDING

### 8. 檢查商品庫存變更

**請求：**
```http
GET /api/merchandise/products/PROD001
Content-Type: application/json
Authorization: Bearer {token}
```

**預期響應：**
```json
{
  "productId": "PROD001",
  "name": "高效能筆記型電腦",
  "availableQuantity": 14,
  "inStock": true
}
```

**驗證點：**
- 商品庫存數量減少

### 9. 使用者查看訂單詳情

**請求：**
```http
GET /api/sales/orders/SALE123456
Content-Type: application/json
Authorization: Bearer {token}
```

**預期響應：**
```json
{
  "saleId": "SALE123456",
  "status": "CONFIRMED",
  "paymentStatus": "COMPLETED",
  "shipmentStatus": "PENDING",
  "items": [...],
  "subtotal": {
    "value": 36500,
    "currency": "TWD"
  },
  "tax": {
    "value": 1825,
    "currency": "TWD"
  },
  "shippingCost": {
    "value": 100,
    "currency": "TWD"
  },
  "total": {
    "value": 38425,
    "currency": "TWD"
  },
  "shippingAddress": {...},
  "createdAt": "2025-09-13T14:15:00Z",
  "paymentDetails": {
    "paymentId": "PAY456789",
    "method": "CREDIT_CARD",
    "lastDigits": "1111",
    "paidAt": "2025-09-13T14:14:30Z"
  },
  "estimatedDelivery": "2025-09-16"
}
```

**驗證點：**
- 可以查看完整訂單資訊
- 包含付款詳細資訊
- 包含預計送達日期

## 期望結果

1. 整個銷售流程從瀏覽商品到創建訂單能夠順利完成
2. 購物車操作正確，包括添加、更新數量等功能
3. 結帳流程順暢，包括地址提供、稅金和運費計算
4. 信用卡付款成功處理
5. 訂單成功創建並反映正確狀態
6. 庫存數量正確更新

## 錯誤處理測試案例

### 1. 購物車中商品庫存不足

**請求：**
```http
POST /api/sales/checkout
Content-Type: application/json
Authorization: Bearer {token}

{
  "cartId": "CART123457"
}
```

**預期響應：**
```json
{
  "error": "INSUFFICIENT_INVENTORY",
  "message": "Insufficient inventory for some items in your cart",
  "details": [
    {
      "productId": "PROD003",
      "productName": "無線耳機",
      "requestedQuantity": 5,
      "availableQuantity": 2
    }
  ],
  "statusCode": 400
}
```

### 2. 信用卡支付失敗

**請求：**
```http
POST /api/payment/process
Content-Type: application/json
Authorization: Bearer {token}

{
  "checkoutId": "CHKOUT789013",
  "paymentMethod": "CREDIT_CARD",
  "creditCardInfo": {
    "cardHolderName": "測試失敗",
    "cardNumber": "4111111111111111",
    "expiryMonth": 12,
    "expiryYear": 2027,
    "cvv": "999"  // 測試失敗的CVV
  }
}
```

**預期響應：**
```json
{
  "error": "PAYMENT_FAILED",
  "message": "Credit card payment failed",
  "details": {
    "reason": "DECLINED",
    "description": "The card was declined by the issuing bank"
  },
  "statusCode": 400
}
```
