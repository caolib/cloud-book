package com.clb.clients;


import com.clb.common.domain.Borrow;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("ms-borrow")
@RequestMapping("/borrow")
public interface BorrowClient {
    @GetMapping("/isbn/{isbn}")
    List<Borrow> getBorrowByIsbn(@PathVariable String isbn);
}
