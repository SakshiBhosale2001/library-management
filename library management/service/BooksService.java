package com.projectrestapi.projectrestapi.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.projectrestapi.projectrestapi.entity.Author;
import com.projectrestapi.projectrestapi.entity.Books;
import com.projectrestapi.projectrestapi.repository.BooksServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class BooksService {


    @Autowired
    private BooksServiceRepository booksServiceRepository;

    public Books findByBookname(String bookName)
    {
        return this.booksServiceRepository.findBybookName(bookName).get();
    }

    public Books findByAuthor(Author author)
    {
        return this.booksServiceRepository.findByAuthor(author).get();
    }


    public ResponseEntity<List> getAllBooks()
    {
        try {

            return ResponseEntity.ok((List<Books>) this.booksServiceRepository.findAll());
        }

        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    public Books setBooks(Books book, MultipartFile file) throws IOException
    {
        new Author(book.getAuthor().getAuthorName(),book.getAuthor().getAuthorAddress());

        book.setFile(file.getBytes());
        return booksServiceRepository.save(book);
    }


    public ResponseEntity<String> updateBook(Books book, int id)
    {
     
            if (booksServiceRepository.findById(id).isPresent())
            {
                try {
                    Books books = booksServiceRepository.findById(id).get();

                    books.setId(book.getId());
                    books.setBookName(book.getBookName());

                    books.setAuthor(Author.build(book.getAuthor().getAuthorId()
                            , book.getAuthor().getAuthorName()
                            , book.getAuthor().getAuthorAddress()));


                    return new ResponseEntity<>("Data of Id : " + id + "Updated...!", HttpStatus.OK);
                }
                catch (Exception ex)
                    { return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR); }

            } else return new ResponseEntity<>
                    ("Id not Found...\n Insure given Id : " + id + "Present in book or Not...! "
                            , HttpStatus.NOT_FOUND);

    }

    int[] arr=new  int[9];

    public ResponseEntity<String> deleteBook(int integer)
    {
            try {
                if (booksServiceRepository.existsById(integer)) {
                    this.booksServiceRepository.deleteById(integer);
                    return new ResponseEntity<>("Deleted data of Id:"+integer+" Successfully",HttpStatus.OK);
                }
                else throw new Exception();

            }
            catch (Exception e) {
                return new ResponseEntity<>("No Such Id present in database", HttpStatus.NOT_FOUND);
            }
    }


    public void deleteall()
    {
        booksServiceRepository.deleteAll();
    }


}
