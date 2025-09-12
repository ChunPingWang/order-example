package com.example.order.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithCorrectValues() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100);
        Currency currency = Currency.getInstance("USD");

        // Act
        Money money = new Money(amount, currency);

        // Assert
        assertEquals(amount, money.getAmount());
        assertEquals(currency, money.getCurrency());
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        // Arrange
        Money money1 = Money.of(BigDecimal.valueOf(100), "USD");
        Money money2 = Money.of(BigDecimal.valueOf(50), "USD");

        // Act
        Money result = money1.add(money2);

        // Assert
        assertEquals(BigDecimal.valueOf(150), result.getAmount());
        assertEquals(Currency.getInstance("USD"), result.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenAddingDifferentCurrencies() {
        // Arrange
        Money usd = Money.of(BigDecimal.valueOf(100), "USD");
        Money eur = Money.of(BigDecimal.valueOf(100), "EUR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
    }

    @Test
    void shouldSubtractMoneyWithSameCurrency() {
        // Arrange
        Money money1 = Money.of(BigDecimal.valueOf(100), "USD");
        Money money2 = Money.of(BigDecimal.valueOf(30), "USD");

        // Act
        Money result = money1.subtract(money2);

        // Assert
        assertEquals(BigDecimal.valueOf(70), result.getAmount());
        assertEquals(Currency.getInstance("USD"), result.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenSubtractingDifferentCurrencies() {
        // Arrange
        Money usd = Money.of(BigDecimal.valueOf(100), "USD");
        Money eur = Money.of(BigDecimal.valueOf(50), "EUR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> usd.subtract(eur));
    }

    @Test
    void shouldMultiplyMoneyByNumber() {
        // Arrange
        Money money = Money.of(BigDecimal.valueOf(100), "USD");

        // Act
        Money result = money.multiply(3);

        // Assert
        assertEquals(BigDecimal.valueOf(300), result.getAmount());
        assertEquals(Currency.getInstance("USD"), result.getCurrency());
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        Money money1 = Money.of(BigDecimal.valueOf(100), "USD");
        Money money2 = Money.of(BigDecimal.valueOf(100), "USD");
        Money money3 = Money.of(BigDecimal.valueOf(100), "EUR");

        // Assert
        assertEquals(money1, money2);
        assertNotEquals(money1, money3);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        // Arrange
        Money money = Money.of(BigDecimal.valueOf(100), "USD");

        // Act
        String result = money.toString();

        // Assert
        assertEquals("100 USD", result);
    }
}
