package com.clb.service;


import com.clb.common.domain.Result;
import com.clb.common.domain.dto.LoginDto;
import com.clb.common.domain.entity.Reader;
import com.clb.common.domain.vo.ReaderVo;

import java.util.List;

public interface ReaderService {

    Result<ReaderVo> login(LoginDto reader);

    Result<Reader> updateReader(Reader reader);

    Result<String> register(Reader reader);

    Result<List<Reader>> getAllReader(Reader condition);

    Result<String> deleteById(Integer id);
}
