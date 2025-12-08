package com.example.vaultX.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vaultX.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {
    
}
