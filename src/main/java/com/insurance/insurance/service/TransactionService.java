package com.insurance.insurance.service;


import com.insurance.insurance.entity.Request;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.Transaction;
import com.insurance.insurance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    public CompletableFuture<Transaction> transaction(SiteUser siteUser, Request request) {
        String description = request.getDescription();
        if (description != null){
            // 조건을 만족하지 않으면 null 반환
            return CompletableFuture.completedFuture(null);
        }

        int price = request.getPrice();
        LocalDateTime dateTime = LocalDateTime.now();
        String status = "대기중";
        Transaction transaction = new Transaction(siteUser,request.getInsurance(),price,dateTime,status);
        transaction = transactionRepository.save(transaction);
        //돈지급
        //
        transaction.setStatus("지급완료");

        return CompletableFuture.completedFuture(transactionRepository.save(transaction));
    }

    public List<Transaction> getBySiteUser(SiteUser siteUser) {
        return transactionRepository.findAllBySiteUser(siteUser);
    }

    public Transaction getBySiteUserAndId(SiteUser siteUser, Integer id) {
        return transactionRepository.findBySiteUserAndId(siteUser,id);
    }
}
