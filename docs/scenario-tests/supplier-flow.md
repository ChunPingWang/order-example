# 供應商流程情境測試

## 情境概述

本情境測試驗證從供應商註冊、商品採購、到收貨入庫的完整流程，包括供應商管理、採購單處理、商品接收與庫存更新的整個過程。

## 前置條件

1. 系統中已有供應商分類設定
2. 品項分類與編碼系統已設置好
3. 已有庫存系統與倉儲位置設定

## 測試步驟

### 1. 註冊新供應商

**請求：**
```http
POST /api/supplier/suppliers
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "科技精品有限公司",
  "taxId": "12345678",
  "contact": {
    "name": "李經理",
    "phone": "0912345678",
    "email": "manager@techpremium.com"
  },
  "address": {
    "street": "科技五路88號",
    "city": "新竹市",
    "district": "東區",
    "postalCode": "300",
    "country": "Taiwan"
  },
  "paymentTerms": "NET_30",
  "categories": ["ELECTRONICS", "COMPUTER_ACCESSORIES"],
  "currency": "TWD"
}
```

**預期響應：**
```json
{
  "supplierId": "SUP123456",
  "name": "科技精品有限公司",
  "taxId": "12345678",
  "status": "PENDING_APPROVAL",
  "contact": {
    "name": "李經理",
    "phone": "0912345678",
    "email": "manager@techpremium.com"
  },
  "address": {
    "street": "科技五路88號",
    "city": "新竹市",
    "district": "東區",
    "postalCode": "300",
    "country": "Taiwan"
  },
  "paymentTerms": "NET_30",
  "categories": ["ELECTRONICS", "COMPUTER_ACCESSORIES"],
  "currency": "TWD",
  "createdAt": "2025-09-14T10:00:00Z"
}
```

**驗證點：**
- 供應商成功註冊，處於待審核狀態
- 返回唯一供應商ID
- 包含所有提交的資訊

### 2. 審核供應商資料

**請求：**
```http
PUT /api/supplier/suppliers/SUP123456/approve
Content-Type: application/json
Authorization: Bearer {token}

{
  "approvedBy": "USER_ADMIN_001",
  "notes": "供應商資料齊全，評估為A級供應商",
  "riskLevel": "LOW",
  "creditLimit": {
    "value": 1000000,
    "currency": "TWD"
  }
}
```

**預期響應：**
```json
{
  "supplierId": "SUP123456",
  "status": "APPROVED",
  "approvedAt": "2025-09-14T11:30:00Z",
  "approvedBy": "USER_ADMIN_001",
  "riskLevel": "LOW",
  "creditLimit": {
    "value": 1000000,
    "currency": "TWD"
  }
}
```

**驗證點：**
- 供應商狀態更新為已審核
- 包含審核人員與時間
- 設定了適當的風險等級與信用額度

### 3. 查詢供應商詳細資料

**請求：**
```http
GET /api/supplier/suppliers/SUP123456
Content-Type: application/json
Authorization: Bearer {token}
```

**預期響應：**
```json
{
  "supplierId": "SUP123456",
  "name": "科技精品有限公司",
  "taxId": "12345678",
  "status": "APPROVED",
  "contact": {
    "name": "李經理",
    "phone": "0912345678",
    "email": "manager@techpremium.com"
  },
  "address": {
    "street": "科技五路88號",
    "city": "新竹市",
    "district": "東區",
    "postalCode": "300",
    "country": "Taiwan"
  },
  "paymentTerms": "NET_30",
  "categories": ["ELECTRONICS", "COMPUTER_ACCESSORIES"],
  "currency": "TWD",
  "riskLevel": "LOW",
  "creditLimit": {
    "value": 1000000,
    "currency": "TWD"
  },
  "createdAt": "2025-09-14T10:00:00Z",
  "approvedAt": "2025-09-14T11:30:00Z",
  "approvedBy": "USER_ADMIN_001",
  "performance": {
    "averageResponseTime": "N/A",
    "qualityRating": "N/A",
    "deliveryRating": "N/A"
  }
}
```

