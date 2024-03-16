package io.github.tankingcao.controller;


import io.github.tankingcao.domain.Book;
import io.github.tankingcao.domain.Result;
import io.github.tankingcao.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookMapper bookMapper;

    @GetMapping("/{isbn}")
    public Result<Book> getBookByIsbn(@PathVariable String isbn){
        log.debug("isbn:{}", isbn);

        Book book = bookMapper.selectById(isbn);

        return Result.success(book);
    }

}
