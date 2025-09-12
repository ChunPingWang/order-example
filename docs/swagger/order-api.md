# 訂單微服務 API 文檔

## API 概述

訂單微服務負責處理系統中的訂單管理，包括訂單創建、訂單狀態更新、訂單追蹤和訂單履行等功能。該微服務與其他微服務如產品、客戶、支付和庫存等協作，提供完整的訂單處理流程。

## API 基礎路徑

```
/api/orders
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 訂單管理 API

### 1. 創建訂單

**端點:** `POST /`

**說明:** 創建新訂單

**請求體:**
```json
{
  "customerId": "string",
  "items": [
    {
      "productId": "string",
      "quantity": "number",
      "price": {
        "value": "number",
        "currency": "string"
      },
      "metadata": {}
    }
  ],
  "shippingAddress": {
    "recipientName": "string",
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string",
    "phoneNumber": "string"
  },
  "billingAddress": {
    "recipientName": "string",
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string",
    "phoneNumber": "string"
  },
  "shippingMethod": "string",
  "paymentMethodId": "string",
  "couponCode": "string",
  "notes": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "customerId": "string",
  "status": "string",
  "items": [
    {
      "productId": "string",
      "productName": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
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
  "tax": {
    "value": "number",
    "currency": "string"
  },
  "shippingCost": {
    "value": "number",
    "currency": "string"
  },
  "discount": {
    "value": "number",
    "currency": "string"
  },
  "total": {
    "value": "number",
    "currency": "string"
  },
  "createdAt": "string",
  "paymentStatus": "string",
  "paymentDue": {
    "value": "number",
    "currency": "string"
  },
  "estimatedShippingDate": "string"
}
```

**狀態碼:**
- `201 Created` - 訂單創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品或客戶不存在
- `409 Conflict` - 庫存不足

### 2. 獲取訂單詳情

**端點:** `GET /{orderId}`

**說明:** 獲取特定訂單的詳細信息

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "customerId": "string",
  "customerInfo": {
    "name": "string",
    "email": "string",
    "phone": "string"
  },
  "status": "string",
  "items": [
    {
      "productId": "string",
      "productName": "string",
      "productSku": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
      "subtotal": {
        "value": "number",
        "currency": "string"
      },
      "status": "string",
      "estimatedShippingDate": "string"
    }
  ],
  "subtotal": {
    "value": "number",
    "currency": "string"
  },
  "tax": {
    "value": "number",
    "currency": "string"
  },
  "shippingCost": {
    "value": "number",
    "currency": "string"
  },
  "discount": {
    "value": "number",
    "currency": "string"
  },
  "total": {
    "value": "number",
    "currency": "string"
  },
  "shippingAddress": {
    "recipientName": "string",
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string",
    "phoneNumber": "string"
  },
  "billingAddress": {
    "recipientName": "string",
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string",
    "phoneNumber": "string"
  },
  "shippingMethod": "string",
  "paymentMethod": "string",
  "paymentStatus": "string",
  "paymentDue": {
    "value": "number",
    "currency": "string"
  },
  "payments": [
    {
      "paymentId": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "status": "string",
      "date": "string"
    }
  ],
  "createdAt": "string",
  "updatedAt": "string",
  "estimatedShippingDate": "string",
  "shippedAt": "string",
  "deliveredAt": "string",
  "cancellationReason": "string",
  "notes": "string",
  "metadata": {},
  "timeline": [
    {
      "status": "string",
      "timestamp": "string",
      "description": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在

### 3. 更新訂單狀態

**端點:** `PUT /{orderId}/status`

**說明:** 更新特定訂單的狀態

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "status": "string",
  "reason": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "status": "string",
  "previousStatus": "string",
  "updatedAt": "string",
  "timeline": {
    "status": "string",
    "timestamp": "string",
    "description": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效或狀態更新不合法
- `404 Not Found` - 訂單不存在

### 4. 取消訂單

**端點:** `PUT /{orderId}/cancel`

**說明:** 取消特定訂單

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
  "orderNumber": "string",
  "status": "string",
  "cancelledAt": "string",
  "reason": "string",
  "refundStatus": "string"
}
```

**狀態碼:**
- `200 OK` - 訂單取消成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許取消

### 5. 更新訂單項目

**端點:** `PUT /{orderId}/items`

**說明:** 更新特定訂單的項目（僅在PENDING狀態下可用）

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "items": [
    {
      "productId": "string",
      "quantity": "number",
      "price": {
        "value": "number",
        "currency": "string"
      },
      "metadata": {}
    }
  ]
}
```

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "items": [
    {
      "productId": "string",
      "productName": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
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
  "tax": {
    "value": "number",
    "currency": "string"
  },
  "total": {
    "value": "number",
    "currency": "string"
  },
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 項目更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單或產品不存在
- `409 Conflict` - 訂單狀態不允許更新項目或庫存不足

### 6. 查詢訂單列表

**端點:** `GET /`

**說明:** 獲取符合條件的訂單列表

**查詢參數:**
- `customerId` (可選) - 按客戶ID過濾
- `status` (可選) - 按狀態過濾 (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- `fromDate` (可選) - 按訂單創建日期範圍過濾（開始日期）
- `toDate` (可選) - 按訂單創建日期範圍過濾（結束日期）
- `minTotal` (可選) - 按最小訂單總額過濾
- `maxTotal` (可選) - 按最大訂單總額過濾
- `sort` (可選, 默認為createdAt:desc) - 排序字段和方向 (createdAt:asc, createdAt:desc, total:asc, total:desc)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "orders": [
    {
      "orderId": "string",
      "orderNumber": "string",
      "customerId": "string",
      "customerName": "string",
      "status": "string",
      "total": {
        "value": "number",
        "currency": "string"
      },
      "itemCount": "number",
      "createdAt": "string",
      "paymentStatus": "string",
      "shippingMethod": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "totalOrders": "number",
    "totalAmount": {
      "value": "number",
      "currency": "string"
    },
    "statusBreakdown": {
      "PENDING": "number",
      "PROCESSING": "number",
      "SHIPPED": "number",
      "DELIVERED": "number",
      "CANCELLED": "number"
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 7. 查詢客戶訂單

**端點:** `GET /customer/{customerId}`

**說明:** 獲取特定客戶的所有訂單

**路徑參數:**
- `customerId` - 客戶唯一標識符

**查詢參數:**
- `status` (可選) - 按狀態過濾 (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "orders": [
    {
      "orderId": "string",
      "orderNumber": "string",
      "status": "string",
      "total": {
        "value": "number",
        "currency": "string"
      },
      "itemCount": "number",
      "createdAt": "string",
      "estimatedDeliveryDate": "string"
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
- `404 Not Found` - 客戶不存在

## 訂單履行 API

### 1. 標記訂單準備中

**端點:** `PUT /{orderId}/processing`

**說明:** 將訂單標記為處理中（已開始準備）

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "processorId": "string",
  "estimatedCompletionTime": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "status": "string",
  "processingStartedAt": "string",
  "estimatedCompletionTime": "string",
  "processor": {
    "id": "string",
    "name": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許處理

### 2. 標記訂單已出貨

**端點:** `PUT /{orderId}/shipped`

**說明:** 將訂單標記為已出貨

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "trackingNumber": "string",
  "carrier": "string",
  "shippedDate": "string",
  "estimatedDeliveryDate": "string",
  "packages": [
    {
      "packageId": "string",
      "weight": {
        "value": "number",
        "unit": "string"
      },
      "dimensions": {
        "length": "number",
        "width": "number",
        "height": "number",
        "unit": "string"
      },
      "items": [
        {
          "productId": "string",
          "quantity": "number"
        }
      ]
    }
  ],
  "shippingDocuments": [
    {
      "type": "string",
      "url": "string"
    }
  ],
  "notes": "string"
}
```

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "status": "string",
  "shippedAt": "string",
  "estimatedDeliveryDate": "string",
  "trackingInfo": {
    "trackingNumber": "string",
    "carrier": "string",
    "trackingUrl": "string"
  },
  "packageCount": "number"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許出貨

### 3. 標記訂單已送達

**端點:** `PUT /{orderId}/delivered`

**說明:** 將訂單標記為已送達

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "deliveryDate": "string",
  "signedBy": "string",
  "deliveryNotes": "string",
  "proofOfDelivery": "string"
}
```

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "status": "string",
  "deliveredAt": "string",
  "signedBy": "string",
  "deliveryDetails": {
    "notes": "string",
    "proof": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許標記為已送達

### 4. 獲取訂單追蹤信息

**端點:** `GET /{orderId}/tracking`

**說明:** 獲取特定訂單的追蹤信息

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "orderId": "string",
  "orderNumber": "string",
  "status": "string",
  "timeline": [
    {
      "status": "string",
      "timestamp": "string",
      "description": "string",
      "location": "string"
    }
  ],
  "packages": [
    {
      "packageId": "string",
      "trackingNumber": "string",
      "carrier": "string",
      "status": "string",
      "trackingUrl": "string",
      "events": [
        {
          "status": "string",
          "timestamp": "string",
          "description": "string",
          "location": "string"
        }
      ],
      "estimatedDeliveryDate": "string"
    }
  ],
  "shippedAt": "string",
  "estimatedDeliveryDate": "string",
  "deliveredAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在

### 5. 添加訂單注釋

**端點:** `POST /{orderId}/notes`

**說明:** 為特定訂單添加注釋

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "note": "string",
  "authorId": "string",
  "isPrivate": "boolean"
}
```

