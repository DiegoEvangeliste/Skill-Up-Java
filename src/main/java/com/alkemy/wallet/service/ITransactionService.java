package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDTO;
import org.springframework.http.ResponseEntity;

public interface ITransactionService {
     ResponseEntity<Object> saveDeposit(TransactionDTO transaction);
     ResponseEntity<Object> savePayment(TransactionDTO transaction);

     ResponseEntity<Object> sendArs(TransactionDTO transaction);
}
