package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceInfoDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceInfoDiscountRepository extends JpaRepository<InvoiceInfoDiscount, Long> {
    @Query(value = "select nextval(invoice_info_discount_seq) from dual", nativeQuery = true)
    Long getNextInvoiceInfoDiscount();
}
