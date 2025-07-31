package com.aloha.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.board.domain.File;

@Mapper
public interface FileMapper extends BaseMapper<File> {

  // 부모 기준 목록
  public List<File> listByParent(File file) throws Exception;
  // 부모 기준 삭제
  public int deleteByParent(File file) throws Exception;
  // 선택 삭제 - no
  public int deleteFiles(String noList) throws Exception;
  // 선택 삭제 - id
  public int deleteFilesById(String idList) throws Exception;
  // 선택 삭제 - no
  public int deleteFileList(@Param("noList") List<Long> noList) throws Exception;
  // 선택 삭제 - id
  public int deleteFileListById(@Param("idList") List<String> idList) throws Exception;
  // 타입별 파일 조회
  public File selectByType(File file) throws Exception;
  // 타입별 파일 목록
  public List<File> listByType(File file) throws Exception;
}
