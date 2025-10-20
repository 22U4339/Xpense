package com.abanamoses.xpense.controllers;


import com.abanamoses.xpense.dtos.TransactionDto;
import com.abanamoses.xpense.entities.Transaction;
import com.abanamoses.xpense.entities.Users;
import com.abanamoses.xpense.repositories.TransactionRepo;
import com.abanamoses.xpense.repositories.UsersRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionRepo repo;
    private final UsersRepo userRepo;

    public TransactionController(TransactionRepo repo, UsersRepo userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }



    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable(name = "id")UUID id){
        Transaction transaction = repo.findById(id).orElse(null);

        if(null == transaction)
            return ResponseEntity.notFound().build();
        var transactionDto = new TransactionDto();
        transactionDto.fromTransaction(transaction);
        return ResponseEntity.ok(transactionDto);

    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionDto t, @AuthenticationPrincipal Users user, UriComponentsBuilder componentsBuilder){

        if(null == user)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        var transaction = t.toTransaction(t, user);

        repo.save(transaction);

        URI uri = componentsBuilder.path("/transaction/{id}").buildAndExpand(transaction.getId()).toUri();


        return ResponseEntity.created(uri).body(t.fromTransactions(transaction));
    }

    @PutMapping
    public ResponseEntity<TransactionDto> update(@RequestBody TransactionDto transaction, @AuthenticationPrincipal Users user){

        if(null == transaction.getId())
            return ResponseEntity.badRequest().build();
        if(!repo.existsById(transaction.getId()))
            return ResponseEntity.notFound().build();
        var updated = transaction.toTransaction(transaction,user);
        repo.save(updated);
        return ResponseEntity.ok(TransactionDto.fromTransactions(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id")UUID uuid){
        repo.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }
}
