package com.example.vaultX.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vaultX.entity.Transactions;

public interface TransactionalRepository extends JpaRepository<Transactions,Long> {
    
}
