# 財務微服務需求

## 功能概述
財務微服務負責處理發票開立、帳務管理與財務報表，確保準確的財務記錄與稅務合規。

## 主要功能

1. 發票管理功能
   - 針對完成訂單自動開立發票
   - 客製化發票格式與內容
   - 電子發票與紙本發票管理
   - 發票查詢與補印功能

2. 帳務管理
   - 交易記錄與財務報表
   - 收入與支出追蹤
   - 利潤分析與成本計算
   - 稅務報表與申報資料準備

3. 財務整合
   - 與其他微服務整合財務資料
   - 定期結算與對帳功能
   - 財務異常偵測與處理

## 技術需求
- 遵循六角形架構設計
- 使用 OpenAPI 定義所有 API 介面
- 透過 HTTP Client 與其他微服務互動
- 實作 CQRS 命令模式處理業務邏輯
- 確保財務數據準確性與安全性

---

請依上述需求設計財務微服務的領域模型、使用案例及 API 介面。

## Domain Entity 範例

```java
// Invoice.java - 發票實體
public class Invoice {
    private final InvoiceId id;
    private final OrderId orderId;
    private final UserId userId;
    private final InvoiceNumber invoiceNumber;
    private final List<InvoiceItem> items;
    private final Amount subtotal;
    private final Amount tax;
    private final Amount total;
    private InvoiceStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;
    
    // Constructor, getters, domain methods
    public Invoice(InvoiceId id, OrderId orderId, UserId userId, InvoiceNumber invoiceNumber,
                  List<InvoiceItem> items, Amount subtotal, Amount tax, Amount total) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.invoiceNumber = invoiceNumber;
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.status = InvoiceStatus.DRAFT;
    }
    
    public void issue() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Invoice is not in DRAFT state");
        }
        this.status = InvoiceStatus.ISSUED;
        this.issuedAt = LocalDateTime.now();
    }
    
    public void markAsPaid() {
        if (this.status != InvoiceStatus.ISSUED) {
            throw new IllegalStateException("Invoice is not in ISSUED state");
        }
        this.status = InvoiceStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }
    
    public void cancel() {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot cancel PAID invoice");
        }
        this.status = InvoiceStatus.CANCELED;
    }
    
    // Value Objects
    public enum InvoiceStatus {
        DRAFT, ISSUED, PAID, CANCELED
    }
}

// InvoiceItem.java - 發票項目值對象
public class InvoiceItem {
    private final ProductId productId;
    private final String description;
    private final int quantity;
    private final Amount unitPrice;
    private final Amount subtotal;
    
    // Constructor, getters
    public InvoiceItem(ProductId productId, String description, int quantity, Amount unitPrice) {
        this.productId = productId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        
        // Calculate subtotal based on quantity and unit price
        BigDecimal subtotalValue = unitPrice.getValue().multiply(BigDecimal.valueOf(quantity));
        this.subtotal = new Amount(subtotalValue, unitPrice.getCurrency());
    }
}

// FinancialTransaction.java - 財務交易實體
public class FinancialTransaction {
    private final TransactionId id;
    private final TransactionType type;
    private final Amount amount;
    private final String reference;
    private final LocalDateTime transactionDate;
    private final String description;
    
    // Constructor, getters
    public FinancialTransaction(TransactionId id, TransactionType type, Amount amount,
                               String reference, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.reference = reference;
        this.description = description;
        this.transactionDate = LocalDateTime.now();
    }
    
    // Value Objects
    public enum TransactionType {
        INCOME, EXPENSE, REFUND
    }
}

// TaxRate.java - 稅率值對象
public class TaxRate {
    private final String code;
    private final String name;
    private final BigDecimal rate;
    
    // Constructor, getters
    public TaxRate(String code, String name, BigDecimal rate) {
        this.code = code;
        this.name = name;
        this.rate = rate;
    }
    
    public Amount calculateTax(Amount amount) {
        BigDecimal taxAmount = amount.getValue().multiply(rate);
        return new Amount(taxAmount, amount.getCurrency());
    }
}

// FinancialPeriod.java - 財務期間實體
public class FinancialPeriod {
    private final FinancialPeriodId id;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private FinancialPeriodStatus status;
    private Amount totalIncome;
    private Amount totalExpense;
    private Amount balance;
    
    // Constructor, getters, domain methods
    public FinancialPeriod(FinancialPeriodId id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = FinancialPeriodStatus.OPEN;
        this.totalIncome = Amount.zero(Currency.TWD);
        this.totalExpense = Amount.zero(Currency.TWD);
        this.balance = Amount.zero(Currency.TWD);
    }
    
    public void close() {
        this.status = FinancialPeriodStatus.CLOSED;
    }
    
    public void recordTransaction(FinancialTransaction transaction) {
        if (this.status == FinancialPeriodStatus.CLOSED) {
            throw new IllegalStateException("Cannot record transaction in a closed period");
        }
        
        if (transaction.getType() == TransactionType.INCOME) {
            this.totalIncome = this.totalIncome.add(transaction.getAmount());
            this.balance = this.balance.add(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.EXPENSE) {
            this.totalExpense = this.totalExpense.add(transaction.getAmount());
            this.balance = this.balance.subtract(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.REFUND) {
            // Refund logic depends on business rules
        }
    }
    
    // Value Objects
    public enum FinancialPeriodStatus {
        OPEN, CLOSED
    }
}
```
