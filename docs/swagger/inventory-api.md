# 庫存微服務 API 文檔

## API 概述

庫存微服務負責處理系統中的所有庫存相關功能，包括庫存追蹤、庫存更新、倉庫管理和庫存報表等。該微服務與訂單、產品和供應商微服務協作，確保庫存資訊的準確性和即時性。

## API 基礎路徑

```
/api/inventory
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 庫存管理 API

### 1. 獲取產品庫存

**端點:** `GET /products/{productId}`

**說明:** 獲取特定產品的庫存信息

**路徑參數:**
- `productId` - 產品唯一標識符

**響應:**
```json
{
  "productId": "string",
  "sku": "string",
  "name": "string",
  "totalQuantity": "number",
  "availableQuantity": "number",
  "reservedQuantity": "number",
  "reorderLevel": "number",
  "reorderQuantity": "number",
  "status": "string",
  "lastUpdated": "string",
  "locations": [
    {
      "locationId": "string",
      "locationName": "string",
      "quantity": "number",
      "zone": "string",
      "bin": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 產品不存在

### 2. 獲取多個產品庫存

**端點:** `POST /products/batch`

**說明:** 批量獲取多個產品的庫存信息

**請求體:**
```json
{
  "productIds": ["string"]
}
```

**響應:**
```json
{
  "products": [
    {
      "productId": "string",
      "sku": "string",
      "name": "string",
      "totalQuantity": "number",
      "availableQuantity": "number",
      "status": "string"
    }
  ],
  "timestamp": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求資料無效

### 3. 更新產品庫存

**端點:** `PUT /products/{productId}`

**說明:** 更新特定產品的庫存信息

**路徑參數:**
- `productId` - 產品唯一標識符

**請求體:**
```json
{
  "adjustmentQuantity": "number",
  "adjustmentType": "string",
  "reason": "string",
  "reference": "string",
  "locationId": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "productId": "string",
  "sku": "string",
  "previousQuantity": "number",
  "adjustmentQuantity": "number",
  "currentQuantity": "number",
  "adjustmentType": "string",
  "locationId": "string",
  "transactionId": "string",
  "timestamp": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在

### 4. 批量更新產品庫存

**端點:** `PUT /products/batch`

**說明:** 批量更新多個產品的庫存信息

**請求體:**
```json
{
  "adjustments": [
    {
      "productId": "string",
      "adjustmentQuantity": "number",
      "adjustmentType": "string",
      "locationId": "string"
    }
  ],
  "reason": "string",
  "reference": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "batchId": "string",
  "successful": "number",
  "failed": "number",
  "details": [
    {
      "productId": "string",
      "status": "string",
      "message": "string"
    }
  ],
  "timestamp": "string"
}
```

**狀態碼:**
- `200 OK` - 批量更新成功
- `400 Bad Request` - 請求資料無效

### 5. 預留產品庫存

**端點:** `POST /reservations`

**說明:** 為訂單預留產品庫存

**請求體:**
```json
{
  "orderId": "string",
  "items": [
    {
      "productId": "string",
      "quantity": "number"
    }
  ],
  "expirationMinutes": "number"
}
```

**響應:**
```json
{
  "reservationId": "string",
  "orderId": "string",
  "status": "string",
  "items": [
    {
      "productId": "string",
      "requested": "number",
      "reserved": "number",
      "status": "string"
    }
  ],
  "expiresAt": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 預留成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 庫存不足，無法預留

### 6. 獲取預留詳情

**端點:** `GET /reservations/{reservationId}`

**說明:** 獲取特定庫存預留的詳細信息

**路徑參數:**
- `reservationId` - 預留唯一標識符

**響應:**
```json
{
  "reservationId": "string",
  "orderId": "string",
  "status": "string",
  "items": [
    {
      "productId": "string",
      "sku": "string",
      "productName": "string",
      "quantity": "number",
      "locationId": "string",
      "locationName": "string"
    }
  ],
  "createdAt": "string",
  "updatedAt": "string",
  "expiresAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 預留不存在

### 7. 確認預留

**端點:** `PUT /reservations/{reservationId}/confirm`

**說明:** 確認預留，將庫存從預留狀態轉為已出貨

**路徑參數:**
- `reservationId` - 預留唯一標識符

**請求體:**
```json
{
  "items": [
    {
      "productId": "string",
      "quantity": "number"
    }
  ],
  "shipmentId": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "reservationId": "string",
  "status": "string",
  "confirmedAt": "string",
  "shipmentId": "string"
}
```

**狀態碼:**
- `200 OK` - 確認成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 預留不存在
- `409 Conflict` - 預留狀態不允許確認

### 8. 取消預留

**端點:** `PUT /reservations/{reservationId}/cancel`

**說明:** 取消預留，釋放預留的庫存

**路徑參數:**
- `reservationId` - 預留唯一標識符

**請求體:**
```json
{
  "reason": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "reservationId": "string",
  "status": "string",
  "cancelledAt": "string",
  "reason": "string"
}
```

**狀態碼:**
- `200 OK` - 取消成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 預留不存在
- `409 Conflict` - 預留狀態不允許取消

### 9. 查詢訂單預留

**端點:** `GET /reservations/order/{orderId}`

**說明:** 查詢特定訂單的庫存預留

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "orderId": "string",
  "reservations": [
    {
      "reservationId": "string",
      "status": "string",
      "items": [
        {
          "productId": "string",
          "quantity": "number",
          "status": "string"
        }
      ],
      "createdAt": "string",
      "expiresAt": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在或沒有預留

## 倉庫管理 API

### 1. 獲取倉庫列表

**端點:** `GET /locations`

**說明:** 獲取所有倉庫位置列表

**查詢參數:**
- `status` (可選) - 按狀態過濾 (ACTIVE, INACTIVE)
- `type` (可選) - 按類型過濾 (WAREHOUSE, STORE, SUPPLIER)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "locations": [
    {
      "locationId": "string",
      "name": "string",
      "type": "string",
      "status": "string",
      "address": {
        "line1": "string",
        "line2": "string",
        "city": "string",
        "state": "string",
        "postalCode": "string",
        "country": "string"
      },
      "contactInfo": {
        "phone": "string",
        "email": "string",
        "contactPerson": "string"
      },
      "productCount": "number"
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

### 2. 創建新倉庫

**端點:** `POST /locations`

**說明:** 創建新的倉庫位置

**請求體:**
```json
{
  "name": "string",
  "type": "string",
  "address": {
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string"
  },
  "contactInfo": {
    "phone": "string",
    "email": "string",
    "contactPerson": "string"
  },
  "isDefault": "boolean",
  "notes": "string"
}
```

**響應:**
```json
{
  "locationId": "string",
  "name": "string",
  "type": "string",
  "status": "string",
  "address": {
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string"
  },
  "contactInfo": {
    "phone": "string",
    "email": "string",
    "contactPerson": "string"
  },
  "createdAt": "string",
  "isDefault": "boolean"
}
```

**狀態碼:**
- `201 Created` - 倉庫創建成功
- `400 Bad Request` - 請求資料無效

### 3. 獲取倉庫詳情

**端點:** `GET /locations/{locationId}`

**說明:** 獲取特定倉庫位置的詳細信息

**路徑參數:**
- `locationId` - 倉庫唯一標識符

**響應:**
```json
{
  "locationId": "string",
  "name": "string",
  "type": "string",
  "status": "string",
  "address": {
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string",
    "coordinates": {
      "latitude": "number",
      "longitude": "number"
    }
  },
  "contactInfo": {
    "phone": "string",
    "email": "string",
    "contactPerson": "string"
  },
  "operatingHours": [
    {
      "day": "string",
      "openTime": "string",
      "closeTime": "string"
    }
  ],
  "capacity": {
    "totalCapacity": "number",
    "usedCapacity": "number",
    "availableCapacity": "number",
    "unit": "string"
  },
  "zones": [
    {
      "zoneId": "string",
      "name": "string",
      "type": "string"
    }
  ],
  "createdAt": "string",
  "updatedAt": "string",
  "isDefault": "boolean",
  "notes": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 倉庫不存在

### 4. 更新倉庫信息

**端點:** `PUT /locations/{locationId}`

**說明:** 更新特定倉庫位置的信息

**路徑參數:**
- `locationId` - 倉庫唯一標識符

**請求體:**
```json
{
  "name": "string",
  "status": "string",
  "address": {
    "line1": "string",
    "line2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string"
  },
  "contactInfo": {
    "phone": "string",
    "email": "string",
    "contactPerson": "string"
  },
  "operatingHours": [
    {
      "day": "string",
      "openTime": "string",
      "closeTime": "string"
    }
  ],
  "isDefault": "boolean",
  "notes": "string"
}
```

**響應:**
```json
{
  "locationId": "string",
  "name": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 倉庫不存在

### 5. 獲取倉庫產品庫存

**端點:** `GET /locations/{locationId}/products`

**說明:** 獲取特定倉庫中的所有產品庫存

**路徑參數:**
- `locationId` - 倉庫唯一標識符

**查詢參數:**
- `status` (可選) - 按庫存狀態過濾 (IN_STOCK, LOW_STOCK, OUT_OF_STOCK)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "locationId": "string",
  "locationName": "string",
  "products": [
    {
      "productId": "string",
      "sku": "string",
      "name": "string",
      "quantity": "number",
      "reorderLevel": "number",
      "status": "string",
      "zone": "string",
      "bin": "string"
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
- `404 Not Found` - 倉庫不存在

## 庫存移動 API

### 1. 創建庫存移動

**端點:** `POST /transfers`

**說明:** 創建產品庫存從一個位置到另一個位置的移動

**請求體:**
```json
{
  "sourceLocationId": "string",
  "destinationLocationId": "string",
  "items": [
    {
      "productId": "string",
      "quantity": "number"
    }
  ],
  "reference": "string",
  "scheduledDate": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "transferId": "string",
  "sourceLocationId": "string",
  "sourceLocationName": "string",
  "destinationLocationId": "string",
  "destinationLocationName": "string",
  "status": "string",
  "items": [
    {
      "productId": "string",
      "productName": "string",
      "quantity": "number"
    }
  ],
  "reference": "string",
  "scheduledDate": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 移動創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 位置或產品不存在
- `409 Conflict` - 庫存不足，無法移動

### 2. 獲取庫存移動詳情

**端點:** `GET /transfers/{transferId}`

**說明:** 獲取特定庫存移動的詳細信息

**路徑參數:**
- `transferId` - 移動唯一標識符

**響應:**
```json
{
  "transferId": "string",
  "sourceLocationId": "string",
  "sourceLocationName": "string",
  "destinationLocationId": "string",
  "destinationLocationName": "string",
  "status": "string",
  "items": [
    {
      "productId": "string",
      "sku": "string",
      "productName": "string",
      "quantity": "number",
      "status": "string"
    }
  ],
  "reference": "string",
  "scheduledDate": "string",
  "dispatchedDate": "string",
  "receivedDate": "string",
  "createdBy": {
    "id": "string",
    "name": "string"
  },
  "createdAt": "string",
  "updatedAt": "string",
  "notes": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 移動不存在

### 3. 更新庫存移動狀態

**端點:** `PUT /transfers/{transferId}/status`

**說明:** 更新特定庫存移動的狀態

**路徑參數:**
- `transferId` - 移動唯一標識符

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
  "transferId": "string",
  "status": "string",
  "previousStatus": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效或狀態更新不合法
- `404 Not Found` - 移動不存在

### 4. 確認庫存移動發貨

**端點:** `PUT /transfers/{transferId}/dispatch`

**說明:** 確認庫存移動已從源位置發貨

**路徑參數:**
- `transferId` - 移動唯一標識符

**請求體:**
```json
{
  "dispatchedDate": "string",
  "dispatchedBy": "string",
  "carrierInfo": {
    "carrier": "string",
    "trackingNumber": "string"
  },
  "notes": "string"
}
```

**響應:**
```json
{
  "transferId": "string",
  "status": "string",
  "dispatchedDate": "string",
  "dispatchedBy": {
    "id": "string",
    "name": "string"
  },
  "carrierInfo": {
    "carrier": "string",
    "trackingNumber": "string",
    "trackingUrl": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 確認發貨成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 移動不存在
- `409 Conflict` - 移動狀態不允許確認發貨

### 5. 確認庫存移動接收

**端點:** `PUT /transfers/{transferId}/receive`

**說明:** 確認庫存移動已在目標位置接收

**路徑參數:**
- `transferId` - 移動唯一標識符

**請求體:**
```json
{
  "receivedDate": "string",
  "receivedBy": "string",
  "receivedItems": [
    {
      "productId": "string",
      "quantity": "number",
      "condition": "string",
      "notes": "string"
    }
  ],
  "notes": "string"
}
```

**響應:**
```json
{
  "transferId": "string",
  "status": "string",
  "receivedDate": "string",
  "receivedBy": {
    "id": "string",
    "name": "string"
  },
  "discrepancies": [
    {
      "productId": "string",
      "productName": "string",
      "expected": "number",
      "received": "number",
      "condition": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 確認接收成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 移動不存在
- `409 Conflict` - 移動狀態不允許確認接收

## 盤點管理 API

### 1. 創建盤點

**端點:** `POST /stocktakes`

**說明:** 創建新的庫存盤點任務

**請求體:**
```json
{
  "locationId": "string",
  "name": "string",
  "scheduledDate": "string",
  "products": [
    {
      "productId": "string"
    }
  ],
  "zones": [
    "string"
  ],
  "assignedTo": [
    {
      "userId": "string"
    }
  ],
  "notes": "string"
}
```

**響應:**
```json
{
  "stocktakeId": "string",
  "name": "string",
  "locationId": "string",
  "locationName": "string",
  "status": "string",
  "scheduledDate": "string",
  "productCount": "number",
  "createdAt": "string",
  "createdBy": {
    "id": "string",
    "name": "string"
  }
}
```

**狀態碼:**
- `201 Created` - 盤點創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 位置或產品不存在

### 2. 獲取盤點詳情

**端點:** `GET /stocktakes/{stocktakeId}`

**說明:** 獲取特定庫存盤點的詳細信息

**路徑參數:**
- `stocktakeId` - 盤點唯一標識符

**響應:**
```json
{
  "stocktakeId": "string",
  "name": "string",
  "locationId": "string",
  "locationName": "string",
  "status": "string",
  "scheduledDate": "string",
  "startedDate": "string",
  "completedDate": "string",
  "products": [
    {
      "productId": "string",
      "sku": "string",
      "name": "string",
      "expectedQuantity": "number",
      "countedQuantity": "number",
      "discrepancy": "number",
      "countedBy": "string",
      "countedAt": "string",
      "status": "string",
      "notes": "string"
    }
  ],
  "zones": [
    "string"
  ],
  "assignedTo": [
    {
      "userId": "string",
      "name": "string"
    }
  ],
  "summary": {
    "totalProducts": "number",
    "countedProducts": "number",
    "productsWithDiscrepancies": "number",
    "totalDiscrepancy": "number",
    "totalValueImpact": {
      "value": "number",
      "currency": "string"
    }
  },
  "createdAt": "string",
  "createdBy": {
    "id": "string",
    "name": "string"
  },
  "notes": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 盤點不存在

### 3. 更新盤點狀態

**端點:** `PUT /stocktakes/{stocktakeId}/status`

**說明:** 更新特定盤點的狀態

**路徑參數:**
- `stocktakeId` - 盤點唯一標識符

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
  "stocktakeId": "string",
  "status": "string",
  "previousStatus": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效或狀態更新不合法
- `404 Not Found` - 盤點不存在

### 4. 提交產品盤點結果

**端點:** `PUT /stocktakes/{stocktakeId}/count`

**說明:** 提交特定盤點的產品盤點結果

**路徑參數:**
- `stocktakeId` - 盤點唯一標識符

**請求體:**
```json
{
  "counts": [
    {
      "productId": "string",
      "countedQuantity": "number",
      "notes": "string"
    }
  ],
  "countedBy": "string"
}
```

**響應:**
```json
{
  "stocktakeId": "string",
  "status": "string",
  "counts": [
    {
      "productId": "string",
      "productName": "string",
      "expectedQuantity": "number",
      "countedQuantity": "number",
      "discrepancy": "number",
      "status": "string"
    }
  ],
  "countedBy": {
    "id": "string",
    "name": "string"
  },
  "countedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 提交成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 盤點或產品不存在
- `409 Conflict` - 盤點狀態不允許提交結果

### 5. 確認盤點完成

**端點:** `PUT /stocktakes/{stocktakeId}/complete`

**說明:** 確認特定盤點已完成，並應用庫存調整

**路徑參數:**
- `stocktakeId` - 盤點唯一標識符

**請求體:**
```json
{
  "adjustments": [
    {
      "productId": "string",
      "adjustQuantity": "boolean"
    }
  ],
  "notes": "string"
}
```

**響應:**
```json
{
  "stocktakeId": "string",
  "status": "string",
  "completedAt": "string",
  "completedBy": {
    "id": "string",
    "name": "string"
  },
  "adjustmentSummary": {
    "productsAdjusted": "number",
    "totalAdjustments": "number",
    "adjustmentReference": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 確認完成成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 盤點不存在
- `409 Conflict` - 盤點狀態不允許確認完成

## 報表與統計 API

### 1. 獲取庫存摘要

**端點:** `GET /reports/summary`

**說明:** 獲取系統庫存的摘要信息

**查詢參數:**
- `locationId` (可選) - 按倉庫位置過濾
- `categoryId` (可選) - 按產品類別過濾

**響應:**
```json
{
  "summary": {
    "totalProducts": "number",
    "totalQuantity": "number",
    "totalValue": {
      "value": "number",
      "currency": "string"
    },
    "inStockProducts": "number",
    "lowStockProducts": "number",
    "outOfStockProducts": "number",
    "reservedQuantity": "number"
  },
  "locationBreakdown": [
    {
      "locationId": "string",
      "locationName": "string",
      "productCount": "number",
      "totalQuantity": "number",
      "totalValue": {
        "value": "number",
        "currency": "string"
      }
    }
  ],
  "categoryBreakdown": [
    {
      "categoryId": "string",
      "categoryName": "string",
      "productCount": "number",
      "totalQuantity": "number",
      "totalValue": {
        "value": "number",
        "currency": "string"
      }
    }
  ],
  "timestamp": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 2. 庫存變動報表

**端點:** `GET /reports/movements`

**說明:** 獲取庫存變動的報表

**查詢參數:**
- `fromDate` (可選) - 按變動日期範圍過濾（開始日期）
- `toDate` (可選) - 按變動日期範圍過濾（結束日期）
- `productId` (可選) - 按產品過濾
- `locationId` (可選) - 按倉庫位置過濾
- `type` (可選) - 按變動類型過濾 (INBOUND, OUTBOUND, TRANSFER, ADJUSTMENT, STOCKTAKE)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "movements": [
    {
      "movementId": "string",
      "date": "string",
      "productId": "string",
      "productName": "string",
      "locationId": "string",
      "locationName": "string",
      "type": "string",
      "quantityBefore": "number",
      "quantityChange": "number",
      "quantityAfter": "number",
      "reference": "string",
      "reason": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "totalMovements": "number",
    "inboundQuantity": "number",
    "outboundQuantity": "number",
    "netChange": "number",
    "typeBreakdown": {
      "INBOUND": "number",
      "OUTBOUND": "number",
      "TRANSFER": "number",
      "ADJUSTMENT": "number",
      "STOCKTAKE": "number"
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 3. 獲取低庫存產品

**端點:** `GET /reports/low-stock`

**說明:** 獲取低於再訂購水平的產品列表

**查詢參數:**
- `locationId` (可選) - 按倉庫位置過濾
- `categoryId` (可選) - 按產品類別過濾
- `threshold` (可選, 默認為0) - 自定義閾值百分比（覆蓋再訂購水平）
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "products": [
    {
      "productId": "string",
      "sku": "string",
      "name": "string",
      "currentQuantity": "number",
      "reorderLevel": "number",
      "reorderQuantity": "number",
      "deficit": "number",
      "daysOutOfStock": "number",
      "lastReorderDate": "string",
      "expectedRestockDate": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "totalLowStockProducts": "number",
    "totalOutOfStockProducts": "number",
    "averageDeficitPercentage": "number"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

## 庫存交易歷史 API

### 1. 獲取產品交易歷史

**端點:** `GET /transactions/product/{productId}`

**說明:** 獲取特定產品的庫存交易歷史

**路徑參數:**
- `productId` - 產品唯一標識符

**查詢參數:**
- `fromDate` (可選) - 按交易日期範圍過濾（開始日期）
- `toDate` (可選) - 按交易日期範圍過濾（結束日期）
- `type` (可選) - 按交易類型過濾
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "productId": "string",
  "productName": "string",
  "sku": "string",
  "transactions": [
    {
      "transactionId": "string",
      "date": "string",
      "type": "string",
      "quantity": "number",
      "locationId": "string",
      "locationName": "string",
      "reference": "string",
      "reason": "string",
      "performedBy": {
        "id": "string",
        "name": "string"
      }
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "openingBalance": "number",
    "inflow": "number",
    "outflow": "number",
    "currentBalance": "number"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 產品不存在

### 2. 獲取位置交易歷史

**端點:** `GET /transactions/location/{locationId}`

**說明:** 獲取特定倉庫位置的庫存交易歷史

**路徑參數:**
- `locationId` - 倉庫位置唯一標識符

**查詢參數:**
- `fromDate` (可選) - 按交易日期範圍過濾（開始日期）
- `toDate` (可選) - 按交易日期範圍過濾（結束日期）
- `type` (可選) - 按交易類型過濾
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "locationId": "string",
  "locationName": "string",
  "transactions": [
    {
      "transactionId": "string",
      "date": "string",
      "type": "string",
      "productId": "string",
      "productName": "string",
      "quantity": "number",
      "reference": "string",
      "reason": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "totalTransactions": "number",
    "inboundCount": "number",
    "outboundCount": "number",
    "adjustmentCount": "number"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 倉庫位置不存在

## 模型定義

### 庫存狀態 (InventoryStatus)
- `IN_STOCK` - 有庫存
- `LOW_STOCK` - 庫存不足
- `OUT_OF_STOCK` - 無庫存
- `DISCONTINUED` - 已停產
- `BACKORDERED` - 延期交貨

### 庫存調整類型 (AdjustmentType)
- `RECEIVE` - 收貨
- `SHIP` - 出貨
- `RETURN` - 退貨
- `DAMAGED` - 損壞
- `EXPIRED` - 過期
- `CORRECTION` - 校正
- `STOCKTAKE` - 盤點

### 預留狀態 (ReservationStatus)
- `PENDING` - 待處理
- `CONFIRMED` - 已確認
- `SHIPPED` - 已出貨
- `CANCELLED` - 已取消
- `EXPIRED` - 已過期

### 位置類型 (LocationType)
- `WAREHOUSE` - 倉庫
- `STORE` - 商店
- `SUPPLIER` - 供應商
- `CUSTOMER` - 客戶
- `TRANSIT` - 運輸中

### 位置狀態 (LocationStatus)
- `ACTIVE` - 活躍
- `INACTIVE` - 非活躍
- `MAINTENANCE` - 維護中
- `CLOSED` - 已關閉

### 移動狀態 (TransferStatus)
- `DRAFT` - 草稿
- `SCHEDULED` - 已排程
- `DISPATCHED` - 已發貨
- `IN_TRANSIT` - 運輸中
- `RECEIVED` - 已接收
- `COMPLETED` - 已完成
- `CANCELLED` - 已取消

### 盤點狀態 (StocktakeStatus)
- `PLANNED` - 已計劃
- `IN_PROGRESS` - 進行中
- `COMPLETED` - 已完成
- `CANCELLED` - 已取消

### 交易類型 (TransactionType)
- `INBOUND` - 入庫
- `OUTBOUND` - 出庫
- `TRANSFER` - 轉移
- `ADJUSTMENT` - 調整
- `STOCKTAKE` - 盤點
- `RESERVATION` - 預留
- `RELEASE` - 釋放

## 錯誤代碼

| 代碼                       | 說明                              |
|----------------------------|---------------------------------|
| PRODUCT_NOT_FOUND          | 產品不存在                         |
| LOCATION_NOT_FOUND         | 倉庫位置不存在                     |
| INSUFFICIENT_INVENTORY     | 庫存不足                          |
| RESERVATION_NOT_FOUND      | 預留不存在                         |
| TRANSFER_NOT_FOUND         | 移動不存在                         |
| STOCKTAKE_NOT_FOUND        | 盤點不存在                         |
| INVALID_ADJUSTMENT_TYPE    | 無效的調整類型                     |
| INVALID_RESERVATION_STATUS | 無效的預留狀態                     |
| INVALID_TRANSFER_STATUS    | 無效的移動狀態                     |
| INVALID_STOCKTAKE_STATUS   | 無效的盤點狀態                     |
| LOCATION_CONFLICT          | 位置衝突                          |
| RESERVATION_EXPIRED        | 預留已過期                         |
| DUPLICATE_STOCKTAKE        | 重複的盤點                         |
