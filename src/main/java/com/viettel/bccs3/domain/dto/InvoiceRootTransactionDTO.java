package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceRootTransactionDTO {
    private Long invoiceNo;
    private LocalDateTime invoiceDateTime;
    private Long invoiceUsedId;
}
