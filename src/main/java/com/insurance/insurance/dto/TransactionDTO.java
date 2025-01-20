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
    private String productTitle;
    private LocalDateTime dateTime;
    private String status;
    private Integer price;
    private String bank;
    private String bankAccount;
    private Integer requestId;


    public void EntityToDTO(Transaction transaction){
        this.transactionId = transaction.getId();
        this.productTitle = transaction.getRequest().getInsurance().getProduct().getTitle();
        this.dateTime = transaction.getDateTime();
        this.status = Status.getKoreanStatus(transaction.getStatus());
        this.price =transaction.getPrice();
        this.bank = transaction.getRequest().getInsurance().getBank();
        this.bankAccount = transaction.getRequest().getInsurance().getBankAccount();
        this.requestId = transaction.getRequest().getId();
    }
}
