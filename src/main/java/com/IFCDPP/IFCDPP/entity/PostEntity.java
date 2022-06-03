package com.ifcdpp.ifcdpp.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;


@Entity
@Table(name="post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Type(type="text")
    private String text;
    private int views;

    public PostEntity(String title, String text) {
        this.title = title;
        this.text = text;
    }

}
