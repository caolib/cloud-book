package com.clb.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clb.clients.BookClient;
import com.clb.common.domain.Borrow;
import com.clb.common.domain.Result;
import com.clb.common.domain.entity.Reader;
import com.clb.common.domain.vo.BorrowVo;
import com.clb.common.utils.ThreadLocalUtil;
import com.clb.mapper.BorrowMapper;
import com.clb.service.BorrowService;
import com.clb.util.MyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {
    private final BorrowMapper borrowMapper;
    private final BookClient bookClient;

    /**
     * 根据用户id查询借书记录
     */
    @Override
    public Result<List<BorrowVo>> getBorrowByReaderId() {
        // 从ThreadLocal中获取用户信息
        Reader reader = ThreadLocalUtil.get();
        log.debug("用户:{}", reader);

        Integer id = Integer.valueOf(reader.getId());
        List<BorrowVo> result = borrowMapper.selectByReaderId(id);

        return Result.success(result);
    }


    @Override
    public Result<String> borrow(String isbn, Date dueDate) {
        Reader reader = ThreadLocalUtil.get();
        String readerId = reader.getId();

        // 向借阅表中插入借阅记录
        Borrow borrow = Borrow.builder()
                .isbn(isbn)
                .dueDate(dueDate)
                .borrowDate(MyUtils.now())
                .readerId(readerId)
                .build();

        borrowMapper.insert(borrow);

        // 更新图书库存-1

        return bookClient.updateNumByIsbn(isbn, -1);
    }

    @Override
    public Result<String> borrow2(String isbn, Date borrow, Date due) {
        Reader reader = ThreadLocalUtil.get();
        String readerId = reader.getId();

        // 向借阅表中插入借阅记录
        Borrow b = Borrow.builder()
                .isbn(isbn)
                .borrowDate(borrow)
                .dueDate(due)
                .readerId(readerId)
                .build();

        borrowMapper.insert(b);

        // 更新图书库存-1
        return bookClient.updateNumByIsbn(isbn, -1);
    }

    /**
     * 读者归还图书，向借阅表中插入归还日期，同时更新图书的库存量
     */

    @Override
    public Result<String> returnBook(Integer id, String isbn) {

        //更新借阅表中信息
        borrowMapper.updateReturnDateById(id, MyUtils.now());
        //同时更新图书库存+1
        bookClient.updateNumByIsbn(isbn, 1);
        return Result.success();
    }

    @Override
    public Result<String> deleteById(Integer id) {
        //根据id删除借阅记录
        borrowMapper.deleteById(id);

        return Result.success();
    }


    @Override
    public Result<String> deleteBatchByIds(List<Integer> ids) {
        borrowMapper.deleteBatchIds(ids);

        return Result.success();
    }


    @Override
    public List<Borrow> getBorrowByIsbn(String isbn) {
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Borrow::getIsbn, isbn);
        return borrowMapper.selectList(queryWrapper);
    }


}
