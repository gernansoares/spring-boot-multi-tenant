package com.multitenant.example.tenant.config.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Should be implemented by services to auto-rollback transactions when an exception occur
 */
@Transactional(value = "tenantTransactionManager", rollbackFor = Exception.class)
public interface AutoRollbackService {
}
