# 物流微服務 API 文檔

## API 概述

物流微服務負責處理系統中的所有物流相關功能，包括配送管理、運輸追踪、倉儲管理、路線規劃等。該服務與訂單服務和庫存服務密切協作，確保商品準確、及時地送達客戶手中。

## API 基礎路徑

```
/api/logistics
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 配送管理 API

### 1. 創建配送訂單

**端點:** `POST /deliveries`

**說明:** 創建新的配送訂單

**請求體:**
```json
{
  "orderId": "string",
  "deliveryType": "string",
  "priority": "string",
  "pickupLocation": {
    "warehouseId": "string",
    "address": {
      "addressLine1": "string",
      "addressLine2": "string",
      "city": "string",
      "state": "string",
      "postalCode": "string",
      "country": "string",
      "contactName": "string",
      "phoneNumber": "string"
    }
  },
  "deliveryLocation": {
    "addressLine1": "string",
    "addressLine2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string",
    "contactName": "string",
    "phoneNumber": "string"
  },
  "items": [
    {
      "productId": "string",
      "quantity": "number",
      "weight": {
        "value": "number",
        "unit": "string"
      },
      "dimensions": {
        "length": "number",
        "width": "number",
        "height": "number",
        "unit": "string"
      }
    }
  ],
  "scheduledDate": "string",
  "timeWindow": {
    "start": "string",
    "end": "string"
  },
  "specialInstructions": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "deliveryId": "string",
  "orderId": "string",
  "status": "string",
  "trackingNumber": "string",
  "estimatedDeliveryDate": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 配送訂單創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單或倉庫不存在

### 2. 獲取配送訂單詳情

**端點:** `GET /deliveries/{deliveryId}`

**說明:** 獲取特定配送訂單的詳細信息

**路徑參數:**
- `deliveryId` - 配送訂單唯一標識符

**響應:**
```json
{
  "deliveryId": "string",
  "orderId": "string",
  "deliveryType": "string",
  "priority": "string",
  "status": "string",
  "trackingNumber": "string",
  "pickupLocation": {
    "warehouseId": "string",
    "warehouseName": "string",
    "address": {
      "addressLine1": "string",
      "addressLine2": "string",
      "city": "string",
      "state": "string",
      "postalCode": "string",
      "country": "string",
      "contactName": "string",
      "phoneNumber": "string"
    }
  },
  "deliveryLocation": {
    "addressLine1": "string",
    "addressLine2": "string",
    "city": "string",
    "state": "string",
    "postalCode": "string",
    "country": "string",
    "contactName": "string",
    "phoneNumber": "string"
  },
  "items": [
    {
      "productId": "string",
      "productName": "string",
      "quantity": "number",
      "weight": {
        "value": "number",
        "unit": "string"
      },
      "dimensions": {
        "length": "number",
        "width": "number",
        "height": "number",
        "unit": "string"
      }
    }
  ],
  "timeline": [
    {
      "status": "string",
      "location": "string",
      "timestamp": "string",
      "description": "string"
    }
  ],
  "scheduledDate": "string",
  "timeWindow": {
    "start": "string",
    "end": "string"
  },
  "estimatedDeliveryDate": "string",
  "actualDeliveryDate": "string",
  "carrier": {
    "id": "string",
    "name": "string",
    "trackingUrl": "string"
  },
  "driver": {
    "id": "string",
    "name": "string",
    "phoneNumber": "string",
    "vehicleNumber": "string"
  },
  "specialInstructions": "string",
  "signature": {
    "signedBy": "string",
    "signatureImage": "string",
    "timestamp": "string"
  },
  "issues": [
    {
      "type": "string",
      "description": "string",
      "reportedAt": "string",
      "status": "string",
      "resolution": "string"
    }
  ],
  "cost": {
    "amount": "number",
    "currency": "string",
    "breakdown": {
      "baseRate": "number",
      "fuel": "number",
      "handling": "number",
      "insurance": "number",
      "tax": "number"
    }
  },
  "createdAt": "string",
  "updatedAt": "string",
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 配送訂單不存在

### 3. 更新配送訂單

**端點:** `PUT /deliveries/{deliveryId}`

**說明:** 更新特定配送訂單的信息

**路徑參數:**
- `deliveryId` - 配送訂單唯一標識符

**請求體:**
```json
{
  "deliveryType": "string",
  "priority": "string",
  "scheduledDate": "string",
  "timeWindow": {
    "start": "string",
    "end": "string"
  },
  "specialInstructions": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "deliveryId": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 配送訂單不存在
- `409 Conflict` - 配送訂單狀態不允許更新

### 4. 取消配送訂單

**端點:** `DELETE /deliveries/{deliveryId}`

**說明:** 取消特定配送訂單

**路徑參數:**
- `deliveryId` - 配送訂單唯一標識符

**請求體:**
```json
{
  "reason": "string",
  "comments": "string"
}
```

**響應:**
```json
{
  "success": "boolean",
  "canceledAt": "string"
}
```

**狀態碼:**
- `200 OK` - 取消成功
- `404 Not Found` - 配送訂單不存在
- `409 Conflict` - 配送訂單狀態不允許取消

### 5. 查詢配送訂單列表

**端點:** `GET /deliveries`

**說明:** 獲取符合條件的配送訂單列表

**查詢參數:**
- `orderId` (可選) - 按訂單ID過濾
- `status` (可選) - 按狀態過濾
- `deliveryType` (可選) - 按配送類型過濾
- `priority` (可選) - 按優先級過濾
- `fromDate` (可選) - 按開始日期過濾
- `toDate` (可選) - 按結束日期過濾
- `carrierId` (可選) - 按承運商過濾
- `warehouseId` (可選) - 按倉庫過濾
- `sort` (可選, 默認為createdAt:desc) - 排序字段和方向
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "deliveries": [
    {
      "deliveryId": "string",
      "orderId": "string",
      "status": "string",
      "deliveryType": "string",
      "priority": "string",
      "trackingNumber": "string",
      "scheduledDate": "string",
      "estimatedDeliveryDate": "string",
      "carrier": {
        "id": "string",
        "name": "string"
      },
      "pickupLocation": {
        "warehouseId": "string",
        "warehouseName": "string"
      },
      "deliveryLocation": {
        "city": "string",
        "state": "string"
      },
      "createdAt": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "filters": {
    "status": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "deliveryType": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "priority": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "carriers": [
      {
        "id": "string",
        "name": "string",
        "count": "number"
      }
    ],
    "warehouses": [
      {
        "id": "string",
        "name": "string",
        "count": "number"
      }
    ]
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

## 運輸追踪 API

### 1. 更新配送狀態

**端點:** `POST /deliveries/{deliveryId}/status`

**說明:** 更新配送訂單的狀態和位置

**路徑參數:**
- `deliveryId` - 配送訂單唯一標識符

**請求體:**
```json
{
  "status": "string",
  "location": {
    "latitude": "number",
    "longitude": "number",
    "address": "string"
  },
  "description": "string",
  "issues": [
    {
      "type": "string",
      "description": "string"
    }
  ]
}
```

**響應:**
```json
{
  "deliveryId": "string",
  "status": "string",
  "timestamp": "string",
  "location": {
    "latitude": "number",
    "longitude": "number",
    "address": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 配送訂單不存在

### 2. 獲取運輸追踪信息

**端點:** `GET /deliveries/{deliveryId}/tracking`

**說明:** 獲取配送訂單的詳細追踪信息

**路徑參數:**
- `deliveryId` - 配送訂單唯一標識符

**響應:**
```json
{
  "deliveryId": "string",
  "trackingNumber": "string",
  "currentStatus": "string",
  "currentLocation": {
    "latitude": "number",
    "longitude": "number",
    "address": "string",
    "timestamp": "string"
  },
  "estimatedDeliveryDate": "string",
  "timeline": [
    {
      "status": "string",
      "location": {
        "latitude": "number",
        "longitude": "number",
        "address": "string"
      },
      "timestamp": "string",
      "description": "string"
    }
  ],
  "issues": [
    {
      "type": "string",
      "description": "string",
      "reportedAt": "string",
      "status": "string",
      "resolution": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 配送訂單不存在

### 3. 確認送達

**端點:** `POST /deliveries/{deliveryId}/confirmation`

**說明:** 確認配送訂單已送達

**路徑參數:**
- `deliveryId` - 配送訂單唯一標識符

**請求體:**
```json
{
  "signedBy": "string",
  "signatureImage": "string",
  "notes": "string",
  "photos": ["string"],
  "actualDeliveryDate": "string"
}
```

**響應:**
```json
{
  "deliveryId": "string",
  "status": "string",
  "confirmedAt": "string",
  "signedBy": "string"
}
```

**狀態碼:**
- `200 OK` - 確認成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 配送訂單不存在
- `409 Conflict` - 配送訂單狀態不允許確認送達

## 路線規劃 API

### 1. 獲取最佳配送路線

**端點:** `POST /routes/optimize`

**說明:** 為一組配送訂單生成最佳配送路線

**請求體:**
```json
{
  "deliveryIds": ["string"],
  "startLocation": {
    "latitude": "number",
    "longitude": "number",
    "address": "string"
  },
  "endLocation": {
    "latitude": "number",
    "longitude": "number",
    "address": "string"
  },
  "constraints": {
    "maxDistance": "number",
    "maxDuration": "number",
    "vehicleCapacity": "number",
    "timeWindows": "boolean"
  },
  "preferences": {
    "priorityFirst": "boolean",
    "minimizeDistance": "boolean",
    "minimizeTime": "boolean"
  }
}
```

**響應:**
```json
{
  "routeId": "string",
  "deliveries": [
    {
      "deliveryId": "string",
      "sequence": "number",
      "estimatedArrivalTime": "string",
      "location": {
        "latitude": "number",
        "longitude": "number",
        "address": "string"
      }
    }
  ],
  "summary": {
    "totalDistance": "number",
    "totalDuration": "number",
    "totalStops": "number"
  },
  "directions": [
    {
      "sequence": "number",
      "instruction": "string",
      "distance": "number",
      "duration": "number",
      "startLocation": {
        "latitude": "number",
        "longitude": "number"
      },
      "endLocation": {
        "latitude": "number",
        "longitude": "number"
      }
    }
  ],
  "map": {
    "centerLatitude": "number",
    "centerLongitude": "number",
    "zoomLevel": "number",
    "polyline": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 路線生成成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 配送訂單不存在

### 2. 更新路線

**端點:** `PUT /routes/{routeId}`

**說明:** 更新現有配送路線

**路徑參數:**
- `routeId` - 路線唯一標識符

**請求體:**
```json
{
  "deliveryIds": ["string"],
  "constraints": {
    "maxDistance": "number",
    "maxDuration": "number",
    "vehicleCapacity": "number",
    "timeWindows": "boolean"
  },
  "preferences": {
    "priorityFirst": "boolean",
    "minimizeDistance": "boolean",
    "minimizeTime": "boolean"
  }
}
```

**響應:**
```json
{
  "routeId": "string",
  "updated": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 路線更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 路線不存在

## 承運商管理 API

### 1. 創建承運商

**端點:** `POST /carriers`

**說明:** 創建新的承運商

**請求體:**
```json
{
  "name": "string",
  "code": "string",
  "type": "string",
  "contactInfo": {
    "name": "string",
    "email": "string",
    "phone": "string",
    "address": {
      "addressLine1": "string",
      "addressLine2": "string",
      "city": "string",
      "state": "string",
      "postalCode": "string",
      "country": "string"
    }
  },
  "services": ["string"],
  "coverage": {
    "countries": ["string"],
    "regions": ["string"]
  },
  "capabilities": {
    "tracking": "boolean",
    "signature": "boolean",
    "insurance": "boolean",
    "cod": "boolean"
  },
  "rateCard": {
    "currency": "string",
    "baseRate": "number",
    "weightRates": [
      {
        "minWeight": "number",
        "maxWeight": "number",
        "rate": "number"
      }
    ],
    "distanceRates": [
      {
        "minDistance": "number",
        "maxDistance": "number",
        "rate": "number"
      }
    ],
    "surcharges": [
      {
        "type": "string",
        "amount": "number",
        "description": "string"
      }
    ]
  },
  "status": "string"
}
```

**響應:**
```json
{
  "carrierId": "string",
  "name": "string",
  "code": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 承運商創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 承運商代碼已存在

### 2. 獲取承運商詳情

**端點:** `GET /carriers/{carrierId}`

**說明:** 獲取特定承運商的詳細信息

**路徑參數:**
- `carrierId` - 承運商唯一標識符

**響應:**
```json
{
  "carrierId": "string",
  "name": "string",
  "code": "string",
  "type": "string",
  "contactInfo": {
    "name": "string",
    "email": "string",
    "phone": "string",
    "address": {
      "addressLine1": "string",
      "addressLine2": "string",
      "city": "string",
      "state": "string",
      "postalCode": "string",
      "country": "string"
    }
  },
  "services": ["string"],
  "coverage": {
    "countries": ["string"],
    "regions": ["string"]
  },
  "capabilities": {
    "tracking": "boolean",
    "signature": "boolean",
    "insurance": "boolean",
    "cod": "boolean"
  },
  "rateCard": {
    "currency": "string",
    "baseRate": "number",
    "weightRates": [
      {
        "minWeight": "number",
        "maxWeight": "number",
        "rate": "number"
      }
    ],
    "distanceRates": [
      {
        "minDistance": "number",
        "maxDistance": "number",
        "rate": "number"
      }
    ],
    "surcharges": [
      {
        "type": "string",
        "amount": "number",
        "description": "string"
      }
    ]
  },
  "performance": {
    "deliverySuccessRate": "number",
    "onTimeDeliveryRate": "number",
    "averageDeliveryTime": "number",
    "claimRate": "number",
    "rating": "number"
  },
  "status": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 承運商不存在

## 模型定義

### 配送狀態 (DeliveryStatus)
- `PENDING` - 待處理
- `CONFIRMED` - 已確認
- `PICKING` - 揀貨中
- `PICKED` - 已揀貨
- `IN_TRANSIT` - 運輸中
- `OUT_FOR_DELIVERY` - 派送中
- `DELIVERED` - 已送達
- `FAILED` - 配送失敗
- `CANCELLED` - 已取消

### 配送類型 (DeliveryType)
- `STANDARD` - 標準配送
- `EXPRESS` - 快速配送
- `SAME_DAY` - 當日配送
- `NEXT_DAY` - 次日配送
- `SCHEDULED` - 預約配送

### 優先級 (Priority)
- `LOW` - 低優先級
- `NORMAL` - 正常優先級
- `HIGH` - 高優先級
- `URGENT` - 緊急

### 承運商類型 (CarrierType)
- `LOCAL` - 本地承運商
- `NATIONAL` - 全國承運商
- `INTERNATIONAL` - 國際承運商
- `SPECIALIZED` - 專業承運商

### 承運商狀態 (CarrierStatus)
- `ACTIVE` - 活躍
- `INACTIVE` - 不活躍
- `SUSPENDED` - 已暫停
- `TERMINATED` - 已終止

### 配送問題類型 (DeliveryIssueType)
- `ADDRESS_NOT_FOUND` - 地址未找到
- `RECIPIENT_UNAVAILABLE` - 收件人不在
- `WEATHER_DELAY` - 天氣延誤
- `VEHICLE_BREAKDOWN` - 車輛故障
- `PACKAGE_DAMAGED` - 包裹損壞
- `ACCESS_PROBLEM` - 通行問題

## 錯誤代碼

| 代碼                          | 說明                                |
|------------------------------|-------------------------------------|
| DELIVERY_NOT_FOUND           | 配送訂單不存在                       |
| CARRIER_NOT_FOUND            | 承運商不存在                         |
| ROUTE_NOT_FOUND              | 配送路線不存在                       |
| INVALID_DELIVERY_STATUS      | 無效的配送狀態                       |
| INVALID_DELIVERY_TYPE        | 無效的配送類型                       |
| INVALID_PRIORITY             | 無效的優先級                         |
| INVALID_CARRIER_TYPE         | 無效的承運商類型                     |
| DUPLICATE_TRACKING_NUMBER    | 追踪號碼重複                         |
| DELIVERY_ALREADY_CONFIRMED   | 配送已確認                           |
| DELIVERY_ALREADY_CANCELLED   | 配送已取消                           |
| INVALID_LOCATION             | 無效的位置信息                       |
| ROUTE_OPTIMIZATION_FAILED    | 路線優化失敗                         |
| MAX_DELIVERIES_EXCEEDED      | 超過最大配送數量                     |
| INVALID_TIME_WINDOW          | 無效的時間窗口                       |
| CARRIER_COVERAGE_NOT_FOUND   | 承運商覆蓋範圍不存在                 |
