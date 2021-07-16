package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceInfoItemsDTO {
    private Long invoiceInfoItemsId;
    private Long invoiceInfoId;
    private Long unitPrice;
    private Long price;
    private Long priceNotTax;
    private Double vat;
    private Double itemTotalAmountNotTax;
    private String lineNumber;
    private Long quantity;
    private String unitName;
    private Double taxAmount;
    private Long itemId;
    private String itemName;
    private String taxPercentage;
    private Double discountNotTax;
    private Double totalTaxAmount;
    private Double totalAmountNotTax;
}
