# 產品微服務 API 文檔

## API 概述

產品微服務負責處理系統中的所有產品相關功能，包括產品管理、產品分類、產品變體、產品屬性和產品評論等。該微服務提供完整的產品資訊管理功能，支持產品從創建到下架的全生命週期管理。

## API 基礎路徑

```
/api/products
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 產品管理 API

### 1. 創建產品

**端點:** `POST /`

**說明:** 創建新產品

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "sku": "string",
  "barcode": "string",
  "categoryId": "string",
  "brandId": "string",
  "price": {
    "amount": "number",
    "currency": "string"
  },
  "cost": {
    "amount": "number",
    "currency": "string"
  },
  "taxRate": "number",
  "status": "string",
  "attributes": [
    {
      "name": "string",
      "value": "string"
    }
  ],
  "images": [
    {
      "url": "string",
      "alt": "string",
      "isPrimary": "boolean",
      "order": "number"
    }
  ],
  "tags": ["string"],
  "dimensions": {
    "length": "number",
    "width": "number",
    "height": "number",
    "unit": "string"
  },
  "weight": {
    "value": "number",
    "unit": "string"
  },
  "metadata": {}
}
```

**響應:**
```json
{
  "productId": "string",
  "name": "string",
  "description": "string",
  "sku": "string",
  "barcode": "string",
  "categoryId": "string",
  "categoryName": "string",
  "brandId": "string",
  "brandName": "string",
  "price": {
    "amount": "number",
    "currency": "string"
  },
  "status": "string",
  "createdAt": "string",
  "primaryImage": "string"
}
```

**狀態碼:**
- `201 Created` - 產品創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - SKU或條碼已存在

### 2. 獲取產品詳情

**端點:** `GET /{productId}`

**說明:** 獲取特定產品的詳細信息

**路徑參數:**
- `productId` - 產品唯一標識符

**查詢參數:**
- `includeVariants` (可選, 默認為false) - 是否包含產品變體
- `includeAttributes` (可選, 默認為true) - 是否包含產品屬性
- `includeReviews` (可選, 默認為false) - 是否包含產品評論
- `includeInventory` (可選, 默認為false) - 是否包含庫存信息

**響應:**
```json
{
  "productId": "string",
  "name": "string",
  "description": "string",
  "sku": "string",
  "barcode": "string",
  "categoryId": "string",
  "categoryName": "string",
  "categoryPath": ["string"],
  "brandId": "string",
  "brandName": "string",
  "price": {
    "amount": "number",
    "currency": "string",
    "compareAtPrice": "number",
    "onSale": "boolean",
    "salePercentage": "number"
  },
  "cost": {
    "amount": "number",
    "currency": "string"
  },
  "taxRate": "number",
  "taxable": "boolean",
  "status": "string",
  "attributes": [
    {
      "name": "string",
      "value": "string",
      "filterable": "boolean"
    }
  ],
  "images": [
    {
      "id": "string",
      "url": "string",
      "alt": "string",
      "width": "number",
      "height": "number",
      "isPrimary": "boolean",
      "order": "number"
    }
  ],
  "tags": ["string"],
  "dimensions": {
    "length": "number",
    "width": "number",
    "height": "number",
    "unit": "string"
  },
  "weight": {
    "value": "number",
    "unit": "string"
  },
  "variants": [
    {
      "variantId": "string",
      "name": "string",
      "sku": "string",
      "price": {
        "amount": "number",
        "currency": "string"
      },
      "attributes": [
        {
          "name": "string",
          "value": "string"
        }
      ],
      "image": "string",
      "inventory": {
        "quantity": "number",
        "status": "string"
      }
    }
  ],
  "inventory": {
    "quantity": "number",
    "status": "string",
    "backorderAllowed": "boolean",
    "reorderLevel": "number",
    "reorderQuantity": "number"
  },
  "reviews": {
    "averageRating": "number",
    "count": "number",
    "distributionByRating": {
      "5": "number",
      "4": "number",
      "3": "number",
      "2": "number",
      "1": "number"
    }
  },
  "relatedProducts": [
    {
      "productId": "string",
      "name": "string",
      "price": {
        "amount": "number",
        "currency": "string"
      },
      "image": "string"
    }
  ],
  "createdAt": "string",
  "updatedAt": "string",
  "publishedAt": "string",
  "seoDetails": {
    "title": "string",
    "description": "string",
    "keywords": ["string"],
    "slug": "string"
  },
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 產品不存在

### 3. 更新產品

**端點:** `PUT /{productId}`

**說明:** 更新特定產品的信息

**路徑參數:**
- `productId` - 產品唯一標識符

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "sku": "string",
  "barcode": "string",
  "categoryId": "string",
  "brandId": "string",
  "price": {
    "amount": "number",
    "currency": "string"
  },
  "cost": {
    "amount": "number",
    "currency": "string"
  },
  "taxRate": "number",
  "status": "string",
  "attributes": [
    {
      "name": "string",
      "value": "string"
    }
  ],
  "tags": ["string"],
  "dimensions": {
    "length": "number",
    "width": "number",
    "height": "number",
    "unit": "string"
  },
  "weight": {
    "value": "number",
    "unit": "string"
  },
  "seoDetails": {
    "title": "string",
    "description": "string",
    "keywords": ["string"],
    "slug": "string"
  },
  "metadata": {}
}
```

