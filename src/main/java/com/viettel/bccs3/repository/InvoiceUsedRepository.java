package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceUsedRepository extends JpaRepository<InvoiceUsed, Long> {
    @Query(value = "select nextval(invoice_used_seq) from dual", nativeQuery = true)
    Long getNextInvoiceUsedId();
}
