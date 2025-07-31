package com.aloha.board.domain;

import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class File {
  private Long no;
  private String id;
  private String parentTable;
  private Long parentNo;
  private String fileName;
  private String originName;
  private String filePath;
  private Long fileSize;
  private Long seq;       // 순서
  private String type;    // 파일종류('MAIN', 'SUB')
  private Date createdAt;
  private Date updatedAt;

  // 파일 데이터
  MultipartFile data;

  public File() {
    this.id = UUID.randomUUID().toString();
  }
}
