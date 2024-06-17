package com.clb.clients;

import com.clb.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("ms-book")
@RequestMapping("/book")
public interface BookClient {

    @PutMapping("/{isbn}/{num}")
    Result<String> updateNumByIsbn(@PathVariable String isbn, @PathVariable Integer num);

}
