# 客戶微服務 API 文檔

## API 概述

客戶微服務負責管理系統中的所有客戶相關功能，包括帳號管理、身份驗證、個人資料管理、購物車操作與收藏清單管理。該微服務提供了完整的客戶生命週期管理功能。

## API 基礎路徑

```
/api/client
```

## 認證與授權

除了註冊、登入與密碼重置等特定端點外，所有API都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 帳號管理 API

### 1. 註冊新帳號

**端點:** `POST /accounts/register`

**說明:** 創建新的客戶帳號

**請求體:**
```json
{
  "email": "string",
  "password": "string",
  "confirmPassword": "string",
  "name": "string",
  "phone": "string",
  "birthDate": "string",
  "gender": "string",
  "acceptTerms": "boolean",
  "marketingPreferences": {
    "email": "boolean",
    "sms": "boolean",
    "phone": "boolean"
  }
}
```

**響應:**
```json
{
  "userId": "string",
  "email": "string",
  "name": "string",
  "status": "string",
  "createdAt": "string",
  "requiresEmailVerification": "boolean",
  "message": "string"
}
```

**狀態碼:**
- `201 Created` - 帳號創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 電子郵件已存在

### 2. 驗證電子郵件

**端點:** `PUT /accounts/verify-email`

**說明:** 驗證客戶電子郵件

**請求體:**
```json
{
  "verificationCode": "string"
}
```

**響應:**
```json
{
  "userId": "string",
  "status": "string",
  "verifiedAt": "string",
  "message": "string"
}
```

**狀態碼:**
- `200 OK` - 電子郵件驗證成功
- `400 Bad Request` - 驗證碼無效
- `404 Not Found` - 用戶不存在

### 3. 重發驗證郵件

**端點:** `POST /accounts/resend-verification`

**說明:** 重新發送電子郵件驗證連結

**請求體:**
```json
{
  "email": "string"
}
```

**響應:**
```json
{
  "message": "string",
  "expiresAt": "string"
}
```

**狀態碼:**
- `200 OK` - 驗證郵件已重發
- `404 Not Found` - 電子郵件不存在
- `429 Too Many Requests` - 請求過於頻繁

### 4. 客戶登入

**端點:** `POST /accounts/login`

**說明:** 客戶帳號登入

**請求體:**
```json
{
  "email": "string",
  "password": "string",
  "deviceInfo": {
    "deviceType": "string",
    "os": "string",
    "osVersion": "string",
    "browser": "string",
    "browserVersion": "string"
  }
}
```

**響應:**
```json
{
  "userId": "string",
  "name": "string",
  "email": "string",
  "status": "string",
  "token": "string",
  "refreshToken": "string",
  "expiresAt": "string",
  "lastLoginAt": "string"
}
```

**狀態碼:**
- `200 OK` - 登入成功
- `400 Bad Request` - 請求資料無效
- `401 Unauthorized` - 登入失敗，帳號或密碼錯誤
- `403 Forbidden` - 帳號已被停用

### 5. 刷新令牌

**端點:** `POST /accounts/refresh-token`

**說明:** 使用刷新令牌獲取新的訪問令牌

**請求體:**
```json
{
  "refreshToken": "string"
}
```

**響應:**
```json
{
  "token": "string",
  "refreshToken": "string",
  "expiresAt": "string"
}
```

**狀態碼:**
- `200 OK` - 令牌刷新成功
- `400 Bad Request` - 刷新令牌無效
- `401 Unauthorized` - 刷新令牌已過期

### 6. 忘記密碼

**端點:** `POST /accounts/forgot-password`

**說明:** 發送密碼重置連結

**請求體:**
```json
{
  "email": "string"
}
```

**響應:**
```json
{
  "message": "string",
  "expiresAt": "string"
}
```

**狀態碼:**
- `200 OK` - 密碼重置連結已發送
- `404 Not Found` - 電子郵件不存在

### 7. 重設密碼

**端點:** `POST /accounts/reset-password`

**說明:** 使用重設碼重設密碼

**請求體:**
```json
{
  "resetCode": "string",
  "newPassword": "string",
  "confirmNewPassword": "string"
}
```

**響應:**
```json
{
  "message": "string",
  "resetAt": "string"
}
```

**狀態碼:**
- `200 OK` - 密碼重設成功
- `400 Bad Request` - 請求資料無效或重設碼無效

### 8. 變更密碼

**端點:** `PUT /accounts/change-password`

**說明:** 已登入用戶變更密碼

