package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceInfoItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceInfoItemsRepository extends JpaRepository<InvoiceInfoItems, Long> {
    @Query(value = "select nextval(invoice_info_items_seq) from dual", nativeQuery = true)
    Long getNextInvoiceInfoItems();
}
