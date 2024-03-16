package io.github.tankingcao.clients;

import io.github.tankingcao.domain.Book;
import io.github.tankingcao.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("book-service")
public interface BookClients {

    @GetMapping("/book/{isbn}")
    Result<Book> getBookByIsbn(@PathVariable String isbn);
}
