package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceInfoDiscountDTO {
    private Double discountPrice;
    private Double taxDiscount;
    private Double discountNotTax;
    private Long invoiceInfoDiscountId;
    private Long invoiceInfoId;
    private String unitName;
    private String itemName;
    private Long quantity;
    private Long taxAmount;
    private Long itemId;
    private Long itemTotalAmountNotTax;
    private String taxPercentage;
    private Long unitPrice;
    private Double priceNotTax;
    private Double amountNotTax;
    private Double vat;
}
