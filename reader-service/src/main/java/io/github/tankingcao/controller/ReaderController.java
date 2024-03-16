package io.github.tankingcao.controller;

import io.github.tankingcao.clients.BookClients;
import io.github.tankingcao.domain.Book;
import io.github.tankingcao.domain.Reader;
import io.github.tankingcao.domain.Result;
import io.github.tankingcao.mapper.ReaderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reader")
public class ReaderController {

    private final ReaderMapper readerMapper;
    private final BookClients bookClients;

    @GetMapping("/{id}")
    public Result<Reader> getReaderById(@PathVariable String id) {
        log.debug("reader-id:{}", id);

        Reader reader = readerMapper.selectById(id);

        return Result.success(reader);
    }

    @GetMapping
    public Result<Book> getBook(String isbn) {
        log.debug("isbn:{}", isbn);

        return bookClients.getBookByIsbn(isbn);
    }

}
