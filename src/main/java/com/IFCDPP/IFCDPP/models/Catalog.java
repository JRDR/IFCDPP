package com.IFCDPP.IFCDPP.models;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Catalog {

    private Long id;
    private String title;
    private String developer;
    private String description;
    private String download_link;

}
