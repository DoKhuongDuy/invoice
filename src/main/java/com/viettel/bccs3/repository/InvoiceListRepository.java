package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceListRepository extends JpaRepository<InvoiceList, Long> {
    InvoiceList findFirstByCurrInvoiceAndInvoiceListId(Long currInvoice, Long invoiceListId);
}
