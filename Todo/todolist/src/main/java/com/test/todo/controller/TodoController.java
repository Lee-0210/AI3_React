package com.test.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.test.todo.domain.Pagination;
import com.test.todo.domain.Todos;
import com.test.todo.service.TodoService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/todos")
public class TodoController {

  @Autowired private TodoService todoService;


  @GetMapping()
  public ResponseEntity<?> getAll(
    @RequestParam(value = "page", defaultValue = "1", required = false) int page,
    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
    Pagination pagination,
    @RequestParam(value = "isDone", defaultValue = "yes", required = false) String isDone
  ) {
      log.info("page : {}", page);
      log.info("size : {}", size);
      try {
        PageInfo<Todos> pageInfo = null;
        if(isDone.equals("yes"))
          pageInfo = todoService.listDone(page, size);
        else if(isDone == null || isDone.equals("no"))
          pageInfo = todoService.list(page, size);

        pagination.setPage(page);
        pagination.setSize(size);
        pagination.setTotal(pageInfo.getTotal());
        Map<String, Object> response = new HashMap<>();
        List<Todos> list = pageInfo.getList();
        log.info("pagination?? : {}", pagination);

        response.put("list", list);
        response.put("pagination", pagination);

        return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOne(@PathVariable("id") String id) {
      try {
        Todos todo = todoService.selectById(id);
        if(id == null || todo == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(todo, HttpStatus.OK);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @PostMapping()
  public ResponseEntity<?> create(@RequestBody Todos todo) {
      try {
        boolean result = todoService.insert(todo);
        if(result)
          return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        else
          return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @PutMapping()
  public ResponseEntity<?> update(@RequestBody Todos todo) {
      try {
        boolean result = todoService.updateById(todo);
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
        boolean result = todoService.deleteById(id);
        if(result)
          return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        else
          return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
      } catch (Exception e) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @DeleteMapping("/all")
  public ResponseEntity<?> destroyAll() {
    try {
      boolean result = todoService.deleteAll();
      if(result)
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
      else
        return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/all")
  public ResponseEntity<?> updateAll() {
    try {
      boolean result = todoService.completeAll();
      if(result)
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
      else
        return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
