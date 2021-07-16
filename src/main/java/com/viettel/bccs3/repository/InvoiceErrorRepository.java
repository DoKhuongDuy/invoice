package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InvoiceErrorRepository extends JpaRepository<InvoiceError, Long> {
    InvoiceError getByPkIdIsAndCreateDateAfter(Long pkId, LocalDateTime startOfDay);
}
