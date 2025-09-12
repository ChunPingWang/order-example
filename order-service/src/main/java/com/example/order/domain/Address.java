package com.example.order.domain;

import lombok.Value;

@Value
public class Address {
    String street;
    String city;
    String state;
    String country;
    String postalCode;
    String recipientName;
    String phoneNumber;

    public Address(
            String street,
            String city,
            String state,
            String country,
            String postalCode,
            String recipientName,
            String phoneNumber) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s, %s %s",
                street, city, state, country, postalCode);
    }
}
