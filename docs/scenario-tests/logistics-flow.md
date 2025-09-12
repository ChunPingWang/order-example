# 物流出貨流程情境測試

## 情境概述

本情境測試驗證從銷售訂單確認到商品完成配送的整個物流流程，包括倉庫備貨、揀貨、包裝、出貨、物流追蹤到最終送達的完整過程。

## 前置條件

1. 已有活躍的用戶帳號
2. 已有確認且已付款的銷售訂單
3. 倉庫中有足夠的商品庫存
4. 物流系統已設置好物流商資訊

## 測試步驟

### 1. 倉庫接收出貨通知

**請求：**
```http
POST /api/logistics/warehouses/{warehouseId}/shipment-notifications
Content-Type: application/json
Authorization: Bearer {token}

{
  "saleId": "SALE123456",
  "priority": "NORMAL",
  "items": [
    {
      "productId": "PROD001",
      "quantity": 2
    },
    {
      "productId": "PROD002",
      "quantity": 1
    }
  ],
  "shippingAddress": {
    "street": "台北市信義區信義路五段7號",
    "city": "台北市",
    "state": "台灣",
    "postalCode": "110",
    "country": "TW"
  }
}
```

**預期響應：**
```json
{
  "shipmentId": "SHIP789012",
  "status": "CREATED",
  "estimatedProcessingTime": "2 hours"
}
```

**驗證點：**
- 返回新創建的出貨單ID
- 狀態為CREATED
- 倉庫系統已記錄該出貨任務

### 2. 檢查商品庫存

**請求：**
```http
GET /api/logistics/warehouses/{warehouseId}/inventory-check
Content-Type: application/json
Authorization: Bearer {token}

{
  "items": [
    {
      "productId": "PROD001",
      "quantity": 2
    },
    {
      "productId": "PROD002",
      "quantity": 1
    }
  ]
}
```

**預期響應：**
```json
{
  "allItemsAvailable": true,
  "items": [
    {
      "productId": "PROD001",
      "available": true,
      "availableQuantity": 15
    },
    {
      "productId": "PROD002",
      "available": true,
      "availableQuantity": 8
    }
  ]
}
```

**驗證點：**
- 所有商品都有足夠庫存
- 返回每個商品的可用庫存數量

### 3. 生成揀貨單並分配揀貨任務

**請求：**
```http
POST /api/logistics/warehouses/{warehouseId}/picking-tasks
Content-Type: application/json
Authorization: Bearer {token}

{
  "shipmentId": "SHIP789012"
}
```

**預期響應：**
```json
{
  "pickingTaskId": "PICK345678",
  "assignedTo": "WORKER001",
  "status": "ASSIGNED",
  "items": [
    {
      "productId": "PROD001",
      "quantity": 2,
      "location": "ZONE-A-12-3"
    },
    {
      "productId": "PROD002",
      "quantity": 1,
      "location": "ZONE-B-08-5"
    }
  ]
}
```

**驗證點：**
- 揀貨任務已被分配
- 每個商品都有指定的倉庫位置
- 揀貨單狀態為ASSIGNED

### 4. 完成揀貨並更新庫存

**請求：**
```http
PUT /api/logistics/warehouses/{warehouseId}/picking-tasks/{pickingTaskId}
Content-Type: application/json
Authorization: Bearer {token}

{
  "status": "COMPLETED",
  "completedBy": "WORKER001",
  "items": [
    {
      "productId": "PROD001",
      "pickedQuantity": 2
    },
    {
      "productId": "PROD002",
      "pickedQuantity": 1
    }
  ]
}
```

**預期響應：**
```json
{
  "pickingTaskId": "PICK345678",
  "status": "COMPLETED",
  "completedAt": "2025-09-13T10:15:30Z"
}
```

**驗證點：**
- 揀貨任務狀態更新為COMPLETED
- 庫存數量已相應減少

### 5. 包裝出貨並分配物流商

**請求：**
```http
POST /api/logistics/shipments/{shipmentId}/package
Content-Type: application/json
Authorization: Bearer {token}

{
  "packages": [
    {
      "packageId": "PKG456789",
      "weight": {
        "value": 1.5,
        "unit": "KG"
      },
      "dimensions": {
        "length": 30,
        "width": 20,
        "height": 10,
        "unit": "CM"
      },
      "items": [
        {
          "productId": "PROD001",
          "quantity": 2
        },
        {
          "productId": "PROD002",
          "quantity": 1
        }
      ]
    }
  ]
}
```

**預期響應：**
```json
{
  "shipmentId": "SHIP789012",
  "status": "PACKAGED",
  "packages": [
    {
      "packageId": "PKG456789",
      "status": "READY_FOR_PICKUP"
    }
  ]
}
```

**驗證點：**
- 出貨狀態更新為PACKAGED
- 包裹已準備好等待運送

### 6. 分配物流商並生成物流追蹤號

**請求：**
```http
POST /api/logistics/shipments/{shipmentId}/assign-carrier
Content-Type: application/json
Authorization: Bearer {token}

{
  "carrierId": "CARRIER001",
  "serviceLevel": "STANDARD"
}
```

