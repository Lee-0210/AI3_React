package com.aloha.board.service;

import java.util.List;

import com.aloha.board.domain.File;

import jakarta.servlet.http.HttpServletResponse;

public interface FileService extends BaseService<File> {

  // 파일 업로드
  public boolean upload(File file) throws Exception;
  public int upload(List<File> fileList) throws Exception;

  // 파일 다운로드
  public boolean download(String id, HttpServletResponse response) throws Exception;

  // 부모 기준 목록
  public List<File> listByParent(File file) throws Exception;
  // 부모 기준 삭제
  public boolean deleteByParent(File file) throws Exception;
  // 선택 삭제 - no
  public boolean deleteFiles(String noList) throws Exception;
  // 선택 삭제 - id
  public boolean deleteFilesById(String idList) throws Exception;
  // 선택 삭제 - no
  public boolean deleteFileList(List<Long> noList) throws Exception;
  // 선택 삭제 - id
  public boolean deleteFileListById(List<String> idList) throws Exception;
  // 타입별 파일 조회
  public File selectByType(File file) throws Exception;
  // 타입별 파일 목록
  public List<File> listByType(File file) throws Exception;
}
