package com.multitenant.example.domain.config.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Should be implemented by services to auto-rollback transactions when an exception occur
 */
@Transactional(value = "tenantTransactionManager", rollbackFor = Exception.class)
public interface DomainService {
}
