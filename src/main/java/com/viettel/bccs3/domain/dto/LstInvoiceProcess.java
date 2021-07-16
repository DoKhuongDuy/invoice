package com.viettel.bccs3.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LstInvoiceProcess {
    private Long shopUsedId;
    private Long status;
}