**預期響應：**
```json
{
  "shipmentId": "SHIP789012",
  "carrierId": "CARRIER001",
  "carrierName": "台灣宅配通",
  "trackingNumber": "TW12345678901",
  "estimatedDelivery": "2025-09-15T17:00:00Z",
  "status": "READY_FOR_PICKUP"
}
```

**驗證點：**
- 已分配物流商
- 已生成追蹤號碼
- 狀態更新為READY_FOR_PICKUP

### 7. 物流商取件並更新狀態

**請求：**
```http
PUT /api/logistics/shipments/{shipmentId}/status
Content-Type: application/json
Authorization: Bearer {token}

{
  "status": "IN_TRANSIT",
  "carrierUpdateTime": "2025-09-13T14:30:00Z",
  "notes": "Picked up from warehouse"
}
```

**預期響應：**
```json
{
  "shipmentId": "SHIP789012",
  "status": "IN_TRANSIT",
  "lastUpdated": "2025-09-13T14:30:00Z",
  "currentLocation": "物流中心"
}
```

**驗證點：**
- 出貨狀態更新為IN_TRANSIT
- 銷售訂單狀態同步更新為SHIPPED

### 8. 更新配送進度

**請求：**
```http
PUT /api/logistics/shipments/{shipmentId}/tracking
Content-Type: application/json
Authorization: Bearer {token}

{
  "events": [
    {
      "status": "IN_TRANSIT",
      "location": "台北配送中心",
      "timestamp": "2025-09-14T08:15:00Z",
      "description": "包裹進入配送中心"
    },
    {
      "status": "OUT_FOR_DELIVERY",
      "location": "台北信義區",
      "timestamp": "2025-09-14T10:30:00Z",
      "description": "包裹出庫配送中"
    }
  ]
}
```

**預期響應：**
```json
{
  "shipmentId": "SHIP789012",
  "status": "OUT_FOR_DELIVERY",
  "lastUpdated": "2025-09-14T10:30:00Z",
  "currentLocation": "台北信義區"
}
```

**驗證點：**
- 物流狀態成功更新
- 客戶可以透過追蹤號查詢實時物流狀態

### 9. 確認配送完成

**請求：**
```http
PUT /api/logistics/shipments/{shipmentId}/status
Content-Type: application/json
Authorization: Bearer {token}

{
  "status": "DELIVERED",
  "carrierUpdateTime": "2025-09-14T15:45:00Z",
  "notes": "Delivered to recipient",
  "proof": {
    "signature": "base64encoded_signature",
    "receivedBy": "王小明",
    "photo": "base64encoded_photo"
  }
}
```

**預期響應：**
```json
{
  "shipmentId": "SHIP789012",
  "status": "DELIVERED",
  "deliveredAt": "2025-09-14T15:45:00Z",
  "receivedBy": "王小明"
}
```

**驗證點：**
- 物流狀態更新為DELIVERED
- 銷售訂單狀態同步更新為COMPLETED
- 送達時間與簽收人資訊已記錄

## 期望結果

1. 整個物流流程從倉庫備貨到客戶收貨能夠順利完成
2. 每一步驟狀態能夠正確更新
3. 庫存數量變更正確
4. 物流狀態能即時反映到銷售訂單狀態
5. 客戶能夠通過追蹤號查詢物流實時狀態

## 錯誤處理測試案例

### 1. 庫存不足

**請求：**
```http
POST /api/logistics/warehouses/{warehouseId}/shipment-notifications
Content-Type: application/json
Authorization: Bearer {token}

{
  "saleId": "SALE123457",
  "items": [
    {
      "productId": "PROD003",
      "quantity": 50
    }
  ],
  "shippingAddress": {...}
}
```

**預期響應：**
```json
{
  "error": "INSUFFICIENT_INVENTORY",
  "message": "Not enough inventory for product PROD003",
  "details": {
    "productId": "PROD003",
    "requested": 50,
    "available": 10
  },
  "statusCode": 400
}
```

### 2. 物流商服務區域不可達

**請求：**
```http
POST /api/logistics/shipments/{shipmentId}/assign-carrier
Content-Type: application/json
Authorization: Bearer {token}

{
  "carrierId": "CARRIER001",
  "serviceLevel": "STANDARD",
  "shippingAddress": {
    "country": "US",
    "state": "California",
    "city": "San Francisco",
    "street": "..."
  }
}
```

**預期響應：**
```json
{
  "error": "SERVICE_AREA_UNAVAILABLE",
  "message": "Selected carrier does not service this destination",
  "details": {
    "carrierId": "CARRIER001",
    "carrierName": "台灣宅配通",
    "country": "US",
    "alternatives": [
      {
        "carrierId": "CARRIER003",
        "carrierName": "國際快遞",
        "estimatedCost": 1200,
        "currency": "TWD",
        "estimatedDeliveryDays": 5
      }
    ]
  },
  "statusCode": 400
}
```
