package com.insurance.insurance.service;


import com.insurance.insurance.entity.Request;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.Transaction;
import com.insurance.insurance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    public CompletableFuture<Transaction> transaction(SiteUser siteUser, Request request) {
        int price = request.getPrice();
        LocalDateTime dateTime = LocalDateTime.now();
        String status = "대기중";
        Transaction transaction = new Transaction(siteUser,price,dateTime,status);
        transaction = transactionRepository.save(transaction);
        //돈지급

        //
        transaction.setStatus("지급완료");

        return CompletableFuture.completedFuture(transactionRepository.save(transaction));
    }
}
