/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.projectrestapi.projectrestapi.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.projectrestapi.projectrestapi.entity.Author;
import com.projectrestapi.projectrestapi.entity.Books;
import com.projectrestapi.projectrestapi.service.BooksService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/book")
public class BooksController {

    @Autowired
    private BooksService booksService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/allBooks")
    public ResponseEntity<List> getbooks()
    {
        return booksService.getAllBooks();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping  ("/bookById/{bookName}")
    public ResponseEntity<Books> getbookByName(@PathVariable("bookName") String name){
        Books book;
        try
        {
            book=booksService.findByBookname(name);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(book));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping  ("/bookById/{author}")
    public ResponseEntity<Object> getbookByAuthor(@PathVariable("author")Author author){
        try
        {
            return ResponseEntity.of(Optional.of(booksService.findByAuthor(author)));

        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/booksadd")
    public Books setbooks(@RequestPart("image") MultipartFile file,@RequestPart("books") Books books) throws IOException
    {
           return booksService.setBooks(books,file);
    }
    


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/admin/update/{id}")
    public ResponseEntity<String> update(@RequestBody Books book,@PathVariable("id") int id)
    {
        return booksService.updateBook(book, id);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String>  deleteBook(@PathVariable("id") int id){
        return  booksService.deleteBook(id);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/deleteAll")
    public void  deleteAll(){
         booksService.deleteall();
    }

}
