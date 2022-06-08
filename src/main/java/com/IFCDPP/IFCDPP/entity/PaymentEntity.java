package com.ifcdpp.ifcdpp.entity;

import javax.persistence.Entity;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(name="payment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentEntity {

    @Id
    private String id;

    @ManyToOne
    private ProductEntity product;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

}

