package com.example.vaultX.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Pagable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.vaultX.Repository.TransactionalRepository;
import com.example.vaultX.Repository.WalletRepository;
import com.example.vaultX.entity.Transactions;
import com.example.vaultX.entity.Users;
import com.example.vaultX.entity.Wallet;
import com.example.vaultX.enums.TransactionalStatus;
import com.example.vaultX.enums.TransactionalType;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionalService {
    @Autowired
    private TransactionalRepository transacrepo;
    @Autowired
    private WalletRepository walletrepo;
    @Autowired
    private WalletService walletService;
    public Transactions createdepositTransaction(int wallet_id, BigDecimal amount){
        Wallet wallet = walletrepo.getWalletById(wallet_id);
        Users user = wallet.getuser();
        Transactions transaction = new Transactions(amount,TransactionalType.DEPOSIT, user);
        transaction.setToWallet(wallet);
        transaction.setDescription("Desposit amount");
        transaction.setStatus(TransactionalStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        return transacrepo.save(transaction);
    }
    public Transactions createWithdrawalTransaction(int wallet_id, BigDecimal amount){
        Wallet wallet = walletrepo.getWalletById(wallet_id);
        Users user = wallet.getuser();
        Transactions transaction = new Transactions(amount, TransactionalType.WITHDRAWAL, user);
        transaction.setFromWallet(wallet);
        transaction.setDescription("Withdrawal amount");
        transaction.setStatus(TransactionalStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        return transacrepo.save(transaction);
    }
    public Transactions createTransferFromwallet_idtoTo_wallet_id(int fromWallet_id,int toWallet_id,BigDecimal amount){
        Wallet fromwallet = walletrepo.getWalletById(fromWallet_id);
        Wallet towallet = walletrepo.getWalletById(toWallet_id);
        Users user = fromwallet.getuser();
        Transactions transaction = new Transactions (amount,TransactionalType.TRANSFER,user);
        transaction.setFromWallet(fromwallet);
        transaction.setToWallet(towallet);
        transaction.setDescription("Transfer amount");
        transaction.setStatus(TransactionalStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        return transacrepo.save(transaction);
    }
    public Page<Transactions> getallTransactionsWithPagination(int userId, int page, int size){
        Pagable pagable = PageRequest.of (page,size,Sort.by(Sort.Direction.DESC,"createdAt"));
        return transacrepo.findByuseruserIdOrderByCreatedAtDESC(userId,pagable);
    }
    public List<Transactions> getallTransactionsByTransactionalType(int userId, TransactionalType type){
        return transacrepo.findByuseruserIdandTransactionalTypeOrderByCreatedAtDESC(userId,type);
    }
    public List<Transactions> getallTransactionsByDateRange(int userId,LocalDate start, LocalDate end){
        return transacrepo.findByuseruserIDandDateRange(userId,start,end);
    }
    public List<Transactions> getwallettransactionsById(int wallet_id){
        return transacrepo.findBywallet_id(wallet_id);
    }
    public Map<String,Object> getallTransactionSummary(Long userId){
        Map<String,Object> summary = new HashMap<>();
        BigDecimal totalDeposit = transacrepo.sumByUserandTypeandStatus(userId,TransactionalType.DEPOSIT,TransactionalStatus.COMPLETED);
        BigDecimal totalWithdrawal = transacrepo.sumByUserandTypeandStatus(userId,TransactionalType.WITHDRAWAL,TransactionalStatus.COMPLETED);
        BigDecimal totalTransfer = transacrepo.sumByUserandTypeandStatus(userId,TransactionalType.TRANSFER,TransactionalStatus.COMPLETED);
        List<Transactions> recentTransactions = getUserTransactions(userId).stream().limit(5).toList();//getUserTransactions;
        summary.put("totalDeposits",totalDeposit);
        summary.put("totalWithdrawal",totalWithdrawal);
        summary.put("totalTransfer",totalTransfer);
        summary.put("recentTransactions",recentTransactions);
        summary.put("TotalTransactions",recentTransactions.size());
        return summary;
    }
    public Wallet DepositWithTransactions(Integer wallet_id, BigDecimal amount){
        Wallet wallet = walletService.deposit(wallet_id,amount);
        createdepositTransaction(wallet_id, amount);
        return wallet;
    }
    public Wallet WithdrawalWithTransactions(Integer wallet_id, BigDecimal amount){
        Wallet wallet = walletService.Withdrawal(wallet_id,amount);
        createWithdrawalTransaction(wallet_id, amount);
        return wallet;
    }


}
