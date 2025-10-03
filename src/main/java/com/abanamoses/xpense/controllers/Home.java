package com.abanamoses.xpense.controllers;

import com.abanamoses.xpense.entities.Transaction;
import com.abanamoses.xpense.repositories.TransactionRepo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/")
public class Home {
    private final TransactionRepo repo;

    public Home(TransactionRepo repo) {
        this.repo = repo;
    }

        @GetMapping
        public List<Transaction> home(){
        return repo.findAllByOrderByDateTimeDesc();
        }
}