**驗證點：**
- 返回供應商完整資訊
- 包含審核狀態與績效資料

### 4. 新增供應商提供的品項

**請求：**
```http
POST /api/supplier/suppliers/SUP123456/items
Content-Type: application/json
Authorization: Bearer {token}

{
  "items": [
    {
      "supplierItemCode": "TP-LT-001",
      "name": "高效能筆記型電腦",
      "description": "Intel i9 處理器, 32GB RAM, 1TB SSD, 15.6吋螢幕",
      "category": "ELECTRONICS",
      "subCategory": "LAPTOPS",
      "unitPrice": {
        "value": 28000,
        "currency": "TWD"
      },
      "minimumOrderQuantity": 5,
      "leadTimeInDays": 14
    },
    {
      "supplierItemCode": "TP-MS-002",
      "name": "專業級無線滑鼠",
      "description": "高精準度雷射感應, 可程式化按鍵, 充電式",
      "category": "COMPUTER_ACCESSORIES",
      "subCategory": "MICE",
      "unitPrice": {
        "value": 1200,
        "currency": "TWD"
      },
      "minimumOrderQuantity": 10,
      "leadTimeInDays": 7
    }
  ]
}
```

**預期響應：**
```json
{
  "supplierId": "SUP123456",
  "itemsAdded": 2,
  "items": [
    {
      "supplierItemId": "SUI-12345601",
      "supplierItemCode": "TP-LT-001",
      "status": "ACTIVE"
    },
    {
      "supplierItemId": "SUI-12345602",
      "supplierItemCode": "TP-MS-002",
      "status": "ACTIVE"
    }
  ]
}
```

**驗證點：**
- 品項成功添加到供應商資料
- 每個品項都分配到唯一ID
- 品項狀態設為活躍

### 5. 創建採購訂單

**請求：**
```http
POST /api/supplier/purchase-orders
Content-Type: application/json
Authorization: Bearer {token}

{
  "supplierId": "SUP123456",
  "expectedDeliveryDate": "2025-10-01",
  "shippingMethod": "STANDARD",
  "deliveryAddress": {
    "warehouseId": "WH001",
    "street": "物流路100號",
    "city": "台北市",
    "district": "內湖區",
    "postalCode": "114",
    "country": "Taiwan"
  },
  "items": [
    {
      "supplierItemId": "SUI-12345601",
      "quantity": 10,
      "unitPrice": {
        "value": 28000,
        "currency": "TWD"
      }
    },
    {
      "supplierItemId": "SUI-12345602",
      "quantity": 20,
      "unitPrice": {
        "value": 1200,
        "currency": "TWD"
      }
    }
  ],
  "notes": "需要發票，送達前請先電話聯繫"
}
```

**預期響應：**
```json
{
  "purchaseOrderId": "PO987654",
  "purchaseOrderNumber": "PO-2025-001",
  "supplierId": "SUP123456",
  "status": "DRAFT",
  "expectedDeliveryDate": "2025-10-01",
  "shippingMethod": "STANDARD",
  "deliveryAddress": {
    "warehouseId": "WH001",
    "street": "物流路100號",
    "city": "台北市",
    "district": "內湖區",
    "postalCode": "114",
    "country": "Taiwan"
  },
  "items": [
    {
      "lineItemId": "LI001",
      "supplierItemId": "SUI-12345601",
      "name": "高效能筆記型電腦",
      "quantity": 10,
      "unitPrice": {
        "value": 28000,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 280000,
        "currency": "TWD"
      }
    },
    {
      "lineItemId": "LI002",
      "supplierItemId": "SUI-12345602",
      "name": "專業級無線滑鼠",
      "quantity": 20,
      "unitPrice": {
        "value": 1200,
        "currency": "TWD"
      },
      "subtotal": {
        "value": 24000,
        "currency": "TWD"
      }
    }
  ],
  "subtotal": {
    "value": 304000,
    "currency": "TWD"
  },
  "tax": {
    "value": 15200,
    "currency": "TWD"
  },
  "total": {
    "value": 319200,
    "currency": "TWD"
  },
  "createdAt": "2025-09-15T09:30:00Z",
  "createdBy": "USER_PURCHASE_001",
  "notes": "需要發票，送達前請先電話聯繫"
}
```

