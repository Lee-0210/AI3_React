package com.aloha.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.board.domain.Board;
import com.aloha.board.domain.File;
import com.aloha.board.mapper.BoardMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {

  @Autowired
  private BoardMapper boardMapper;

  @Autowired private FileService fileService;

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
    // 게시글 등록
    int result = boardMapper.insert(entity);
    // 파일 업로드
    result += upload(entity);

    return result > 1;
  }

  // 파일 업로드
  public int upload(Board entity) {
    int result = 0;
    String pTable = "board";
    Long pNo = entity.getNo();
    try {
    List<File> uploadFileList = new ArrayList<>();
    MultipartFile mainFile = entity.getMainFile();
    if(mainFile != null && !mainFile.isEmpty()) {
      File mainFileInfo = new File();
      mainFileInfo.setParentTable(pTable);
      mainFileInfo.setParentNo(pNo);
      mainFileInfo.setData(mainFile);
      mainFileInfo.setType("MAIN");
      uploadFileList.add(mainFileInfo);
    }

    List<MultipartFile> files = entity.getFiles();
    if(files != null && !files.isEmpty()) {
      for(MultipartFile multipartFile : files) {
        if(multipartFile.isEmpty()) {
          continue;
        }
        File fileInfo = new File();
        fileInfo.setParentTable(pTable);
        fileInfo.setParentNo(pNo);
        fileInfo.setData(multipartFile);
        fileInfo.setType("SUB");
      }
    }
      result += fileService.upload(uploadFileList);
    } catch(Exception e) {
      log.error("게시글 파일 업로드 중 에러 발생");
      e.printStackTrace();
    }

    return result;
  }

  @Override
  public boolean update(Board entity) throws Exception {
    // 게시글 수정
    int result = boardMapper.update(entity);
    result += upload(entity);

    return result > 0;
  }

  @Override
  public boolean updateById(Board entity) throws Exception {
    int result = boardMapper.updateById(entity);
    Board oldBoard = boardMapper.selectById(entity.getId());
    entity.setNo(oldBoard.getNo());
    result += upload(entity);

    return result > 0;
  }

  @Override
  public boolean delete(Long no) throws Exception {
    // 게시글 삭제
    int result = boardMapper.delete(no);
    // 종속된 첨부파일 삭제
    File file = new File();
    file.setParentTable("board");
    file.setParentNo(no);
    boolean deleteCount = fileService.deleteByParent(file);
    if(deleteCount)
      log.info("파일이 전부 삭제되었습니다.");

    return result > 0;
  }

  @Override
  public boolean deleteById(String id) throws Exception {
    // 게시글 삭제
    int result = boardMapper.deleteById(id);
    // 종속된 첨부파일 삭제
    Board board = boardMapper.selectById(id);
    Long no = board.getNo();
    File file = new File();
    file.setParentTable("board");
    file.setParentNo(no);
    boolean deleteCount = fileService.deleteByParent(file);
    if(deleteCount)
      log.info("파일이 전부 삭제되었습니다.");

    return result > 0;
  }

}
