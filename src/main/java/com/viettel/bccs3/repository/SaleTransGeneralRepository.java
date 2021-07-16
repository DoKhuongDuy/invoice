package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.SaleTransGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTransGeneralRepository extends JpaRepository<SaleTransGeneral, Long> {
    @Query("select max(stg.invoiceRequestId) from SaleTransGeneral stg where stg.checkCreateInvoice = 1L")
    Long getMaxInvoiceRequestId();

    SaleTransGeneral findFirstBySaleTransGeneralId(Long saleTransId);
}
