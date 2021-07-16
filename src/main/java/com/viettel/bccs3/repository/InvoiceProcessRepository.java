package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceProcessRepository extends JpaRepository<InvoiceProcess, Long> {
    List<InvoiceProcess> findAllByCreateDateIsAfter(LocalDateTime currentDate);
}
