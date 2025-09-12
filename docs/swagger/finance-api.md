# 財務微服務 API 文檔

## API 概述

財務微服務負責處理系統中的所有財務相關功能，包括交易處理、發票生成、財務報表和稅務管理。該微服務提供了完整的財務操作與報表功能。

## API 基礎路徑

```
/api/finance
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 交易管理 API

### 1. 獲取交易列表

**端點:** `GET /transactions`

**說明:** 獲取符合條件的財務交易列表

**查詢參數:**
- `status` (可選) - 按狀態過濾 (PENDING, PROCESSED, COMPLETED)
- `type` (可選) - 按交易類型過濾 (INCOME, EXPENSE, REFUND)
- `fromDate` (可選) - 按交易日期範圍過濾（開始日期）
- `toDate` (可選) - 按交易日期範圍過濾（結束日期）
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "transactions": [
    {
      "transactionId": "string",
      "reference": "string",
      "type": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "status": "string",
      "createdAt": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "totalIncome": {
      "value": "number",
      "currency": "string"
    },
    "totalExpense": {
      "value": "number",
      "currency": "string"
    },
    "netAmount": {
      "value": "number",
      "currency": "string"
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 2. 獲取待處理的交易列表

**端點:** `GET /transactions/pending`

**說明:** 獲取所有待處理的財務交易

**響應:**
```json
{
  "pendingTransactions": [
    {
      "transactionId": "string",
      "saleId": "string",
      "userId": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "type": "string",
      "status": "string",
      "paymentMethod": "string",
      "createdAt": "string",
      "reference": "string",
      "needsInvoice": "boolean"
    }
  ],
  "totalPending": "number",
  "totalAmount": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 3. 獲取交易詳情

**端點:** `GET /transactions/{transactionId}`

**說明:** 獲取特定交易的詳細信息

**路徑參數:**
- `transactionId` - 交易唯一標識符

**響應:**
```json
{
  "transactionId": "string",
  "saleId": "string",
  "userId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "type": "string",
  "status": "string",
  "paymentMethod": "string",
  "paymentDetails": {
    "paymentId": "string",
    "provider": "string",
    "accountLast4": "string"
  },
  "createdAt": "string",
  "processedAt": "string",
  "completedAt": "string",
  "reference": "string",
  "accountingCode": "string",
  "notes": "string",
  "relatedInvoices": [
    {
      "invoiceId": "string",
      "invoiceNumber": "string"
    }
  ],
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 交易不存在

### 4. 將交易標記為已處理

**端點:** `PUT /transactions/{transactionId}/process`

**說明:** 將特定交易標記為已處理

**路徑參數:**
- `transactionId` - 交易唯一標識符

**請求體:**
```json
{
  "accountingCode": "string",
  "processorId": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "transactionId": "string",
  "status": "string",
  "processedAt": "string",
  "processorId": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 交易不存在
- `409 Conflict` - 交易狀態不允許處理

### 5. 將交易標記為已完成

**端點:** `PUT /transactions/{transactionId}/complete`

**說明:** 將特定交易標記為已完成

**路徑參數:**
- `transactionId` - 交易唯一標識符

**請求體:**
```json
{
  "invoiceId": "string",
  "reconciliationReference": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "transactionId": "string",
  "status": "string",
  "completedAt": "string",
  "invoiceIds": ["string"]
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 交易不存在
- `409 Conflict` - 交易狀態不允許完成

### 6. 創建新交易

**端點:** `POST /transactions`

**說明:** 手動創建新的財務交易記錄

**請求體:**
```json
{
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "type": "string",
  "paymentMethod": "string",
  "reference": "string",
  "accountingCode": "string",
  "description": "string",
  "metadata": {},
  "needsInvoice": "boolean"
}
```

**響應:**
```json
{
  "transactionId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "type": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 交易創建成功
- `400 Bad Request` - 請求資料無效

## 發票管理 API

### 1. 創建發票

**端點:** `POST /invoices`

**說明:** 為交易創建發票

**請求體:**
```json
{
  "transactionId": "string",
  "saleId": "string",
  "userId": "string"
}
```

**響應:**
```json
{
  "invoiceId": "string",
  "invoiceNumber": "string",
  "status": "string",
  "saleId": "string",
  "userId": "string",
  "items": [
    {
      "productId": "string",
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
  "total": {
    "value": "number",
    "currency": "string"
  },
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 發票創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 交易或銷售訂單不存在
- `409 Conflict` - 發票已存在

### 2. 發行發票

**端點:** `PUT /invoices/{invoiceId}/issue`

**說明:** 將發票從草稿狀態發行

**路徑參數:**
- `invoiceId` - 發票唯一標識符

**請求體:**
```json
{
  "issueDate": "string",
  "dueDate": "string",
  "recipientInfo": {
    "name": "string",
    "email": "string",
    "taxId": "string"
  },
  "invoiceFormat": "string"
}
```

**響應:**
```json
{
  "invoiceId": "string",
  "invoiceNumber": "string",
  "status": "string",
  "issuedAt": "string",
  "dueDate": "string",
  "downloadUrl": "string",
  "recipientInfo": {
    "name": "string",
    "email": "string",
    "taxId": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 發行成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 發票不存在
- `409 Conflict` - 發票狀態不允許發行

### 3. 發送發票

**端點:** `POST /invoices/{invoiceId}/send`

**說明:** 發送發票給客戶

**路徑參數:**
- `invoiceId` - 發票唯一標識符

**請求體:**
```json
{
  "deliveryMethod": "string",
  "recipientEmail": "string"
}
```

**響應:**
```json
{
  "invoiceId": "string",
  "deliveryStatus": "string",
  "sentAt": "string",
  "sentTo": "string",
  "deliveryId": "string"
}
```

**狀態碼:**
- `200 OK` - 發送成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 發票不存在
- `409 Conflict` - 發票狀態不允許發送

### 4. 獲取發票

**端點:** `GET /invoices/{invoiceId}`

**說明:** 獲取特定發票的詳細信息

**路徑參數:**
- `invoiceId` - 發票唯一標識符

**響應:**
```json
{
  "invoiceId": "string",
  "invoiceNumber": "string",
  "status": "string",
  "saleId": "string",
  "userId": "string",
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
  "issuedAt": "string",
  "dueDate": "string",
  "recipientInfo": {
    "name": "string",
    "email": "string",
    "taxId": "string"
  },
  "deliveryStatus": "string",
  "paymentStatus": "string",
  "transactionId": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 發票不存在

### 5. 作廢發票

**端點:** `PUT /invoices/{invoiceId}/void`

**說明:** 作廢已開立的發票

**路徑參數:**
- `invoiceId` - 發票唯一標識符

**請求體:**
```json
{
  "reason": "string",
  "voidedBy": "string"
}
```

**響應:**
```json
{
  "invoiceId": "string",
  "invoiceNumber": "string",
  "status": "string",
  "voidedAt": "string",
  "voidedBy": "string",
  "reason": "string"
}
```

**狀態碼:**
- `200 OK` - 作廢成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 發票不存在
- `409 Conflict` - 發票狀態不允許作廢

### 6. 下載發票

**端點:** `GET /invoices/{invoiceId}/download`

**說明:** 下載發票PDF檔案

**路徑參數:**
- `invoiceId` - 發票唯一標識符

**查詢參數:**
- `format` (可選, 默認為PDF) - 下載格式 (PDF, XML)

**響應:**
```
發票文件的二進制數據
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 發票不存在
- `409 Conflict` - 發票狀態不允許下載

## 財務報表 API

### 1. 生成每日財務報表

**端點:** `POST /reports/daily`

**說明:** 生成特定日期的每日財務報表

**請求體:**
```json
{
  "date": "string",
  "reportFormat": "string"
}
```

**響應:**
```json
{
  "reportId": "string",
  "reportType": "string",
  "date": "string",
  "summary": {
    "totalSales": {
      "value": "number",
      "currency": "string"
    },
    "totalTax": {
      "value": "number",
      "currency": "string"
    },
    "transactionCount": "number",
    "invoiceCount": "number"
  },
  "details": {
    "transactions": [
      {
        "transactionId": "string",
        "saleId": "string",
        "amount": {
          "value": "number",
          "currency": "string"
        },
        "status": "string",
        "type": "string"
      }
    ],
    "invoices": [
      {
        "invoiceId": "string",
        "invoiceNumber": "string",
        "amount": {
          "value": "number",
          "currency": "string"
        },
        "status": "string"
      }
    ]
  },
  "generatedAt": "string",
  "downloadUrl": "string"
}
```

**狀態碼:**
- `200 OK` - 報表生成成功
- `400 Bad Request` - 請求資料無效

### 2. 生成月度財務報表

**端點:** `POST /reports/monthly`

**說明:** 生成特定月份的月度財務報表

**請求體:**
```json
{
  "year": "number",
  "month": "number",
  "reportFormat": "string"
}
```

**響應:**
```json
{
  "reportId": "string",
  "reportType": "string",
  "period": {
    "year": "number",
    "month": "number"
  },
  "summary": {
    "totalSales": {
      "value": "number",
      "currency": "string"
    },
    "totalExpenses": {
      "value": "number",
      "currency": "string"
    },
    "totalTax": {
      "value": "number",
      "currency": "string"
    },
    "netIncome": {
      "value": "number",
      "currency": "string"
    },
    "transactionCount": "number",
    "invoiceCount": "number"
  },
  "dailySummary": [
    {
      "date": "string",
      "sales": {
        "value": "number",
        "currency": "string"
      },
      "expenses": {
        "value": "number",
        "currency": "string"
      }
    }
  ],
  "categorySummary": [
    {
      "category": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "percentage": "number"
    }
  ],
  "generatedAt": "string",
  "downloadUrl": "string"
}
```

**狀態碼:**
- `200 OK` - 報表生成成功
- `400 Bad Request` - 請求資料無效

### 3. 獲取報表列表

**端點:** `GET /reports`

**說明:** 獲取已生成的報表列表

**查詢參數:**
- `reportType` (可選) - 按報表類型過濾 (DAILY, MONTHLY, ANNUAL, CUSTOM)
- `fromDate` (可選) - 按報表日期範圍過濾（開始日期）
- `toDate` (可選) - 按報表日期範圍過濾（結束日期）
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "reports": [
    {
      "reportId": "string",
      "reportType": "string",
      "name": "string",
      "period": "string",
      "generatedAt": "string",
      "downloadUrl": "string"
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

### 4. 下載報表

**端點:** `GET /reports/{reportId}/download`

**說明:** 下載已生成的報表

**路徑參數:**
- `reportId` - 報表唯一標識符

**查詢參數:**
- `format` (可選, 默認為PDF) - 下載格式 (PDF, EXCEL, CSV)

**響應:**
```
報表文件的二進制數據
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 報表不存在

## 稅務管理 API

### 1. 獲取稅務設置

**端點:** `GET /tax/settings`

**說明:** 獲取系統的稅務設置

**響應:**
```json
{
  "settings": {
    "defaultTaxRate": "number",
    "taxCalculationMethod": "string",
    "taxInclusive": "boolean",
    "taxRoundingMode": "string",
    "businessTaxId": "string",
    "taxAuthority": "string"
  },
  "taxRates": [
    {
      "id": "string",
      "name": "string",
      "rate": "number",
      "country": "string",
      "region": "string",
      "productCategories": ["string"]
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 2. 更新稅務設置

**端點:** `PUT /tax/settings`

**說明:** 更新系統的稅務設置

**請求體:**
```json
{
  "defaultTaxRate": "number",
  "taxCalculationMethod": "string",
  "taxInclusive": "boolean",
  "taxRoundingMode": "string",
  "businessTaxId": "string",
  "taxAuthority": "string"
}
```

**響應:**
```json
{
  "settings": {
    "defaultTaxRate": "number",
    "taxCalculationMethod": "string",
    "taxInclusive": "boolean",
    "taxRoundingMode": "string",
    "businessTaxId": "string",
    "taxAuthority": "string"
  },
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效

### 3. 創建稅率

**端點:** `POST /tax/rates`

**說明:** 創建新的稅率

**請求體:**
```json
{
  "name": "string",
  "rate": "number",
  "country": "string",
  "region": "string",
  "productCategories": ["string"],
  "startDate": "string",
  "endDate": "string",
  "description": "string"
}
```

**響應:**
```json
{
  "id": "string",
  "name": "string",
  "rate": "number",
  "country": "string",
  "region": "string",
  "productCategories": ["string"],
  "startDate": "string",
  "endDate": "string",
  "description": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 稅率創建成功
- `400 Bad Request` - 請求資料無效

### 4. 稅務計算

**端點:** `POST /tax/calculate`

**說明:** 根據提供的資料計算稅額

**請求體:**
```json
{
  "amount": "number",
  "country": "string",
  "region": "string",
  "productCategory": "string",
  "isBusinessCustomer": "boolean",
  "customerTaxId": "string"
}
```

**響應:**
```json
{
  "amount": "number",
  "taxRate": "number",
  "taxAmount": "number",
  "total": "number",
  "taxRateId": "string",
  "taxRateName": "string"
}
```

**狀態碼:**
- `200 OK` - 計算成功
- `400 Bad Request` - 請求資料無效

## 模型定義

### 交易類型 (TransactionType)
- `INCOME` - 收入
- `EXPENSE` - 支出
- `REFUND` - 退款
- `ADJUSTMENT` - 調整

### 交易狀態 (TransactionStatus)
- `PENDING` - 待處理
- `PROCESSED` - 已處理
- `COMPLETED` - 已完成
- `FAILED` - 失敗
- `CANCELLED` - 已取消

### 支付方式 (PaymentMethod)
- `CREDIT_CARD` - 信用卡
- `BANK_TRANSFER` - 銀行轉帳
- `CASH` - 現金
- `MOBILE_PAYMENT` - 行動支付
- `ONLINE_PAYMENT` - 線上支付

### 發票狀態 (InvoiceStatus)
- `DRAFT` - 草稿
- `ISSUED` - 已開立
- `SENT` - 已發送
- `PAID` - 已付款
- `OVERDUE` - 已逾期
- `CANCELLED` - 已取消
- `VOID` - 作廢

### 發票格式 (InvoiceFormat)
- `STANDARD` - 標準
- `ELECTRONIC` - 電子
- `SIMPLIFIED` - 簡化

### 發送方式 (DeliveryMethod)
- `EMAIL` - 電子郵件
- `MAIL` - 郵寄
- `DOWNLOAD` - 下載
- `API` - API

### 報表類型 (ReportType)
- `DAILY` - 每日
- `MONTHLY` - 月度
- `ANNUAL` - 年度
- `CUSTOM` - 自定義

### 報表格式 (ReportFormat)
- `PDF` - PDF格式
- `EXCEL` - Excel格式
- `CSV` - CSV格式
- `JSON` - JSON格式

### 稅務計算方式 (TaxCalculationMethod)
- `INCLUSIVE` - 含稅
- `EXCLUSIVE` - 未稅

## 錯誤代碼

| 代碼                     | 說明                             |
|--------------------------|----------------------------------|
| TRANSACTION_NOT_FOUND    | 交易不存在                       |
| INVOICE_NOT_FOUND        | 發票不存在                       |
| AMOUNT_MISMATCH          | 金額不匹配                       |
| DUPLICATE_INVOICE_NUMBER | 重複的發票號碼                   |
| INVALID_TRANSACTION_STATUS| 無效的交易狀態                   |
| INVALID_INVOICE_STATUS   | 無效的發票狀態                   |
| TAX_CALCULATION_ERROR    | 稅務計算錯誤                     |
| REPORT_GENERATION_ERROR  | 報表生成錯誤                     |
