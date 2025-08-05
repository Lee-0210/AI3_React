package com.test.todo.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.test.todo.domain.Todos;

public interface TodoService extends BaseService<Todos> {

    // 완료 리스트
    public List<Todos> listDone() throws Exception;
    public PageInfo<Todos> listDone(int page, int size) throws Exception;
    // 전체 완료
    public boolean completeAll() throws Exception;
    // 전체 삭제
    public boolean deleteAll() throws Exception;
}