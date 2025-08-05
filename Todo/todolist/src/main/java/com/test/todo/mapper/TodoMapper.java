package com.test.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.test.todo.domain.Todos;

@Mapper
public interface TodoMapper extends BaseMapper<Todos> {
  // 완료 리스트
  public List<Todos> listDone() throws Exception;
  // 전체 완료
  public int completeAll() throws Exception;
  // 전체 삭제
  public int deleteAll() throws Exception;
}
