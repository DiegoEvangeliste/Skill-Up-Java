package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.entity.TransactionEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITransactionService {
     ResponseEntity<Object> saveDeposit(TransactionDTO transaction);
     ResponseEntity<Object> savePayment(TransactionDTO transaction);
     ResponseEntity<Object> sendArs(TransactionDTO transaction);
     ResponseEntity<List<TransactionEntity>> showAllTransactionsByUserId(Long userId);
     ResponseEntity<Object> sendUsd(TransactionDTO transaction);
     ResponseEntity<Object>updateTransaction(Long id, TransactionDTO transactionDTO);
}
