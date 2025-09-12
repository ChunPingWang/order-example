# 客戶流程情境測試

## 情境概述

本情境測試驗證從客戶註冊、登入、瀏覽商品、下單購買到售後服務的完整客戶體驗流程，包括會員管理、購物車操作、訂單處理與客戶支援的全過程。

## 前置條件

1. 系統中已有商品資料
2. 支付系統已設置好且可以處理交易
3. 用戶權限系統已配置完成

## 測試步驟

### 1. 註冊新客戶帳號

**請求：**
```http
POST /api/client/accounts/register
Content-Type: application/json

{
  "email": "customer@example.com",
  "password": "SecureP@ssw0rd",
  "confirmPassword": "SecureP@ssw0rd",
  "name": "王大明",
  "phone": "0923456789",
  "birthDate": "1990-05-15",
  "gender": "MALE",
  "acceptTerms": true,
  "marketingPreferences": {
    "email": true,
    "sms": false,
    "phone": false
  }
}
```

**預期響應：**
```json
{
  "userId": "USER123456",
  "email": "customer@example.com",
  "name": "王大明",
  "status": "REGISTERED",
  "createdAt": "2025-09-10T09:00:00Z",
  "requiresEmailVerification": true,
  "message": "註冊成功，請查看您的電子郵件以驗證帳號"
}
```

**驗證點：**
- 客戶帳號成功創建
- 返回唯一用戶ID
- 提示用戶需要進行電子郵件驗證

### 2. 驗證電子郵件

**請求：**
```http
PUT /api/client/accounts/verify-email
Content-Type: application/json

{
  "verificationCode": "ABC123XYZ"
}
```

**預期響應：**
```json
{
  "userId": "USER123456",
  "status": "VERIFIED",
  "verifiedAt": "2025-09-10T09:15:00Z",
  "message": "電子郵件驗證成功"
}
```

**驗證點：**
- 電子郵件驗證成功
- 帳號狀態更新為已驗證

### 3. 客戶登入

**請求：**
```http
POST /api/client/accounts/login
Content-Type: application/json

{
  "email": "customer@example.com",
  "password": "SecureP@ssw0rd",
  "deviceInfo": {
    "deviceType": "MOBILE",
    "os": "iOS",
    "osVersion": "18.0",
    "browser": "Safari",
    "browserVersion": "18.0"
  }
}
```

**預期響應：**
```json
{
  "userId": "USER123456",
  "name": "王大明",
  "email": "customer@example.com",
  "status": "ACTIVE",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "expiresAt": "2025-09-10T11:15:00Z",
  "lastLoginAt": "2025-09-10T09:15:00Z"
}
```

**驗證點：**
- 登入成功
- 返回有效的身份驗證令牌
- 更新最後登入時間

### 4. 更新客戶資料

**請求：**
```http
PUT /api/client/accounts/profile
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "address": {
    "street": "和平東路三段100號",
    "city": "台北市",
    "district": "大安區",
    "postalCode": "106",
    "country": "Taiwan"
  },
  "phone": "0934567890",
  "preferences": {
    "language": "zh-TW",
    "currency": "TWD",
    "newsletterSubscription": true
  }
}
```

**預期響應：**
```json
{
  "userId": "USER123456",
  "name": "王大明",
  "phone": "0934567890",
  "address": {
    "street": "和平東路三段100號",
    "city": "台北市",
    "district": "大安區",
    "postalCode": "106",
    "country": "Taiwan"
  },
  "preferences": {
    "language": "zh-TW",
    "currency": "TWD",
    "newsletterSubscription": true
  },
  "updatedAt": "2025-09-10T09:30:00Z"
}
```

**驗證點：**
- 客戶資料成功更新
- 返回完整的更新後資料

### 5. 瀏覽商品列表

