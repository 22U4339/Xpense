package com.abanamoses.xpense.controllers;


import com.abanamoses.xpense.entities.Transaction;
import com.abanamoses.xpense.repositories.TransactionRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionRepo repo;

    public TransactionController(TransactionRepo repo) {
        this.repo = repo;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable(name = "id")UUID id){
        Transaction transaction = repo.findById(id).orElse(null);

        if(null == transaction)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(transaction);

    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction, UriComponentsBuilder componentsBuilder){
        repo.save(transaction);

        URI uri = componentsBuilder.path("/transaction/{id}").buildAndExpand(transaction.getId()).toUri();

        return ResponseEntity.created(uri).body(transaction);
    }

    @PutMapping
    public ResponseEntity<Transaction> update(@RequestBody Transaction transaction){

        if(null == transaction.getId())
            return ResponseEntity.badRequest().body(transaction);
        if(!repo.existsById(transaction.getId()))
            return ResponseEntity.notFound().build();

        repo.save(transaction);
        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> delete(@PathVariable(name = "id")UUID uuid){
        repo.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }
}
