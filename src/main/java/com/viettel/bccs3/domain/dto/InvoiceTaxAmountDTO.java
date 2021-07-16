package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceTaxAmountDTO {
    private Long invoiceInfoTaxId;
    private Long invoiceInfoId;
    private String taxPercentage;
    private Double totalTaxAmount;
    private Double totalAmountNotTax;
    private Double totalTaxDiscount;
    private Double totalDiscountNotTax;
    private Long taxAmount;
    private Long taxAbleAmount;
}
