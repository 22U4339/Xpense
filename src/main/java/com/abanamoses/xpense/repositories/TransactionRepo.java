package com.abanamoses.xpense.repositories;

import com.abanamoses.xpense.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, UUID> {
    //List<Transaction> findAllByOrderByDateTimeDesc();
    List<Transaction> findByUser_Id(Long id);
}