**驗證點：**
- 採購訂單成功創建，處於草稿狀態
- 包含所有品項與價格計算
- 顯示訂單總金額與稅金

### 6. 提交採購訂單

**請求：**
```http
PUT /api/supplier/purchase-orders/PO987654/submit
Content-Type: application/json
Authorization: Bearer {token}

{
  "submittedBy": "USER_PURCHASE_001",
  "notes": "緊急訂單，請優先處理"
}
```

**預期響應：**
```json
{
  "purchaseOrderId": "PO987654",
  "status": "SUBMITTED",
  "submittedAt": "2025-09-15T10:00:00Z",
  "submittedBy": "USER_PURCHASE_001"
}
```

**驗證點：**
- 採購訂單狀態更新為已提交
- 記錄提交時間與提交人員

### 7. 供應商確認訂單

**請求：**
```http
PUT /api/supplier/purchase-orders/PO987654/confirm
Content-Type: application/json
Authorization: Bearer {token}

{
  "supplierReference": "SO-2025-123",
  "confirmedDeliveryDate": "2025-09-30",
  "notes": "確認訂單並安排物流",
  "items": [
    {
      "lineItemId": "LI001",
      "confirmedQuantity": 10
    },
    {
      "lineItemId": "LI002",
      "confirmedQuantity": 20
    }
  ]
}
```

**預期響應：**
```json
{
  "purchaseOrderId": "PO987654",
  "status": "CONFIRMED",
  "supplierReference": "SO-2025-123",
  "confirmedDeliveryDate": "2025-09-30",
  "confirmedAt": "2025-09-15T14:00:00Z"
}
```

**驗證點：**
- 採購訂單狀態更新為已確認
- 包含供應商參考號
- 更新確認的交貨日期

### 8. 接收商品入庫

**請求：**
```http
POST /api/supplier/purchase-orders/PO987654/receive
Content-Type: application/json
Authorization: Bearer {token}

{
  "receivedBy": "USER_WAREHOUSE_001",
  "receivedAt": "2025-09-30T11:00:00Z",
  "warehouseId": "WH001",
  "deliveryNoteNumber": "DN-56789",
  "items": [
    {
      "lineItemId": "LI001",
      "receivedQuantity": 10,
      "locationCode": "ZONE-A-123",
      "condition": "GOOD"
    },
    {
      "lineItemId": "LI002",
      "receivedQuantity": 20,
      "locationCode": "ZONE-B-456",
      "condition": "GOOD"
    }
  ],
  "notes": "所有商品完好無損，品質良好"
}
```

**預期響應：**
```json
{
  "receiptId": "GR123456",
  "purchaseOrderId": "PO987654",
  "status": "RECEIVED",
  "receivedBy": "USER_WAREHOUSE_001",
  "receivedAt": "2025-09-30T11:00:00Z",
  "warehouseId": "WH001",
  "items": [
    {
      "lineItemId": "LI001",
      "receivedQuantity": 10,
      "locationCode": "ZONE-A-123"
    },
    {
      "lineItemId": "LI002",
      "receivedQuantity": 20,
      "locationCode": "ZONE-B-456"
    }
  ],
  "inventoryUpdated": true
}
```

**驗證點：**
- 商品接收記錄成功創建
- 庫存已相應更新
- 包含所有接收項目與倉儲位置

### 9. 查詢採購訂單狀態

**請求：**
```http
GET /api/supplier/purchase-orders/PO987654
Content-Type: application/json
Authorization: Bearer {token}
```

