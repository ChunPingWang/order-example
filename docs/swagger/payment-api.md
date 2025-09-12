# 支付微服務 API 文檔

## API 概述

支付微服務負責處理系統中的所有支付相關功能，包括支付處理、支付方式管理、退款處理和支付狀態追蹤。該微服務與多種支付提供商整合，提供統一的支付操作介面。

## API 基礎路徑

```
/api/payment
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 支付處理 API

### 1. 創建支付請求

**端點:** `POST /payments`

**說明:** 為訂單創建新的支付請求

**請求體:**
```json
{
  "orderId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "paymentMethodId": "string",
  "customerId": "string",
  "description": "string",
  "returnUrl": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "paymentId": "string",
  "status": "string",
  "orderId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "createdAt": "string",
  "paymentUrl": "string",
  "expiresAt": "string"
}
```

**狀態碼:**
- `201 Created` - 支付請求創建成功
- `400 Bad Request` - 請求資料無效

### 2. 獲取支付詳情

**端點:** `GET /payments/{paymentId}`

**說明:** 獲取特定支付的詳細信息

**路徑參數:**
- `paymentId` - 支付唯一標識符

**響應:**
```json
{
  "paymentId": "string",
  "orderId": "string",
  "customerId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "status": "string",
  "paymentMethod": {
    "id": "string",
    "type": "string",
    "name": "string",
    "last4": "string"
  },
  "createdAt": "string",
  "updatedAt": "string",
  "processedAt": "string",
  "transactionId": "string",
  "gatewayResponse": {},
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 支付不存在

### 3. 確認支付

**端點:** `PUT /payments/{paymentId}/confirm`

**說明:** 確認支付已完成

**路徑參數:**
- `paymentId` - 支付唯一標識符

**請求體:**
```json
{
  "transactionId": "string",
  "gatewayResponse": {}
}
```

**響應:**
```json
{
  "paymentId": "string",
  "status": "string",
  "transactionId": "string",
  "processedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 支付確認成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 支付不存在
- `409 Conflict` - 支付狀態不允許確認

### 4. 取消支付

**端點:** `PUT /payments/{paymentId}/cancel`

**說明:** 取消未完成的支付

**路徑參數:**
- `paymentId` - 支付唯一標識符

**請求體:**
```json
{
  "reason": "string",
  "cancelledBy": "string"
}
```

**響應:**
```json
{
  "paymentId": "string",
  "status": "string",
  "cancelledAt": "string",
  "reason": "string"
}
```

**狀態碼:**
- `200 OK` - 支付取消成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 支付不存在
- `409 Conflict` - 支付狀態不允許取消

### 5. 查詢訂單支付

**端點:** `GET /payments/order/{orderId}`

**說明:** 查詢特定訂單的所有支付記錄

**路徑參數:**
- `orderId` - 訂單唯一標識符

**響應:**
```json
{
  "payments": [
    {
      "paymentId": "string",
      "status": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "createdAt": "string",
      "processedAt": "string",
      "paymentMethod": {
        "type": "string",
        "name": "string"
      }
    }
  ],
  "totalAmount": {
    "value": "number",
    "currency": "string"
  },
  "paidAmount": {
    "value": "number",
    "currency": "string"
  },
  "remaining": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂單不存在

## 支付方式管理 API

### 1. 獲取支持的支付方式

**端點:** `GET /payment-methods`

**說明:** 獲取系統支持的所有支付方式

**響應:**
```json
{
  "paymentMethods": [
    {
      "id": "string",
      "name": "string",
      "type": "string",
      "isEnabled": "boolean",
      "supportedCurrencies": ["string"],
      "processingFees": {
        "percentage": "number",
        "fixed": "number",
        "currency": "string"
      },
      "icon": "string",
      "description": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 2. 獲取客戶的支付方式

**端點:** `GET /payment-methods/customer/{customerId}`

**說明:** 獲取特定客戶已儲存的支付方式

**路徑參數:**
- `customerId` - 客戶唯一標識符

**響應:**
```json
{
  "customerPaymentMethods": [
    {
      "id": "string",
      "type": "string",
      "name": "string",
      "isDefault": "boolean",
      "last4": "string",
      "expiryMonth": "number",
      "expiryYear": "number",
      "cardBrand": "string",
      "billingAddress": {
        "city": "string",
        "country": "string",
        "line1": "string",
        "line2": "string",
        "postalCode": "string",
        "state": "string"
      },
      "createdAt": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在

### 3. 新增客戶支付方式

**端點:** `POST /payment-methods/customer/{customerId}`

**說明:** 為客戶添加新的支付方式

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "type": "string",
  "paymentToken": "string",
  "billingAddress": {
    "city": "string",
    "country": "string",
    "line1": "string",
    "line2": "string",
    "postalCode": "string",
    "state": "string"
  },
  "isDefault": "boolean",
  "metadata": {}
}
```

**響應:**
```json
{
  "id": "string",
  "type": "string",
  "name": "string",
  "last4": "string",
  "expiryMonth": "number",
  "expiryYear": "number",
  "cardBrand": "string",
  "isDefault": "boolean",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 支付方式添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在

### 4. 設定默認支付方式

**端點:** `PUT /payment-methods/{paymentMethodId}/default`

**說明:** 將特定支付方式設為客戶的默認支付方式

**路徑參數:**
- `paymentMethodId` - 支付方式唯一標識符

**請求體:**
```json
{
  "customerId": "string"
}
```

**響應:**
```json
{
  "id": "string",
  "isDefault": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 設置成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 支付方式或客戶不存在

### 5. 刪除支付方式

**端點:** `DELETE /payment-methods/{paymentMethodId}`

**說明:** 刪除客戶的支付方式

**路徑參數:**
- `paymentMethodId` - 支付方式唯一標識符

**請求體:**
```json
{
  "customerId": "string"
}
```

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 支付方式或客戶不存在

## 退款管理 API

### 1. 請求退款

**端點:** `POST /refunds`

**說明:** 為已完成的支付發起退款請求

**請求體:**
```json
{
  "paymentId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "reason": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "refundId": "string",
  "paymentId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "status": "string",
  "reason": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 退款請求創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 支付不存在
- `409 Conflict` - 支付狀態不允許退款

### 2. 獲取退款詳情

**端點:** `GET /refunds/{refundId}`

**說明:** 獲取特定退款的詳細信息

**路徑參數:**
- `refundId` - 退款唯一標識符

**響應:**
```json
{
  "refundId": "string",
  "paymentId": "string",
  "orderId": "string",
  "customerId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "status": "string",
  "reason": "string",
  "createdAt": "string",
  "processedAt": "string",
  "transactionId": "string",
  "gatewayResponse": {},
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 退款不存在

### 3. 確認退款

**端點:** `PUT /refunds/{refundId}/confirm`

**說明:** 確認退款已處理

**路徑參數:**
- `refundId` - 退款唯一標識符

**請求體:**
```json
{
  "transactionId": "string",
  "gatewayResponse": {}
}
```

**響應:**
```json
{
  "refundId": "string",
  "status": "string",
  "transactionId": "string",
  "processedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 退款確認成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 退款不存在
- `409 Conflict` - 退款狀態不允許確認

### 4. 拒絕退款

**端點:** `PUT /refunds/{refundId}/reject`

**說明:** 拒絕退款請求

**路徑參數:**
- `refundId` - 退款唯一標識符

**請求體:**
```json
{
  "reason": "string",
  "rejectedBy": "string"
}
```

**響應:**
```json
{
  "refundId": "string",
  "status": "string",
  "rejectedAt": "string",
  "reason": "string"
}
```

**狀態碼:**
- `200 OK` - 退款拒絕成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 退款不存在
- `409 Conflict` - 退款狀態不允許拒絕

### 5. 查詢支付的退款

**端點:** `GET /refunds/payment/{paymentId}`

**說明:** 查詢特定支付的所有退款記錄

**路徑參數:**
- `paymentId` - 支付唯一標識符

**響應:**
```json
{
  "refunds": [
    {
      "refundId": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "status": "string",
      "reason": "string",
      "createdAt": "string",
      "processedAt": "string"
    }
  ],
  "totalAmount": {
    "value": "number",
    "currency": "string"
  },
  "refundedAmount": {
    "value": "number",
    "currency": "string"
  },
  "remaining": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 支付不存在

## 訂閱支付 API

### 1. 創建訂閱

**端點:** `POST /subscriptions`

**說明:** 為客戶創建新的訂閱

**請求體:**
```json
{
  "customerId": "string",
  "planId": "string",
  "paymentMethodId": "string",
  "startDate": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "subscriptionId": "string",
  "customerId": "string",
  "plan": {
    "id": "string",
    "name": "string",
    "amount": {
      "value": "number",
      "currency": "string"
    },
    "interval": "string",
    "intervalCount": "number"
  },
  "status": "string",
  "currentPeriodStart": "string",
  "currentPeriodEnd": "string",
  "nextBillingDate": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 訂閱創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶或計劃不存在

### 2. 獲取訂閱詳情

**端點:** `GET /subscriptions/{subscriptionId}`

**說明:** 獲取特定訂閱的詳細信息

**路徑參數:**
- `subscriptionId` - 訂閱唯一標識符

**響應:**
```json
{
  "subscriptionId": "string",
  "customerId": "string",
  "plan": {
    "id": "string",
    "name": "string",
    "amount": {
      "value": "number",
      "currency": "string"
    },
    "interval": "string",
    "intervalCount": "number"
  },
  "status": "string",
  "startDate": "string",
  "canceledAt": "string",
  "currentPeriodStart": "string",
  "currentPeriodEnd": "string",
  "nextBillingDate": "string",
  "paymentMethod": {
    "id": "string",
    "type": "string",
    "last4": "string"
  },
  "invoices": [
    {
      "invoiceId": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "status": "string",
      "date": "string"
    }
  ],
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 訂閱不存在

### 3. 更新訂閱

**端點:** `PUT /subscriptions/{subscriptionId}`

**說明:** 更新特定訂閱的參數

**路徑參數:**
- `subscriptionId` - 訂閱唯一標識符

**請求體:**
```json
{
  "planId": "string",
  "paymentMethodId": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "subscriptionId": "string",
  "plan": {
    "id": "string",
    "name": "string",
    "amount": {
      "value": "number",
      "currency": "string"
    },
    "interval": "string",
    "intervalCount": "number"
  },
  "status": "string",
  "nextBillingDate": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 訂閱更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂閱不存在
- `409 Conflict` - 訂閱狀態不允許更新

### 4. 取消訂閱

**端點:** `PUT /subscriptions/{subscriptionId}/cancel`

**說明:** 取消特定訂閱

**路徑參數:**
- `subscriptionId` - 訂閱唯一標識符

**請求體:**
```json
{
  "cancelAtPeriodEnd": "boolean",
  "reason": "string"
}
```

**響應:**
```json
{
  "subscriptionId": "string",
  "status": "string",
  "canceledAt": "string",
  "endDate": "string"
}
```

**狀態碼:**
- `200 OK` - 訂閱取消成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂閱不存在
- `409 Conflict` - 訂閱狀態不允許取消

### 5. 獲取客戶的訂閱

**端點:** `GET /subscriptions/customer/{customerId}`

**說明:** 查詢特定客戶的所有訂閱

**路徑參數:**
- `customerId` - 客戶唯一標識符

**響應:**
```json
{
  "subscriptions": [
    {
      "subscriptionId": "string",
      "plan": {
        "id": "string",
        "name": "string",
        "amount": {
          "value": "number",
          "currency": "string"
        },
        "interval": "string"
      },
      "status": "string",
      "currentPeriodEnd": "string",
      "nextBillingDate": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在

## 支付計劃 API

### 1. 獲取所有計劃

**端點:** `GET /plans`

**說明:** 獲取系統中所有可用的訂閱計劃

**查詢參數:**
- `status` (可選) - 按狀態過濾 (ACTIVE, ARCHIVED)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "plans": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "amount": {
        "value": "number",
        "currency": "string"
      },
      "interval": "string",
      "intervalCount": "number",
      "trialPeriodDays": "number",
      "status": "string",
      "features": ["string"],
      "createdAt": "string"
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

### 2. 創建計劃

**端點:** `POST /plans`

**說明:** 創建新的訂閱計劃

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "interval": "string",
  "intervalCount": "number",
  "trialPeriodDays": "number",
  "features": ["string"],
  "metadata": {}
}
```

**響應:**
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "interval": "string",
  "intervalCount": "number",
  "trialPeriodDays": "number",
  "status": "string",
  "features": ["string"],
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 計劃創建成功
- `400 Bad Request` - 請求資料無效

### 3. 更新計劃

**端點:** `PUT /plans/{planId}`

**說明:** 更新特定訂閱計劃的參數

**路徑參數:**
- `planId` - 計劃唯一標識符

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "features": ["string"],
  "metadata": {}
}
```

**響應:**
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "interval": "string",
  "intervalCount": "number",
  "trialPeriodDays": "number",
  "status": "string",
  "features": ["string"],
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 計劃更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 計劃不存在

### 4. 歸檔計劃

**端點:** `PUT /plans/{planId}/archive`

**說明:** 將特定訂閱計劃歸檔（不再提供給新客戶）

**路徑參數:**
- `planId` - 計劃唯一標識符

**響應:**
```json
{
  "id": "string",
  "status": "string",
  "archivedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 計劃歸檔成功
- `404 Not Found` - 計劃不存在
- `409 Conflict` - 計劃狀態不允許歸檔

## 支付網關整合 API

### 1. 獲取支付網關配置

**端點:** `GET /gateway/configuration`

**說明:** 獲取系統當前支付網關配置

**響應:**
```json
{
  "activeGateways": [
    {
      "id": "string",
      "name": "string",
      "isDefault": "boolean",
      "supportedMethods": ["string"],
      "supportedCurrencies": ["string"]
    }
  ],
  "defaultGateway": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 2. 生成支付網關令牌

**端點:** `POST /gateway/token`

**說明:** 生成用於客戶端支付處理的令牌

**請求體:**
```json
{
  "gatewayId": "string",
  "customerId": "string",
  "amount": {
    "value": "number",
    "currency": "string"
  },
  "metadata": {}
}
```

**響應:**
```json
{
  "token": "string",
  "expiresAt": "string",
  "clientKey": "string",
  "paymentUrl": "string"
}
```

**狀態碼:**
- `201 Created` - 令牌生成成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 網關或客戶不存在

## 模型定義

### 支付狀態 (PaymentStatus)
- `PENDING` - 待處理
- `PROCESSING` - 處理中
- `COMPLETED` - 已完成
- `FAILED` - 失敗
- `CANCELLED` - 已取消
- `REFUNDED` - 已退款

### 支付方式類型 (PaymentMethodType)
- `CREDIT_CARD` - 信用卡
- `DEBIT_CARD` - 借記卡
- `BANK_TRANSFER` - 銀行轉帳
- `WALLET` - 電子錢包
- `PAYPAL` - PayPal
- `APPLE_PAY` - Apple Pay
- `GOOGLE_PAY` - Google Pay
- `CRYPTO` - 加密貨幣
- `OTHER` - 其他

### 退款狀態 (RefundStatus)
- `PENDING` - 待處理
- `PROCESSING` - 處理中
- `COMPLETED` - 已完成
- `FAILED` - 失敗
- `CANCELLED` - 已取消
- `REJECTED` - 已拒絕

### 訂閱狀態 (SubscriptionStatus)
- `ACTIVE` - 活躍
- `TRIAL` - 試用期
- `PAST_DUE` - 逾期
- `UNPAID` - 未支付
- `CANCELED` - 已取消
- `ENDED` - 已結束

### 訂閱間隔 (SubscriptionInterval)
- `DAY` - 每日
- `WEEK` - 每週
- `MONTH` - 每月
- `YEAR` - 每年

### 計劃狀態 (PlanStatus)
- `ACTIVE` - 活躍
- `ARCHIVED` - 已歸檔

## 錯誤代碼

| 代碼                    | 說明                              |
|-------------------------|---------------------------------|
| PAYMENT_NOT_FOUND       | 支付不存在                         |
| PAYMENT_ALREADY_PROCESSED| 支付已處理                        |
| REFUND_NOT_FOUND        | 退款不存在                         |
| REFUND_AMOUNT_EXCEEDED  | 退款金額超過可退款金額               |
| PAYMENT_METHOD_NOT_FOUND| 支付方式不存在                     |
| INVALID_PAYMENT_STATUS  | 無效的支付狀態                     |
| GATEWAY_ERROR           | 支付網關錯誤                       |
| INSUFFICIENT_FUNDS      | 資金不足                          |
| CARD_DECLINED          | 卡片被拒絕                        |
| EXPIRED_CARD           | 卡片已過期                        |
| INVALID_CARD_NUMBER    | 無效的卡號                        |
| INVALID_CVV            | 無效的安全碼                      |
| SUBSCRIPTION_NOT_FOUND | 訂閱不存在                        |
| PLAN_NOT_FOUND         | 計劃不存在                        |
