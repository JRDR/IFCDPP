package com.ifcdpp.ifcdpp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "issue")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String topic;
    @Type(type = "text")
    private String description;

    @ManyToOne
    private User user;
}
