package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateInvoiceDTO {
    private List<TransDTO> lstTransDTO;
    private Long invoiceRequestId;
    private Long shopId;
}
