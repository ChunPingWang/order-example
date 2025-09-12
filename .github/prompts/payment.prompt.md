# 結帳微服務需求

## 功能概述
結帳微服務負責處理各種付款方式與交易流程，確保安全且可靠的支付體驗。

## 主要功能

1. 信用卡支付功能
   - 處理信用卡付款交易
   - 信用卡驗證與授權流程
   - 交易加密與安全處理
   - 交易失敗處理與重試機制

2. 其他付款方式
   - 支援多種付款選項（可擴充）
   - 付款方式驗證與處理
   - 交易狀態追蹤與更新

3. 交易管理
   - 交易紀錄建立與保存
   - 退款與取消處理
   - 交易報表與分析

## 技術需求
- 遵循六角形架構設計
- 使用 OpenAPI 定義所有 API 介面
- 透過 HTTP Client 與其他微服務互動
- 實作 CQRS 命令模式處理業務邏輯
- 確保支付安全性與符合 PCI DSS 標準

---

請依上述需求設計結帳微服務的領域模型、使用案例及 API 介面。

## Domain Entity 範例

```java
// Payment.java - 付款實體
public class Payment {
    private final PaymentId id;
    private final OrderId orderId;
    private final UserId userId;
    private final Amount amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionReference;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    
    // Constructor, getters, domain methods
    public Payment(PaymentId id, OrderId orderId, UserId userId, Amount amount, PaymentMethod paymentMethod) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
    
    public void authorize(String transactionReference) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment is not in PENDING state");
        }
        this.status = PaymentStatus.AUTHORIZED;
        this.transactionReference = transactionReference;
        this.processedAt = LocalDateTime.now();
    }
    
    public void capture() {
        if (this.status != PaymentStatus.AUTHORIZED) {
            throw new IllegalStateException("Payment is not in AUTHORIZED state");
        }
        this.status = PaymentStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
    }
    
    public void fail(String reason) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.AUTHORIZED) {
            throw new IllegalStateException("Payment cannot be failed in current state");
        }
        this.status = PaymentStatus.FAILED;
        this.processedAt = LocalDateTime.now();
    }
    
    public void refund() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded");
        }
        this.status = PaymentStatus.REFUNDED;
        this.processedAt = LocalDateTime.now();
    }
    
    // Value Objects
    public enum PaymentStatus {
        PENDING, AUTHORIZED, COMPLETED, FAILED, REFUNDED
    }
}

// CreditCardPayment.java - 信用卡付款實體
public class CreditCardPayment {
    private final CreditCardPaymentId id;
    private final PaymentId paymentId;
    private final String cardHolderName;
    private final String maskedCardNumber;
    private final ExpiryDate expiryDate;
    private final String authorizationCode;
    
    // Constructor, getters
    public CreditCardPayment(CreditCardPaymentId id, PaymentId paymentId,
                            String cardHolderName, String maskedCardNumber,
                            ExpiryDate expiryDate) {
        this.id = id;
        this.paymentId = paymentId;
        this.cardHolderName = cardHolderName;
        this.maskedCardNumber = maskedCardNumber;
        this.expiryDate = expiryDate;
        this.authorizationCode = null;
    }
    
    public CreditCardPayment withAuthorizationCode(String authorizationCode) {
        return new CreditCardPayment(
            this.id, this.paymentId, this.cardHolderName, 
            this.maskedCardNumber, this.expiryDate, authorizationCode
        );
    }
    
    // Private constructor with authorizationCode
    private CreditCardPayment(CreditCardPaymentId id, PaymentId paymentId,
                            String cardHolderName, String maskedCardNumber,
                            ExpiryDate expiryDate, String authorizationCode) {
        this.id = id;
        this.paymentId = paymentId;
        this.cardHolderName = cardHolderName;
        this.maskedCardNumber = maskedCardNumber;
        this.expiryDate = expiryDate;
        this.authorizationCode = authorizationCode;
    }
}

// PaymentMethod.java - 付款方式值對象
public class PaymentMethod {
    private final PaymentMethodType type;
    private final PaymentMethodDetails details;
    
    // Constructor, getters
    public PaymentMethod(PaymentMethodType type, PaymentMethodDetails details) {
        this.type = type;
        this.details = details;
    }
    
    // Value Objects
    public enum PaymentMethodType {
        CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER
    }
}

// Amount.java - 金額值對象
public class Amount {
    private final BigDecimal value;
    private final Currency currency;
    
    // Constructor, getters
    public Amount(BigDecimal value, Currency currency) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.value = value;
        this.currency = currency;
    }
    
    public Amount add(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add amounts with different currencies");
        }
        return new Amount(this.value.add(other.value), this.currency);
    }
    
    public Amount subtract(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract amounts with different currencies");
        }
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        return new Amount(result, this.currency);
    }
}
```
