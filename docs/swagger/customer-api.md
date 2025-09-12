# 客戶微服務 API 文檔

## API 概述

客戶微服務負責管理系統中的所有客戶相關功能，包括客戶註冊、客戶資料管理、客戶地址管理、客戶分組、客戶忠誠度計劃等。該微服務提供完整的客戶生命週期管理，從註冊到客戶關係維護。

## API 基礎路徑

```
/api/customers
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

某些端點需要特定權限，這些將在端點描述中標明。

## 客戶管理 API

### 1. 創建客戶

**端點:** `POST /`

**說明:** 創建新客戶

**請求體:**
```json
{
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "dateOfBirth": "string",
  "gender": "string",
  "preferredLanguage": "string",
  "preferredCurrency": "string",
  "marketingConsent": "boolean",
  "source": "string",
  "type": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "customerId": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 客戶創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 電子郵件已存在

### 2. 獲取客戶詳情

**端點:** `GET /{customerId}`

**說明:** 獲取特定客戶的詳細信息

**路徑參數:**
- `customerId` - 客戶唯一標識符

**查詢參數:**
- `includeAddresses` (可選, 默認為true) - 是否包含客戶地址
- `includeOrders` (可選, 默認為false) - 是否包含訂單摘要
- `includeSubscriptions` (可選, 默認為false) - 是否包含訂閱摘要
- `includePaymentMethods` (可選, 默認為false) - 是否包含付款方式摘要

**響應:**
```json
{
  "customerId": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "dateOfBirth": "string",
  "gender": "string",
  "preferredLanguage": "string",
  "preferredCurrency": "string",
  "status": "string",
  "marketingConsent": "boolean",
  "type": "string",
  "source": "string",
  "registrationDate": "string",
  "lastLoginDate": "string",
  "addresses": [
    {
      "addressId": "string",
      "type": "string",
      "isDefault": "boolean",
      "firstName": "string",
      "lastName": "string",
      "company": "string",
      "addressLine1": "string",
      "addressLine2": "string",
      "city": "string",
      "state": "string",
      "postalCode": "string",
      "country": "string",
      "phoneNumber": "string"
    }
  ],
  "loyaltyProgram": {
    "membershipId": "string",
    "tier": "string",
    "points": "number",
    "joinDate": "string",
    "nextTier": "string",
    "pointsToNextTier": "number"
  },
  "segments": [
    {
      "segmentId": "string",
      "name": "string",
      "description": "string",
      "addedAt": "string"
    }
  ],
  "orders": {
    "totalCount": "number",
    "totalValue": {
      "amount": "number",
      "currency": "string"
    },
    "lastOrderDate": "string",
    "recentOrders": [
      {
        "orderId": "string",
        "date": "string",
        "status": "string",
        "total": {
          "amount": "number",
          "currency": "string"
        }
      }
    ]
  },
  "subscriptions": {
    "activeCount": "number",
    "totalValue": {
      "amount": "number",
      "currency": "string"
    },
    "recentSubscriptions": [
      {
        "subscriptionId": "string",
        "status": "string",
        "product": "string",
        "nextBillingDate": "string",
        "amount": {
          "amount": "number",
          "currency": "string"
        }
      }
    ]
  },
  "paymentMethods": [
    {
      "paymentMethodId": "string",
      "type": "string",
      "isDefault": "boolean",
      "lastFourDigits": "string",
      "expiryDate": "string",
      "cardType": "string"
    }
  ],
  "createdAt": "string",
  "updatedAt": "string",
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在

### 3. 更新客戶

**端點:** `PUT /{customerId}`

**說明:** 更新特定客戶的信息

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "firstName": "string",
  "lastName": "string",
  "phoneNumber": "string",
  "dateOfBirth": "string",
  "gender": "string",
  "preferredLanguage": "string",
  "preferredCurrency": "string",
  "marketingConsent": "boolean",
  "metadata": {}
}
```

**響應:**
```json
{
  "customerId": "string",
  "firstName": "string",
  "lastName": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在

### 4. 刪除客戶

**端點:** `DELETE /{customerId}`

**說明:** 刪除特定客戶（軟刪除，僅標記為已刪除）

**路徑參數:**
- `customerId` - 客戶唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 客戶不存在

### 5. 查詢客戶列表

**端點:** `GET /`

**說明:** 獲取符合條件的客戶列表

**查詢參數:**
- `query` (可選) - 文本搜索，匹配電子郵件、姓名和電話號碼
- `status` (可選) - 按狀態過濾 (ACTIVE, INACTIVE, BLOCKED)
- `type` (可選) - 按客戶類型過濾 (INDIVIDUAL, BUSINESS)
- `source` (可選) - 按客戶來源過濾 (WEBSITE, MOBILE_APP, STORE, REFERRAL, SOCIAL_MEDIA)
- `segmentId` (可選) - 按客戶分群過濾
- `minRegistrationDate` (可選) - 按最小註冊日期過濾
- `maxRegistrationDate` (可選) - 按最大註冊日期過濾
- `minOrderValue` (可選) - 按最小訂單總價值過濾
- `sort` (可選, 默認為createdAt:desc) - 排序字段和方向 (lastName:asc, lastName:desc, registrationDate:asc, registrationDate:desc, lastOrderDate:asc, lastOrderDate:desc)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "customers": [
    {
      "customerId": "string",
      "email": "string",
      "firstName": "string",
      "lastName": "string",
      "status": "string",
      "type": "string",
      "source": "string",
      "registrationDate": "string",
      "lastOrderDate": "string",
      "totalOrderValue": {
        "amount": "number",
        "currency": "string"
      },
      "loyaltyTier": "string",
      "loyaltyPoints": "number",
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
    "type": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "source": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "segments": [
      {
        "id": "string",
        "name": "string",
        "count": "number"
      }
    ],
    "registrationDateRange": {
      "min": "string",
      "max": "string"
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 6. 更新客戶狀態

**端點:** `PUT /{customerId}/status`

**說明:** 更新特定客戶的狀態

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "status": "string",
  "reason": "string"
}
```

**響應:**
```json
{
  "customerId": "string",
  "status": "string",
  "previousStatus": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在

### 7. 重設客戶密碼

**端點:** `POST /{customerId}/password-reset`

**說明:** 為客戶生成並發送密碼重設令牌

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "notificationChannel": "string"
}
```

**響應:**
```json
{
  "success": "boolean",
  "expiresAt": "string",
  "notificationSent": "boolean"
}
```

**狀態碼:**
- `200 OK` - 密碼重設令牌已生成並發送
- `404 Not Found` - 客戶不存在
- `429 Too Many Requests` - 重設請求頻率過高

## 客戶地址 API

### 1. 添加地址

**端點:** `POST /{customerId}/addresses`

**說明:** 為客戶添加新地址

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "type": "string",
  "isDefault": "boolean",
  "firstName": "string",
  "lastName": "string",
  "company": "string",
  "addressLine1": "string",
  "addressLine2": "string",
  "city": "string",
  "state": "string",
  "postalCode": "string",
  "country": "string",
  "phoneNumber": "string"
}
```

**響應:**
```json
{
  "addressId": "string",
  "customerId": "string",
  "type": "string",
  "isDefault": "boolean",
  "formattedAddress": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 地址添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在

### 2. 獲取地址

**端點:** `GET /{customerId}/addresses/{addressId}`

**說明:** 獲取客戶特定地址的詳細信息

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `addressId` - 地址唯一標識符

**響應:**
```json
{
  "addressId": "string",
  "customerId": "string",
  "type": "string",
  "isDefault": "boolean",
  "firstName": "string",
  "lastName": "string",
  "company": "string",
  "addressLine1": "string",
  "addressLine2": "string",
  "city": "string",
  "state": "string",
  "postalCode": "string",
  "country": "string",
  "phoneNumber": "string",
  "formattedAddress": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶或地址不存在

### 3. 更新地址

**端點:** `PUT /{customerId}/addresses/{addressId}`

**說明:** 更新客戶特定地址的信息

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `addressId` - 地址唯一標識符

**請求體:**
```json
{
  "type": "string",
  "isDefault": "boolean",
  "firstName": "string",
  "lastName": "string",
  "company": "string",
  "addressLine1": "string",
  "addressLine2": "string",
  "city": "string",
  "state": "string",
  "postalCode": "string",
  "country": "string",
  "phoneNumber": "string"
}
```

**響應:**
```json
{
  "addressId": "string",
  "customerId": "string",
  "formattedAddress": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶或地址不存在

### 4. 刪除地址

**端點:** `DELETE /{customerId}/addresses/{addressId}`

**說明:** 刪除客戶特定地址

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `addressId` - 地址唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 客戶或地址不存在

### 5. 獲取客戶所有地址

**端點:** `GET /{customerId}/addresses`

**說明:** 獲取客戶的所有地址

**路徑參數:**
- `customerId` - 客戶唯一標識符

**響應:**
```json
{
  "customerId": "string",
  "addresses": [
    {
      "addressId": "string",
      "type": "string",
      "isDefault": "boolean",
      "firstName": "string",
      "lastName": "string",
      "addressLine1": "string",
      "city": "string",
      "state": "string",
      "postalCode": "string",
      "country": "string",
      "formattedAddress": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在

### 6. 設置默認地址

**端點:** `PUT /{customerId}/addresses/{addressId}/default`

**說明:** 將客戶特定地址設為默認地址

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `addressId` - 地址唯一標識符

**請求體:**
```json
{
  "type": "string"
}
```

**響應:**
```json
{
  "addressId": "string",
  "customerId": "string",
  "type": "string",
  "isDefault": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 設置成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶或地址不存在

## 客戶忠誠度 API

### 1. 查詢忠誠度資料

**端點:** `GET /{customerId}/loyalty`

**說明:** 獲取客戶的忠誠度計劃資料

**路徑參數:**
- `customerId` - 客戶唯一標識符

**響應:**
```json
{
  "customerId": "string",
  "membershipId": "string",
  "enrolled": "boolean",
  "enrollmentDate": "string",
  "tier": {
    "id": "string",
    "name": "string",
    "benefits": ["string"],
    "requiredPoints": "number"
  },
  "nextTier": {
    "id": "string",
    "name": "string",
    "benefits": ["string"],
    "requiredPoints": "number",
    "pointsNeeded": "number"
  },
  "points": {
    "current": "number",
    "lifetime": "number",
    "expired": "number",
    "pending": "number",
    "expiringNext30Days": "number",
    "nextExpirationDate": "string"
  },
  "membershipCard": {
    "cardNumber": "string",
    "barcode": "string",
    "qrCode": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在或未加入忠誠度計劃

### 2. 查詢忠誠度積分記錄

**端點:** `GET /{customerId}/loyalty/transactions`

**說明:** 獲取客戶的忠誠度積分交易記錄

**路徑參數:**
- `customerId` - 客戶唯一標識符

**查詢參數:**
- `type` (可選) - 按交易類型過濾 (EARNED, REDEEMED, EXPIRED, ADJUSTMENT)
- `startDate` (可選) - 按開始日期過濾
- `endDate` (可選) - 按結束日期過濾
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "customerId": "string",
  "membershipId": "string",
  "currentPoints": "number",
  "transactions": [
    {
      "transactionId": "string",
      "type": "string",
      "points": "number",
      "description": "string",
      "source": "string",
      "referenceId": "string",
      "referenceType": "string",
      "date": "string",
      "expirationDate": "string",
      "status": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "earned": "number",
    "redeemed": "number",
    "expired": "number",
    "adjustments": "number",
    "net": "number"
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在或未加入忠誠度計劃

### 3. 註冊忠誠度計劃

**端點:** `POST /{customerId}/loyalty/enrollment`

**說明:** 將客戶註冊至忠誠度計劃

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "initialPoints": "number",
  "enrollmentSource": "string",
  "referralCode": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "customerId": "string",
  "membershipId": "string",
  "enrolled": "boolean",
  "enrollmentDate": "string",
  "tier": {
    "id": "string",
    "name": "string"
  },
  "points": "number",
  "message": "string"
}
```

**狀態碼:**
- `201 Created` - 註冊成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在
- `409 Conflict` - 客戶已註冊忠誠度計劃

### 4. 調整忠誠度積分

**端點:** `POST /{customerId}/loyalty/points/adjust`

**說明:** 調整客戶忠誠度積分

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "points": "number",
  "adjustmentType": "string",
  "reason": "string",
  "description": "string",
  "referenceId": "string",
  "referenceType": "string",
  "expirationDate": "string",
  "metadata": {}
}
```

**響應:**
```json
{
  "transactionId": "string",
  "customerId": "string",
  "adjustmentType": "string",
  "points": "number",
  "currentPoints": "number",
  "date": "string",
  "status": "string"
}
```

**狀態碼:**
- `200 OK` - 調整成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在或未加入忠誠度計劃

### 5. 取消註冊忠誠度計劃

**端點:** `DELETE /{customerId}/loyalty/enrollment`

**說明:** 將客戶從忠誠度計劃中移除

**路徑參數:**
- `customerId` - 客戶唯一標識符

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
  "customerId": "string",
  "membershipId": "string",
  "disenrollmentDate": "string",
  "forfeittedPoints": "number"
}
```

**狀態碼:**
- `200 OK` - 取消註冊成功
- `404 Not Found` - 客戶不存在或未加入忠誠度計劃

## 客戶分群 API

### 1. 獲取客戶分群

**端點:** `GET /{customerId}/segments`

**說明:** 獲取客戶所屬的分群

**路徑參數:**
- `customerId` - 客戶唯一標識符

**響應:**
```json
{
  "customerId": "string",
  "segments": [
    {
      "segmentId": "string",
      "name": "string",
      "description": "string",
      "type": "string",
      "addedAt": "string",
      "expiresAt": "string",
      "attributes": {}
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在

### 2. 添加客戶到分群

**端點:** `POST /{customerId}/segments`

**說明:** 將客戶添加至指定分群

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "segmentId": "string",
  "expiresAt": "string",
  "attributes": {},
  "reason": "string"
}
```

**響應:**
```json
{
  "customerId": "string",
  "segmentId": "string",
  "segmentName": "string",
  "addedAt": "string",
  "expiresAt": "string"
}
```

**狀態碼:**
- `201 Created` - 添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶或分群不存在
- `409 Conflict` - 客戶已在分群中

### 3. 從分群中移除客戶

**端點:** `DELETE /{customerId}/segments/{segmentId}`

**說明:** 將客戶從指定分群中移除

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `segmentId` - 分群唯一標識符

**請求體:**
```json
{
  "reason": "string"
}
```

**響應:**
```json
{
  "success": "boolean",
  "customerId": "string",
  "segmentId": "string",
  "segmentName": "string",
  "removedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 移除成功
- `404 Not Found` - 客戶或分群不存在，或客戶不在分群中

## 客戶偏好 API

### 1. 獲取客戶偏好

**端點:** `GET /{customerId}/preferences`

**說明:** 獲取客戶的所有偏好設置

**路徑參數:**
- `customerId` - 客戶唯一標識符

**響應:**
```json
{
  "customerId": "string",
  "preferences": {
    "communication": {
      "email": {
        "marketing": "boolean",
        "newsletters": "boolean",
        "productUpdates": "boolean",
        "orderUpdates": "boolean"
      },
      "sms": {
        "marketing": "boolean",
        "orderUpdates": "boolean"
      },
      "push": {
        "marketing": "boolean",
        "orderUpdates": "boolean"
      },
      "frequency": "string"
    },
    "privacy": {
      "dataSharing": "boolean",
      "analytics": "boolean",
      "thirdPartyMarketing": "boolean"
    },
    "product": {
      "favoriteCategories": ["string"],
      "interests": ["string"],
      "sizePreferences": {}
    },
    "ui": {
      "language": "string",
      "currency": "string",
      "timezone": "string"
    }
  },
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在

### 2. 更新客戶偏好

**端點:** `PUT /{customerId}/preferences`

**說明:** 更新客戶的偏好設置

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "communication": {
    "email": {
      "marketing": "boolean",
      "newsletters": "boolean",
      "productUpdates": "boolean",
      "orderUpdates": "boolean"
    },
    "sms": {
      "marketing": "boolean",
      "orderUpdates": "boolean"
    },
    "push": {
      "marketing": "boolean",
      "orderUpdates": "boolean"
    },
    "frequency": "string"
  },
  "privacy": {
    "dataSharing": "boolean",
    "analytics": "boolean",
    "thirdPartyMarketing": "boolean"
  },
  "product": {
    "favoriteCategories": ["string"],
    "interests": ["string"],
    "sizePreferences": {}
  },
  "ui": {
    "language": "string",
    "currency": "string",
    "timezone": "string"
  }
}
```

**響應:**
```json
{
  "customerId": "string",
  "preferencesUpdated": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在

### 3. 更新單個偏好類別

**端點:** `PATCH /{customerId}/preferences/{category}`

**說明:** 更新客戶的特定偏好類別

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `category` - 偏好類別 (communication, privacy, product, ui)

**請求體:**
```json
{
  // 類別特定的偏好
  "key1": "value1",
  "key2": {
    "nestedKey": "value"
  }
}
```

**響應:**
```json
{
  "customerId": "string",
  "category": "string",
  "updated": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在或類別無效

## 客戶訪問記錄 API

### 1. 記錄客戶訪問

**端點:** `POST /{customerId}/visits`

**說明:** 記錄客戶訪問系統的信息

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "channel": "string",
  "deviceType": "string",
  "userAgent": "string",
  "ipAddress": "string",
  "referrer": "string",
  "utmSource": "string",
  "utmMedium": "string",
  "utmCampaign": "string",
  "location": {
    "country": "string",
    "region": "string",
    "city": "string"
  }
}
```

**響應:**
```json
{
  "visitId": "string",
  "customerId": "string",
  "timestamp": "string",
  "recorded": "boolean"
}
```

**狀態碼:**
- `201 Created` - 訪問記錄創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在

### 2. 獲取客戶訪問記錄

**端點:** `GET /{customerId}/visits`

**說明:** 獲取客戶的訪問記錄

**路徑參數:**
- `customerId` - 客戶唯一標識符

**查詢參數:**
- `startDate` (可選) - 按開始日期過濾
- `endDate` (可選) - 按結束日期過濾
- `channel` (可選) - 按渠道過濾 (WEB, MOBILE_APP, IN_STORE)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "customerId": "string",
  "visits": [
    {
      "visitId": "string",
      "timestamp": "string",
      "channel": "string",
      "deviceType": "string",
      "userAgent": "string",
      "ipAddress": "string",
      "referrer": "string",
      "utmSource": "string",
      "utmMedium": "string",
      "utmCampaign": "string",
      "location": {
        "country": "string",
        "region": "string",
        "city": "string"
      },
      "duration": "number"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "summary": {
    "totalVisits": "number",
    "uniqueDays": "number",
    "averageDuration": "number",
    "channelBreakdown": {
      "WEB": "number",
      "MOBILE_APP": "number",
      "IN_STORE": "number"
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 客戶不存在

## 客戶備註 API

### 1. 添加客戶備註

**端點:** `POST /{customerId}/notes`

**說明:** 為客戶添加新備註

**路徑參數:**
- `customerId` - 客戶唯一標識符

**請求體:**
```json
{
  "type": "string",
  "content": "string",
  "visibility": "string",
  "author": "string"
}
```

**響應:**
```json
{
  "noteId": "string",
  "customerId": "string",
  "type": "string",
  "author": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 備註添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶不存在

### 2. 獲取客戶備註

**端點:** `GET /{customerId}/notes`

**說明:** 獲取客戶的所有備註

**路徑參數:**
- `customerId` - 客戶唯一標識符

**查詢參數:**
- `type` (可選) - 按類型過濾
- `visibility` (可選) - 按可見性過濾 (PUBLIC, INTERNAL)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "customerId": "string",
  "notes": [
    {
      "noteId": "string",
      "type": "string",
      "content": "string",
      "visibility": "string",
      "author": "string",
      "createdAt": "string",
      "updatedAt": "string"
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
- `404 Not Found` - 客戶不存在

### 3. 更新客戶備註

**端點:** `PUT /{customerId}/notes/{noteId}`

**說明:** 更新客戶特定備註的信息

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `noteId` - 備註唯一標識符

**請求體:**
```json
{
  "content": "string",
  "visibility": "string"
}
```

**響應:**
```json
{
  "noteId": "string",
  "customerId": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 客戶或備註不存在

### 4. 刪除客戶備註

**端點:** `DELETE /{customerId}/notes/{noteId}`

**說明:** 刪除客戶特定備註

**路徑參數:**
- `customerId` - 客戶唯一標識符
- `noteId` - 備註唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 客戶或備註不存在

## 模型定義

### 客戶狀態 (CustomerStatus)
- `ACTIVE` - 活躍客戶
- `INACTIVE` - 不活躍客戶
- `BLOCKED` - 被封鎖客戶
- `DELETED` - 已刪除客戶

### 客戶類型 (CustomerType)
- `INDIVIDUAL` - 個人客戶
- `BUSINESS` - 企業客戶

### 客戶來源 (CustomerSource)
- `WEBSITE` - 網站註冊
- `MOBILE_APP` - 移動應用註冊
- `STORE` - 實體店鋪註冊
- `REFERRAL` - 推薦註冊
- `SOCIAL_MEDIA` - 社交媒體註冊
- `IMPORTED` - 導入客戶

### 地址類型 (AddressType)
- `SHIPPING` - 收貨地址
- `BILLING` - 帳單地址
- `BOTH` - 收貨和帳單地址

### 忠誠度積分交易類型 (LoyaltyTransactionType)
- `EARNED` - 賺取積分
- `REDEEMED` - 兌換積分
- `EXPIRED` - 過期積分
- `ADJUSTMENT` - 調整積分

### 客戶分群類型 (SegmentType)
- `AUTOMATIC` - 自動分群
- `MANUAL` - 手動分群
- `TEMPORARY` - 臨時分群
- `CAMPAIGN` - 營銷活動分群

### 訪問渠道 (VisitChannel)
- `WEB` - 網站
- `MOBILE_APP` - 移動應用
- `IN_STORE` - 實體店鋪

### 備註類型 (NoteType)
- `GENERAL` - 一般備註
- `SUPPORT` - 支持備註
- `COMPLAINT` - 投訴備註
- `PREFERENCE` - 偏好備註
- `INTERACTION` - 互動備註

### 備註可見性 (NoteVisibility)
- `PUBLIC` - 公開備註
- `INTERNAL` - 內部備註

## 錯誤代碼

| 代碼                          | 說明                                |
|------------------------------|-------------------------------------|
| CUSTOMER_NOT_FOUND           | 客戶不存在                           |
| ADDRESS_NOT_FOUND            | 地址不存在                           |
| SEGMENT_NOT_FOUND            | 分群不存在                           |
| NOTE_NOT_FOUND               | 備註不存在                           |
| DUPLICATE_EMAIL              | 電子郵件已存在                       |
| INVALID_CUSTOMER_STATUS      | 無效的客戶狀態                       |
| INVALID_ADDRESS_TYPE         | 無效的地址類型                       |
| LOYALTY_ENROLLMENT_EXISTS    | 客戶已註冊忠誠度計劃                 |
| LOYALTY_ENROLLMENT_NOT_FOUND | 客戶未註冊忠誠度計劃                 |
| SEGMENT_MEMBERSHIP_EXISTS    | 客戶已在分群中                       |
| SEGMENT_MEMBERSHIP_NOT_FOUND | 客戶不在分群中                       |
| INVALID_PREFERENCE_CATEGORY  | 無效的偏好類別                       |
| PASSWORD_RESET_RATE_LIMIT    | 密碼重設請求頻率過高                 |
| INVALID_LOYALTY_ADJUSTMENT   | 無效的忠誠度積分調整                 |
| MAX_ADDRESSES_REACHED        | 達到最大地址數量限制                 |
