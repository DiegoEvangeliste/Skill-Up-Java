package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        List<Transaction> allTransactions = transactionRepository.findAll();
        List<Transaction> accountTransactions = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getAccount().getAccountId() == accountId) {
                accountTransactions.add(transaction);
            }
        }

        return accountTransactions;
    }
}
