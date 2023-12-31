package com.te.zealhr.dto.account;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductType {
    HARDWARE("Hardware"), SOFTWARE("Software"), OTHERS("Others");
    private final String type;
}
