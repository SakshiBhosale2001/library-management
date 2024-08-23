package com.projectrestapi.projectrestapi.repository;

import com.projectrestapi.projectrestapi.entity.Author;
import com.projectrestapi.projectrestapi.entity.Books;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksServiceRepository extends CrudRepository<Books,Integer> {
    Optional<Books> findByAuthor(Author author);
    Optional<Books> findBybookName(String bookName);


}
