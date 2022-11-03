package com.alkemy.wallet.service;

import java.util.List;

import com.alkemy.wallet.model.entity.Transaction;

public interface ITransactionService {
    public void saveTransaction(Transaction transaction);

    public List<Transaction> findAllByAccountId(Long accountId);
}
