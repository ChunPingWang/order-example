# 搜索微服務 API 文檔

## API 概述

搜索微服務負責處理系統中的所有搜索相關功能，包括產品搜索、訂單搜索、客戶搜索等。該服務提供統一的搜索介面，支持多種搜索類型、過濾和排序選項，並提供搜索建議和自動完成功能。

## API 基礎路徑

```
/api/search
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 通用搜索 API

### 1. 執行搜索

**端點:** `POST /`

**說明:** 執行通用搜索查詢

**請求體:**
```json
{
  "query": "string",
  "type": "string",
  "filters": {
    "field1": ["value1", "value2"],
    "field2": {
      "min": "number",
      "max": "number"
    },
    "field3": {
      "near": {
        "lat": "number",
        "lon": "number"
      },
      "distance": "string"
    }
  },
  "sort": [
    {
      "field": "string",
      "order": "string"
    }
  ],
  "page": "number",
  "pageSize": "number",
  "highlight": {
    "fields": ["string"],
    "preTag": "string",
    "postTag": "string"
  },
  "aggregations": ["string"]
}
```

**響應:**
```json
{
  "total": "number",
  "page": "number",
  "pageSize": "number",
  "results": [
    {
      "type": "string",
      "id": "string",
      "score": "number",
      "highlights": {},
      "data": {}
    }
  ],
  "aggregations": {
    "field1": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "field2": {
      "min": "number",
      "max": "number",
      "avg": "number"
    }
  },
  "suggestions": [
    {
      "text": "string",
      "score": "number"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求資料無效

### 2. 搜索建議

**端點:** `GET /suggest`

**說明:** 獲取搜索建議

**查詢參數:**
- `query` - 搜索關鍵詞
- `type` (可選) - 搜索類型
- `limit` (可選, 默認為10) - 返回建議的數量

**響應:**
```json
{
  "suggestions": [
    {
      "text": "string",
      "type": "string",
      "highlighted": "string",
      "score": "number",
      "metadata": {}
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求參數無效

## 產品搜索 API

### 1. 搜索產品

**端點:** `POST /products`

**說明:** 搜索產品

**請求體:**
```json
{
  "query": "string",
  "filters": {
    "categories": ["string"],
    "brands": ["string"],
    "price": {
      "min": "number",
      "max": "number"
    },
    "attributes": {
      "color": ["string"],
      "size": ["string"]
    },
    "inStock": "boolean",
    "rating": {
      "min": "number"
    }
  },
  "sort": [
    {
      "field": "string",
      "order": "string"
    }
  ],
  "page": "number",
  "pageSize": "number",
  "highlight": {
    "fields": ["name", "description"],
    "preTag": "<em>",
    "postTag": "</em>"
  }
}
```

**響應:**
```json
{
  "total": "number",
  "page": "number",
  "pageSize": "number",
  "products": [
    {
      "productId": "string",
      "name": "string",
      "description": "string",
      "price": {
        "amount": "number",
        "currency": "string"
      },
      "category": {
        "id": "string",
        "name": "string"
      },
      "brand": {
        "id": "string",
        "name": "string"
      },
      "attributes": {},
      "rating": "number",
      "image": "string",
      "score": "number",
      "highlights": {
        "name": ["string"],
        "description": ["string"]
      }
    }
  ],
  "aggregations": {
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
    "price": {
      "min": "number",
      "max": "number",
      "ranges": [
        {
          "range": "string",
          "count": "number"
        }
      ]
    },
    "attributes": {
      "color": [
        {
          "value": "string",
          "count": "number"
        }
      ]
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求資料無效

### 2. 產品自動完成

**端點:** `GET /products/autocomplete`

**說明:** 獲取產品名稱的自動完成建議

**查詢參數:**
- `query` - 搜索關鍵詞
- `categories` (可選) - 按類別過濾，多個類別以逗號分隔
- `limit` (可選, 默認為10) - 返回建議的數量

**響應:**
```json
{
  "suggestions": [
    {
      "text": "string",
      "highlighted": "string",
      "category": "string",
      "productId": "string",
      "score": "number"
    }
  ]
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求參數無效

## 訂單搜索 API

### 1. 搜索訂單

**端點:** `POST /orders`

**說明:** 搜索訂單

**請求體:**
```json
{
  "query": "string",
  "filters": {
    "status": ["string"],
    "dateRange": {
      "start": "string",
      "end": "string"
    },
    "customer": "string",
    "totalAmount": {
      "min": "number",
      "max": "number"
    },
    "paymentStatus": ["string"]
  },
  "sort": [
    {
      "field": "string",
      "order": "string"
    }
  ],
  "page": "number",
  "pageSize": "number"
}
```

**響應:**
```json
{
  "total": "number",
  "page": "number",
  "pageSize": "number",
  "orders": [
    {
      "orderId": "string",
      "customer": {
        "id": "string",
        "name": "string"
      },
      "status": "string",
      "totalAmount": {
        "amount": "number",
        "currency": "string"
      },
      "items": "number",
      "createdAt": "string",
      "score": "number",
      "highlights": {}
    }
  ],
  "aggregations": {
    "status": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "paymentStatus": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "totalAmount": {
      "min": "number",
      "max": "number",
      "ranges": [
        {
          "range": "string",
          "count": "number"
        }
      ]
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求資料無效

## 客戶搜索 API

### 1. 搜索客戶

**端點:** `POST /customers`

**說明:** 搜索客戶

**請求體:**
```json
{
  "query": "string",
  "filters": {
    "status": ["string"],
    "type": ["string"],
    "registrationDate": {
      "start": "string",
      "end": "string"
    },
    "segments": ["string"],
    "totalSpent": {
      "min": "number",
      "max": "number"
    },
    "location": {
      "countries": ["string"],
      "regions": ["string"]
    }
  },
  "sort": [
    {
      "field": "string",
      "order": "string"
    }
  ],
  "page": "number",
  "pageSize": "number"
}
```

**響應:**
```json
{
  "total": "number",
  "page": "number",
  "pageSize": "number",
  "customers": [
    {
      "customerId": "string",
      "name": "string",
      "email": "string",
      "type": "string",
      "status": "string",
      "segments": ["string"],
      "totalSpent": {
        "amount": "number",
        "currency": "string"
      },
      "lastOrderDate": "string",
      "score": "number",
      "highlights": {}
    }
  ],
  "aggregations": {
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
    "segments": [
      {
        "value": "string",
        "count": "number"
      }
    ],
    "location": {
      "countries": [
        {
          "value": "string",
          "count": "number"
        }
      ]
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求資料無效

## 搜索管理 API

### 1. 管理同義詞

**端點:** `POST /synonyms`

**說明:** 創建或更新搜索同義詞

**請求體:**
```json
{
  "type": "string",
  "terms": ["string"],
  "synonyms": ["string"],
  "bidirectional": "boolean"
}
```

**響應:**
```json
{
  "id": "string",
  "type": "string",
  "terms": ["string"],
  "synonyms": ["string"],
  "bidirectional": "boolean",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 同義詞創建成功
- `400 Bad Request` - 請求資料無效

### 2. 管理停用詞

**端點:** `POST /stopwords`

**說明:** 添加或更新停用詞

**請求體:**
```json
{
  "words": ["string"],
  "language": "string"
}
```

**響應:**
```json
{
  "added": "number",
  "updated": "number",
  "language": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效

### 3. 重建索引

**端點:** `POST /reindex`

**說明:** 重建特定類型的搜索索引

**請求體:**
```json
{
  "type": "string",
  "options": {
    "batchSize": "number",
    "refreshInterval": "string"
  }
}
```

**響應:**
```json
{
  "taskId": "string",
  "type": "string",
  "status": "string",
  "startTime": "string",
  "estimatedTime": "string"
}
```

**狀態碼:**
- `202 Accepted` - 重建索引任務已接受
- `400 Bad Request` - 請求資料無效

## 搜索分析 API

### 1. 獲取搜索分析

**端點:** `GET /analytics`

**說明:** 獲取搜索使用情況的分析數據

**查詢參數:**
- `type` - 搜索類型
- `startDate` - 開始日期
- `endDate` - 結束日期
- `interval` (可選, 默認為day) - 統計間隔

**響應:**
```json
{
  "period": {
    "start": "string",
    "end": "string"
  },
  "overview": {
    "totalSearches": "number",
    "uniqueSearches": "number",
    "averageResults": "number",
    "noResultsRate": "number",
    "clickThroughRate": "number"
  },
  "trends": [
    {
      "date": "string",
      "searches": "number",
      "uniqueSearches": "number",
      "noResults": "number"
    }
  ],
  "topQueries": [
    {
      "query": "string",
      "count": "number",
      "uniqueUsers": "number",
      "averageResults": "number",
      "clickThroughRate": "number"
    }
  ],
  "noResultsQueries": [
    {
      "query": "string",
      "count": "number",
      "lastSearched": "string"
    }
  ],
  "filters": {
    "mostUsed": [
      {
        "field": "string",
        "value": "string",
        "count": "number"
      }
    ]
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 請求參數無效

## 模型定義

### 搜索類型 (SearchType)
- `PRODUCT` - 產品搜索
- `ORDER` - 訂單搜索
- `CUSTOMER` - 客戶搜索
- `GENERAL` - 通用搜索

### 排序順序 (SortOrder)
- `ASC` - 升序
- `DESC` - 降序

### 同義詞類型 (SynonymType)
- `EQUIVALENT` - 等價詞
- `EXPANSION` - 擴展詞
- `CORRECTION` - 糾錯詞

### 索引狀態 (IndexStatus)
- `IDLE` - 空閒
- `INDEXING` - 索引中
- `FAILED` - 失敗
- `COMPLETED` - 完成

### 分析間隔 (AnalyticsInterval)
- `HOUR` - 每小時
- `DAY` - 每天
- `WEEK` - 每週
- `MONTH` - 每月

## 錯誤代碼

| 代碼                          | 說明                                |
|------------------------------|-------------------------------------|
| INVALID_QUERY                | 無效的搜索查詢                       |
| INVALID_FILTER              | 無效的過濾條件                       |
| INVALID_SORT                | 無效的排序條件                       |
| INDEX_NOT_READY             | 索引未就緒                           |
| REINDEX_IN_PROGRESS         | 重建索引進行中                       |
| SYNONYM_CONFLICT            | 同義詞衝突                           |
| INVALID_DATE_RANGE          | 無效的日期範圍                       |
| ANALYTICS_NOT_AVAILABLE     | 分析數據不可用                       |
| SEARCH_TIMEOUT              | 搜索超時                             |
| TOO_MANY_FILTERS           | 過濾條件過多                         |
| INVALID_AGGREGATION        | 無效的聚合請求                       |
| SUGGEST_ERROR              | 搜索建議錯誤                         |
