package com.viettel.bccs3.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "SALE_TRANS_GENERAL")
public class SaleTransGeneral {
    @Id
    @SequenceGenerator(name = "SALE_TRANS_GENERAL_GENERATOR", sequenceName = "SALE_TRANS_GENERAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_TRANS_GENERAL_GENERATOR")
    @Column(name = "SALE_TRANS_GENERAL_ID")
    private Long saleTransGeneralId;
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @Column(name = "LAST_UPDATE")
    private LocalDateTime lastUpdate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "ISDN_EPAY")
    private String iSDNEpay;
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "REQUEST_ID")
    private Long requestId;
    @Column(name = "RETRY")
    private Long retry;
    @Column(name = "RESPONSE_CODE")
    private String responseCode;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STAFFVHR_CODE")
    private String staffvhrCode;
    @Column(name = "DISTRICT_CODE")
    private String districtCode;
    @Column(name = "BRANCH_CODE")
    private String branchCode;
    @Column(name = "BANK_CODE")
    private String bankCode;
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;
    @Column(name = "CONTENT_TRANS")
    private String contentTrans;
    @Column(name = "EPAY_ID")
    private String epayId;
    @Column(name = "CONTRACT_ID")
    private Long contractId;
    @Column(name = "SUB_ID")
    private Long subId;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "LOAD_STATUS")
    private Long loadStatus;
    @Column(name = "APPROVE_TYPE")
    private Long approveType;
    @Column(name = "APPROVE_STAFF_ID")
    private Double approveStaffId;
    @Column(name = "APPOVE_DATE")
    private LocalDateTime approveDate;
    @Column(name = "PREPAID_BILLING_TYPE")
    private Long prepaidBillingType;
    @Column(name = "PREPAID_BILLING_CODE")
    private String prepaidBillingCode;
    @Column(name = "REQUEST_ID_BANK_PLUS")
    private Long requestIdBankPlus;
    @Column(name = "SALE_TRANS_GENERAL_CODE")
    private String saleTransGeneralCode;
    @Column(name = "FROM_SALE_GENERAL_CODE")
    private String fromSaleGeneralCode;
    @Column(name = "SHOP_PATH")
    private String shopPath;
    @Column(name = "DESTROY_USER")
    private String destroyUser;
    @Column(name = "DESTROY_DATE")
    private LocalDateTime destroyDate;
    @Column(name = "BALANCE")
    private Double balance;
    @Column(name = "BANK_TRANSFER_ID")
    private Long bankTransferId;
    @Column(name = "PAYMENT_ID")
    private Long paymentId;
    @Column(name = "PAY_METHOD")
    private String paymentMethod;
    @Column(name = "NUM_MONTH")
    private Long numMonth;
    @Column(name = "PRODUCT_CODE")
    private String productCode;
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "CUST_NAME")
    private String custName;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "INVOICE_USED_ID")
    private Long invoiceUsedId;
    @Column(name = "INVOICE_DATETIME")
    private LocalDateTime invoiceDateTime;
    @Column(name = "INVOICE_STATUS")
    private Long invoiceStatus;
    @Column(name = "SYSTEM")
    private String system;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "BANK_RECEIPT_CODE")
    private String bankReceiptCode;
    @Column(name = "INVOICE_TYPE")
    private Long invoiceType;
    @Column(name = "ORG_OWNER_ID")
    private Long orgOwnerId;
    @Column(name = "ORG_OWNER_TYPE")
    private Long orgOwnerType;
    @Column(name = "INVOICE_NO")
    private String invoiceNo;
    @Column(name = "BANK_RECEIPT_ID")
    private Long bankReceiptId;
    @Column(name = "SALE_PAY_INFO_ID")
    private Long salePayInfoId;
    @Column(name = "SAFE_PAY_INFO_ID")
    private Long safePayInfoId;
    @Column(name = "CUST_ID")
    private Long custId;
    @Column(name = "AMOUNT_NOT_TAX")
    private Double amountNotTax;
    @Column(name = "VAT")
    private Double vat;
    @Column(name = "VAT_AMOUNT")
    private Double vatAmount;
    @Column(name = "CHECK_CREATE_INVOICE")
    private Long checkCreateInvoice;
    @Column(name = "CUST_TIN")
    private String custTin;
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Column(name = "invoice_request_id")
    private Long invoiceRequestId;
}
