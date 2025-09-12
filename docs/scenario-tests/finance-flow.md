# 財務處理流程情境測試

## 情境概述

本情境測試驗證從訂單付款到發票開立與財務處理的完整流程，包括交易紀錄、發票生成、稅務處理與報表生成的過程。

## 前置條件

1. 已有完成付款的銷售訂單
2. 財務系統已設置好稅率與會計科目
3. 發票系統已準備就緒

## 測試步驟

### 1. 檢查待處理的財務交易

**請求：**
```http
GET /api/finance/transactions/pending
Content-Type: application/json
Authorization: Bearer {token}
```

**預期響應：**
```json
{
  "pendingTransactions": [
    {
      "transactionId": "TXN987654321",
      "saleId": "SALE123456",
      "userId": "USER123456",
      "amount": {
        "value": 38425,
        "currency": "TWD"
      },
      "type": "INCOME",
      "status": "PENDING",
      "paymentMethod": "CREDIT_CARD",
      "createdAt": "2025-09-13T14:14:30Z",
      "reference": "PAY456789",
      "needsInvoice": true
    }
  ],
  "totalPending": 1,
  "totalAmount": {
    "value": 38425,
    "currency": "TWD"
  }
}
```

**驗證點：**
- 列出所有待處理的財務交易
- 包含交易金額與相關參考資訊

### 2. 將交易標記為已處理

**請求：**
```http
PUT /api/finance/transactions/TXN987654321/process
Content-Type: application/json
Authorization: Bearer {token}

{
  "accountingCode": "IN-SALES-2025",
  "processorId": "FINANCE001",
  "notes": "正常銷售交易"
}
```

**預期響應：**
```json
{
  "transactionId": "TXN987654321",
  "status": "PROCESSED",
  "processedAt": "2025-09-13T15:20:00Z",
  "processorId": "FINANCE001"
}
```

**驗證點：**
- 交易狀態更新為PROCESSED
- 記錄處理時間與處理人員

### 3. 為交易生成發票

**請求：**
```http
POST /api/finance/invoices
Content-Type: application/json
Authorization: Bearer {token}

{
  "transactionId": "TXN987654321",
  "saleId": "SALE123456",
  "userId": "USER123456"
}
```

**預期響應：**
```json
{
  "invoiceId": "INV789012",
  "invoiceNumber": "B12345678",
  "status": "DRAFT",
  "saleId": "SALE123456",
  "userId": "USER123456",
  "items": [
    {
      "productId": "PROD001",
      "description": "高效能筆記型電腦",
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
      "description": "專業級無線滑鼠",
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
  "total": {
    "value": 38325,
    "currency": "TWD"
  },
  "createdAt": "2025-09-13T15:25:00Z"
}
```

**驗證點：**
- 發票成功創建，處於DRAFT狀態
- 包含所有訂單項目與金額
- 稅金計算正確

### 4. 發行發票

**請求：**
```http
PUT /api/finance/invoices/INV789012/issue
Content-Type: application/json
Authorization: Bearer {token}

{
  "issueDate": "2025-09-13",
  "dueDate": "2025-10-13",
  "recipientInfo": {
    "name": "王小明",
    "email": "wang@example.com",
    "taxId": "12345678"
  },
  "invoiceFormat": "ELECTRONIC"
}
```

**預期響應：**
```json
{
  "invoiceId": "INV789012",
  "invoiceNumber": "B12345678",
  "status": "ISSUED",
  "issuedAt": "2025-09-13T15:30:00Z",
  "dueDate": "2025-10-13",
  "downloadUrl": "https://example.com/api/finance/invoices/INV789012/download",
  "recipientInfo": {
    "name": "王小明",
    "email": "wang@example.com",
    "taxId": "12345678"
  }
}
```

**驗證點：**
- 發票狀態更新為ISSUED
- 提供發票下載連結
- 包含收件人資訊

### 5. 發送發票給客戶

**請求：**
```http
POST /api/finance/invoices/INV789012/send
Content-Type: application/json
Authorization: Bearer {token}

{
  "deliveryMethod": "EMAIL",
  "recipientEmail": "wang@example.com"
}
```

**預期響應：**
```json
{
  "invoiceId": "INV789012",
  "deliveryStatus": "SENT",
  "sentAt": "2025-09-13T15:35:00Z",
  "sentTo": "wang@example.com",
  "deliveryId": "EMAIL789012"
}
```

**驗證點：**
- 發票成功發送至客戶郵箱
- 記錄發送時間與收件人

### 6. 記錄財務系統交易完成

