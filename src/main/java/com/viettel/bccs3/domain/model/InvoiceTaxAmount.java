package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INVOICE_TAX_AMOUNT")
public class InvoiceTaxAmount {
    @Id
    @Column(name = "INVOICE_TAX_AMOUNT_ID")
    private Long invoiceTaxAmountId;
    @Column(name = "INVOICE_INFO_ID")
    private Long invoiceInfoId;
    @Column(name = "TAX_PERCENTAGE")
    private String taxPercentage;
    @Column(name = "TOTAL_AMOUNT_NOT_TAX")
    private Double totalAmountNotTax;
    @Column(name = "TOTAL_DISCOUNT_NOT_TAX")
    private Double totalDiscountNotTax;
    @Column(name = "TOTAL_TAX_AMOUNT")
    private Double totalTaxAmount;
    @Column(name = "TOTAL_TAX_DISCOUNT")
    private Double totalTaxDiscount;
}
