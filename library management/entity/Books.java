/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.projectrestapi.projectrestapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "author") //to prevent backfire or recursion
@Entity
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name ="id")
    private int id;

    @Column(name ="bookName",unique = true)
    private String bookName;

   @OneToOne(cascade = CascadeType.ALL)
   //@JsonManagedReference        //can go to author & used to avoid infinite get data AS bidirectional mapping means always author to books & book to author which will continue to infinite
   private Author author;

    @Lob                          //used to store large files 2 type : 1:BLob-store binary  2:CLob:store text
    @Column(name ="bookImage",columnDefinition ="LONGBLOB")
    private byte[] file;

}
