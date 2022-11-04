package com.alkemy.wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.service.ITransactionService;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getAllByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.findAllByAccountId(accountId));
    }
}