**響應:**
```json
{
  "noteId": "string",
  "orderId": "string",
  "note": "string",
  "author": {
    "id": "string",
    "name": "string"
  },
  "isPrivate": "boolean",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 注釋添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在

## 發票與收據 API

### 1. 生成訂單發票

**端點:** `POST /{orderId}/invoice`

**說明:** 為特定訂單生成發票

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "invoiceDate": "string",
  "dueDate": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "invoiceId": "string",
  "orderId": "string",
  "orderNumber": "string",
  "invoiceNumber": "string",
  "status": "string",
  "customerId": "string",
  "customerInfo": {
    "name": "string",
    "email": "string",
    "billingAddress": {}
  },
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
  "downloadUrl": "string"
}
```

**狀態碼:**
- `201 Created` - 發票生成成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 發票已存在或訂單狀態不允許生成發票

### 2. 獲取訂單發票

**端點:** `GET /{orderId}/invoice`

**說明:** 獲取特定訂單的發票詳情

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "invoiceId": "string",
  "orderId": "string",
  "orderNumber": "string",
  "invoiceNumber": "string",
  "status": "string",
  "customerId": "string",
  "customerInfo": {
    "name": "string",
    "email": "string",
    "billingAddress": {}
  },
  "items": [
    {
      "description": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
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
  "tax": {
    "value": "number",
    "currency": "string"
  },
  "shippingCost": {
    "value": "number",
    "currency": "string"
  },
  "discount": {
    "value": "number",
    "currency": "string"
  },
  "total": {
    "value": "number",
    "currency": "string"
  },
  "createdAt": "string",
  "dueDate": "string",
  "paidAt": "string",
  "payments": [
    {
      "paymentId": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "date": "string",
      "method": "string"
    }
  ],
  "downloadUrl": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單或發票不存在

### 3. 下載訂單收據

**端點:** `GET /{orderId}/receipt`

**說明:** 下載特定訂單的收據

**路徑參數:**
- `orderId` - 訂單唯一標識符

**查詢參數:**
- `format` (可選, 默認為PDF) - 下載格式 (PDF, HTML)

**響應:**
```
收據文件的二進制數據
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在或收據未生成
- `409 Conflict` - 訂單未完成支付，無法生成收據

## 退貨與退款 API

### 1. 創建退貨請求

**端點:** `POST /{orderId}/returns`

**說明:** 為特定訂單創建退貨請求

**路徑參數:**
- `orderId` - 訂單唯一標識符

**請求體:**
```json
{
  "items": [
    {
      "productId": "string",
      "quantity": "number",
      "reason": "string",
      "condition": "string"
    }
  ],
  "returnReason": "string",
  "requestedRefundMethod": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "returnId": "string",
  "orderId": "string",
  "orderNumber": "string",
  "status": "string",
  "items": [
    {
      "productId": "string",
      "productName": "string",
      "quantity": "number",
      "reason": "string",
      "condition": "string"
    }
  ],
  "returnReason": "string",
  "returnAmount": {
    "value": "number",
    "currency": "string"
  },
  "requestedRefundMethod": "string",
  "returnLabel": {
    "url": "string",
    "expiresAt": "string"
  },
  "createdAt": "string",
  "instructions": "string"
}
```

**狀態碼:**
- `201 Created` - 退貨請求創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單不存在
- `409 Conflict` - 訂單狀態不允許創建退貨或已存在退貨請求

### 2. 獲取退貨詳情

**端點:** `GET /returns/{returnId}`

**說明:** 獲取特定退貨請求的詳細信息

**路徑參數:**
- `returnId` - 退貨請求唯一標識符

**響應:**
```json
{
  "returnId": "string",
  "orderId": "string",
  "orderNumber": "string",
  "customerId": "string",
  "status": "string",
  "items": [
    {
      "productId": "string",
      "productName": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
      "subtotal": {
        "value": "number",
        "currency": "string"
      },
      "reason": "string",
      "condition": "string",
      "inspectionStatus": "string",
      "inspectionNotes": "string"
    }
  ],
  "returnReason": "string",
  "returnAmount": {
    "value": "number",
    "currency": "string"
  },
  "requestedRefundMethod": "string",
  "approvedRefundMethod": "string",
  "refundStatus": "string",
  "refundId": "string",
  "returnLabel": {
    "url": "string",
    "trackingNumber": "string",
    "carrier": "string",
    "expiresAt": "string"
  },
  "timeline": [
    {
      "status": "string",
      "timestamp": "string",
      "description": "string"
    }
  ],
  "receivedAt": "string",
  "inspectedAt": "string",
  "completedAt": "string",
  "notes": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 退貨不存在

### 3. 更新退貨狀態

**端點:** `PUT /returns/{returnId}/status`

**說明:** 更新特定退貨請求的狀態

**路徑參數:**
- `returnId` - 退貨請求唯一標識符

**請求體:**
```json
{
  "status": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "returnId": "string",
  "status": "string",
  "previousStatus": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 退貨不存在
- `409 Conflict` - 退貨狀態不允許更新

### 4. 確認退貨接收

**端點:** `PUT /returns/{returnId}/receive`

**說明:** 確認退貨物品已接收

**路徑參數:**
- `returnId` - 退貨請求唯一標識符

**請求體:**
```json
{
  "receivedItems": [
    {
      "productId": "string",
      "quantity": "number",
      "condition": "string",
      "inspectionNotes": "string"
    }
  ],
  "receivedDate": "string",
  "receivedBy": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "returnId": "string",
  "status": "string",
  "receivedAt": "string",
  "receivedItems": [
    {
      "productId": "string",
      "quantity": "number",
      "condition": "string"
    }
  ],
  "nextSteps": "string"
}
```

**狀態碼:**
- `200 OK` - 接收確認成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 退貨不存在
- `409 Conflict` - 退貨狀態不允許確認接收

### 5. 處理退款

**端點:** `POST /returns/{returnId}/refund`

**說明:** 為已確認的退貨處理退款

**路徑參數:**
- `returnId` - 退貨請求唯一標識符

**請求體:**
```json
{
  "refundMethod": "string",
  "refundAmount": {
    "value": "number",
    "currency": "string"
  },
  "refundReason": "string",
  "processorId": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "returnId": "string",
  "refundId": "string",
  "status": "string",
  "refundMethod": "string",
  "refundAmount": {
    "value": "number",
    "currency": "string"
  },
  "refundedAt": "string",
  "processor": {
    "id": "string",
    "name": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 退款處理成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 退貨不存在
- `409 Conflict` - 退貨狀態不允許處理退款

## 報表與統計 API

### 1. 獲取訂單統計摘要

**端點:** `GET /statistics/summary`

**說明:** 獲取訂單統計的摘要信息

**查詢參數:**
- `fromDate` (可選) - 統計開始日期
- `toDate` (可選) - 統計結束日期

**響應:**
```json
{
  "period": {
    "fromDate": "string",
    "toDate": "string"
  },
  "totalOrders": "number",
  "totalRevenue": {
    "value": "number",
    "currency": "string"
  },
  "averageOrderValue": {
    "value": "number",
    "currency": "string"
  },
  "statusBreakdown": {
    "PENDING": "number",
    "PROCESSING": "number",
    "SHIPPED": "number",
    "DELIVERED": "number",
    "CANCELLED": "number"
  },
  "returnRate": "number",
  "topSellingProducts": [
    {
      "productId": "string",
      "productName": "string",
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

### 2. 獲取訂單時間趨勢

**端點:** `GET /statistics/trends`

**說明:** 獲取訂單數據的時間趨勢

**查詢參數:**
- `fromDate` (可選) - 統計開始日期
- `toDate` (可選) - 統計結束日期
- `interval` (可選, 默認為day) - 時間間隔 (hour, day, week, month)

**響應:**
```json
{
  "period": {
    "fromDate": "string",
    "toDate": "string",
    "interval": "string"
  },
  "trends": [
    {
      "period": "string",
      "orders": "number",
      "revenue": {
        "value": "number",
        "currency": "string"
      },
      "averageOrderValue": {
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
- `PENDING` - 待處理
- `PROCESSING` - 處理中
- `SHIPPED` - 已出貨
- `DELIVERED` - 已送達
- `CANCELLED` - 已取消
- `RETURNED` - 已退貨
- `PARTIALLY_RETURNED` - 部分退貨

### 支付狀態 (PaymentStatus)
- `PENDING` - 待支付
- `PARTIAL` - 部分支付
- `PAID` - 已付款
- `REFUNDED` - 已退款
- `PARTIALLY_REFUNDED` - 部分退款
- `FAILED` - 支付失敗

### 退貨狀態 (ReturnStatus)
- `REQUESTED` - 已申請
- `APPROVED` - 已批准
- `REJECTED` - 已拒絕
- `RETURN_LABEL_CREATED` - 已創建退貨標籤
- `IN_TRANSIT` - 運送中
- `RECEIVED` - 已接收
- `INSPECTED` - 已檢查
- `COMPLETED` - 已完成
- `CANCELLED` - 已取消

### 退貨理由 (ReturnReason)
- `DAMAGED` - 損壞
- `DEFECTIVE` - 有缺陷
- `WRONG_ITEM` - 錯誤商品
- `NOT_AS_DESCRIBED` - 不符合描述
- `UNWANTED` - 不想要
- `SIZE_FIT_ISSUE` - 尺寸問題
- `ARRIVED_LATE` - 到貨遲延
- `OTHER` - 其他

### 退款方式 (RefundMethod)
- `ORIGINAL_PAYMENT` - 原支付方式
- `STORE_CREDIT` - 商店積分
- `BANK_TRANSFER` - 銀行轉帳
- `CHECK` - 支票
- `OTHER` - 其他

### 發票狀態 (InvoiceStatus)
- `DRAFT` - 草稿
- `ISSUED` - 已開立
- `SENT` - 已發送
- `PAID` - 已付款
- `OVERDUE` - 已逾期
- `CANCELLED` - 已取消
- `VOID` - 作廢

## 錯誤代碼

| 代碼                   | 說明                              |
|------------------------|---------------------------------|
| ORDER_NOT_FOUND        | 訂單不存在                         |
| INVALID_ORDER_STATUS   | 無效的訂單狀態                     |
| PRODUCT_NOT_FOUND      | 產品不存在                         |
| CUSTOMER_NOT_FOUND     | 客戶不存在                         |
| INSUFFICIENT_INVENTORY | 庫存不足                          |
| RETURN_NOT_FOUND       | 退貨不存在                         |
| INVALID_RETURN_STATUS  | 無效的退貨狀態                     |
| INVOICE_ALREADY_EXISTS | 發票已存在                         |
| INVALID_PAYMENT_METHOD | 無效的支付方式                     |
| SHIPPING_METHOD_ERROR  | 運送方式錯誤                       |
| REFUND_FAILED          | 退款失敗                          |
| INVOICE_NOT_FOUND      | 發票不存在                         |
| RECEIPT_NOT_AVAILABLE  | 收據不可用                         |
| RETURN_PERIOD_EXPIRED  | 退貨期限已過                       |
