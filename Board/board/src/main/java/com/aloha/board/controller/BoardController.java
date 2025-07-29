package com.aloha.board.controller;

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

import com.aloha.board.domain.Board;
import com.aloha.board.domain.Pagination;
import com.aloha.board.service.BoardService;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping()
    public ResponseEntity<?> getAll(
        @RequestParam(value = "page", defaultValue = "1", required = false) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
        Pagination pagination
    ) {
        try {
            PageInfo<Board> pageInfo = boardService.list(page, size);
            pagination.setPage(page);
            pagination.setSize(size);
            pagination.setTotal(pageInfo.getTotal());
            Map<String, Object> response = new HashMap<>();
            List<Board> list = pageInfo.getList();
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
            Board board = boardService.selectById(id);
            if(board != null)
                return new ResponseEntity<>(board, HttpStatus.OK);
            else
                return new ResponseEntity<>(board, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Board board) {
        try {
            boolean result = boardService.insert(board);
            if(result)
                return new ResponseEntity<>("게시글 생성 성공", HttpStatus.OK);
            else
                return new ResponseEntity<>("게시글 생성 실패", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
        @RequestBody Board board
        ) {
        try {
            boolean result = boardService.updateById(board);
            if(result)
                return new ResponseEntity<>("게시글 수정 성공", HttpStatus.OK);
            else
                return new ResponseEntity<>("게시글 수정 실패", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable("id") String id) {
        try {
            boolean result = boardService.deleteById(id);
            if(result)
                return new ResponseEntity<>("게시글 삭제 성공", HttpStatus.OK);
            else
                return new ResponseEntity<>("게시글 삭제 실패", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
