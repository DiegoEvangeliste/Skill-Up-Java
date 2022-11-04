package com.alkemy.wallet.controller;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> develop

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
=======
import org.springframework.stereotype.Controller;
>>>>>>> develop
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alkemy.wallet.model.entity.Account;
<<<<<<< HEAD
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
=======
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.repository.TransactionRepository;
>>>>>>> develop

@Controller
@RequestMapping("/transactions")
public class TransactionController {

<<<<<<< HEAD
    @Autowired
    private Account account;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ITransactionService transactionService;

    User user;
    double currentBalance;
    double newBalance;
    LocalDateTime currentDateTime = LocalDateTime.now();

    @PostMapping("/deposit")
    public String deposit(@RequestParam("amount") Double amount,
            @RequestParam("accountId") Long accountId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // CHECK FOR EMPTY STRINGS:
        if (amount == 0 || accountId == 0) {
            redirectAttributes.addFlashAttribute("Deposit Amount or Account Depositing to Can't Be 0!");
            return "redirect:/";
        }
        // GET LOGGED IN USER:
        user = (User) session.getAttribute("user");

        // CHECK IF DEPOSIT AMOUNT IS 0:
        if (amount == 0) {
            redirectAttributes.addFlashAttribute("Deposit Amount Can't Be 0!");
            return "redirect:/";
        }

        // UPDATE BALANCE:
        currentBalance = account.getBalance();

        newBalance = currentBalance + amount;

        // Log Successful Transaction
        transactionRepository.save(null);
        redirectAttributes.addFlashAttribute("success", "Amount Deposited Successfully!");
        return "redirect:/";
    }
    // End Of Deposits.

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getAllByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.findAllByAccountId(accountId));
    }
=======
@Autowired
private Account account;

@Autowired
private TransactionRepository transactionRepository;

User user;
double currentBalance;
double newBalance;
LocalDateTime currentDateTime = LocalDateTime.now();

@PostMapping("/deposit")
public String deposit(@RequestParam("amount")Double amount,
                      @RequestParam("accountId") Long accountId,
                      HttpSession session,
                      RedirectAttributes redirectAttributes){

    // CHECK FOR EMPTY STRINGS:
    if(amount == 0 || accountId == 0){
        redirectAttributes.addFlashAttribute("Deposit Amount or Account Depositing to Can't Be 0!");
        return "redirect:/";
    }
    // GET LOGGED IN USER:
    user = (User)session.getAttribute("user");
   
    // CHECK IF DEPOSIT AMOUNT IS 0:
    if(amount == 0){
        redirectAttributes.addFlashAttribute("Deposit Amount Can't Be 0!");
        return "redirect:/";
    }

    // UPDATE BALANCE:
    currentBalance = account.getBalance();

    newBalance = currentBalance + amount;   
        
    // Log Successful Transaction
    transactionRepository.save(null);
    redirectAttributes.addFlashAttribute("success", "Amount Deposited Successfully!");
    return "redirect:/";
}
// End Of Deposits.
>>>>>>> develop
}
