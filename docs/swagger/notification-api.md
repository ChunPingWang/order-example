# 通知微服務 API 文檔

## API 概述

通知微服務負責處理系統中的所有通知相關功能，包括郵件通知、簡訊通知、推送通知等。該服務為其他微服務提供統一的通知介面，支持多種通知渠道和模板管理。

## API 基礎路徑

```
/api/notifications
```

## 認證與授權

所有API端點都需要授權。請在請求標頭中包含有效的JWT令牌：

```
Authorization: Bearer {token}
```

## 通知管理 API

### 1. 發送通知

**端點:** `POST /send`

**說明:** 發送新通知

**請求體:**
```json
{
  "recipients": [
    {
      "type": "string",
      "value": "string",
      "name": "string"
    }
  ],
  "template": {
    "id": "string",
    "code": "string",
    "data": {}
  },
  "channel": "string",
  "priority": "string",
  "scheduling": {
    "sendAt": "string",
    "timeZone": "string",
    "expireAt": "string"
  },
  "options": {
    "requireDeliveryConfirmation": "boolean",
    "requireReadReceipt": "boolean",
    "attachments": [
      {
        "name": "string",
        "type": "string",
        "content": "string"
      }
    ]
  },
  "metadata": {}
}
```

**響應:**
```json
{
  "notificationId": "string",
  "status": "string",
  "channel": "string",
  "recipientCount": "number",
  "scheduledAt": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 通知創建成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 模板不存在

### 2. 獲取通知詳情

**端點:** `GET /notifications/{notificationId}`

**說明:** 獲取特定通知的詳細信息

**路徑參數:**
- `notificationId` - 通知唯一標識符

**響應:**
```json
{
  "notificationId": "string",
  "recipients": [
    {
      "type": "string",
      "value": "string",
      "name": "string",
      "status": "string",
      "errorDetails": "string",
      "deliveredAt": "string",
      "readAt": "string"
    }
  ],
  "template": {
    "id": "string",
    "code": "string",
    "name": "string",
    "version": "string"
  },
  "channel": "string",
  "priority": "string",
  "content": {
    "subject": "string",
    "body": "string",
    "renderedBody": "string"
  },
  "status": "string",
  "statistics": {
    "total": "number",
    "delivered": "number",
    "failed": "number",
    "pending": "number",
    "read": "number"
  },
  "scheduling": {
    "sendAt": "string",
    "timeZone": "string",
    "expireAt": "string",
    "sentAt": "string"
  },
  "options": {
    "requireDeliveryConfirmation": "boolean",
    "requireReadReceipt": "boolean",
    "attachments": [
      {
        "name": "string",
        "type": "string",
        "size": "number"
      }
    ]
  },
  "createdAt": "string",
  "updatedAt": "string",
  "metadata": {}
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 通知不存在

### 3. 取消通知

**端點:** `DELETE /notifications/{notificationId}`

**說明:** 取消尚未發送的通知

**路徑參數:**
- `notificationId` - 通知唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "canceledAt": "string"
}
```

**狀態碼:**
- `200 OK` - 取消成功
- `404 Not Found` - 通知不存在
- `409 Conflict` - 通知已發送，無法取消

### 4. 查詢通知列表

**端點:** `GET /notifications`

**說明:** 獲取符合條件的通知列表

**查詢參數:**
- `channel` (可選) - 按渠道過濾
- `status` (可選) - 按狀態過濾
- `templateId` (可選) - 按模板過濾
- `recipient` (可選) - 按接收者過濾
- `startDate` (可選) - 按開始日期過濾
- `endDate` (可選) - 按結束日期過濾
- `sort` (可選, 默認為createdAt:desc) - 排序字段和方向
- `page` (可選, 默認為1) - 頁碼
- `pageSize` (可選, 默認為20) - 每頁項目數

**響應:**
```json
{
  "notifications": [
    {
      "notificationId": "string",
      "channel": "string",
      "template": {
        "id": "string",
        "name": "string"
      },
      "status": "string",
      "recipientCount": "number",
      "successRate": "number",
      "scheduledAt": "string",
      "createdAt": "string"
    }
  ],
  "pagination": {
    "page": "number",
    "pageSize": "number",
    "totalItems": "number",
    "totalPages": "number"
  },
  "statistics": {
    "total": "number",
    "byStatus": {
      "PENDING": "number",
      "SENDING": "number",
      "SENT": "number",
      "FAILED": "number",
      "CANCELLED": "number"
    },
    "byChannel": {
      "EMAIL": "number",
      "SMS": "number",
      "PUSH": "number"
    }
  }
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `400 Bad Request` - 查詢參數無效

## 模板管理 API

### 1. 創建通知模板

**端點:** `POST /templates`

**說明:** 創建新的通知模板

**請求體:**
```json
{
  "name": "string",
  "code": "string",
  "description": "string",
  "channel": "string",
  "content": {
    "subject": "string",
    "body": "string",
    "variables": [
      {
        "name": "string",
        "type": "string",
        "description": "string",
        "required": "boolean",
        "defaultValue": "string"
      }
    ]
  },
  "metadata": {
    "category": "string",
    "tags": ["string"]
  },
  "settings": {
    "sender": {
      "name": "string",
      "address": "string"
    },
    "replyTo": "string"
  },
  "status": "string"
}
```

**響應:**
```json
{
  "templateId": "string",
  "name": "string",
  "code": "string",
  "channel": "string",
  "version": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 模板創建成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 模板代碼已存在

### 2. 獲取模板詳情

**端點:** `GET /templates/{templateId}`

**說明:** 獲取特定通知模板的詳細信息

**路徑參數:**
- `templateId` - 模板唯一標識符

**響應:**
```json
{
  "templateId": "string",
  "name": "string",
  "code": "string",
  "description": "string",
  "channel": "string",
  "content": {
    "subject": "string",
    "body": "string",
    "variables": [
      {
        "name": "string",
        "type": "string",
        "description": "string",
        "required": "boolean",
        "defaultValue": "string"
      }
    ]
  },
  "metadata": {
    "category": "string",
    "tags": ["string"]
  },
  "settings": {
    "sender": {
      "name": "string",
      "address": "string"
    },
    "replyTo": "string"
  },
  "versions": [
    {
      "version": "string",
      "createdAt": "string",
      "createdBy": "string",
      "status": "string"
    }
  ],
  "status": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 模板不存在

### 3. 更新模板

**端點:** `PUT /templates/{templateId}`

**說明:** 更新特定通知模板

**路徑參數:**
- `templateId` - 模板唯一標識符

**請求體:**
```json
{
  "name": "string",
  "description": "string",
  "content": {
    "subject": "string",
    "body": "string",
    "variables": [
      {
        "name": "string",
        "type": "string",
        "description": "string",
        "required": "boolean",
        "defaultValue": "string"
      }
    ]
  },
  "metadata": {
    "category": "string",
    "tags": ["string"]
  },
  "settings": {
    "sender": {
      "name": "string",
      "address": "string"
    },
    "replyTo": "string"
  },
  "status": "string"
}
```

**響應:**
```json
{
  "templateId": "string",
  "version": "string",
  "status": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 模板不存在

### 4. 刪除模板

**端點:** `DELETE /templates/{templateId}`

**說明:** 刪除特定通知模板

**路徑參數:**
- `templateId` - 模板唯一標識符

**響應:**
```json
{
  "success": "boolean",
  "deletedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 刪除成功
- `404 Not Found` - 模板不存在
- `409 Conflict` - 模板正在使用中，無法刪除

### 5. 預覽模板

**端點:** `POST /templates/{templateId}/preview`

**說明:** 使用測試數據預覽模板渲染結果

**路徑參數:**
- `templateId` - 模板唯一標識符

**請求體:**
```json
{
  "data": {},
  "recipient": {
    "type": "string",
    "value": "string",
    "name": "string"
  }
}
```

**響應:**
```json
{
  "templateId": "string",
  "version": "string",
  "rendered": {
    "subject": "string",
    "body": "string"
  },
  "preview": {
    "html": "string",
    "text": "string"
  },
  "variables": {
    "used": ["string"],
    "missing": ["string"],
    "unused": ["string"]
  }
}
```

**狀態碼:**
- `200 OK` - 預覽成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 模板不存在

## 通知設置 API

### 1. 更新接收者偏好

**端點:** `PUT /preferences/{recipientId}`

**說明:** 更新接收者的通知偏好設置

**路徑參數:**
- `recipientId` - 接收者唯一標識符

**請求體:**
```json
{
  "channels": {
    "email": {
      "enabled": "boolean",
      "address": "string"
    },
    "sms": {
      "enabled": "boolean",
      "number": "string"
    },
    "push": {
      "enabled": "boolean",
      "tokens": ["string"]
    }
  },
  "preferences": {
    "categories": {
      "marketing": "boolean",
      "system": "boolean",
      "security": "boolean"
    },
    "frequency": {
      "marketing": "string",
      "digest": "string"
    },
    "timeWindow": {
      "start": "string",
      "end": "string",
      "timezone": "string"
    },
    "blacklist": ["string"]
  }
}
```

**響應:**
```json
{
  "recipientId": "string",
  "updated": "boolean",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 更新成功
- `400 Bad Request` - 請求資料無效
- `404 Not Found` - 接收者不存在

### 2. 獲取接收者偏好

**端點:** `GET /preferences/{recipientId}`

**說明:** 獲取接收者的通知偏好設置

**路徑參數:**
- `recipientId` - 接收者唯一標識符

**響應:**
```json
{
  "recipientId": "string",
  "channels": {
    "email": {
      "enabled": "boolean",
      "address": "string",
      "verified": "boolean",
      "lastVerifiedAt": "string"
    },
    "sms": {
      "enabled": "boolean",
      "number": "string",
      "verified": "boolean",
      "lastVerifiedAt": "string"
    },
    "push": {
      "enabled": "boolean",
      "tokens": ["string"],
      "lastUpdatedAt": "string"
    }
  },
  "preferences": {
    "categories": {
      "marketing": "boolean",
      "system": "boolean",
      "security": "boolean"
    },
    "frequency": {
      "marketing": "string",
      "digest": "string"
    },
    "timeWindow": {
      "start": "string",
      "end": "string",
      "timezone": "string"
    },
    "blacklist": ["string"]
  },
  "statistics": {
    "totalReceived": "number",
    "byChannel": {
      "email": "number",
      "sms": "number",
      "push": "number"
    },
    "byCategory": {
      "marketing": "number",
      "system": "number",
      "security": "number"
    }
  },
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 接收者不存在

## 通道管理 API

### 1. 配置通知通道

**端點:** `POST /channels`

**說明:** 配置新的通知通道

**請求體:**
```json
{
  "type": "string",
  "name": "string",
  "provider": "string",
  "config": {
    "apiKey": "string",
    "apiSecret": "string",
    "region": "string",
    "endpoint": "string"
  },
  "settings": {
    "rateLimit": "number",
    "retryPolicy": {
      "maxAttempts": "number",
      "backoffPeriod": "number"
    },
    "timeout": "number"
  },
  "status": "string"
}
```

**響應:**
```json
{
  "channelId": "string",
  "type": "string",
  "name": "string",
  "provider": "string",
  "status": "string",
  "createdAt": "string"
}
```

**狀態碼:**
- `201 Created` - 通道配置成功
- `400 Bad Request` - 請求資料無效
- `409 Conflict` - 通道名稱已存在

### 2. 獲取通道詳情

**端點:** `GET /channels/{channelId}`

**說明:** 獲取特定通知通道的詳細信息

**路徑參數:**
- `channelId` - 通道唯一標識符

**響應:**
```json
{
  "channelId": "string",
  "type": "string",
  "name": "string",
  "provider": "string",
  "config": {
    "region": "string",
    "endpoint": "string"
  },
  "settings": {
    "rateLimit": "number",
    "retryPolicy": {
      "maxAttempts": "number",
      "backoffPeriod": "number"
    },
    "timeout": "number"
  },
  "status": "string",
  "health": {
    "status": "string",
    "lastCheck": "string",
    "message": "string"
  },
  "statistics": {
    "totalSent": "number",
    "successRate": "number",
    "averageLatency": "number",
    "errorRate": "number"
  },
  "createdAt": "string",
  "updatedAt": "string"
}
```

**狀態碼:**
- `200 OK` - 請求成功
- `404 Not Found` - 通道不存在

## 模型定義

### 通知狀態 (NotificationStatus)
- `DRAFT` - 草稿
- `SCHEDULED` - 已排程
- `PENDING` - 待發送
- `SENDING` - 發送中
- `SENT` - 已發送
- `FAILED` - 發送失敗
- `CANCELLED` - 已取消

### 通知通道 (NotificationChannel)
- `EMAIL` - 電子郵件
- `SMS` - 簡訊
- `PUSH` - 推送通知
- `WEBHOOK` - Webhook
- `IN_APP` - 應用內通知

### 通知優先級 (NotificationPriority)
- `LOW` - 低優先級
- `NORMAL` - 正常優先級
- `HIGH` - 高優先級
- `URGENT` - 緊急優先級

### 模板狀態 (TemplateStatus)
- `DRAFT` - 草稿
- `ACTIVE` - 活躍
- `INACTIVE` - 不活躍
- `DEPRECATED` - 已棄用

### 通道類型 (ChannelType)
- `SMTP` - SMTP郵件服務
- `AWS_SES` - Amazon SES
- `SENDGRID` - SendGrid
- `TWILIO` - Twilio
- `FCM` - Firebase Cloud Messaging

### 通道狀態 (ChannelStatus)
- `ACTIVE` - 活躍
- `INACTIVE` - 不活躍
- `SUSPENDED` - 已暫停
- `MAINTENANCE` - 維護中

### 頻率選項 (FrequencyOption)
- `IMMEDIATELY` - 立即
- `HOURLY` - 每小時
- `DAILY` - 每日
- `WEEKLY` - 每週
- `MONTHLY` - 每月

## 錯誤代碼

| 代碼                          | 說明                                |
|------------------------------|-------------------------------------|
| NOTIFICATION_NOT_FOUND       | 通知不存在                           |
| TEMPLATE_NOT_FOUND           | 模板不存在                           |
| CHANNEL_NOT_FOUND            | 通道不存在                           |
| RECIPIENT_NOT_FOUND          | 接收者不存在                         |
| INVALID_TEMPLATE             | 無效的模板                           |
| INVALID_CHANNEL              | 無效的通道                           |
| INVALID_RECIPIENT            | 無效的接收者                         |
| TEMPLATE_RENDERING_ERROR     | 模板渲染錯誤                         |
| CHANNEL_CONFIGURATION_ERROR  | 通道配置錯誤                         |
| RATE_LIMIT_EXCEEDED         | 超過發送頻率限制                     |
| INVALID_NOTIFICATION_STATUS  | 無效的通知狀態                       |
| DUPLICATE_NOTIFICATION       | 重複的通知                           |
| MISSING_REQUIRED_VARIABLE    | 缺少必要變數                         |
| INVALID_ATTACHMENT           | 無效的附件                           |
| CHANNEL_NOT_AVAILABLE        | 通道不可用                           |
