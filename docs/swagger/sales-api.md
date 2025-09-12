# 銷售微服務 API 文檔

## API 概述

銷售微服務負責處理系統中的所有訂單管理、結帳流程與銷售報表。這包括訂單創建與處理、付款流程、訂單狀態追蹤和相關報表生成。

## API 基礎路徑

```
/api/sales
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 訂單管理 API

### 1. 創建訂單

**端點:** `POST /orders`

**說明:** 創建新的客戶訂單

**請求體:**
```json
{
  "shippingAddress": {
    "name": "string",
    "phone": "string",
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "billingAddressSameAsShipping": "boolean",
  "billingAddress": {
    "name": "string",
    "phone": "string",
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "paymentMethod": "string",
  "shippingMethod": "string",
  "notes": "string",
  "couponCode": "string"
}
```

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "userId": "string",
  "status": "string",
  "items": [
    {
      "orderItemId": "string",
      "productId": "string",
      "name": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
      "selectedOptions": [
        {
          "name": "string",
          "value": "string",
          "displayName": "string",
          "priceAdjustment": {
            "value": "number",
            "currency": "string"
          }
        }
      ],
      "subtotal": {
        "value": "number",
        "currency": "string"
      }
    }
  ],
  "subtotal": {
    "value": "number",
    "currency": "string"
  },
  "discount": {
    "value": "number",
    "currency": "string",
    "couponCode": "string"
  },
  "tax": {
    "value": "number",
    "currency": "string"
  },
  "shipping": {
    "value": "number",
    "currency": "string",
    "method": "string"
  },
  "total": {
    "value": "number",
    "currency": "string"
  },
  "shippingAddress": {
    "name": "string",
    "phone": "string",
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "billingAddress": {
    "name": "string",
    "phone": "string",
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "paymentMethod": "string",
  "paymentUrl": "string",
  "createdAt": "string",
  "expiresAt": "string",
  "notes": "string"
}
```

**狀態碼:**
- `201 Created` - 訂單創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 購物車為空或找不到產品
- `422 Unprocessable Entity` - 產品庫存不足

### 2. 查詢訂單列表

**端點:** `GET /orders`

**說明:** 獲取符合條件的訂單列表

**查詢參數:**
- `userId` (可選) - 按用戶ID過濾
- `status` (可選) - 按狀態過濾 (CREATED, PAID, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- `fromDate` (可選) - 按創建日期範圍過濾（開始日期）
- `toDate` (可選) - 按創建日期範圍過濾（結束日期）
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "orders": [
    {
      "orderId": "string",
      "orderNumber": "string",
      "userId": "string",
      "status": "string",
      "total": {
        "value": "number",
        "currency": "string"
      },
      "createdAt": "string",
      "paymentStatus": "string",
      "shippingStatus": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 3. 獲取訂單詳情

**端點:** `GET /orders/{orderId}`

**說明:** 獲取特定訂單的詳細信息

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "userId": "string",
  "status": "string",
  "items": [
    {
      "orderItemId": "string",
      "productId": "string",
      "name": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
      "selectedOptions": [],
      "subtotal": {
        "value": "number",
        "currency": "string"
      }
    }
  ],
  "subtotal": {
    "value": "number",
    "currency": "string"
  },
  "discount": {
    "value": "number",
    "currency": "string",
    "couponCode": "string"
  },
  "tax": {
    "value": "number",
    "currency": "string"
  },
  "shipping": {
    "value": "number",
    "currency": "string",
    "method": "string"
  },
  "total": {
    "value": "number",
    "currency": "string"
  },
  "shippingAddress": {},
  "billingAddress": {},
  "paymentStatus": "string",
  "paymentDetails": {
    "paymentId": "string",
    "method": "string",
    "cardInfo": {
      "brand": "string",
      "last4": "string"
    },
    "paidAt": "string"
  },
  "fulfillmentStatus": "string",
  "timeline": [
    {
      "status": "string",
      "timestamp": "string"
    }
  ],
  "estimatedDelivery": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在

### 4. 更新訂單狀態

**端點:** `PUT /orders/{orderId}/status`

**說明:** 更新特定訂單的狀態

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "status": "string",
  "notes": "string",
  "notifyCustomer": "boolean"
}
```

**響應:**
```json
{
  "orderId": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 狀態轉換無效

### 5. 取消訂單

**端點:** `PUT /orders/{orderId}/cancel`

**說明:** 取消尚未處理的訂單

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "reason": "string",
  "cancelledBy": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "orderId": "string",
  "status": "string",
  "cancelledAt": "string",
  "refundStatus": "string",
  "refundDetails": {
    "refundId": "string",
    "amount": {
      "value": "number",
      "currency": "string"
    },
    "status": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 取消成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許取消

## 訂單付款 API

### 1. 處理訂單付款

**端點:** `POST /orders/{orderId}/payments`

**說明:** 處理訂單的付款請求

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "paymentMethod": "string",
  "paymentData": {
    "cardDetails": {
      "number": "string",
      "expiryMonth": "number",
      "expiryYear": "number",
      "cvv": "string",
      "holderName": "string"
    },
    "billingAddress": {
      "name": "string",
      "street": "string",
      "city": "string",
      "district": "string",
      "postalCode": "string",
      "country": "string"
    }
  },
  "amount": {
    "value": "number",
    "currency": "string"
  }
}
```

**響應:**
```json
{
  "paymentId": "string",
  "orderId": "string",
  "status": "string",
  "transactionId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "method": "string",
  "cardInfo": {
    "brand": "string",
    "last4": "string",
    "expiryMonth": "number",
    "expiryYear": "number"
  },
  "processedAt": "string",
  "receipt": {
    "url": "string",
    "number": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 付款處理成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單已付款或已取消
- `422 Unprocessable Entity` - 付款處理失敗

### 2. 查詢訂單付款狀態

**端點:** `GET /orders/{orderId}/payments`

**說明:** 獲取特定訂單的付款歷史記錄

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "orderId": "string",
  "payments": [
    {
      "paymentId": "string",
      "status": "string",
      "transactionId": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "method": "string",
      "processedAt": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在

### 3. 處理退款請求

**端點:** `POST /orders/{orderId}/refunds`

**說明:** 為訂單處理退款請求

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "reason": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "isFullRefund": "boolean",
  "refundItems": [
    {
      "orderItemId": "string",
      "quantity": "number",
      "reason": "string"
    }
  ]
}
```

**響應:**
```json
{
  "refundId": "string",
  "orderId": "string",
  "status": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "reason": "string",
  "processedAt": "string",
  "transactionId": "string"
}
```

**狀態碼:**
- `200 OK` - 退款處理成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許退款
- `422 Unprocessable Entity` - 退款處理失敗

## 訂單履行 API

### 1. 標記訂單為處理中

**端點:** `PUT /orders/{orderId}/process`

**說明:** 標記訂單開始處理

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "processedBy": "string",
  "warehouseId": "string",
  "estimatedShippingDate": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "orderId": "string",
  "status": "string",
  "fulfillmentStatus": "string",
  "processedAt": "string",
  "processedBy": "string",
  "estimatedShippingDate": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許處理

### 2. 創建出貨記錄

**端點:** `POST /orders/{orderId}/shipments`

**說明:** 為訂單創建出貨記錄

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "shippedBy": "string",
  "carrier": "string",
  "trackingNumber": "string",
  "shippingDate": "string",
  "items": [
    {
      "orderItemId": "string",
      "quantity": "number"
    }
  ],
  "packageInfo": {
    "weight": "number",
    "weightUnit": "string",
    "dimensions": {
      "length": "number",
      "width": "number",
      "height": "number",
      "unit": "string"
    }
  },
  "notes": "string"
}
```

**響應:**
```json
{
  "shipmentId": "string",
  "orderId": "string",
  "carrier": "string",
  "trackingNumber": "string",
  "trackingUrl": "string",
  "status": "string",
  "shippedAt": "string",
  "shippedBy": "string",
  "estimatedDelivery": "string"
}
```

**狀態碼:**
- `201 Created` - 出貨記錄創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許出貨

### 3. 獲取訂單出貨資料

**端點:** `GET /orders/{orderId}/shipments`

**說明:** 獲取特定訂單的所有出貨記錄

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "orderId": "string",
  "shipments": [
    {
      "shipmentId": "string",
      "carrier": "string",
      "trackingNumber": "string",
      "trackingUrl": "string",
      "status": "string",
      "shippedAt": "string",
      "estimatedDelivery": "string",
      "items": [
        {
          "orderItemId": "string",
          "productId": "string",
          "name": "string",
          "quantity": "number"
        }
      ]
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在

### 4. 更新出貨狀態

**端點:** `PUT /shipments/{shipmentId}/status`

**說明:** 更新特定出貨的狀態

**路徑參數:**
- `shipmentId` - 出貨記錄唯一標識符

**請求體:**
```json
{
  "status": "string",
  "statusDate": "string",
  "notes": "string",
  "notifyCustomer": "boolean",
  "location": {
    "city": "string",
    "country": "string"
  }
}
```

**響應:**
```json
{
  "shipmentId": "string",
  "status": "string",
  "updatedAt": "string",
  "currentLocation": {
    "city": "string",
    "country": "string"
  },
  "notificationSent": "boolean"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 出貨記錄不存在

## 訂單發票 API

### 1. 為訂單生成發票

**端點:** `POST /orders/{orderId}/invoices`

**說明:** 為訂單創建發票記錄

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "invoiceDate": "string",
  "dueDate": "string",
  "recipientInfo": {
    "name": "string",
    "email": "string",
    "taxId": "string"
  },
  "notes": "string"
}
```

**響應:**
```json
{
  "invoiceId": "string",
  "invoiceNumber": "string",
  "orderId": "string",
  "status": "string",
  "items": [],
  "subtotal": {
    "value": "number",
    "currency": "string"
  },
  "tax": {
    "value": "number",
    "currency": "string"
  },
  "total": {
    "value": "number",
    "currency": "string"
  },
  "createdAt": "string",
  "dueDate": "string",
  "recipientInfo": {
    "name": "string",
    "email": "string",
    "taxId": "string"
  }
}
```

**狀態碼:**
- `201 Created` - 發票創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單已有發票

### 2. 獲取訂單發票

**端點:** `GET /orders/{orderId}/invoices`

**說明:** 獲取特定訂單的所有發票記錄

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "orderId": "string",
  "invoices": [
    {
      "invoiceId": "string",
      "invoiceNumber": "string",
      "status": "string",
      "total": {
        "value": "number",
        "currency": "string"
      },
      "createdAt": "string",
      "dueDate": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在

## 銷售報表 API

### 1. 獲取銷售概要報表

**端點:** `GET /reports/sales-summary`

**說明:** 獲取特定期間的銷售摘要報表

**查詢參數:**
- `fromDate` (必填) - 開始日期
- `toDate` (必填) - 結束日期
- `groupBy` (可選) - 分組方式 (DAY, WEEK, MONTH)
- `includeDetails` (可選, 默認為false) - 是否包含詳細資料

**響應:**
```json
{
  "period": {
    "fromDate": "string",
    "toDate": "string"
  },
  "totalSales": {
    "value": "number",
    "currency": "string"
  },
  "totalOrders": "number",
  "averageOrderValue": {
    "value": "number",
    "currency": "string"
  },
  "salesByPeriod": [
    {
      "period": "string",
      "sales": {
        "value": "number",
        "currency": "string"
      },
      "orders": "number"
    }
  ],
  "topSellingProducts": [
    {
      "productId": "string",
      "name": "string",
      "quantity": "number",
      "revenue": {
        "value": "number",
        "currency": "string"
      }
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 2. 獲取產品銷量報表

**端點:** `GET /reports/product-sales`

**說明:** 獲取產品銷量相關報表

**查詢參數:**
- `fromDate` (必填) - 開始日期
- `toDate` (必填) - 結束日期
- `categoryId` (可選) - 按類別過濾
- `productId` (可選) - 按產品過濾
- `sortBy` (可選, 默認為quantity) - 排序方式 (quantity, revenue)
- `limit` (可選, 默認為10) - 結果數量限制

**響應:**
```json
{
  "period": {
    "fromDate": "string",
    "toDate": "string"
  },
  "productSales": [
    {
      "productId": "string",
      "name": "string",
      "category": "string",
      "quantity": "number",
      "revenue": {
        "value": "number",
        "currency": "string"
      },
      "averageUnitPrice": {
        "value": "number",
        "currency": "string"
      }
    }
  ],
  "totalQuantitySold": "number",
  "totalRevenue": {
    "value": "number",
    "currency": "string"
  },
  "salesByCategory": [
    {
      "category": "string",
      "quantity": "number",
      "revenue": {
        "value": "number",
        "currency": "string"
      }
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

## 模型定義

### 訂單狀態 (OrderStatus)
- `CREATED` - 已創建
- `PAID` - 已付款
- `PROCESSING` - 處理中
- `SHIPPED` - 已出貨
- `DELIVERED` - 已送達
- `CANCELLED` - 已取消

### 支付狀態 (PaymentStatus)
- `PENDING` - 待支付
- `COMPLETED` - 已完成
- `FAILED` - 失敗
- `REFUNDED` - 已退款
- `PARTIALLY_REFUNDED` - 部分退款

### 支付方式 (PaymentMethod)
- `CREDIT_CARD` - 信用卡
- `DEBIT_CARD` - 金融卡
- `BANK_TRANSFER` - 銀行轉帳
- `MOBILE_PAYMENT` - 行動支付
- `COD` - 貨到付款

### 運送方式 (ShippingMethod)
- `STANDARD` - 標準運送
- `EXPRESS` - 快速運送
- `SAME_DAY` - 當日送達
- `PICKUP` - 自取

### 出貨狀態 (ShipmentStatus)
- `PROCESSING` - 處理中
- `SHIPPED` - 已出貨
- `IN_TRANSIT` - 運送中
- `OUT_FOR_DELIVERY` - 配送中
- `DELIVERED` - 已送達
- `EXCEPTION` - 異常

### 發票狀態 (InvoiceStatus)
- `DRAFT` - 草稿
- `ISSUED` - 已開立
- `SENT` - 已發送
- `PAID` - 已付款
- `CANCELLED` - 已取消
- `VOID` - 作廢

## 錯誤代碼

| 代碼                     | 說明                             |
|--------------------------|----------------------------------|
| ORDER_NOT_FOUND          | 訂單不存在                       |
| PAYMENT_FAILED           | 支付失敗                         |
| INSUFFICIENT_STOCK       | 庫存不足                         |
| INVALID_ORDER_STATUS     | 無效的訂單狀態                   |
| INVOICE_ALREADY_EXISTS   | 發票已存在                       |
| SHIPMENT_NOT_FOUND       | 出貨記錄不存在                   |
| REFUND_FAILED            | 退款失敗                         |
| INVALID_COUPON           | 無效的優惠券                     |
