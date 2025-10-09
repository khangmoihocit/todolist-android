package com.khangmoihocit.shopapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<String> getInstance(){
        return ResponseEntity.ok("hihi");
    }
}