**請求：**
```http
PUT /api/finance/transactions/TXN987654321/complete
Content-Type: application/json
Authorization: Bearer {token}

{
  "invoiceId": "INV789012",
  "reconciliationReference": "REC123456",
  "notes": "交易完成且已開立發票"
}
```

**預期響應：**
```json
{
  "transactionId": "TXN987654321",
  "status": "COMPLETED",
  "completedAt": "2025-09-13T15:40:00Z",
  "invoiceIds": ["INV789012"]
}
```

**驗證點：**
- 交易狀態更新為COMPLETED
- 交易與發票關聯已建立

### 7. 查詢發票狀態

**請求：**
```http
GET /api/finance/invoices/INV789012
Content-Type: application/json
Authorization: Bearer {token}
```

**預期響應：**
```json
{
  "invoiceId": "INV789012",
  "invoiceNumber": "B12345678",
  "status": "ISSUED",
  "saleId": "SALE123456",
  "userId": "USER123456",
  "items": [...],
  "subtotal": {
    "value": 36500,
    "currency": "TWD"
  },
  "tax": {
    "value": 1825,
    "currency": "TWD"
  },
  "total": {
    "value": 38325,
    "currency": "TWD"
  },
  "issuedAt": "2025-09-13T15:30:00Z",
  "dueDate": "2025-10-13",
  "recipientInfo": {
    "name": "王小明",
    "email": "wang@example.com",
    "taxId": "12345678"
  },
  "deliveryStatus": "SENT",
  "paymentStatus": "PAID",
  "transactionId": "TXN987654321"
}
```

**驗證點：**
- 發票詳情包含完整資訊
- 顯示發票已發送且已付款

### 8. 生成每日財務報表

**請求：**
```http
POST /api/finance/reports/daily
Content-Type: application/json
Authorization: Bearer {token}

{
  "date": "2025-09-13",
  "reportFormat": "JSON"
}
```

**預期響應：**
```json
{
  "reportId": "REP456789",
  "reportType": "DAILY",
  "date": "2025-09-13",
  "summary": {
    "totalSales": {
      "value": 38325,
      "currency": "TWD"
    },
    "totalTax": {
      "value": 1825,
      "currency": "TWD"
    },
    "transactionCount": 1,
    "invoiceCount": 1
  },
  "details": {
    "transactions": [
      {
        "transactionId": "TXN987654321",
        "saleId": "SALE123456",
        "amount": {
          "value": 38325,
          "currency": "TWD"
        },
        "status": "COMPLETED",
        "type": "INCOME"
      }
    ],
    "invoices": [
      {
        "invoiceId": "INV789012",
        "invoiceNumber": "B12345678",
        "amount": {
          "value": 38325,
          "currency": "TWD"
        },
        "status": "ISSUED"
      }
    ]
  },
  "generatedAt": "2025-09-13T23:00:00Z",
  "downloadUrl": "https://example.com/api/finance/reports/REP456789/download"
}
```

**驗證點：**
- 報表成功生成
- 包含交易與發票摘要資訊
- 提供報表下載連結

## 期望結果

1. 財務交易可以被正確識別與處理
2. 發票能夠被正確生成並發送給客戶
3. 交易狀態能夠正確更新與追蹤
4. 財務報表能夠正確生成並包含所有必要資訊

## 錯誤處理測試案例

### 1. 發票金額與交易不符

**請求：**
```http
POST /api/finance/invoices
Content-Type: application/json
Authorization: Bearer {token}

{
  "transactionId": "TXN987654322",
  "saleId": "SALE123457",
  "userId": "USER123457",
  "overrideAmount": {
    "value": 50000,
    "currency": "TWD"
  }
}
```

**預期響應：**
```json
{
  "error": "AMOUNT_MISMATCH",
  "message": "Invoice amount does not match transaction amount",
  "details": {
    "transactionAmount": {
      "value": 38325,
      "currency": "TWD"
    },
    "requestedAmount": {
      "value": 50000,
      "currency": "TWD"
    }
  },
  "statusCode": 400
}
```

### 2. 重複發票號碼

**請求：**
```http
PUT /api/finance/invoices/INV789013/issue
Content-Type: application/json
Authorization: Bearer {token}

{
  "issueDate": "2025-09-13",
  "invoiceNumber": "B12345678"  // 已使用的發票號碼
}
```

**預期響應：**
```json
{
  "error": "DUPLICATE_INVOICE_NUMBER",
  "message": "Invoice number B12345678 is already in use",
  "details": {
    "existingInvoiceId": "INV789012",
    "issuedAt": "2025-09-13T15:30:00Z"
  },
  "statusCode": 400
}
```
