package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class BankDAO {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final RoleRepository roleRepository;

    private final TransactionRepository transactionRepository;

    private final FixedTermDepositRepository fixedTermDepositRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<TransactionEntity> getTransactionId(Long id){
        return transactionRepository.findById(id);
    }
    public TransactionEntity updateTransaction(Long id, TransactionDTO transactionDTO){
        Optional<TransactionEntity> transaction = getTransactionId(id);
        transaction.get().setDescription(transactionDTO.getDescription());
        return  transactionRepository.saveAndFlush(transaction.get());
    }

    public AccountEntity getAccount(Long userId, String currency) {
        return accountRepository.getAccount(userId, currency);
    }

    public Optional<AccountEntity> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public void deleteByUserId(Long userId) {
        userRepository.deleteById(userId);
    }

    public RoleEntity getRole(RoleDTO role) {
        Optional<RoleEntity> roleEntity = roleRepository.findById(role.getId());
        if (!roleEntity.isPresent()) {
            RoleEntity roleEntityResponse = RoleEntity.builder()
                    .roleId(role.getId())
                    .name(role.getName())
                    .description(role.getDescription())
                    .build();
            roleRepository.saveAndFlush(roleEntityResponse);
            return roleEntityResponse;
        }
        return roleEntity.get();
    }

    public UserEntity createUser(UserRequestDTO user, RoleDTO role) {
        RoleEntity roleEntity = getRole(role);
        UserEntity userEntity = UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(roleEntity)
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        return userRepository.saveAndFlush(userEntity);
    }

    public AccountEntity createAccount(AccountDTO accountDTO, UserEntity userEntity) {
        AccountEntity accountEntity = AccountEntity.builder()
                .currency(accountDTO.getCurrency())
                .transactionLimit(accountDTO.getTransactionLimit())
                .balance(0.0)
                .user(userEntity)
                .build();
        return accountRepository.saveAndFlush(accountEntity);
    }
    public AccountEntity updateAccount(Long id, AccountDTO account){
      Optional<AccountEntity> accountEntity = getAccountById(id);
        accountEntity.get().setTransactionLimit(account.getTransactionLimit());
        return  accountRepository.saveAndFlush(accountEntity.get());
    }

    public TransactionEntity createTransaction(TransactionDTO transaction, TypeEnum type, AccountEntity accountEntity) {
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .type(type)
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .account(accountEntity)
                .build();
        return transactionRepository.saveAndFlush(transactionEntity);
    }

    public FixedTermDepositEntity createFixedTermDeposit(FixedTermDepositDTO fixedTermDeposit, AccountEntity accountEntity, UserEntity userEntity) {
        FixedTermDepositEntity fixedTermDepositEntity = FixedTermDepositEntity.builder()
                .amount(fixedTermDeposit.getAmount())
                .closingDate(fixedTermDeposit.getClosingDate())
                .interests(fixedTermDeposit.getInterests())
                .account(accountEntity)
                .user(userEntity)
               .build();
        return fixedTermDepositRepository.saveAndFlush(fixedTermDepositEntity);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public List<AccountEntity> getAllAccountByUser(UserEntity user) {
        return new ArrayList<>(user.getAccount());
    }

    public List<TransactionEntity> getAllTransactionByAccount(AccountEntity account) {
        return new ArrayList<>(account.getTransactions());
    }
    public UserEntity updateUser(Long id,UserRequestDTO user){
        Optional<UserEntity> userUpdate = getUserById(id);
                userUpdate.get().setFirstName(user.getFirstName());
                userUpdate.get().setLastName(user.getLastName());
                userUpdate.get().setPassword(user.getPassword());
        return userRepository.saveAndFlush(userUpdate.get());
    }
    public String returnUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