**請求：**
```http
GET /api/merchandise/products?category=ELECTRONICS&page=1&pageSize=10
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**預期響應：**
```json
{
  "products": [
    {
      "productId": "PROD001",
      "name": "高效能筆記型電腦",
      "description": "Intel i9 處理器, 32GB RAM, 1TB SSD, 15.6吋螢幕",
      "price": {
        "value": 35000,
        "currency": "TWD"
      },
      "category": "ELECTRONICS",
      "subCategory": "LAPTOPS",
      "rating": 4.7,
      "reviewCount": 235,
      "stockStatus": "IN_STOCK",
      "thumbnailUrl": "https://example.com/images/products/laptop-thumb.jpg",
      "tags": ["筆電", "高效能", "Intel"]
    },
    {
      "productId": "PROD002",
      "name": "專業級無線滑鼠",
      "description": "高精準度雷射感應, 可程式化按鍵, 充電式",
      "price": {
        "value": 1500,
        "currency": "TWD"
      },
      "category": "ELECTRONICS",
      "subCategory": "COMPUTER_ACCESSORIES",
      "rating": 4.5,
      "reviewCount": 182,
      "stockStatus": "IN_STOCK",
      "thumbnailUrl": "https://example.com/images/products/mouse-thumb.jpg",
      "tags": ["滑鼠", "無線", "配件"]
    }
    // 更多商品...
  ],
  "pagination": {
    "page": 1,
    "pageSize": 10,
    "totalItems": 56,
    "totalPages": 6
  },
  "filters": {
    "categories": ["ELECTRONICS", "COMPUTER_ACCESSORIES"],
    "priceRange": {
      "min": 500,
      "max": 50000,
      "currency": "TWD"
    }
  }
}
```

**驗證點：**
- 成功返回商品列表
- 包含分頁資訊
- 顯示過濾選項

### 6. 獲取商品詳情

**請求：**
```http
GET /api/merchandise/products/PROD001
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**預期響應：**
```json
{
  "productId": "PROD001",
  "name": "高效能筆記型電腦",
  "description": "Intel i9 處理器, 32GB RAM, 1TB SSD, 15.6吋螢幕",
  "fullDescription": "這款高效能筆記型電腦搭載最新第13代Intel i9處理器，擁有8核心16執行緒，基本頻率3.2GHz，最高可達5.0GHz。配備32GB DDR5記憶體，可輕鬆應對多工處理需求。1TB NVMe SSD讀寫速度極快，提供充足的儲存空間。15.6吋IPS螢幕解析度為2560x1440，色準Delta E<2，適合設計與影像處理工作。機身僅重1.8公斤，續航力可達10小時。",
  "specifications": [
    {
      "name": "處理器",
      "value": "Intel Core i9-13900H"
    },
    {
      "name": "記憶體",
      "value": "32GB DDR5 4800MHz"
    },
    {
      "name": "儲存空間",
      "value": "1TB NVMe PCIe 4.0 SSD"
    },
    {
      "name": "顯示器",
      "value": "15.6吋 IPS 2560x1440 165Hz"
    },
    {
      "name": "顯示卡",
      "value": "NVIDIA GeForce RTX 4070 8GB GDDR6"
    },
    {
      "name": "作業系統",
      "value": "Windows 11 Pro"
    }
  ],
  "price": {
    "value": 35000,
    "currency": "TWD"
  },
  "originalPrice": {
    "value": 38000,
    "currency": "TWD"
  },
  "discountPercentage": 8,
  "availability": {
    "inStock": true,
    "availableQuantity": 23,
    "leadTimeInDays": 1
  },
  "images": [
    {
      "url": "https://example.com/images/products/laptop-main.jpg",
      "type": "MAIN"
    },
    {
      "url": "https://example.com/images/products/laptop-angle1.jpg",
      "type": "GALLERY"
    },
    {
      "url": "https://example.com/images/products/laptop-angle2.jpg",
      "type": "GALLERY"
    }
  ],
  "rating": {
    "average": 4.7,
    "count": 235,
    "distribution": {
      "5": 170,
      "4": 50,
      "3": 10,
      "2": 3,
      "1": 2
    }
  },
  "categories": [
    {
      "id": "CAT001",
      "name": "電子產品"
    },
    {
      "id": "CAT003",
      "name": "筆記型電腦"
    }
  ],
  "relatedProducts": [
    "PROD005",
    "PROD008",
    "PROD012"
  ]
}
```

**驗證點：**
- 返回商品詳細資訊
- 包含完整的產品規格
- 顯示相關商品推薦

### 7. 新增商品到購物車

**請求：**
```http
POST /api/client/cart/items
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "productId": "PROD001",
  "quantity": 1,
  "selectedOptions": [
    {
      "name": "warranty",
      "value": "extended"
    }
  ]
}
```

