package com.aloha.board.service;

import java.io.FileInputStream;
import java.net.URLEncoder;
// import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.board.domain.File;
import com.aloha.board.mapper.FileMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

  @Autowired FileMapper fileMapper;

  @Value("${upload.path}")
  private String uploadPath;      // 업로드 경로

  @Override
  public List<File> list() throws Exception {
    return fileMapper.list();
  }

  @Override
  public PageInfo<File> list(int page, int size) throws Exception {
    PageHelper.startPage(page, size);
    List<File> list = fileMapper.list();
    PageInfo<File> pageInfo = new PageInfo<>(list);
    return pageInfo;
  }

  @Override
  public File select(Long no) throws Exception {
    return fileMapper.select(no);
  }

  @Override
  public File selectById(String id) throws Exception {
    return fileMapper.selectById(id);
  }

  @Override
  public boolean insert(File entity) throws Exception {
    return fileMapper.insert(entity) > 0;
  }

  @Override
  public boolean update(File entity) throws Exception {
    return fileMapper.update(entity) > 0;
  }

  @Override
  public boolean updateById(File entity) throws Exception {
    return fileMapper.updateById(entity) > 0;
  }

  // 파일 시스템의 파일 삭제
  public boolean delete(File file) {
    if(file == null) {
      log.info("파일이 없습니다.");
      return false;
    }

    String filePath = file.getFilePath();
    java.io.File deleteFile = new java.io.File(filePath);

    if(!deleteFile.exists()) {
      log.error("파일이 존재하지 않습니다.");
      return false;
    }

    // 파일 삭제
    boolean deleted = deleteFile.delete();
    if(deleted) {
      log.info("파일이 삭제되었습니다.");
      log.info("- " + filePath);
    }

    return true;
  }

  @Override
  public boolean delete(Long no) throws Exception {
    File file = fileMapper.select(no);
    delete(file);
    return fileMapper.delete(no) > 0;
  }

  @Override
  public boolean deleteById(String id) throws Exception {
    File file = fileMapper.selectById(id);
    delete(file);
    return fileMapper.deleteById(id) > 0;
  }

  @Override
  public boolean upload(File file) throws Exception {
    boolean result = false;
    MultipartFile multipartFile = file.getData();

    // 파일이 없을 때
    if(multipartFile.isEmpty()) {
      return result;
    }

    // 1️⃣ 파일 시스템에 등록 (파일 복사)
    // - 파일 정보 : 원본파일명, 파일 용량, 파일 데이터
    //               파일명, 파일경로
    String originName = multipartFile.getOriginalFilename();
    long fileSize = multipartFile.getSize();
    byte[] fileData = multipartFile.getBytes();
    String fileName = UUID.randomUUID().toString() + "_" + originName;
    String filePath = uploadPath + "/" + fileName;
    java.io.File uploadFile = new java.io.File(filePath);
    FileCopyUtils.copy(fileData, uploadFile);   // 파일 복사 (업로드)

    // 2️⃣ DB 에 등록
    file.setOriginName(originName);
    file.setFileName(fileName);
    file.setFilePath(filePath);
    file.setFileSize(fileSize);

    result = fileMapper.insert(file) > 0;
    return result;
  }

  @Override
  public int upload(List<File> fileList) throws Exception {
    int result = 0;
    if(fileList == null || fileList.isEmpty())
      return result;

    for(File file : fileList) {
      result += upload(file) ? 1 : 0;
    }

    return result;
  }

  @Override
  public boolean download(String id, HttpServletResponse response) throws Exception {
    File file = fileMapper.selectById(id);

    // 파일이 없으면
    if(file == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return false;
    }

    // 파일 입력
    String fileName = file.getOriginName();
    String filePath = file.getFilePath();
    java.io.File downloadFile = new java.io.File(filePath);
    FileInputStream fis = new FileInputStream(downloadFile);

    // 파일 출력
    ServletOutputStream sos = response.getOutputStream();

    // 파일 다운로드를 위한 응답 헤더 세팅
    // - Content-Type         : application/octet-stream
    // - Content-Disposition  : attachment; filename="파일명.확장자"
    fileName = URLEncoder.encode(fileName, "UTF-8");
    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
    response.setHeader("Content-Dispostion", "attachment; filename=\"" + fileName + " \"");

    // 다운로드
    boolean result = FileCopyUtils.copy(fis, sos) > 0;
    fis.close();
    sos.close();
    return result;
  }

  @Override
  public List<File> listByParent(File file) throws Exception {
    return fileMapper.listByParent(file);
  }

  @Override
  public boolean deleteByParent(File file) throws Exception {
    List<File> fileList = fileMapper.listByParent(file);

    // 파일 삭제
    for(File deleteFile : fileList) {
      delete(deleteFile);
    }

    // DB 삭제
    return fileMapper.deleteByParent(file) > 0;
  }

  @Override
  public boolean deleteFiles(String noList) throws Exception {
    if(noList == null || noList.isEmpty()) return false;

    int count = 0;
    String[] nos = noList.split(",");
    for(String noStr : nos) {
      Long no = Long.parseLong(noStr);
      count += delete(no) ? 1 : 0;
    }

    log.info("파일 " + count + "개를 삭제하였습니다.");

    return count > 0;
  }

  @Override
  public boolean deleteFilesById(String idList) throws Exception {
    if(idList == null || idList.isEmpty()) return false;
    int count = 0;

    String[] ids = idList.split(",");
    for(String idStr : ids) {
      count += deleteById(idStr) ? 1 : 0;
    }
    log.info("파일 " + count + "개를 삭제하였습니다.");

    return count > 0;
  }

  @Override
  public boolean deleteFileList(List<Long> noList) throws Exception {
    if(noList == null || noList.isEmpty()) return false;

    // 파일 삭제
    for(Long no : noList) {
      File file = select(no);
      delete(file);
    }

    // DB 삭제
    int count = fileMapper.deleteFileList(noList);
    log.info("파일 " + count + "개를 삭제하였습니다.");

    return count > 0;
  }

  @Override
  public boolean deleteFileListById(List<String> idList) throws Exception {
    if(idList == null || idList.isEmpty()) return false;

    // 파일 삭제
    for(String id : idList) {
      File file = selectById(id);
      delete(file);
    }

    // DB 삭제
    int count = fileMapper.deleteFileListById(idList);
    log.info("파일 " + count + "개를 삭제하였습니다.");

    return count > 0;
  }

  @Override
  public File selectByType(File file) throws Exception {
    return fileMapper.selectByType(file);
  }

  @Override
  public List<File> listByType(File file) throws Exception {
    return fileMapper.listByType(file);
  }

}
