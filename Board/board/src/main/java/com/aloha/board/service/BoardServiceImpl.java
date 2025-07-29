package com.aloha.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.board.domain.Board;
import com.aloha.board.mapper.BoardMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class BoardServiceImpl implements BoardService {

  @Autowired
  BoardMapper boardMapper;

  @Override
  public List<Board> list() throws Exception {
    return boardMapper.list();
  }

  @Override
  public PageInfo<Board> list(int page, int size) throws Exception {
    // ⭐PageHelper.startPage(현재 페이지, 페이지당 데이터 수)
    PageHelper.startPage(page, size);
    List<Board> list = boardMapper.list();
    PageInfo<Board> pageInfo = new PageInfo<>(list);

    return pageInfo;
  }

  @Override
  public Board select(Long no) throws Exception {
    return boardMapper.select(no);
  }

  @Override
  public Board selectById(String id) throws Exception {
    return boardMapper.selectById(id);
  }

  @Override
  public boolean insert(Board entity) throws Exception {
    return boardMapper.insert(entity) > 0;
  }

  @Override
  public boolean update(Board entity) throws Exception {
    return boardMapper.update(entity) > 0;
  }

  @Override
  public boolean updateById(Board entity) throws Exception {
    return boardMapper.updateById(entity) > 0;
  }

  @Override
  public boolean delete(Long no) throws Exception {
    return boardMapper.delete(no) > 0;
  }

  @Override
  public boolean deleteById(String id) throws Exception {
    return boardMapper.deleteById(id) > 0;
  }

}
