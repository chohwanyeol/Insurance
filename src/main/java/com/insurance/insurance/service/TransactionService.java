package com.insurance.insurance.service;


import com.insurance.insurance.entity.Request;
import com.insurance.insurance.entity.SiteUser;
import com.insurance.insurance.entity.Transaction;
import com.insurance.insurance.repository.RequestRepository;
import com.insurance.insurance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RequestRepository requestRepository;
    public Transaction transaction(SiteUser siteUser, Request request, int price) {
        String description = request.getStatus();
        if (description != "approved"){
            // 조건을 만족하지 않으면 null 반환
            return null;
        }

        LocalDateTime dateTime = LocalDateTime.now();
        String status = "pending";
        Transaction transaction = new Transaction(siteUser,request,price,dateTime,status);
        transaction = transactionRepository.save(transaction);
        request.setTransaction(transaction);
        requestRepository.save(request);
        //돈지급
        transaction.setStatus("approved");

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getBySiteUser(SiteUser siteUser) {
        return transactionRepository.findAllBySiteUser(siteUser);
    }
    public Page<Transaction> getBySiteUser(SiteUser siteUser, Pageable pageable) {
        return transactionRepository.findAllBySiteUser(siteUser, pageable);
    }

    public Transaction getBySiteUserAndId(SiteUser siteUser, Integer id) {
        return transactionRepository.findBySiteUserAndId(siteUser,id);
    }
}