**請求體:**
```json
{
  "currentPassword": "string",
  "newPassword": "string",
  "confirmNewPassword": "string"
}
```

**響應:**
```json
{
  "message": "string",
  "changedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 密碼變更成功
- `400 Bad Request` - 請求資料無效
- `401 Unauthorized` - 當前密碼錯誤

### 9. 登出

**端點:** `POST /accounts/logout`

**說明:** 登出客戶帳號並使令牌無效

**請求體:**
```json
{
  "allDevices": "boolean"
}
```

**響應:**
```json
{
  "message": "string",
  "loggedOutAt": "string"
}
```

**狀態碼:**
- `200 OK` - 登出成功

## 個人資料管理 API

### 1. 獲取個人資料

**端點:** `GET /accounts/profile`

**說明:** 獲取已登入客戶的個人資料

**響應:**
```json
{
  "userId": "string",
  "email": "string",
  "name": "string",
  "phone": "string",
  "birthDate": "string",
  "gender": "string",
  "address": {
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "preferences": {
    "language": "string",
    "currency": "string",
    "newsletterSubscription": "boolean"
  },
  "memberSince": "string",
  "lastLoginAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 個人資料不存在

### 2. 更新個人資料

**端點:** `PUT /accounts/profile`

**說明:** 更新已登入客戶的個人資料

**請求體:**
```json
{
  "name": "string",
  "phone": "string",
  "birthDate": "string",
  "gender": "string",
  "address": {
    "street": "string",
    "city": "string",
    "district": "string",
    "postalCode": "string",
    "country": "string"
  },
  "preferences": {
    "language": "string",
    "currency": "string",
    "newsletterSubscription": "boolean"
  }
}
```

**響應:**
```json
{
  "userId": "string",
  "name": "string",
  "phone": "string",
  "address": {},
  "preferences": {},
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效

### 3. 更新營銷偏好

**端點:** `PUT /accounts/marketing-preferences`

**說明:** 更新客戶的營銷通訊偏好

**請求體:**
```json
{
  "email": "boolean",
  "sms": "boolean",
  "phone": "boolean",
  "push": "boolean"
}
```

**響應:**
```json
{
  "userId": "string",
  "marketingPreferences": {
    "email": "boolean",
    "sms": "boolean",
    "phone": "boolean",
    "push": "boolean"
  },
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效

### 4. 添加地址

**端點:** `POST /accounts/addresses`

**說明:** 添加新的地址到客戶的地址簿

**請求體:**
```json
{
  "label": "string",
  "name": "string",
  "phone": "string",
  "street": "string",
  "city": "string",
  "district": "string",
  "postalCode": "string",
  "country": "string",
  "isDefault": "boolean",
  "notes": "string"
}
```

**響應:**
```json
{
  "addressId": "string",
  "label": "string",
  "name": "string",
  "phone": "string",
  "street": "string",
  "city": "string",
  "district": "string",
  "postalCode": "string",
  "country": "string",
  "isDefault": "boolean",
  "notes": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 地址添加成功
- `400 Bad Request` - 請求資料無效

### 5. 獲取地址列表

**端點:** `GET /accounts/addresses`

**說明:** 獲取客戶的所有保存地址

**響應:**
```json
{
  "addresses": [
    {
      "addressId": "string",
      "label": "string",
      "name": "string",
      "phone": "string",
      "street": "string",
      "city": "string",
      "district": "string",
      "postalCode": "string",
      "country": "string",
      "isDefault": "boolean"
    }
  ],
  "defaultAddressId": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 6. 更新地址

**端點:** `PUT /accounts/addresses/{addressId}`

**說明:** 更新客戶的特定地址

**路徑參數:**
- `addressId` - 地址唯一標識符

**請求體:**
```json
{
  "label": "string",
  "name": "string",
  "phone": "string",
  "street": "string",
  "city": "string",
  "district": "string",
  "postalCode": "string",
  "country": "string",
  "isDefault": "boolean",
  "notes": "string"
}
```

**響應:**
```json
{
  "addressId": "string",
  "label": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 地址不存在

### 7. 刪除地址

**端點:** `DELETE /accounts/addresses/{addressId}`

**說明:** 從客戶的地址簿中刪除地址

**路徑參數:**
- `addressId` - 地址唯一標識符

**響應:**
```json
{
  "addressId": "string",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 地址不存在

## 購物車 API

### 1. 添加商品到購物車

**端點:** `POST /cart/items`

**說明:** 添加商品到客戶的購物車

**請求體:**
```json
{
  "productId": "string",
  "quantity": "number",
  "selectedOptions": [
    {
      "name": "string",
      "value": "string"
    }
  ]
}
```

**響應:**
```json
{
  "cartId": "string",
  "userId": "string",
  "items": [
    {
      "cartItemId": "string",
      "productId": "string",
      "name": "string",
      "quantity": "number",
      "unitPrice": {
        "value": "number",
        "currency": "string"
      },
      "selectedOptions": [
        {
          "name": "string",
          "value": "string",
          "displayName": "string",
          "priceAdjustment": {
            "value": "number",
            "currency": "string"
          }
        }
      ],
      "subtotal": {
        "value": "number",
        "currency": "string"
      },
      "thumbnailUrl": "string"
    }
  ],
  "itemCount": "number",
  "subtotal": {
    "value": "number",
    "currency": "string"
  },
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 商品添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 商品不存在
- `422 Unprocessable Entity` - 商品庫存不足

### 2. 獲取購物車內容

**端點:** `GET /cart`

**說明:** 獲取客戶的購物車內容

**響應:**
```json
{
  "cartId": "string",
  "userId": "string",
  "items": [],
  "itemCount": "number",
  "subtotal": {
    "value": "number",
    "currency": "string"
  },
  "estimatedTax": {
    "value": "number",
    "currency": "string"
  },
  "estimatedShipping": {
    "value": "number",
    "currency": "string"
  },
  "estimatedTotal": {
    "value": "number",
    "currency": "string"
  },
  "availableCoupons": [
    {
      "code": "string",
      "description": "string",
      "discountPercentage": "number"
    }
  ],
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 3. 更新購物車商品數量

**端點:** `PUT /cart/items/{cartItemId}`

**說明:** 更新購物車中特定商品的數量

**路徑參數:**
- `cartItemId` - 購物車項目唯一標識符

**請求體:**
```json
{
  "quantity": "number"
}
```

**響應:**
```json
{
  "cartItemId": "string",
  "quantity": "number",
  "subtotal": {
    "value": "number",
    "currency": "string"
  },
  "cartTotal": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 購物車項目不存在
- `422 Unprocessable Entity` - 商品庫存不足

### 4. 從購物車中刪除商品

**端點:** `DELETE /cart/items/{cartItemId}`

**說明:** 從購物車中刪除特定商品

**路徑參數:**
- `cartItemId` - 購物車項目唯一標識符

**響應:**
```json
{
  "cartItemId": "string",
  "cartTotal": {
    "value": "number",
    "currency": "string"
  },
  "itemCount": "number"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 購物車項目不存在

### 5. 清空購物車

**端點:** `DELETE /cart`

**說明:** 清空客戶的購物車

**響應:**
```json
{
  "cartId": "string",
  "userId": "string",
  "itemCount": 0,
  "clearedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 清空成功

### 6. 應用優惠券

**端點:** `POST /cart/apply-coupon`

**說明:** 對購物車應用優惠券

**請求體:**
```json
{
  "couponCode": "string"
}
```

**響應:**
```json
{
  "couponCode": "string",
  "couponDescription": "string",
  "discountPercentage": "number",
  "discountAmount": {
    "value": "number",
    "currency": "string"
  },
  "cartSubtotalBeforeDiscount": {
    "value": "number",
    "currency": "string"
  },
  "cartSubtotalAfterDiscount": {
    "value": "number",
    "currency": "string"
  },
  "cartTotal": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 優惠券應用成功
- `400 Bad Request` - 優惠券無效
- `404 Not Found` - 優惠券不存在
- `409 Conflict` - 優惠券已過期或不適用

### 7. 移除優惠券

**端點:** `DELETE /cart/coupon`

**說明:** 從購物車中移除應用的優惠券

**響應:**
```json
{
  "cartSubtotal": {
    "value": "number",
    "currency": "string"
  },
  "cartTotal": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 優惠券移除成功
- `404 Not Found` - 沒有應用的優惠券

## 收藏清單 API

### 1. 添加商品到收藏清單

**端點:** `POST /wishlist/items`

**說明:** 添加商品到客戶的收藏清單

**請求體:**
```json
{
  "productId": "string",
  "notes": "string"
}
```

**響應:**
```json
{
  "wishlistItemId": "string",
  "productId": "string",
  "addedAt": "string"
}
```

**狀態碼:**
- `201 Created` - 商品添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 商品不存在
- `409 Conflict` - 商品已在收藏清單中

### 2. 獲取收藏清單

**端點:** `GET /wishlist`

**說明:** 獲取客戶的收藏清單

**響應:**
```json
{
  "wishlist": [
    {
      "wishlistItemId": "string",
      "productId": "string",
      "name": "string",
      "price": {
        "value": "number",
        "currency": "string"
      },
      "thumbnailUrl": "string",
      "stockStatus": "string",
      "addedAt": "string",
      "notes": "string"
    }
  ],
  "totalItems": "number"
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 3. 從收藏清單中移除商品

**端點:** `DELETE /wishlist/items/{wishlistItemId}`

**說明:** 從客戶的收藏清單中移除特定商品

**路徑參數:**
- `wishlistItemId` - 收藏項目唯一標識符

**響應:**
```json
{
  "wishlistItemId": "string",
  "removedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 移除成功
- `404 Not Found` - 收藏項目不存在

### 4. 將收藏清單商品添加到購物車

**端點:** `POST /wishlist/items/{wishlistItemId}/add-to-cart`

**說明:** 將收藏清單中的商品添加到購物車

**路徑參數:**
- `wishlistItemId` - 收藏項目唯一標識符

**請求體:**
```json
{
  "quantity": "number",
  "selectedOptions": [
    {
      "name": "string",
      "value": "string"
    }
  ],
  "removeFromWishlist": "boolean"
}
```

**響應:**
```json
{
  "cartItemId": "string",
  "wishlistItemId": "string",
  "removedFromWishlist": "boolean",
  "cartTotal": {
    "value": "number",
    "currency": "string"
  }
}
```

**狀態碼:**
- `200 OK` - 添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 收藏項目不存在
- `422 Unprocessable Entity` - 商品庫存不足

## 產品評價 API

### 1. 提交產品評價

**端點:** `POST /reviews`

**說明:** 提交產品評價

**請求體:**
```json
{
  "orderId": "string",
  "productId": "string",
  "rating": "number",
  "title": "string",
  "comment": "string",
  "recommendToOthers": "boolean",
  "attachments": [
    {
      "type": "string",
      "url": "string"
    }
  ]
}
```

**響應:**
```json
{
  "reviewId": "string",
  "productId": "string",
  "userId": "string",
  "rating": "number",
  "title": "string",
  "comment": "string",
  "recommendToOthers": "boolean",
  "status": "string",
  "createdAt": "string",
  "helpfulVotes": "number",
  "verified": "boolean"
}
```

**狀態碼:**
- `201 Created` - 評價提交成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 訂單或產品不存在
- `409 Conflict` - 已經評價過此產品

### 2. 獲取用戶評價

**端點:** `GET /reviews`

**說明:** 獲取已登入客戶的所有評價

**響應:**
```json
{
  "reviews": [
    {
      "reviewId": "string",
      "productId": "string",
      "productName": "string",
      "rating": "number",
      "title": "string",
      "status": "string",
      "createdAt": "string",
      "helpfulVotes": "number"
    }
  ],
  "totalReviews": "number"
}
```

**狀態碼:**
- `200 OK` - 請求成功

### 3. 編輯評價

**端點:** `PUT /reviews/{reviewId}`

**說明:** 編輯已提交的評價

**路徑參數:**
- `reviewId` - 評價唯一標識符

**請求體:**
```json
{
  "rating": "number",
  "title": "string",
  "comment": "string",
  "recommendToOthers": "boolean",
  "attachments": []
}
```

**響應:**
```json
{
  "reviewId": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 評價不存在
- `409 Conflict` - 評價狀態不允許編輯

## 客戶支援 API

### 1. 創建支援請求

**端點:** `POST /support/tickets`

**說明:** 創建新的客戶支援請求

**請求體:**
```json
{
  "subject": "string",
  "category": "string",
  "priority": "string",
  "message": "string",
  "orderId": "string",
  "productId": "string",
  "preferredContactMethod": "string"
}
```

**響應:**
```json
{
  "ticketId": "string",
  "ticketNumber": "string",
  "userId": "string",
  "subject": "string",
  "category": "string",
  "priority": "string",
  "status": "string",
  "orderId": "string",
  "productId": "string",
  "createdAt": "string",
  "estimatedResponseTime": "string",
  "message": "string"
}
```

**狀態碼:**
- `201 Created` - 支援請求創建成功
- `400 Bad Request` - 請求資料無效

### 2. 獲取支援請求列表

**端點:** `GET /support/tickets`

**說明:** 獲取客戶的所有支援請求

**查詢參數:**
- `status` (可選) - 按狀態過濾 (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為10) - 每頁項目數

**響應:**
```json
{
  "tickets": [
    {
      "ticketId": "string",
      "ticketNumber": "string",
      "subject": "string",
      "category": "string",
      "priority": "string",
      "status": "string",
      "createdAt": "string",
      "lastUpdatedAt": "string"
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

### 3. 獲取支援請求詳情

**端點:** `GET /support/tickets/{ticketId}`

**說明:** 獲取特定支援請求的詳情

**路徑參數:**
- `ticketId` - 支援請求唯一標識符

**響應:**
```json
{
  "ticketId": "string",
  "ticketNumber": "string",
  "subject": "string",
  "category": "string",
  "priority": "string",
  "status": "string",
  "orderId": "string",
  "productId": "string",
  "createdAt": "string",
  "lastUpdatedAt": "string",
  "messages": [
    {
      "messageId": "string",
      "sender": "string",
      "senderType": "string",
      "message": "string",
      "createdAt": "string",
      "attachments": []
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 支援請求不存在

### 4. 回覆支援請求

**端點:** `POST /support/tickets/{ticketId}/messages`

**說明:** 向支援請求添加回覆

**路徑參數:**
- `ticketId` - 支援請求唯一標識符

**請求體:**
```json
{
  "message": "string",
  "attachments": [
    {
      "type": "string",
      "url": "string",
      "name": "string"
    }
  ]
}
```

**響應:**
```json
{
  "messageId": "string",
  "ticketId": "string",
  "createdAt": "string",
  "ticketStatus": "string"
}
```

**狀態碼:**
- `201 Created` - 回覆添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 支援請求不存在

### 5. 關閉支援請求

**端點:** `PUT /support/tickets/{ticketId}/close`

**說明:** 關閉支援請求

**路徑參數:**
- `ticketId` - 支援請求唯一標識符

**請求體:**
```json
{
  "feedback": "string",
  "satisfactionRating": "number"
}
```

**響應:**
```json
{
  "ticketId": "string",
  "status": "string",
  "closedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 關閉成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 支援請求不存在
- `409 Conflict` - 支援請求已關閉

## 模型定義

### 帳號狀態 (AccountStatus)
- `REGISTERED` - 已註冊
- `VERIFIED` - 已驗證
- `ACTIVE` - 活躍
- `SUSPENDED` - 已暫停
- `LOCKED` - 已鎖定
- `DEACTIVATED` - 已停用

### 性別 (Gender)
- `MALE` - 男性
- `FEMALE` - 女性
- `OTHER` - 其他
- `PREFER_NOT_TO_SAY` - 不願透露

### 設備類型 (DeviceType)
- `DESKTOP` - 桌面電腦
- `MOBILE` - 行動裝置
- `TABLET` - 平板電腦
- `OTHER` - 其他

### 優先級 (Priority)
- `LOW` - 低
- `MEDIUM` - 中
- `HIGH` - 高
- `URGENT` - 緊急

### 支援請求狀態 (TicketStatus)
- `OPEN` - 開放
- `IN_PROGRESS` - 處理中
- `RESOLVED` - 已解決
- `CLOSED` - 已關閉

### 評價狀態 (ReviewStatus)
- `PENDING` - 待審核
- `PUBLISHED` - 已發布
- `REJECTED` - 已拒絕

### 聯繫方式 (ContactMethod)
- `EMAIL` - 電子郵件
- `PHONE` - 電話
- `SMS` - 簡訊

## 錯誤代碼

| 代碼                     | 說明                             |
|--------------------------|----------------------------------|
| EMAIL_ALREADY_EXISTS     | 電子郵件已存在                   |
| INVALID_CREDENTIALS      | 登入憑證無效                     |
| ACCOUNT_LOCKED           | 帳號已被鎖定                     |
| INVALID_VERIFICATION_CODE| 驗證碼無效                       |
| INVALID_RESET_CODE       | 重設碼無效                       |
| PASSWORD_MISMATCH        | 密碼不匹配                       |
| INSUFFICIENT_STOCK       | 商品庫存不足                     |
| COUPON_INVALID           | 優惠券無效                       |
| ITEM_ALREADY_IN_WISHLIST | 商品已在收藏清單中               |
| PRODUCT_ALREADY_REVIEWED | 商品已被評價                     |
