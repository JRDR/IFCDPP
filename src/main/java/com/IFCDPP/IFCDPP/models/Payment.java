package com.ifcdpp.ifcdpp.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Payment {

    private String id;
    private Product product;
    private PaymentStatusModel status;
}
