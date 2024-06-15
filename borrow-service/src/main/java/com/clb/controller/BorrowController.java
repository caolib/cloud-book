package com.clb.controller;


import com.clb.common.constant.Cache;
import com.clb.common.constant.Excep;
import com.clb.common.domain.Borrow;
import com.clb.common.domain.Result;
import com.clb.common.domain.vo.BorrowVo;
import com.clb.service.BorrowService;
import com.clb.util.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    /**
     * 查询用户借阅记录
     */
    @GetMapping
    public Result<List<BorrowVo>> getBorrowByReaderId() {
        return borrowService.getBorrowByReaderId();
    }

    /**
     * 用户借阅图书
     *
     * @param isbn    书号
     * @param dueDate 应归还日期
     */
    @GetMapping("/borrowBook")
    @CacheEvict(value = Cache.BOOK_PAGE, allEntries = true)
    public Result<String> borrow(String isbn, String dueDate) {
        log.info("isbn:{} dueDate:{}", isbn, dueDate);

        if (!MyUtils.StrUtil(dueDate)) {
            String msg = Excep.RETURN_DATE_IS_NULL;
            log.error(msg);
            return Result.error(msg);
        }

        Date date = MyUtils.StrToDate(dueDate);
        return borrowService.borrow(isbn, date);
    }

    /**
     * 用户借阅图书
     *
     * @param isbn       书号
     * @param borrowDate 借阅日期
     * @param dueDate    应归还日期
     */
    @GetMapping("/borrow2")
    @CacheEvict(value = Cache.BOOK_PAGE, allEntries = true)
    public Result<String> borrow(String isbn, String borrowDate, String dueDate) {
        log.info("书号:{} 借阅日期:{} 归还日期:{}", isbn, borrowDate, dueDate);

        if (!MyUtils.StrUtil(dueDate) && !MyUtils.StrUtil(borrowDate)) {
            String msg = Excep.DATE_IS_NULL;
            log.error(msg);
            return Result.error(msg);
        }

        Date due = MyUtils.StrToDate(dueDate);
        Date borrow = MyUtils.StrToDate(borrowDate);
        return borrowService.borrow2(isbn, borrow, due);
    }

    /**
     * 归还书籍
     *
     * @param id   借阅号
     * @param isbn 书号
     */
    @GetMapping("/returnBook")
    @CacheEvict(value = Cache.BOOK_PAGE, allEntries = true)
    public Result<String> returnBook(Integer id, String isbn) {
        log.info("returnBook id:{}", id);

        return borrowService.returnBook(id, isbn);
    }


    /**
     * 根据借阅号删除借阅记录
     *
     * @param id 借阅号
     */
    @DeleteMapping
    public Result<String> deleteBorrow(Integer id) {
        log.info("deleteBorrow id:{}", id);

        return borrowService.deleteById(id);
    }

    /**
     * 批量删除借阅记录
     *
     * @param ids 借阅号数组
     */
    @PostMapping("/batch")
    public Result<String> deleteBatch(@RequestBody List<Integer> ids) {
        log.info("ids:{}", ids);

        return borrowService.deleteBatchByIds(ids);
    }

    @GetMapping("/isbn/{isbn}")
    public List<Borrow> getBorrowByIsbn(@PathVariable String isbn) {
        return borrowService.getBorrowByIsbn(isbn);
    }

}
