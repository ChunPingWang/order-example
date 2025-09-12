# 微服務專案總覽

## 專案概述

本專案包含多個微服務組件，包括供應商、採購、商品、使用者、銷售、付款、財務、物流等服務，各服務間透過 Web API 與 HTTP Client 進行溝通。

## 微服務清單

1. 供應商微服務 - 管理供應商資訊與採購流程
2. 商品微服務 - 管理商品目錄與庫存
3. 使用者微服務 - 管理客戶帳號與個人資訊
4. 銷售微服務 - 處理銷售訂單建立與管理
5. 付款微服務 - 處理信用卡與其他付款方式
6. 財務微服務 - 處理發票與帳務管理
7. 物流微服務 - 管理出貨與物流流程

## 微服務詳細資訊

各微服務的詳細需求、領域模型與API設計請參考以下文件：

- [供應商微服務](./.github/prompts/supplier.prompt.md)
- [商品微服務](./.github/prompts/merchandise.prompt.md)
- [使用者微服務](./.github/prompts/client.prompt.md)
- [銷售微服務](./.github/prompts/sales.prompt.md)
- [付款微服務](./.github/prompts/payment.prompt.md)
- [財務微服務](./.github/prompts/finance.prompt.md)
- [物流微服務](./.github/prompts/shipping.prompt.md)

## 技術規範

所有功能操作皆以 Web API 與 HTTP Client 進行微服務間溝通，遵循 OpenAPI 標準。