**預期響應：**
```json
{
  "purchaseOrderId": "PO987654",
  "purchaseOrderNumber": "PO-2025-001",
  "supplierId": "SUP123456",
  "supplierName": "科技精品有限公司",
  "status": "COMPLETED",
  "items": [...],
  "subtotal": {
    "value": 304000,
    "currency": "TWD"
  },
  "tax": {
    "value": 15200,
    "currency": "TWD"
  },
  "total": {
    "value": 319200,
    "currency": "TWD"
  },
  "timeline": [
    {
      "status": "DRAFT",
      "timestamp": "2025-09-15T09:30:00Z",
      "user": "USER_PURCHASE_001"
    },
    {
      "status": "SUBMITTED",
      "timestamp": "2025-09-15T10:00:00Z",
      "user": "USER_PURCHASE_001"
    },
    {
      "status": "CONFIRMED",
      "timestamp": "2025-09-15T14:00:00Z",
      "supplierReference": "SO-2025-123"
    },
    {
      "status": "RECEIVED",
      "timestamp": "2025-09-30T11:00:00Z",
      "user": "USER_WAREHOUSE_001",
      "receiptId": "GR123456"
    },
    {
      "status": "COMPLETED",
      "timestamp": "2025-09-30T14:00:00Z"
    }
  ]
}
```

**驗證點：**
- 採購訂單狀態為已完成
- 包含完整的訂單時間線
- 顯示所有狀態變更

## 期望結果

1. 供應商能夠被正確註冊、審核與管理
2. 採購訂單可以被創建、提交、確認與處理
3. 商品接收流程可以更新庫存系統
4. 整個採購流程可以被正確追蹤與記錄

## 錯誤處理測試案例

### 1. 創建採購訂單時超出信用額度

**請求：**
```http
POST /api/supplier/purchase-orders
Content-Type: application/json
Authorization: Bearer {token}

{
  "supplierId": "SUP123456",
  "items": [
    {
      "supplierItemId": "SUI-12345601",
      "quantity": 100,
      "unitPrice": {
        "value": 28000,
        "currency": "TWD"
      }
    }
  ]
}
```

**預期響應：**
```json
{
  "error": "CREDIT_LIMIT_EXCEEDED",
  "message": "Purchase order total exceeds supplier credit limit",
  "details": {
    "orderTotal": {
      "value": 2800000,
      "currency": "TWD"
    },
    "creditLimit": {
      "value": 1000000,
      "currency": "TWD"
    },
    "availableCredit": {
      "value": 680800,
      "currency": "TWD"
    }
  },
  "statusCode": 400
}
```

### 2. 接收數量不符合訂單數量

**請求：**
```http
POST /api/supplier/purchase-orders/PO987654/receive
Content-Type: application/json
Authorization: Bearer {token}

{
  "receivedBy": "USER_WAREHOUSE_001",
  "warehouseId": "WH001",
  "items": [
    {
      "lineItemId": "LI001",
      "receivedQuantity": 8,
      "locationCode": "ZONE-A-123",
      "condition": "GOOD"
    },
    {
      "lineItemId": "LI002",
      "receivedQuantity": 15,
      "locationCode": "ZONE-B-456",
      "condition": "DAMAGED"
    }
  ],
  "notes": "部分筆電缺少，滑鼠部分損壞"
}
```

**預期響應：**
```json
{
  "receiptId": "GR123457",
  "purchaseOrderId": "PO987654",
  "status": "PARTIALLY_RECEIVED",
  "receivedBy": "USER_WAREHOUSE_001",
  "warehouseId": "WH001",
  "discrepancies": [
    {
      "lineItemId": "LI001",
      "orderedQuantity": 10,
      "receivedQuantity": 8,
      "discrepancyType": "SHORTAGE",
      "discrepancyQuantity": 2
    },
    {
      "lineItemId": "LI002",
      "orderedQuantity": 20,
      "receivedQuantity": 15,
      "discrepancyType": "SHORTAGE_AND_DAMAGE",
      "discrepancyQuantity": 5,
      "damagedQuantity": 5
    }
  ],
  "requiresAction": true,
  "inventoryUpdated": true
}
```
