package com.abanamoses.xpense.dtos;

import com.abanamoses.xpense.entities.Transaction;
import com.abanamoses.xpense.entities.TransactionType;
import com.abanamoses.xpense.entities.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class TransactionDto {
    private UUID id;
    private TransactionType type;
    private float amount;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    public Transaction toTransaction(TransactionDto t, Users user){
       return new Transaction().builder()
                .id(t.getId())
                .type(t.getType())
                .amount(t.getAmount())
                .description(t.getDescription())
                .user(user)
                .build();
    }
    public void fromTransaction(Transaction t){
        this.id = t.getId();
        this.type = t.getType();
        this.amount = t.getAmount();
        this.description = t.getDescription();
        this.dateTime = t.getDateTime();
    }

    public static TransactionDto fromTransactions(Transaction t) {
        TransactionDto dto = new TransactionDto();
        dto.setId(t.getId());
        dto.setType(t.getType());
        dto.setDescription(t.getDescription());
        dto.setAmount(t.getAmount());
        dto.setDateTime(t.getDateTime());
        return dto;
    }
}
