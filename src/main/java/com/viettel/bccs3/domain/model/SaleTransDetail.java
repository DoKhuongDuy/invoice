/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs3.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SALE_TRANS_DETAIL")
public class SaleTransDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SALE_TRANS_DETAIL_GENERATOR", sequenceName = "SALE_TRANS_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_TRANS_DETAIL_GENERATOR")
    @Column(name = "SALE_TRANS_DETAIL_ID")
    private Long saleTransDetailId;
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;
    @Column(name = "SALE_TRANS_DATE")
    private LocalDateTime saleTransDate;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "PRICE_ID")
    private Long priceId;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "VAT")
    private Double vat;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "AMOUNT_TAX")
    private Double amountTax;
    @Column(name = "AMOUNT_NOT_TAX")
    private Double amountNotTax;
    @Column(name = "DISCOUNT_AMOUNT_TAX")
    private Double discountAmountTax;
    @Column(name = "DISCOUNT_AMOUNT")
    private Double discountAmount;
    @Column(name = "DISCOUNT_ID")
    private Long discountId;
    @Column(name = "VAT_AMOUNT")
    private Double vatAmount;
    @Column(name = "PROD_OFFER_TYPE_ID")
    private Long prodOfferTypeId;
    @Column(name = "PROD_OFFER_TYPE_NAME")
    private String prodOfferTypeName;
    @Column(name = "PROD_OFFER_CODE")
    private String prodOfferCode;
    @Column(name = "PROD_OFFER_NAME")
    private String prodOfferName;
    @Column(name = "ACCOUNTING_MODEL_CODE")
    private String accountingModelCode;
    @Column(name = "ACCOUNTING_MODEL_NAME")
    private String accountingModelName;
    @Column(name = "SALE_SERVICES_ID")
    private Long saleServicesId;
    @Column(name = "SALE_SERVICES_PRICE_ID")
    private Long saleServicesPriceId;
    @Column(name = "SALE_SERVICES_CODE")
    private String saleServicesCode;
    @Column(name = "SALE_SERVICES_NAME")
    private String saleServicesName;
    @Column(name = "SALE_SERVICES_PRICE")
    private Double saleServicesPrice;
    @Column(name = "SALE_SERVICES_PRICE_VAT")
    private Double saleServicesPriceVat;
    @Column(name = "PROD_PACK_ID")
    private Long prodPackId;
    @Column(name = "DEPOSIT_PRICE_ID")
    private Long depositPriceId;
    @Column(name = "DEPOSIT_PRICE")
    private Double depositPrice;
    @Column(name = "DISCOUNT_FEE_ID")
    private Long discountFeeId;
    @Column(name = "FEE_AMOUNT")
    private Double feeAmount;
    @Column(name = "REVENUE_VTT")
    private Long revenueVtt;
    @Column(name = "REVENUE_TYPE")
    private String revenueType;
    @Column(name = "TRANSFER_GOOD")
    private String transferGood;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "ACCOUNT")
    private String account;
    @Column(name = "POS_ID")
    private Long posId;
    @Column(name = "TENANT_ID")
    private Long tenantId;
    @Column(name = "CURRENCY")
    private String currency;


}