**預期響應：**
```json
{
  "cartId": "CART98765",
  "userId": "USER123456",
  "items": [
    {
      "cartItemId": "CI123001",
      "productId": "PROD001",
      "name": "高效能筆記型電腦",
      "quantity": 1,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "selectedOptions": [
        {
          "name": "warranty",
          "value": "extended",
          "displayName": "延長保固 (2年)",
          "priceAdjustment": {
            "value": 3000,
            "currency": "TWD"
          }
        }
      ],
      "subtotal": {
        "value": 38000,
        "currency": "TWD"
      },
      "thumbnailUrl": "https://example.com/images/products/laptop-thumb.jpg"
    }
  ],
  "itemCount": 1,
  "subtotal": {
    "value": 38000,
    "currency": "TWD"
  },
  "updatedAt": "2025-09-10T10:00:00Z"
}
```

**驗證點：**
- 商品成功添加到購物車
- 計算了正確的價格
- 包含選擇的選項

### 8. 更新購物車商品數量

**請求：**
```http
PUT /api/client/cart/items/CI123001
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "quantity": 2
}
```

**預期響應：**
```json
{
  "cartItemId": "CI123001",
  "quantity": 2,
  "subtotal": {
    "value": 76000,
    "currency": "TWD"
  },
  "cartTotal": {
    "value": 76000,
    "currency": "TWD"
  }
}
```

**驗證點：**
- 商品數量成功更新
- 小計和購物車總金額也相應更新

### 9. 查看購物車內容

