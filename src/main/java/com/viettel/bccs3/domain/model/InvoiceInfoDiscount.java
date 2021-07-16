package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Builder
@Table(name = "INVOICE_INFO_DISCOUNT")
public class InvoiceInfoDiscount {
    @Id
    @Column(name = "INVOICE_INFO_DISCOUNT_ID")
    private Long invoiceInfoDiscountId;
    @Column(name = "INVOICE_INFO_ID")
    private Long invoiceInfoId;
    @Column(name = "DISCOUNT_PRICE")
    private Double discountPrice;
    @Column(name = "PRICE_NOT_TAX")
    private Double priceNotTax;
    @Column(name = "AMOUNT_NOT_TAX")
    private Double amountNotTax;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "UNIT_NAME")
    private String unitName;
    @Column(name = "ITEM_ID")
    private Long itemId;
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "TAX_DISCOUNT")
    private Double taxDiscount;
    @Column(name = "TAX_PERCENTAGE")
    private String taxPercentage;

}
