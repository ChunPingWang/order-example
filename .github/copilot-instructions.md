
專案開發規範：

1. 本專案採用 JDK 21 與 Spring Boot 進行開發。
2. 採用 mono repo 管理所有微服務 backend 專案。
3. 每個微服務皆以六角形架構（Hexagonal Architecture）設計。
4. 專案目錄結構採用 Directory by features 方式安排，每個微服務依照功能分目錄。

請依以上規範進行開發、設計與程式撰寫。

補充規範：

5. 所有程式碼必須遵守 SOLID 原則。
6. 每一個 feature 需包含 domain、application、adapter 三層：
	- adapter：可放 web api、repository 實作，且所有框架相關程式僅能寫在此層。
	- application：可放 usecase、repository 的 interface，usecase 必須滿足 CQRS 的 command 模式。
	- domain：僅放 Domain Entity。
7. 嚴格遵守分層責任，禁止將框架程式碼寫入 domain 或 application 層。

8. 微服務間資料傳遞皆以 OpenAPI 標準與 HTTP Client 進行。

技術需求：

9. 資料庫規範：
	- 本機開發環境使用 H2 內存資料庫進行快速開發與測試。
	- 其他環境（開發、測試、生產）使用 PostgreSQL 作為資料庫。
	- 所有微服務必須使用 Spring Data JPA 進行資料存取層設計。
	- 使用 Spring Profiles 進行不同環境的配置切換。

10. 環境與設定：
	- 使用 application.yml 作為主要配置文件。
	- 環境特定配置使用 application-{env}.yml 格式，如 application-dev.yml, application-prod.yml。
	- 敏感配置（如密碼、密鑰）不得直接寫在配置文件中，需使用環境變數或配置服務。
	- 所有微服務的配置架構必須保持一致性。
