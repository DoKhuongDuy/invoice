package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceTaxAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceTaxAmountRepository extends JpaRepository<InvoiceTaxAmount, Long> {
    @Query(value = "select nextval(invoice_tax_amount_seq) from dual", nativeQuery = true)
    Long getNextInvoiceTaxAmountId();
}
