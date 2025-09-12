# 使用者微服務需求

## 功能概述
使用者微服務負責管理客戶帳號、個人資料與身份驗證，提供完整的使用者管理功能。

## 主要功能

1. 帳號管理功能
   - 使用者註冊與帳號建立
   - 身分驗證與登入管理
   - 密碼重設與變更功能
   - 權限管理與角色分配

2. 個人資料管理
   - 使用者基本資料維護（姓名、聯絡方式、地址等）
   - 付款方式管理
   - 使用者偏好設定

3. 使用者活動
   - 瀏覽歷史紀錄
   - 收藏清單管理
   - 通知偏好設定

## 技術需求
- 遵循六角形架構設計
- 使用 OpenAPI 定義所有 API 介面
- 透過 HTTP Client 與其他微服務互動
- 實作 CQRS 命令模式處理業務邏輯
- 確保使用者資料安全與隱私保護

---

請依上述需求設計使用者微服務的領域模型、使用案例及 API 介面。

## Domain Entity 範例

```java
// User.java - 使用者實體
public class User {
    private final UserId id;
    private Email email;
    private PasswordHash passwordHash;
    private UserProfile profile;
    private Set<Role> roles;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    
    // Constructor, getters, domain methods
    public User(UserId id, Email email, PasswordHash passwordHash) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profile = new UserProfile();
        this.roles = new HashSet<>(Collections.singleton(Role.CUSTOMER));
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }
    
    public void updateEmail(Email newEmail) {
        this.email = newEmail;
    }
    
    public void updatePassword(PasswordHash newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }
    
    public void updateProfile(UserProfile newProfile) {
        this.profile = newProfile;
    }
    
    public void addRole(Role role) {
        this.roles.add(role);
    }
    
    public void removeRole(Role role) {
        this.roles.remove(role);
    }
    
    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }
    
    public void activate() {
        this.status = UserStatus.ACTIVE;
    }
    
    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }
    
    // Value Objects
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
    
    public enum Role {
        CUSTOMER, ADMIN, STAFF
    }
}

// UserProfile.java - 使用者個人檔案值對象
public class UserProfile {
    private PersonName name;
    private PhoneNumber phoneNumber;
    private Address address;
    private Set<Address> shippingAddresses;
    private Set<PaymentMethod> paymentMethods;
    private Preferences preferences;
    
    // Constructor, getters, setters
    public UserProfile() {
        this.shippingAddresses = new HashSet<>();
        this.paymentMethods = new HashSet<>();
        this.preferences = new Preferences();
    }
    
    public UserProfile(PersonName name, PhoneNumber phoneNumber, Address address) {
        this();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    
    public void addShippingAddress(Address address) {
        this.shippingAddresses.add(address);
    }
    
    public void removeShippingAddress(Address address) {
        this.shippingAddresses.remove(address);
    }
    
    public void addPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.add(paymentMethod);
    }
    
    public void removePaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.remove(paymentMethod);
    }
    
    public void updatePreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}

// Address.java - 地址值對象
public class Address {
    private final String street;
    private final String city;
    private final String state;
    private final String postalCode;
    private final String country;
    
    // Constructor, getters
    public Address(String street, String city, String state, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }
    
    // Value object equality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
               Objects.equals(city, address.city) &&
               Objects.equals(state, address.state) &&
               Objects.equals(postalCode, address.postalCode) &&
               Objects.equals(country, address.country);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, postalCode, country);
    }
}

// PaymentMethod.java - 付款方式值對象
public class PaymentMethod {
    private final PaymentMethodId id;
    private final PaymentType type;
    private final String maskedNumber;
    private final boolean isDefault;
    
    // Constructor, getters
    public PaymentMethod(PaymentMethodId id, PaymentType type, String maskedNumber, boolean isDefault) {
        this.id = id;
        this.type = type;
        this.maskedNumber = maskedNumber;
        this.isDefault = isDefault;
    }
    
    public PaymentMethod makeDefault() {
        return new PaymentMethod(this.id, this.type, this.maskedNumber, true);
    }
    
    // Value Objects
    public enum PaymentType {
        CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER
    }
}
```
