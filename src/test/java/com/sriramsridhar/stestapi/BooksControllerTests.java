package com.sriramsridhar.stestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sriramsridhar.stestapi.models.Book;
import com.sriramsridhar.stestapi.services.BookService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class BooksControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenBookObject_whenCreateBook_thenReturnSavedBook() throws Exception {

        // given - precondition or setup
        Book book = Book.builder()
                .author("Ramesh").title("test")
                .build();
        given(bookService.saveOrUpdateBook(any(Book.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.title",
                        is(book.getTitle())))
                .andExpect(jsonPath("$.author",
                        is(book.getAuthor())));

    }

    @Test
    public void givenListOfBooks_whenGetAllBooks_thenReturnBooksList() throws Exception {
        // given - precondition or setup
        List<Book> listOfBooks = new ArrayList<>();
        listOfBooks.add(Book.builder().title("Ramesh").author("Fadatare").build());
        listOfBooks.add(Book.builder().title("Tony").author("Stark").build());
        given(bookService.getAllBooks()).willReturn(listOfBooks);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/books"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfBooks.size())));

    }

    // positive scenario - valid book id
    // JUnit test for GET book by id REST API
    @Test
    public void givenBookId_whenGetBookById_thenReturnBookObject() throws Exception {
        // given - precondition or setup
        long bookId = 1L;
        Book book = Book.builder()
                .title("Ramesh")
                .author("Fadatare")
                .build();
        given(bookService.getBookById(bookId)).willReturn(Optional.of(book));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/books/{id}", bookId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(book.getTitle())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())));

    }

    // negative scenario - valid book id
    // JUnit test for GET book by id REST API
    @Test
    public void givenInvalidBookId_whenGetBookById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long bookId = 1L;
        Book book = Book.builder()
                .title("Ramesh")
                .author("Fadatare")
                .build();
        given(bookService.getBookById(bookId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/books/{id}", bookId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for update book REST API - positive scenario
    @Test
    public void givenUpdatedBook_whenUpdateBook_thenReturnUpdateBookObject() throws Exception {
        // given - precondition or setup
        long bookId = 1L;
        Book savedBook = Book.builder()
                .title("Ramesh")
                .author("Fadatare")
                .build();

        Book updatedBook = Book.builder()
                .title("Ram")
                .author("Jadhav")
                .build();
        given(bookService.getBookById(bookId)).willReturn(Optional.of(savedBook));
        given(bookService.saveOrUpdateBook(any(Book.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(updatedBook.getTitle())))
                .andExpect(jsonPath("$.author", is(updatedBook.getAuthor())));
    }

    // JUnit test for update book REST API - negative scenario
    @Test
    public void givenUpdatedBook_whenUpdateBookNotAvailable_thenReturn404() throws Exception {
        // given - precondition or setup
        long bookId = 1L;
        Book savedBook = Book.builder()
                .title("Ramesh")
                .author("Fadatare")
                .build();

        Book updatedBook = Book.builder()
                .title("Ram")
                .author("Jadhav")
                .build();
        given(bookService.getBookById(bookId)).willReturn(Optional.empty());
        given(bookService.saveOrUpdateBook(any(Book.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete book REST API
    @Test
    public void givenBookId_whenDeleteBook_thenReturn200() throws Exception {
        // given - precondition or setup
        long bookId = 1L;
        willDoNothing().given(bookService).deleteBook(bookId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/books/{id}", bookId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
