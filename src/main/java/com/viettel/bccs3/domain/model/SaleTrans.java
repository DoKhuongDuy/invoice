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
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "SALE_TRANS")
public class SaleTrans implements Serializable {
    @Id
    @SequenceGenerator(name = "SALE_TRANS_GENERATOR", sequenceName = "SALE_TRANS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_TRANS_GENERATOR")
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;
    @Column(name = "SALE_TRANS_DATE")
    private LocalDateTime saleTransDate;
    @Column(name = "SALE_TRANS_TYPE")
    private Long saleTransType;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "INVOICE_USED_ID")
    private Long invoiceUsedId;
    @Column(name = "INVOICE_CREATE_DATE")
    private LocalDateTime invoiceCreateDate;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "PAY_METHOD")
    private String payMethod;
    @Column(name = "SALE_SERVICE_ID")
    private Long saleServiceId;
    @Column(name = "SALE_SERVICE_PRICE_ID")
    private Long saleServicePriceId;
    @Column(name = "AMOUNT_SERVICE")
    private Double amountService;
    @Column(name = "AMOUNT_MODEL")
    private Double amountModel;
    @Column(name = "DISCOUNT")
    private Double discount;
    @Column(name = "PROMOTION")
    private Double promotion;
    @Column(name = "AMOUNT_TAX")
    private Double amountTax;
    @Column(name = "AMOUNT_NOT_TAX")
    private Double amountNotTax;
    @Column(name = "VAT")
    private Double vat;
    @Column(name = "TAX")
    private Double tax;
    @Column(name = "SUB_ID")
    private Long subId;
    @Column(name = "ISDN")
    private Double isdn;
    @Column(name = "CUST_NAME")
    private String custName;
    @Column(name = "CUST_ID")
    private Long custId;
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TIN")
    private String tin;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "ADJUST_STATUS")
    private Long adjustStatus;
    @Column(name = "SALE_TRANS_CODE")
    private String saleTransCode;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "CREATE_SHOP_ID")
    private Long createShopId;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Column(name = "RECEIVER_ID")
    private Long receiverId;
    @Column(name = "RECEIVER_TYPE")
    private Long receiverType;
    @Column(name = "SALE_PROGRAM")
    private String saleProgram;
    @Column(name = "REVENUE_PAY_STATUS")
    private Long revenuePayStatus;
    @Column(name = "CHECK_CREATE_INVOICE")
    private Long checkCreateInvoice;
    @Column(name = "FROM_SALE_TRANS_ID")
    private Long fromSaleTransId;
    @Column(name = "IS_ADJUST")
    private Long isAdjust;
    @Column(name = "SHOP_PATH")
    private String shopPath;
    @Column(name = "SHOP_NAME")
    private String shopName;
    @Column(name = "STAFF_NAME")
    private String staffName;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "PRIMARY_ACCOUNT")
    private String primaryAccount;
    @Column(name = "REVENUE_STATUS")
    private Character revenueStatus;
    @Column(name = "REVENUE_TYPE")
    private String revenueType;
    @Column(name = "REQUEST_ID")
    private Long requestId;
    @Column(name = "APPROVE_STATUS")
    private String approveStatus;
    @Column(name = "REQUEST_TYPE")
    private String requestType;
    @Column(name = "PAYMENT_DEBIT_STATUS")
    private Long paymentDebitStatus;
    @Column(name = "LAST_UPDATE_DATE")
    private LocalDateTime lastUpdateDate;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TENANT_ID")
    private Long tenantId;
    @Column(name = "VI_MONEY_ISDN")
    private String viMoneyIsdn;
    @Column(name = "VI_STAFF_ISDN")
    private String viStaffIsdn;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "REAL_STEP")
    private String realStep;
    @Column(name = "REVENUE_OBJ")
    private String revenueObject;
    @Column(name = "DESTROY_DATE")
    private LocalDateTime destroyDate;
    @Column(name = "DESTROY_USER")
    private String destroyUser;
    @Column(name = "description")
    private String description;
    @Column(name = "APPROVE_STAFF_ID")
    private Long approveStaffId;
    @Column(name = "APPROVE_DATE")
    private LocalDateTime approveDate;
    @Column(name = "APPROVE_TYPE")
    private String approveType;
    @Column(name = "ISDN_EPAY")
    private String isdnEPay;
    @Column(name = "EPAY_ID")
    private String ePayId;
    @Column(name = "BANK_TRANSFER_ID")
    private Long bankTransferId;
    @Column(name = "FROM_SALE_TRANS_CODE")
    private String fromSaleTransCode;
    @Column(name = "NETWORK_ACCOUNT")
    private String networkAccount;
    @Column(name = "bank_receipt_code")
    private String bankReceiptCode;
    @Column(name = "bank_receipt_id")
    private Long bankReceiptId;
    @Column(name = "sale_pay_info_id")
    private Long salePayInfoId;
    @Column(name = "invoice_no")
    private String invoiceNo;
    @Column(name = "invoice_type")
    private Long invoiceType;
    @Column(name = "invoice_request_id")
    private Long invoiceRequestId;
}