**請求：**
```http
GET /api/client/cart
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**預期響應：**
```json
{
  "cartId": "CART98765",
  "userId": "USER123456",
  "items": [
    {
      "cartItemId": "CI123001",
      "productId": "PROD001",
      "name": "高效能筆記型電腦",
      "quantity": 2,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "selectedOptions": [
        {
          "name": "warranty",
          "value": "extended",
          "displayName": "延長保固 (2年)",
          "priceAdjustment": {
            "value": 3000,
            "currency": "TWD"
          }
        }
      ],
      "subtotal": {
        "value": 76000,
        "currency": "TWD"
      },
      "thumbnailUrl": "https://example.com/images/products/laptop-thumb.jpg"
    }
  ],
  "itemCount": 2,
  "subtotal": {
    "value": 76000,
    "currency": "TWD"
  },
  "estimatedTax": {
    "value": 3800,
    "currency": "TWD"
  },
  "estimatedShipping": {
    "value": 0,
    "currency": "TWD"
  },
  "estimatedTotal": {
    "value": 79800,
    "currency": "TWD"
  },
  "availableCoupons": [
    {
      "code": "NEWUSER10",
      "description": "新用戶9折優惠",
      "discountPercentage": 10
    }
  ],
  "updatedAt": "2025-09-10T10:10:00Z"
}
```

**驗證點：**
- 返回購物車完整內容
- 顯示小計、稅金和總金額
- 提示可用優惠券

### 10. 應用優惠券

**請求：**
```http
POST /api/client/cart/apply-coupon
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "couponCode": "NEWUSER10"
}
```

**預期響應：**
```json
{
  "couponCode": "NEWUSER10",
  "couponDescription": "新用戶9折優惠",
  "discountPercentage": 10,
  "discountAmount": {
    "value": 7600,
    "currency": "TWD"
  },
  "cartSubtotalBeforeDiscount": {
    "value": 76000,
    "currency": "TWD"
  },
  "cartSubtotalAfterDiscount": {
    "value": 68400,
    "currency": "TWD"
  },
  "cartTotal": {
    "value": 71820,
    "currency": "TWD"
  }
}
```

**驗證點：**
- 優惠券成功應用
- 正確計算折扣金額
- 更新購物車總金額

### 11. 創建訂單

**請求：**
```http
POST /api/sales/orders
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "shippingAddress": {
    "name": "王大明",
    "phone": "0934567890",
    "street": "和平東路三段100號",
    "city": "台北市",
    "district": "大安區",
    "postalCode": "106",
    "country": "Taiwan"
  },
  "billingAddressSameAsShipping": true,
  "paymentMethod": "CREDIT_CARD",
  "shippingMethod": "STANDARD",
  "notes": "請於週間送達"
}
```

**預期響應：**
```json
{
  "orderId": "ORD123456",
  "orderNumber": "ORDER-2025-12345",
  "userId": "USER123456",
  "status": "CREATED",
  "items": [
    {
      "orderItemId": "OI123001",
      "productId": "PROD001",
      "name": "高效能筆記型電腦",
      "quantity": 2,
      "unitPrice": {
        "value": 35000,
        "currency": "TWD"
      },
      "selectedOptions": [
        {
          "name": "warranty",
          "value": "extended",
          "displayName": "延長保固 (2年)",
          "priceAdjustment": {
            "value": 3000,
            "currency": "TWD"
          }
        }
      ],
      "subtotal": {
        "value": 76000,
        "currency": "TWD"
      }
    }
  ],
  "subtotal": {
    "value": 76000,
    "currency": "TWD"
  },
  "discount": {
    "value": 7600,
    "currency": "TWD",
    "couponCode": "NEWUSER10"
  },
  "tax": {
    "value": 3420,
    "currency": "TWD"
  },
  "shipping": {
    "value": 0,
    "currency": "TWD",
    "method": "STANDARD"
  },
  "total": {
    "value": 71820,
    "currency": "TWD"
  },
  "shippingAddress": {
    "name": "王大明",
    "phone": "0934567890",
    "street": "和平東路三段100號",
    "city": "台北市",
    "district": "大安區",
    "postalCode": "106",
    "country": "Taiwan"
  },
  "billingAddress": {
    "name": "王大明",
    "phone": "0934567890",
    "street": "和平東路三段100號",
    "city": "台北市",
    "district": "大安區",
    "postalCode": "106",
    "country": "Taiwan"
  },
  "paymentMethod": "CREDIT_CARD",
  "paymentUrl": "https://example.com/payment/checkout?orderId=ORD123456",
  "createdAt": "2025-09-10T10:30:00Z",
  "expiresAt": "2025-09-10T11:30:00Z",
  "notes": "請於週間送達"
}
```

**驗證點：**
- 訂單成功創建
- 返回訂單明細與支付連結
- 包含有效期限

### 12. 處理支付

**請求：**
```http
POST /api/payments/process
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "orderId": "ORD123456",
  "paymentMethod": "CREDIT_CARD",
  "cardDetails": {
    "number": "4111XXXXXXXX1111",
    "expiryMonth": 12,
    "expiryYear": 2027,
    "cvv": "***",
    "holderName": "WANG TA MING"
  },
  "amount": {
    "value": 71820,
    "currency": "TWD"
  },
  "billingAddress": {
    "name": "王大明",
    "street": "和平東路三段100號",
    "city": "台北市",
    "district": "大安區",
    "postalCode": "106",
    "country": "Taiwan"
  }
}
```

**預期響應：**
```json
{
  "paymentId": "PAY456789",
  "orderId": "ORD123456",
  "status": "SUCCESSFUL",
  "transactionId": "TXN987654321",
  "amount": {
    "value": 71820,
    "currency": "TWD"
  },
  "method": "CREDIT_CARD",
  "cardInfo": {
    "brand": "VISA",
    "last4": "1111",
    "expiryMonth": 12,
    "expiryYear": 2027
  },
  "processedAt": "2025-09-10T10:35:00Z",
  "receipt": {
    "url": "https://example.com/payments/receipt/PAY456789",
    "number": "REC-2025-45678"
  }
}
```

**驗證點：**
- 支付處理成功
- 返回交易ID與收據資訊
- 顯示信用卡安全資訊（僅末四碼）

### 13. 查詢訂單狀態

**請求：**
```http
GET /api/sales/orders/ORD123456
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**預期響應：**
```json
{
  "orderId": "ORD123456",
  "orderNumber": "ORDER-2025-12345",
  "userId": "USER123456",
  "status": "PAID",
  "items": [...],
  "total": {
    "value": 71820,
    "currency": "TWD"
  },
  "paymentStatus": "COMPLETED",
  "paymentDetails": {
    "paymentId": "PAY456789",
    "method": "CREDIT_CARD",
    "cardInfo": {
      "brand": "VISA",
      "last4": "1111"
    },
    "paidAt": "2025-09-10T10:35:00Z"
  },
  "fulfillmentStatus": "PROCESSING",
  "timeline": [
    {
      "status": "CREATED",
      "timestamp": "2025-09-10T10:30:00Z"
    },
    {
      "status": "PAID",
      "timestamp": "2025-09-10T10:35:00Z"
    },
    {
      "status": "PROCESSING",
      "timestamp": "2025-09-10T10:40:00Z"
    }
  ],
  "estimatedDelivery": "2025-09-15"
}
```