**響應:**
```json
{
  "productId": "string",
  "name": "string",
  "sku": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在
- `409 Conflict` - SKU或條碼與其他產品衝突

### 4. 刪除產品

**端點:** `DELETE /{productId}`

**說明:** 刪除特定產品

**路徑參數:**
- `productId` - 產品唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 產品不存在
- `409 Conflict` - 產品有關聯訂單，無法刪除

### 5. 查詢產品列表

**端點:** `GET /`

**說明:** 獲取符合條件的產品列表

**查詢參數:**
- `query` (可選) - 文本搜索，匹配名稱和描述
- `categoryId` (可選) - 按類別過濾
- `brandId` (可選) - 按品牌過濾
- `status` (可選) - 按狀態過濾 (ACTIVE, DRAFT, ARCHIVED)
- `minPrice` (可選) - 按最小價格過濾
- `maxPrice` (可選) - 按最大價格過濾
- `tags` (可選) - 按標籤過濾，多個標籤以逗號分隔
- `attributes` (可選) - 按屬性過濾，格式為name:value，多個屬性以逗號分隔
- `sort` (可選, 默認為createdAt:desc) - 排序字段和方向 (name:asc, name:desc, price:asc, price:desc, createdAt:asc, createdAt:desc)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "products": [
    {
      "productId": "string",
      "name": "string",
      "sku": "string",
      "price": {
        "amount": "number",
        "currency": "string",
        "compareAtPrice": "number",
        "onSale": "boolean"
      },
      "categoryId": "string",
      "categoryName": "string",
      "brandId": "string",
      "brandName": "string",
      "status": "string",
      "image": "string",
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
    "categories": [
      {
        "id": "string",
        "name": "string",
        "count": "number"
      }
    ],
    "brands": [
      {
        "id": "string",
        "name": "string",
        "count": "number"
      }
    ],
    "priceRange": {
      "min": "number",
      "max": "number"
    },
    "attributes": [
      {
        "name": "string",
        "values": [
          {
            "value": "string",
            "count": "number"
          }
        ]
      }
    ]
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

### 6. 更新產品狀態

**端點:** `PUT /{productId}/status`

**說明:** 更新特定產品的狀態

**路徑參數:**
- `productId` - 產品唯一標識符

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
  "productId": "string",
  "status": "string",
  "previousStatus": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在

## 產品變體 API

### 1. 創建產品變體

**端點:** `POST /{productId}/variants`

**說明:** 為產品創建新變體

**路徑參數:**
- `productId` - 產品唯一標識符

**請求體:**
```json
{
  "name": "string",
  "sku": "string",
  "barcode": "string",
  "price": {
    "amount": "number",
    "currency": "string"
  },
  "cost": {
    "amount": "number",
    "currency": "string"
  },
  "attributes": [
    {
      "name": "string",
      "value": "string"
    }
  ],
  "imageUrl": "string",
  "dimensions": {
    "length": "number",
    "width": "number",
    "height": "number",
    "unit": "string"
  },
  "weight": {
    "value": "number",
    "unit": "string"
  },
  "inventory": {
    "quantity": "number",
    "reorderLevel": "number",
    "reorderQuantity": "number"
  },
  "metadata": {}
}
```

**響應:**
```json
{
  "variantId": "string",
  "productId": "string",
  "name": "string",
  "sku": "string",
  "price": {
    "amount": "number",
    "currency": "string"
  },
  "attributes": [
    {
      "name": "string",
      "value": "string"
    }
  ],
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 變體創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在
- `409 Conflict` - SKU或條碼已存在

### 2. 獲取產品變體詳情

**端點:** `GET /{productId}/variants/{variantId}`

**說明:** 獲取特定產品變體的詳細信息

**路徑參數:**
- `productId` - 產品唯一標識符
- `variantId` - 變體唯一標識符

**響應:**
```json
{
  "variantId": "string",
  "productId": "string",
  "parentName": "string",
  "name": "string",
  "sku": "string",
  "barcode": "string",
  "price": {
    "amount": "number",
    "currency": "string",
    "compareAtPrice": "number",
    "onSale": "boolean",
    "salePercentage": "number"
  },
  "cost": {
    "amount": "number",
    "currency": "string"
  },
  "attributes": [
    {
      "name": "string",
      "value": "string"
    }
  ],
  "image": {
    "id": "string",
    "url": "string",
    "alt": "string",
    "width": "number",
    "height": "number"
  },
  "dimensions": {
    "length": "number",
    "width": "number",
    "height": "number",
    "unit": "string"
  },
  "weight": {
    "value": "number",
    "unit": "string"
  },
  "inventory": {
    "quantity": "number",
    "status": "string",
    "backorderAllowed": "boolean",
    "reorderLevel": "number",
    "reorderQuantity": "number"
  },
  "createdAt": "string",
  "updatedAt": "string",
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 產品或變體不存在

### 3. 更新產品變體

**端點:** `PUT /{productId}/variants/{variantId}`

**說明:** 更新特定產品變體的信息

**路徑參數:**
- `productId` - 產品唯一標識符
- `variantId` - 變體唯一標識符

**請求體:**
```json
{
  "name": "string",
  "sku": "string",
  "barcode": "string",
  "price": {
    "amount": "number",
    "currency": "string"
  },
  "cost": {
    "amount": "number",
    "currency": "string"
  },
  "attributes": [
    {
      "name": "string",
      "value": "string"
    }
  ],
  "imageUrl": "string",
  "dimensions": {
    "length": "number",
    "width": "number",
    "height": "number",
    "unit": "string"
  },
  "weight": {
    "value": "number",
    "unit": "string"
  },
  "metadata": {}
}
```

**響應:**
```json
{
  "variantId": "string",
  "productId": "string",
  "name": "string",
  "sku": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品或變體不存在
- `409 Conflict` - SKU或條碼與其他產品/變體衝突

### 4. 刪除產品變體

**端點:** `DELETE /{productId}/variants/{variantId}`

**說明:** 刪除特定產品變體

**路徑參數:**
- `productId` - 產品唯一標識符
- `variantId` - 變體唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 產品或變體不存在
- `409 Conflict` - 變體有關聯訂單，無法刪除

### 5. 獲取產品所有變體

**端點:** `GET /{productId}/variants`

**說明:** 獲取特定產品的所有變體

**路徑參數:**
- `productId` - 產品唯一標識符

**響應:**
```json
{
  "productId": "string",
  "productName": "string",
  "variants": [
    {
      "variantId": "string",
      "name": "string",
      "sku": "string",
      "price": {
        "amount": "number",
        "currency": "string"
      },
      "attributes": [
        {
          "name": "string",
          "value": "string"
        }
      ],
      "imageUrl": "string",
      "inventory": {
        "quantity": "number",
        "status": "string"
      }
    }
  ],
  "variantAttributes": [
    {
      "name": "string",
      "values": ["string"]
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 產品不存在

## 產品圖像 API

### 1. 添加產品圖像

**端點:** `POST /{productId}/images`

**說明:** 為產品添加圖像

**路徑參數:**
- `productId` - 產品唯一標識符

**請求體:**
```json
{
  "url": "string",
  "alt": "string",
  "isPrimary": "boolean",
  "order": "number"
}
```

**響應:**
```json
{
  "imageId": "string",
  "productId": "string",
  "url": "string",
  "alt": "string",
  "isPrimary": "boolean",
  "order": "number",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 圖像添加成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在

### 2. 更新產品圖像

**端點:** `PUT /{productId}/images/{imageId}`

**說明:** 更新特定產品圖像的信息

**路徑參數:**
- `productId` - 產品唯一標識符
- `imageId` - 圖像唯一標識符

**請求體:**
```json
{
  "url": "string",
  "alt": "string",
  "isPrimary": "boolean",
  "order": "number"
}
```

**響應:**
```json
{
  "imageId": "string",
  "productId": "string",
  "url": "string",
  "alt": "string",
  "isPrimary": "boolean",
  "order": "number",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品或圖像不存在

### 3. 刪除產品圖像

**端點:** `DELETE /{productId}/images/{imageId}`

**說明:** 刪除特定產品圖像

**路徑參數:**
- `productId` - 產品唯一標識符
- `imageId` - 圖像唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 產品或圖像不存在

### 4. 設置主要產品圖像

**端點:** `PUT /{productId}/images/{imageId}/primary`

**說明:** 將特定產品圖像設為主要圖像

**路徑參數:**
- `productId` - 產品唯一標識符
- `imageId` - 圖像唯一標識符

**響應:**
```json
{
  "productId": "string",
  "imageId": "string",
  "isPrimary": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 設置成功
- `404 Not Found` - 產品或圖像不存在

### 5. 排序產品圖像

**端點:** `PUT /{productId}/images/sort`

**說明:** 調整產品圖像的顯示順序

**路徑參數:**
- `productId` - 產品唯一標識符

**請求體:**
```json
{
  "imageIds": ["string"]
}
```

**響應:**
```json
{
  "productId": "string",
  "success": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 排序成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在

## 類別管理 API

### 1. 創建類別

**端點:** `POST /categories`

**說明:** 創建新產品類別

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "parentId": "string",
  "image": "string",
  "slug": "string",
  "metaTitle": "string",
  "metaDescription": "string",
  "status": "string",
  "order": "number"
}
```

**響應:**
```json
{
  "categoryId": "string",
  "name": "string",
  "description": "string",
  "parentId": "string",
  "parentName": "string",
  "level": "number",
  "slug": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 類別創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 類別名稱或slug已存在

### 2. 獲取類別詳情

**端點:** `GET /categories/{categoryId}`

**說明:** 獲取特定產品類別的詳細信息

**路徑參數:**
- `categoryId` - 類別唯一標識符

**響應:**
```json
{
  "categoryId": "string",
  "name": "string",
  "description": "string",
  "parentId": "string",
  "parentName": "string",
  "path": [
    {
      "id": "string",
      "name": "string"
    }
  ],
  "level": "number",
  "image": "string",
  "slug": "string",
  "metaTitle": "string",
  "metaDescription": "string",
  "status": "string",
  "order": "number",
  "productCount": "number",
  "childCategories": [
    {
      "id": "string",
      "name": "string",
      "productCount": "number"
    }
  ],
  "createdAt": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 類別不存在

### 3. 更新類別

**端點:** `PUT /categories/{categoryId}`

**說明:** 更新特定產品類別的信息

**路徑參數:**
- `categoryId` - 類別唯一標識符

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "parentId": "string",
  "image": "string",
  "slug": "string",
  "metaTitle": "string",
  "metaDescription": "string",
  "status": "string",
  "order": "number"
}
```

**響應:**
```json
{
  "categoryId": "string",
  "name": "string",
  "parentId": "string",
  "slug": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 類別不存在
- `409 Conflict` - 類別名稱或slug與其他類別衝突

### 4. 刪除類別

**端點:** `DELETE /categories/{categoryId}`

**說明:** 刪除特定產品類別

**路徑參數:**
- `categoryId` - 類別唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 類別不存在
- `409 Conflict` - 類別有子類別或關聯產品，無法刪除

### 5. 獲取類別列表

**端點:** `GET /categories`

**說明:** 獲取所有產品類別

**查詢參數:**
- `parentId` (可選) - 按父類別過濾，傳null獲取頂層類別
- `status` (可選) - 按狀態過濾 (ACTIVE, INACTIVE)
- `includeProducts` (可選, 默認為false) - 是否包含產品數量
- `flat` (可選, 默認為false) - 是否返回扁平結構而非樹形結構

**響應:**
```json
{
  "categories": [
    {
      "categoryId": "string",
      "name": "string",
      "description": "string",
      "parentId": "string",
      "level": "number",
      "image": "string",
      "slug": "string",
      "status": "string",
      "order": "number",
      "productCount": "number",
      "children": [
        {
          // 遞歸結構，子類別
        }
      ]
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功

## 品牌管理 API

### 1. 創建品牌

**端點:** `POST /brands`

**說明:** 創建新產品品牌

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "logo": "string",
  "website": "string",
  "slug": "string",
  "metaTitle": "string",
  "metaDescription": "string",
  "status": "string"
}
```

**響應:**
```json
{
  "brandId": "string",
  "name": "string",
  "description": "string",
  "logo": "string",
  "slug": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 品牌創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 品牌名稱或slug已存在

### 2. 獲取品牌詳情

**端點:** `GET /brands/{brandId}`

**說明:** 獲取特定產品品牌的詳細信息

**路徑參數:**
- `brandId` - 品牌唯一標識符

**響應:**
```json
{
  "brandId": "string",
  "name": "string",
  "description": "string",
  "logo": "string",
  "website": "string",
  "slug": "string",
  "metaTitle": "string",
  "metaDescription": "string",
  "status": "string",
  "productCount": "number",
  "featuredProducts": [
    {
      "productId": "string",
      "name": "string",
      "image": "string"
    }
  ],
  "createdAt": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 品牌不存在

### 3. 更新品牌

**端點:** `PUT /brands/{brandId}`

**說明:** 更新特定產品品牌的信息

**路徑參數:**
- `brandId` - 品牌唯一標識符

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "logo": "string",
  "website": "string",
  "slug": "string",
  "metaTitle": "string",
  "metaDescription": "string",
  "status": "string"
}
```

**響應:**
```json
{
  "brandId": "string",
  "name": "string",
  "slug": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 品牌不存在
- `409 Conflict` - 品牌名稱或slug與其他品牌衝突

### 4. 刪除品牌

**端點:** `DELETE /brands/{brandId}`

**說明:** 刪除特定產品品牌

**路徑參數:**
- `brandId` - 品牌唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 品牌不存在
- `409 Conflict` - 品牌有關聯產品，無法刪除

### 5. 獲取品牌列表

**端點:** `GET /brands`

**說明:** 獲取所有產品品牌

**查詢參數:**
- `status` (可選) - 按狀態過濾 (ACTIVE, INACTIVE)
- `includeProducts` (可選, 默認為false) - 是否包含產品數量
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "brands": [
    {
      "brandId": "string",
      "name": "string",
      "logo": "string",
      "slug": "string",
      "status": "string",
      "productCount": "number",
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

## 產品評論 API

### 1. 創建產品評論

**端點:** `POST /{productId}/reviews`

**說明:** 為產品創建新評論

**路徑參數:**
- `productId` - 產品唯一標識符

**請求體:**
```json
{
  "customerId": "string",
  "rating": "number",
  "title": "string",
  "content": "string",
  "images": ["string"],
  "verifiedPurchase": "boolean",
  "metadata": {}
}
```

**響應:**
```json
{
  "reviewId": "string",
  "productId": "string",
  "productName": "string",
  "customerId": "string",
  "rating": "number",
  "title": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 評論創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在
- `409 Conflict` - 客戶已經評論過該產品

### 2. 獲取產品評論

**端點:** `GET /{productId}/reviews`

**說明:** 獲取特定產品的所有評論

**路徑參數:**
- `productId` - 產品唯一標識符

**查詢參數:**
- `status` (可選) - 按狀態過濾 (APPROVED, PENDING, REJECTED)
- `minRating` (可選) - 按最小評分過濾
- `verifiedOnly` (可選, 默認為false) - 是否只顯示已驗證購買的評論
- `sort` (可選, 默認為createdAt:desc) - 排序字段和方向 (rating:asc, rating:desc, createdAt:asc, createdAt:desc)
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "productId": "string",
  "productName": "string",
  "averageRating": "number",
  "reviewCount": "number",
  "ratingDistribution": {
    "5": "number",
    "4": "number",
    "3": "number",
    "2": "number",
    "1": "number"
  },
  "reviews": [
    {
      "reviewId": "string",
      "customerId": "string",
      "customerName": "string",
      "rating": "number",
      "title": "string",
      "content": "string",
      "images": ["string"],
      "verifiedPurchase": "boolean",
      "helpfulVotes": "number",
      "status": "string",
      "createdAt": "string",
      "response": {
        "content": "string",
        "respondedBy": "string",
        "respondedAt": "string"
      }
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
- `404 Not Found` - 產品不存在

### 3. 回覆產品評論

**端點:** `POST /{productId}/reviews/{reviewId}/response`

**說明:** 回覆特定產品評論

**路徑參數:**
- `productId` - 產品唯一標識符
- `reviewId` - 評論唯一標識符

**請求體:**
```json
{
  "content": "string",
  "respondedBy": "string"
}
```

**響應:**
```json
{
  "reviewId": "string",
  "response": {
    "content": "string",
    "respondedBy": "string",
    "respondedAt": "string"
  }
}
```

**狀態碼:**
- `201 Created` - 回覆創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品或評論不存在
- `409 Conflict` - 評論已有回覆

### 4. 評論有用投票

**端點:** `POST /{productId}/reviews/{reviewId}/vote`

**說明:** 標記評論為有用或無用

**路徑參數:**
- `productId` - 產品唯一標識符
- `reviewId` - 評論唯一標識符

**請求體:**
```json
{
  "customerId": "string",
  "isHelpful": "boolean"
}
```

**響應:**
```json
{
  "reviewId": "string",
  "helpfulVotes": "number",
  "notHelpfulVotes": "number",
  "customerVote": "boolean"
}
```

**狀態碼:**
- `200 OK` - 投票成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品或評論不存在

### 5. 更新評論狀態

**端點:** `PUT /{productId}/reviews/{reviewId}/status`

**說明:** 更新評論狀態（審核評論）

**路徑參數:**
- `productId` - 產品唯一標識符
- `reviewId` - 評論唯一標識符

**請求體:**
```json
{
  "status": "string",
  "moderationComment": "string"
}
```

**響應:**
```json
{
  "reviewId": "string",
  "status": "string",
  "previousStatus": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 狀態更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品或評論不存在

## 產品屬性 API

### 1. 創建屬性

**端點:** `POST /attributes`

**說明:** 創建新產品屬性

**請求體:**
```json
{
  "name": "string",
  "code": "string",
  "description": "string",
  "type": "string",
  "isVariant": "boolean",
  "isFilterable": "boolean",
  "isRequired": "boolean",
  "displayOrder": "number",
  "options": ["string"]
}
```

**響應:**
```json
{
  "attributeId": "string",
  "name": "string",
  "code": "string",
  "type": "string",
  "isVariant": "boolean",
  "isFilterable": "boolean",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 屬性創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 屬性名稱或代碼已存在

### 2. 獲取屬性詳情

**端點:** `GET /attributes/{attributeId}`

**說明:** 獲取特定產品屬性的詳細信息

**路徑參數:**
- `attributeId` - 屬性唯一標識符

**響應:**
```json
{
  "attributeId": "string",
  "name": "string",
  "code": "string",
  "description": "string",
  "type": "string",
  "isVariant": "boolean",
  "isFilterable": "boolean",
  "isRequired": "boolean",
  "displayOrder": "number",
  "options": ["string"],
  "createdAt": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 屬性不存在

### 3. 更新屬性

**端點:** `PUT /attributes/{attributeId}`

**說明:** 更新特定產品屬性的信息

**路徑參數:**
- `attributeId` - 屬性唯一標識符

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "isVariant": "boolean",
  "isFilterable": "boolean",
  "isRequired": "boolean",
  "displayOrder": "number",
  "options": ["string"]
}
```

**響應:**
```json
{
  "attributeId": "string",
  "name": "string",
  "isVariant": "boolean",
  "isFilterable": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 屬性不存在
- `409 Conflict` - 屬性名稱與其他屬性衝突

### 4. 刪除屬性

**端點:** `DELETE /attributes/{attributeId}`

**說明:** 刪除特定產品屬性

**路徑參數:**
- `attributeId` - 屬性唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 屬性不存在
- `409 Conflict` - 屬性已被使用，無法刪除

### 5. 獲取屬性列表

**端點:** `GET /attributes`

**說明:** 獲取所有產品屬性

**查詢參數:**
- `type` (可選) - 按類型過濾 (TEXT, NUMBER, BOOLEAN, SELECT, MULTISELECT, DATE)
- `isVariant` (可選) - 是否為變體屬性
- `isFilterable` (可選) - 是否可過濾

**響應:**
```json
{
  "attributes": [
    {
      "attributeId": "string",
      "name": "string",
      "code": "string",
      "type": "string",
      "isVariant": "boolean",
      "isFilterable": "boolean",
      "displayOrder": "number"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功

## 相關產品 API

### 1. 獲取相關產品

**端點:** `GET /{productId}/related`

**說明:** 獲取與特定產品相關的產品

**路徑參數:**
- `productId` - 產品唯一標識符

**查詢參數:**
- `type` (可選, 默認為ALL) - 相關類型 (SIMILAR, UPSELL, CROSS_SELL, ACCESSORY)
- `limit` (可選, 默認為10) - 返回的最大產品數量

**響應:**
```json
{
  "productId": "string",
  "productName": "string",
  "relatedProducts": [
    {
      "productId": "string",
      "name": "string",
      "price": {
        "amount": "number",
        "currency": "string"
      },
      "image": "string",
      "relationType": "string",
      "relationReason": "string"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 產品不存在

### 2. 設置相關產品

**端點:** `POST /{productId}/related`

**說明:** 為特定產品設置相關產品

**路徑參數:**
- `productId` - 產品唯一標識符

**請求體:**
```json
{
  "relatedProducts": [
    {
      "productId": "string",
      "relationType": "string",
      "relationReason": "string",
      "order": "number"
    }
  ]
}
```

**響應:**
```json
{
  "productId": "string",
  "relatedProductsCount": "number",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 設置成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 產品不存在

### 3. 刪除相關產品

**端點:** `DELETE /{productId}/related/{relatedProductId}`

**說明:** 刪除特定產品與另一產品的關聯

**路徑參數:**
- `productId` - 產品唯一標識符
- `relatedProductId` - 相關產品唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 產品或關聯不存在

## 模型定義

### 產品狀態 (ProductStatus)
- `ACTIVE` - 活躍，可購買
- `DRAFT` - 草稿，不可購買
- `ARCHIVED` - 已歸檔，不顯示
- `OUT_OF_STOCK` - 缺貨
- `DISCONTINUED` - 已停產

### 產品屬性類型 (AttributeType)
- `TEXT` - 文本
- `NUMBER` - 數字
- `BOOLEAN` - 布爾值
- `SELECT` - 單選
- `MULTISELECT` - 多選
- `DATE` - 日期

### 庫存狀態 (InventoryStatus)
- `IN_STOCK` - 有庫存
- `LOW_STOCK` - 庫存不足
- `OUT_OF_STOCK` - 無庫存
- `DISCONTINUED` - 已停產
- `BACKORDERED` - 延期交貨

### 評論狀態 (ReviewStatus)
- `PENDING` - 待審核
- `APPROVED` - 已批准
- `REJECTED` - 已拒絕

### 長度單位 (LengthUnit)
- `CM` - 厘米
- `MM` - 毫米
- `IN` - 英寸
- `FT` - 英尺

### 重量單位 (WeightUnit)
- `G` - 克
- `KG` - 千克
- `LB` - 磅
- `OZ` - 盎司

### 相關產品類型 (RelationType)
- `SIMILAR` - 相似產品
- `UPSELL` - 升級產品
- `CROSS_SELL` - 交叉銷售產品
- `ACCESSORY` - 配件
- `REPLACEMENT` - 替換產品

## 錯誤代碼

| 代碼                      | 說明                              |
|---------------------------|---------------------------------|
| PRODUCT_NOT_FOUND         | 產品不存在                         |
| VARIANT_NOT_FOUND         | 產品變體不存在                     |
| CATEGORY_NOT_FOUND        | 類別不存在                         |
| BRAND_NOT_FOUND           | 品牌不存在                         |
| ATTRIBUTE_NOT_FOUND       | 屬性不存在                         |
| REVIEW_NOT_FOUND          | 評論不存在                         |
| IMAGE_NOT_FOUND           | 圖像不存在                         |
| DUPLICATE_SKU             | SKU已存在                         |
| DUPLICATE_BARCODE         | 條碼已存在                         |
| INVALID_PRODUCT_STATUS    | 無效的產品狀態                     |
| INVALID_CATEGORY_HIERARCHY| 無效的類別層次結構                 |
| ATTRIBUTE_IN_USE          | 屬性已被使用                       |
| CATEGORY_IN_USE           | 類別已被使用                       |
| BRAND_IN_USE              | 品牌已被使用                       |
| VARIANT_IN_USE            | 變體已被使用                       |
| INVALID_ATTRIBUTE_TYPE    | 無效的屬性類型                     |
| INVALID_RELATION_TYPE     | 無效的關聯類型                     |
