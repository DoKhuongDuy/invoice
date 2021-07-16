package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.InvoiceSerial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceSerialRepository extends JpaRepository<InvoiceSerial, Long> {
}
