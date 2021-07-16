package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INVOICE_INFO_ITEMS")
public class InvoiceInfoItems {
    @Id
    @Column(name = "INVOICE_INFO_ITEMS_ID")
    private Long invoiceInfoItemsId;
    @Column(name = "INVOICE_INFO_ID")
    private Long invoiceInfoId;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "PRICE_NOT_TAX")
    private Long priceNotTax;
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
    @Column(name = "TAX_AMOUNT")
    private Double taxAmount;
    @Column(name = "TAX_PERCENTAGE")
    private String taxPercentage;
}
