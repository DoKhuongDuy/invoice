package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceInfoRepository extends JpaRepository<InvoiceInfo, Long> {
    @Query(value = "select nextval(invoice_info_seq) from dual", nativeQuery = true)
    Long getNextInvoiceInfoId();
}
