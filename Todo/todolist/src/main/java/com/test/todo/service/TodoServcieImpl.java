package com.test.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.todo.domain.Todos;
import com.test.todo.mapper.TodoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class TodoServcieImpl implements TodoService{

  @Autowired
  private TodoMapper todoMapper;

  @Override
  public List<Todos> list() throws Exception {
    return todoMapper.list();
  }

  @Override
  public PageInfo<Todos> list(int page, int size) throws Exception {
    PageHelper.startPage(page, size);
    List<Todos> list = todoMapper.list();
    PageInfo<Todos> pageInfo = new PageInfo<>(list);

    return pageInfo;
  }

  @Override
  public List<Todos> listDone() throws Exception {
    return todoMapper.listDone();
  }

  @Override
  public PageInfo<Todos> listDone(int page, int size) throws Exception {
    PageHelper.startPage(page, size);
    List<Todos> list = todoMapper.listDone();
    PageInfo<Todos> pageInfo = new PageInfo<>(list);

    return pageInfo;
  }

  @Override
  public Todos select(Long no) throws Exception {
    return todoMapper.select(no);
  }

  @Override
  public Todos selectById(String id) throws Exception {
    return todoMapper.selectById(id);
  }

  @Override
  public boolean insert(Todos entity) throws Exception {
    return todoMapper.insert(entity) > 0;
  }

  @Override
  public boolean update(Todos entity) throws Exception {
    return todoMapper.update(entity) > 0;
  }

  @Override
  public boolean updateById(Todos entity) throws Exception {
    return todoMapper.updateById(entity) > 0;
  }

  @Override
  public boolean delete(Long no) throws Exception {
    return todoMapper.delete(no) > 0;
  }

  @Override
  public boolean deleteById(String id) throws Exception {
    return todoMapper.deleteById(id) > 0;
  }

  @Override
  public boolean completeAll() throws Exception {
    return todoMapper.completeAll() > 0;
  }

  @Override
  public boolean deleteAll() throws Exception {
    return todoMapper.deleteAll() > 0;
  }
}
