package com.projectrestapi.projectrestapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "build" )
@ToString
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name ="authorId" )
    private int authorId;

    @Column(name ="authorName",unique = true )
    private String authorName;

    @Column(name ="authorAddress" )
    private String authorAddress;

    public Author(String authorName, String authorAddress)
    {
        this.authorName = authorName;
        this.authorAddress = authorAddress;
    }

}
