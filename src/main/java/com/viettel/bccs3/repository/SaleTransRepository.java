package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.SaleTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTransRepository extends JpaRepository<SaleTrans, Long> {
    @Query("select max(st.invoiceRequestId) from SaleTrans st where st.checkCreateInvoice = 1L")
    Long getMaxInvoiceRequestId();

    SaleTrans findFirstBySaleTransId(Long saleTransId);
}
