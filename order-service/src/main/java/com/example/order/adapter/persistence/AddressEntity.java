package com.example.order.adapter.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    
    @Column(nullable = false)
    private String street;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    private String country;
    
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
    
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;
    
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
}
