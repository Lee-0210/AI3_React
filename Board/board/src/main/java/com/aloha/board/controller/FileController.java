package com.aloha.board.controller;

import java.io.FileInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.board.domain.File;
import com.aloha.board.service.FileService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/files")
public class FileController {

  @Autowired private FileService fileService;

  @Autowired ResourceLoader resourceLoader;   // resources 자원 가져오는 객체


  @GetMapping()
  public ResponseEntity<?> getAll() {
      try {
          List<File> list = fileService.list();
          return new ResponseEntity<>(list, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOne(@PathVariable("id") String id) {
      try {
          File file = fileService.selectById(id);
          return new ResponseEntity<>(file, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createFormData(File file) {
      try {
          boolean result = fileService.upload(file);
          if(result)
            return new ResponseEntity<>(file.getId(), HttpStatus.CREATED);
          else
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createJSON(@RequestBody File file) {
      try {
          boolean result = fileService.upload(file);
          if(result)
            return new ResponseEntity<>(file, HttpStatus.CREATED);
          else
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @PutMapping()
  public ResponseEntity<?> update(@RequestBody File file) {
      try {
          boolean result = fileService.update(file);
          if(result)
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
          else
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> destroy(@PathVariable("id") String id) {
      try {
          boolean result = fileService.deleteById(id);
          if(result)
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
          else
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  // 파일 선택 삭제
  @DeleteMapping("")
  public ResponseEntity<?> deleteFiles(
    @RequestParam(value = "noList", required = false) List<Long> noList,
    @RequestParam(value = "idList", required = false) List<String> idList
  ) throws Exception {
    log.info("noList : {}", noList);
    log.info("idList : {}", idList);

    boolean result = false;
    if(noList != null) {
      result = fileService.deleteFileList(noList);
    }
    if(idList != null) {
      result = fileService.deleteFileListById(idList);
    }

    if(result)
      return new ResponseEntity<>(HttpStatus.OK);

    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  // 파일 다운로드
  @GetMapping("/download/{id}")
  public void fileDownload(
    @PathVariable("id") String id,
    HttpServletResponse response
    ) throws Exception {
      fileService.download(id, response);
  }


  // 썸네일 이미지
  @GetMapping("/img/{id}")
  public void thumbnailImg(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
    File file = fileService.selectById(id);
    String filePath = file != null ? file.getFilePath() : null;
    log.info("########### file : {}", file);
    log.info("########### filePath : {}", filePath);
    java.io.File imgFile;
    // 파일 경로가 null 또는 파일이 존재하지 않는 경우 -> no-img
    // org.springframework.cor.io.Resource
    Resource resource = resourceLoader.getResource("classpath:static/img/no-img.png");
    if(filePath == null || !(imgFile = new java.io.File(filePath)).exists()) {
      // no-img.png 로 적 용
      imgFile = resource.getFile();
      filePath = imgFile.getPath();
    }

    // 확장자
    String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
    String mimeType = MimeTypeUtils.parseMimeType("image/" + ext).toString();
    MediaType mType = MediaType.valueOf(mimeType);

    if(mType == null) {
      // 이미지 타입이 아닌 경우
      response.setContentType(MediaType.IMAGE_PNG_VALUE);
      imgFile = resource.getFile();
    } else {
      // 이미지 타입
      response.setContentType(mType.toString());
    }

    FileInputStream fis = new FileInputStream(imgFile);
    ServletOutputStream sos = response.getOutputStream();
    FileCopyUtils.copy(fis, sos);
  }


  // 첨부파일 목록 타입별 조회
  // 🔗 /files/{pTable}/{pNo}?type={MAIN, SUB}
  @GetMapping("/{pTable}/{pNo}")
  public ResponseEntity<?> getAllFiles(
    @PathVariable("pTable") String pTable,
    @PathVariable("pNo") String pNo,
    @RequestParam(value = "type", required = false) String type
    ) {
    try {
      File file = new File();
      file.setParentTable(pTable);
      file.setParentNo(Long.parseLong(pNo));
      file.setType(type);
      // type 이 없을 때 -> 부모기준 모든 파일
      if(type == null) {
        List<File> list = fileService.listByParent(file);
        return new ResponseEntity<>(list, HttpStatus.OK);
      }
      // type : "MAIN" -> 메인파일 1개
      if(type.equals("MAIN")) {
        File mainFile = fileService.selectByType(file);
        return new ResponseEntity<>(mainFile, HttpStatus.OK);
      }
      // type : "SUB", "?" -> 타입별 파일 목록
      else {
        List<File> list = fileService.listByParent(file);
        return new ResponseEntity<>(list, HttpStatus.OK);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
