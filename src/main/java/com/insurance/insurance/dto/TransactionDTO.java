package com.insurance.insurance.dto;

import com.insurance.insurance.entity.Transaction;
import com.insurance.insurance.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {
    private Integer transactionId;
    private String productName;
    private LocalDateTime dateTime;
    private String status;

    public void EntityToDTO(Transaction transaction){
        this.transactionId = transaction.getId();
        this.productName = transaction.getInsurance().getProduct().getName();
        this.dateTime = transaction.getDateTime();
        this.status = Status.getKoreanStatus(transaction.getStatus());
    }
}
