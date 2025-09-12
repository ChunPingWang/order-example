# 供應商微服務 API 文檔

## API 概述

供應商微服務負責管理系統中的所有供應商資料、採購流程和供應商品項。這包括供應商註冊、品項管理、採購訂單處理和供應商績效跟踪。

## API 基礎路徑

```
/api/supplier
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 供應商管理 API

### 1. 創建新供應商

**端點:** `POST /suppliers`

**說明:** 在系統中註冊新的供應商

**請求體:**
```json
{
  "name": "string",
  "taxId": "string",
  "contact": {
    "name": "string",
    "phone": "string",
    "email": "string"
  },
  "address": {
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "paymentTerms": "string",
  "categories": ["string"],
  "currency": "string"
}
```

**響應:**
```json
{
  "supplierId": "string",
  "name": "string",
  "taxId": "string",
  "status": "string",
  "contact": {
    "name": "string",
    "phone": "string",
    "email": "string"
  },
  "address": {
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "paymentTerms": "string",
  "categories": ["string"],
  "currency": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 供應商創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 供應商稅號已存在

### 2. 查詢供應商列表

**端點:** `GET /suppliers`

**說明:** 獲取符合條件的供應商列表

**查詢參數:**
- `name` (可選) - 按供應商名稱過濾
- `category` (可選) - 按類別過濾
- `status` (可選) - 按狀態過濾 (PENDING_APPROVAL, APPROVED, SUSPENDED)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "suppliers": [
    {
      "supplierId": "string",
      "name": "string",
      "taxId": "string",
      "status": "string",
      "categories": ["string"],
      "contact": {
        "name": "string",
        "phone": "string",
        "email": "string"
      }
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

### 3. 獲取供應商詳情

**端點:** `GET /suppliers/{supplierId}`

**說明:** 獲取特定供應商的詳細信息

**路徑參數:**
- `supplierId` - 供應商唯一標識符

**響應:**
```json
{
  "supplierId": "string",
  "name": "string",
  "taxId": "string",
  "status": "string",
  "contact": {
    "name": "string",
    "phone": "string",
    "email": "string"
  },
  "address": {
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "paymentTerms": "string",
  "categories": ["string"],
  "currency": "string",
  "riskLevel": "string",
  "creditLimit": {
    "value": "number",
    "currency": "string"
  },
  "createdAt": "string",
  "approvedAt": "string",
  "approvedBy": "string",
  "performance": {
    "averageResponseTime": "string",
    "qualityRating": "string",
    "deliveryRating": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 供應商不存在

### 4. 更新供應商資料

**端點:** `PUT /suppliers/{supplierId}`

**說明:** 更新特定供應商的信息

**路徑參數:**
- `supplierId` - 供應商唯一標識符

**請求體:**
```json
{
  "name": "string",
  "contact": {
    "name": "string",
    "phone": "string",
    "email": "string"
  },
  "address": {
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "paymentTerms": "string",
  "categories": ["string"],
  "currency": "string"
}
```

**響應:**
```json
{
  "supplierId": "string",
  "name": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 供應商不存在

### 5. 審核供應商

**端點:** `PUT /suppliers/{supplierId}/approve`

**說明:** 審核並激活供應商帳戶

**路徑參數:**
- `supplierId` - 供應商唯一標識符

**請求體:**
```json
{
  "approvedBy": "string",
  "notes": "string",
  "riskLevel": "string",
  "creditLimit": {
    "value": "number",
    "currency": "string"
  }
}
```

**響應:**
```json
{
  "supplierId": "string",
  "status": "string",
  "approvedAt": "string",
  "approvedBy": "string",
  "riskLevel": "string",
  "creditLimit": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 審核成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 供應商不存在
- `409 Conflict` - 供應商已被審核

### 6. 暫停供應商

**端點:** `PUT /suppliers/{supplierId}/suspend`

**說明:** 暫停供應商帳戶

**路徑參數:**
- `supplierId` - 供應商唯一標識符

**請求體:**
```json
{
  "suspendedBy": "string",
  "reason": "string",
  "suspendUntil": "string (可選，ISO日期格式)"
}
```

**響應:**
```json
{
  "supplierId": "string",
  "status": "string",
  "suspendedAt": "string",
  "suspendedBy": "string",
  "suspendedUntil": "string"
}
```

**狀態碼:**
- `200 OK` - 暫停成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 供應商不存在

## 供應商品項 API

### 1. 新增供應商品項

**端點:** `POST /suppliers/{supplierId}/items`

**說明:** 為供應商添加可供應的品項

**路徑參數:**
- `supplierId` - 供應商唯一標識符

**請求體:**
```json
{
  "items": [
    {
      "supplierItemCode": "string",
      "name": "string",
      "description": "string",
      "category": "string",
      "subCategory": "string",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
      "minimumOrderQuantity": "number",
      "leadTimeInDays": "number"
    }
  ]
}
```

**響應:**
```json
{
  "supplierId": "string",
  "itemsAdded": "number",
  "items": [
    {
      "supplierItemId": "string",
      "supplierItemCode": "string",
      "status": "string"
    }
  ]
}
```

**狀態碼:**
- `201 Created` - 品項創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 供應商不存在

### 2. 查詢供應商品項

**端點:** `GET /suppliers/{supplierId}/items`

**說明:** 獲取特定供應商的所有品項

**路徑參數:**
- `supplierId` - 供應商唯一標識符

**查詢參數:**
- `category` (可選) - 按類別過濾
- `status` (可選) - 按狀態過濾 (ACTIVE, INACTIVE)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "supplierId": "string",
  "items": [
    {
      "supplierItemId": "string",
      "supplierItemCode": "string",
      "name": "string",
      "description": "string",
      "category": "string",
      "subCategory": "string",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
      "minimumOrderQuantity": "number",
      "leadTimeInDays": "number",
      "status": "string"
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
- `404 Not Found` - 供應商不存在

### 3. 更新供應商品項

**端點:** `PUT /suppliers/{supplierId}/items/{supplierItemId}`

**說明:** 更新供應商特定品項的信息

**路徑參數:**
- `supplierId` - 供應商唯一標識符
- `supplierItemId` - 供應商品項唯一標識符

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "category": "string",
  "subCategory": "string",
  "unitPrice": {
    "value": "number",
    "currency": "string"
  },
  "minimumOrderQuantity": "number",
  "leadTimeInDays": "number",
  "status": "string"
}
```

**響應:**
```json
{
  "supplierItemId": "string",
  "name": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 供應商或品項不存在

## 採購訂單 API

### 1. 創建採購訂單

**端點:** `POST /purchase-orders`

**說明:** 創建新的採購訂單

**請求體:**
```json
{
  "supplierId": "string",
  "expectedDeliveryDate": "string",
  "shippingMethod": "string",
  "deliveryAddress": {
    "warehouseId": "string",
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "items": [
    {
      "supplierItemId": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      }
    }
  ],
  "notes": "string"
}
```

**響應:**
```json
{
  "purchaseOrderId": "string",
  "purchaseOrderNumber": "string",
  "supplierId": "string",
  "status": "string",
  "expectedDeliveryDate": "string",
  "shippingMethod": "string",
  "deliveryAddress": {
    "warehouseId": "string",
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "items": [
    {
      "lineItemId": "string",
      "supplierItemId": "string",
      "name": "string",
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
  "createdAt": "string",
  "createdBy": "string",
  "notes": "string"
}
```

**狀態碼:**
- `201 Created` - 採購訂單創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 供應商或品項不存在
- `422 Unprocessable Entity` - 採購訂單超出信用額度

### 2. 查詢採購訂單列表

**端點:** `GET /purchase-orders`

**說明:** 獲取符合條件的採購訂單列表

**查詢參數:**
- `supplierId` (可選) - 按供應商ID過濾
- `status` (可選) - 按狀態過濾 (DRAFT, SUBMITTED, CONFIRMED, RECEIVED, COMPLETED, CANCELLED)
- `fromDate` (可選) - 按創建日期範圍過濾（開始日期）
- `toDate` (可選) - 按創建日期範圍過濾（結束日期）
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "purchaseOrders": [
    {
      "purchaseOrderId": "string",
      "purchaseOrderNumber": "string",
      "supplierId": "string",
      "supplierName": "string",
      "status": "string",
      "total": {
        "value": "number",
        "currency": "string"
      },
      "createdAt": "string",
      "expectedDeliveryDate": "string"
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

### 3. 獲取採購訂單詳情

**端點:** `GET /purchase-orders/{purchaseOrderId}`

**說明:** 獲取特定採購訂單的詳細信息

**路徑參數:**
- `purchaseOrderId` - 採購訂單唯一標識符

**響應:**
```json
{
  "purchaseOrderId": "string",
  "purchaseOrderNumber": "string",
  "supplierId": "string",
  "supplierName": "string",
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
  "timeline": [
    {
      "status": "string",
      "timestamp": "string",
      "user": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 採購訂單不存在

### 4. 提交採購訂單

**端點:** `PUT /purchase-orders/{purchaseOrderId}/submit`

**說明:** 提交採購訂單給供應商

**路徑參數:**
- `purchaseOrderId` - 採購訂單唯一標識符

**請求體:**
```json
{
  "submittedBy": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "purchaseOrderId": "string",
  "status": "string",
  "submittedAt": "string",
  "submittedBy": "string"
}
```

**狀態碼:**
- `200 OK` - 提交成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 採購訂單不存在
- `409 Conflict` - 採購訂單不在草稿狀態

### 5. 確認採購訂單

**端點:** `PUT /purchase-orders/{purchaseOrderId}/confirm`

**說明:** 供應商確認採購訂單

**路徑參數:**
- `purchaseOrderId` - 採購訂單唯一標識符

**請求體:**
```json
{
  "supplierReference": "string",
  "confirmedDeliveryDate": "string",
  "notes": "string",
  "items": [
    {
      "lineItemId": "string",
      "confirmedQuantity": "number"
    }
  ]
}
```

**響應:**
```json
{
  "purchaseOrderId": "string",
  "status": "string",
  "supplierReference": "string",
  "confirmedDeliveryDate": "string",
  "confirmedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 確認成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 採購訂單不存在
- `409 Conflict` - 採購訂單不在已提交狀態

### 6. 接收採購訂單商品

**端點:** `POST /purchase-orders/{purchaseOrderId}/receive`

**說明:** 記錄採購訂單商品的接收情況

**路徑參數:**
- `purchaseOrderId` - 採購訂單唯一標識符

**請求體:**
```json
{
  "receivedBy": "string",
  "receivedAt": "string",
  "warehouseId": "string",
  "deliveryNoteNumber": "string",
  "items": [
    {
      "lineItemId": "string",
      "receivedQuantity": "number",
      "locationCode": "string",
      "condition": "string"
    }
  ],
  "notes": "string"
}
```

**響應:**
```json
{
  "receiptId": "string",
  "purchaseOrderId": "string",
  "status": "string",
  "receivedBy": "string",
  "receivedAt": "string",
  "warehouseId": "string",
  "items": [
    {
      "lineItemId": "string",
      "receivedQuantity": "number",
      "locationCode": "string"
    }
  ],
  "inventoryUpdated": "boolean"
}
```

**狀態碼:**
- `200 OK` - 接收記錄成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 採購訂單不存在
- `409 Conflict` - 採購訂單不在已確認狀態

### 7. 取消採購訂單

**端點:** `PUT /purchase-orders/{purchaseOrderId}/cancel`

**說明:** 取消未完成的採購訂單

**路徑參數:**
- `purchaseOrderId` - 採購訂單唯一標識符

**請求體:**
```json
{
  "cancelledBy": "string",
  "reason": "string"
}
```

**響應:**
```json
{
  "purchaseOrderId": "string",
  "status": "string",
  "cancelledAt": "string",
  "cancelledBy": "string",
  "reason": "string"
}
```

**狀態碼:**
- `200 OK` - 取消成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 採購訂單不存在
- `409 Conflict` - 採購訂單狀態不允許取消

## 模型定義

### 供應商狀態 (SupplierStatus)
- `PENDING_APPROVAL` - 等待審核
- `APPROVED` - 已審核通過
- `SUSPENDED` - 已暫停

### 風險等級 (RiskLevel)
- `LOW` - 低風險
- `MEDIUM` - 中風險
- `HIGH` - 高風險

### 付款條款 (PaymentTerms)
- `PREPAID` - 預付款
- `COD` - 貨到付款
- `NET_15` - 15天內付款
- `NET_30` - 30天內付款
- `NET_60` - 60天內付款

### 供應商品項狀態 (SupplierItemStatus)
- `ACTIVE` - 活躍
- `INACTIVE` - 不活躍

### 採購訂單狀態 (PurchaseOrderStatus)
- `DRAFT` - 草稿
- `SUBMITTED` - 已提交
- `CONFIRMED` - 已確認
- `PARTIALLY_RECEIVED` - 部分接收
- `RECEIVED` - 已接收
- `COMPLETED` - 已完成
- `CANCELLED` - 已取消

### 運輸方式 (ShippingMethod)
- `STANDARD` - 標準
- `EXPRESS` - 快遞
- `PICKUP` - 自取

### 商品狀況 (ItemCondition)
- `GOOD` - 良好
- `DAMAGED` - 損壞
- `MISSING` - 遺失

## 錯誤代碼

| 代碼                     | 說明                             |
|--------------------------|----------------------------------|
| SUPPLIER_NOT_FOUND       | 供應商不存在                     |
| SUPPLIER_ALREADY_EXISTS  | 供應商已存在                     |
| ITEM_NOT_FOUND           | 品項不存在                       |
| CREDIT_LIMIT_EXCEEDED    | 超出信用額度                     |
| INVALID_STATUS_TRANSITION| 無效的狀態轉換                   |
| PURCHASE_ORDER_NOT_FOUND | 採購訂單不存在                   |
| QUANTITY_MISMATCH        | 數量不符                         |
