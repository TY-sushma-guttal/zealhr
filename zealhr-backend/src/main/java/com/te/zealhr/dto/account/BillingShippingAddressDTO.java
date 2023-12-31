package com.te.zealhr.dto.account;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class BillingShippingAddressDTO {
    private BillingAddressDTO billingAddress;
    private ShippingAddressDTO shippingAddress;
}
