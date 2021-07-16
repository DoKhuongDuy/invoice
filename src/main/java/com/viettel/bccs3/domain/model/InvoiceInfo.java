package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "INVOICE_INFO")
public class InvoiceInfo {
    @Id
    @Column(name = "INVOICE_INFO_ID")
    private Long invoiceInfoId;
    @Column(name = "INVOICE_USED_ID")
    private Long invoiceUsedId;
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @Column(name = "TEMPLATE_CODE")
    private String templateCode;
    @Column(name = "INVOICE_NOTE")
    private String invoiceNote;
    @Column(name = "BUYER_NAME")
    private String buyerName;
    @Column(name = "BUYER_ADDRESS")
    private String buyerAddress;
    @Column(name = "BUYER_PROVINCE")
    private String buyerProvince;
    @Column(name = "BUYER_DISTRICT")
    private String buyerDistrict;
    @Column(name = "BUYER_VILLAGE")
    private String buyerVillage;
    @Column(name = "BUYER_EMAIL")
    private String buyerEmail;
    @Column(name = "BUYER_PHONE_NUMBER")
    private String buyerPhoneNumber;
    @Column(name = "BUYER_TAX_CODE")
    private String buyerTaxCode;
    @Column(name = "SELLER_TAX_CODE")
    private String sellerTaxCode;
    @Column(name = "SELLER_ADDRESS")
    private String sellerAddress;
    @Column(name = "SELLER_LEGAL_NAME")
    private String sellerLegalName;
    @Column(name = "INVOICE_NAME")
    private String invoiceName;
    @Column(name = "INVOICE_NO")
    private String invoiceNo;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "INVOICE_SERIES")
    private String invoiceSeries;
    @Column(name = "INVOICE_SYSTEM")
    private Long invoiceSystem;
    @Column(name = "INVOICE_TYPE")
    private String invoiceType;
    @Column(name = "TOTAL_AMOUNT_WITHOUT_TAX")
    private Double totalAmountWithoutTax;
    @Column(name = "TOTAL_AMOUNT_WITH_TAX_IN_WORD")
    private String totalAmountWithTaxInWord;
    @Column(name = "TOTAL_AMOUNT_WITH_TAX")
    private Double totalAmountWithTax;
    @Column(name = "TOTAL_DISCOUNT_NOT_TAX")
    private Double totalDiscountNotTax;
    @Column(name = "TOTAL_TAX_AMOUNT")
    private Double totalTaxAmount;
    @Column(name = "SUM_TOTAL_LINE_AMOUNT_NOT_TAX")
    private Double sumTotalLineAmountNotTax;
    @Column(name = "PROCESS_TYPE")
    private Long processType;
    @Column(name = "ADJUSTMENT_TYPE")
    private Long adjustmentType;
    @Column(name = "CONTRACT_NUMBER")
    private String contractNumber;
    @Column(name = "CURRENCY_CODE")
    private String currencyCode;
    @Column(name = "ORIGINAL_INVOICE_ID")
    private Long originalInvoiceId;
    @Column(name = "ORIGINAL_INVOICE_ISSUE_DATE")
    private Date originalInvoiceIssueDate;
}