**驗證點：**
- 訂單狀態更新為已支付
- 包含支付詳情
- 顯示訂單時間線和預計交付日期

### 14. 提交商品評價

**請求：**
```http
POST /api/client/reviews
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "orderId": "ORD123456",
  "productId": "PROD001",
  "rating": 5,
  "title": "超高CP值筆電",
  "comment": "效能非常優異，散熱良好，電池續航力也令人滿意。攜帶方便，外型設計簡潔大方。非常推薦給需要高效能的使用者。",
  "recommendToOthers": true,
  "attachments": [
    {
      "type": "IMAGE",
      "url": "https://example.com/user-uploads/review-image-123.jpg"
    }
  ]
}
```

**預期響應：**
```json
{
  "reviewId": "REV789012",
  "productId": "PROD001",
  "userId": "USER123456",
  "rating": 5,
  "title": "超高CP值筆電",
  "comment": "效能非常優異，散熱良好，電池續航力也令人滿意。攜帶方便，外型設計簡潔大方。非常推薦給需要高效能的使用者。",
  "recommendToOthers": true,
  "status": "PUBLISHED",
  "createdAt": "2025-09-20T15:00:00Z",
  "helpfulVotes": 0,
  "verified": true
}
```

**驗證點：**
- 評價成功提交
- 標記為經過驗證（已購買）
- 可供其他用戶查看

### 15. 提交客戶支援請求

**請求：**
```http
POST /api/client/support/tickets
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "subject": "延長保固啟用問題",
  "category": "WARRANTY",
  "priority": "MEDIUM",
  "message": "我於上週購買的筆電已附加延長保固，但系統顯示尚未啟用，請協助處理。",
  "orderId": "ORD123456",
  "productId": "PROD001",
  "preferredContactMethod": "EMAIL"
}
```

**預期響應：**
```json
{
  "ticketId": "TKT345678",
  "ticketNumber": "SUPP-2025-34567",
  "userId": "USER123456",
  "subject": "延長保固啟用問題",
  "category": "WARRANTY",
  "priority": "MEDIUM",
  "status": "OPEN",
  "orderId": "ORD123456",
  "productId": "PROD001",
  "createdAt": "2025-09-25T11:00:00Z",
  "estimatedResponseTime": "24小時內",
  "message": "感謝您提交支援請求。我們已收到您的訊息，客服人員將在24小時內回覆您。"
}
```

**驗證點：**
- 客戶支援請求成功創建
- 返回唯一票證ID與號碼
- 提供預計回應時間

## 期望結果

1. 客戶可以順利完成註冊、驗證與登入
2. 客戶可以瀏覽商品、將商品添加到購物車
3. 客戶可以成功提交訂單並完成支付
4. 客戶可以查看訂單狀態、提交評價與支援請求

## 錯誤處理測試案例

### 1. 註冊時使用已存在的電子郵件

**請求：**
```http
POST /api/client/accounts/register
Content-Type: application/json

{
  "email": "customer@example.com",
  "password": "SecureP@ssw0rd",
  "name": "張小明"
}
```

**預期響應：**
```json
{
  "error": "EMAIL_ALREADY_EXISTS",
  "message": "此電子郵件已被註冊，請使用其他電子郵件或嘗試登入",
  "statusCode": 400
}
```

### 2. 商品庫存不足

**請求：**
```http
POST /api/client/cart/items
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "productId": "PROD001",
  "quantity": 50
}
```

**預期響應：**
```json
{
  "error": "INSUFFICIENT_STOCK",
  "message": "商品庫存不足，目前庫存量: 23",
  "details": {
    "requestedQuantity": 50,
    "availableQuantity": 23,
    "productId": "PROD001"
  },
  "statusCode": 400
}
```
