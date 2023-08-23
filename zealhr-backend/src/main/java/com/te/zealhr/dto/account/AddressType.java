package com.te.zealhr.dto.account;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AddressType {
    BILLING("Billing"), SHIPPING("Shipping");
    private final String type;
}
