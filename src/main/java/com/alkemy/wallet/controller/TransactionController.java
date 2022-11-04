package com.alkemy.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.service.ITransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    @PatchMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateDescription(@PathVariable Long transactionId,
            @RequestBody String description, Long accountId) {
        Transaction transaction = transactionService.updateDescription(transactionId, description, accountId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }

        return ResponseEntity.notFound().build();
    }
}
