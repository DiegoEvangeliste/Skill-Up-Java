package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.TransactionEntity;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;
import static com.alkemy.wallet.model.TypeEnum.*;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements ITransactionService {

    private final BankDAO bankDAO;

    @Override
    public ResponseEntity<Object> saveDeposit(TransactionDTO transaction) {

        isTransactionAllowed(transaction, DEPOSIT, "Incorrect operation, only can be a deposit");

        if (!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency())) {
            throw new BankException("Currency not permitted");
        }

        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        account.setBalance(account.getBalance() + transaction.getAmount());
        return saveTransaction(transaction, DEPOSIT, account);
    }

    @Override
    public ResponseEntity<Object> savePayment(TransactionDTO transaction) {
        isTransactionAllowed(transaction, PAYMENT, "Incorrect operation, only can be a payment");
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        if (!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency())) {
            throw new BankException("Currency not permitted");
        }
        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        if (account.getBalance() <= 0 || !(account.getBalance() >= transaction.getAmount())) {
            throw new BankException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - transaction.getAmount());
        return saveTransaction(transaction, PAYMENT, account);
    }

    @Override
    public ResponseEntity<Object> sendArs(TransactionDTO transaction) {
        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());

        if (destinationAccount.isPresent()) {
            if (destinationAccount.get().getCurrency().equals(ARS.getCurrency())) {
                AccountEntity originAccount = bankDAO.getAccount(user.getUserId(), ARS.getCurrency());
                if (originAccount.getAccountId() != destinationAccount.get().getAccountId()) {
                    if (originAccount.getBalance() >= transaction.getAmount()) {
                        destinationAccount.get().setBalance(destinationAccount.get().getBalance() + transaction.getAmount());
                        originAccount.setBalance(originAccount.getBalance() - transaction.getAmount());
                        saveTransaction(transaction, PAYMENT, originAccount);
                        return saveTransaction(transaction, INCOME, destinationAccount.get());
                    } else {
                        throw new BankException("Insufficient funds ");
                    }
                } else {
                    throw new BankException("the origin account cannot be the same as the destination account");
                }
            } else {
                throw new BankException("The destination account currency is not valid");
            }
        } else {
            throw new BankException("The destination account does not exist");
        }

    }

    @Override
    public ResponseEntity<Object> sendUsd(TransactionDTO transaction) {
        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());

        if (destinationAccount.isPresent()) {
            if (destinationAccount.get().getCurrency().equals(USD.getCurrency())) {
                AccountEntity originAccount = bankDAO.getAccount(user.getUserId(), USD.getCurrency());
                if (originAccount.getAccountId() != destinationAccount.get().getAccountId()) {
                    if (originAccount.getBalance() >= transaction.getAmount()) {
                        destinationAccount.get().setBalance(destinationAccount.get().getBalance() + transaction.getAmount());
                        originAccount.setBalance(originAccount.getBalance() - transaction.getAmount());
                        saveTransaction(transaction, PAYMENT, originAccount);
                        return saveTransaction(transaction, INCOME, destinationAccount.orElseThrow(() -> new BankException("Destination account does not exist")));
                    } else {
                        throw new BankException("Insufficient funds ");
                    }
                } else {
                    throw new BankException("the origin account cannot be the same as the destination account");
                }
            } else {
                throw new BankException("The destination account currency is not valid");
            }
        } else {
            throw new BankException("The destination account does not exist");
        }
    }

    @Override
    public ResponseEntity<Object> updateTransaction(Long id, TransactionDTO transactionDTO) {
        bankDAO.updateTransaction(id, transactionDTO);
        return new ResponseEntity<>("Update transaction", HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<TransactionEntity>> showAllTransactionsByUserId(Long userId) {
        Optional<UserEntity> opUser = bankDAO.getUserById(userId);
        if (opUser.isEmpty()) {
            throw new BankException("The requested user Id does not exist");
        }

        List<AccountEntity> accounts = bankDAO.getAllAccountByUser(opUser.get());
        List<TransactionEntity> transactions = new ArrayList<>();
        for (AccountEntity account : accounts) {
            transactions.addAll(bankDAO.getAllTransactionByAccount(account));
        }

        return ResponseEntity.ok(transactions);
    }

    private ResponseEntity<Object> saveTransaction(TransactionDTO transaction, TypeEnum type, AccountEntity accountEntity) {
        bankDAO.createTransaction(transaction, type, accountEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private static void isTransactionAllowed(TransactionDTO transaction, TypeEnum deposit, String message) {
        if (!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency())) {
            throw new BankException("Currency not permitted");
        }
        if (!transaction.getType().equalsIgnoreCase(deposit.getType())) {
            throw new BankException(message);
        }
    }
}