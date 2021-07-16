package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "INVOICE_USED")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InvoiceUsed implements Serializable {
    @Id
    @Column(name = "invoice_used_id")
    private Long invoiceUsedId;
    @Column(name = "invoice_list_id")
    private Long invoiceListId;
    @Column(name = "invoice_datetime")
    private LocalDateTime invoiceDatetime;
    @Column(name = "shop_id")
    private Long shopId;
    @Column(name = "staff_id")
    private Long staffId;
    @Column(name = "receiver_id")
    private Long receiverId;
    @Column(name = "receiver_type")
    private Long receiverType;
    @Column(name = "serial_no")
    private String serialNo;
    @Column(name = "invoice_id")
    private Long invoiceId;
    @Column(name = "invoice_no")
    private String invoiceNo;
    @Column(name = "invoice_length")
    private Long invoiceLength;
    @Column(name = "invoice_type")
    private Long invoiceType;
    @Column(name = "from_invoice_used_id")
    private Long fromInvoiceUsedId;
    @Column(name = "sub_id")
    private Long subId;
    @Column(name = "shop_name")
    private String shopName;
    @Column(name = "shop_address")
    private String shopAddress;
    @Column(name = "shop_tin")
    private String shopTin;
    @Column(name = "cust_id")
    private Long custId;
    @Column(name = "cust_isdn")
    private String custIsdn;
    @Column(name = "cust_name")
    private String custName;
    @Column(name = "cust_address")
    private String custAddress;
    @Column(name = "cust_company")
    private String custCompany;
    @Column(name = "cust_identity_no")
    private String custIdentityNo;
    @Column(name = "pay_method")
    private String payMedthod;
    @Column(name = "note")
    private String note;
    @Column(name = "status")
    private Long status;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "telecom_service_id")
    private Long telecomServiceId;
    @Column(name = "currency")
    private String currency;
    @Column(name = "amount_not_tax")
    private Double amountNotTax;
    @Column(name = "tax")
    private Double tax;
    @Column(name = "discount")
    private Double discount;
    @Column(name = "vat")
    private Double vat;
    @Column(name = "amount_tax")
    private Double amountTax;
    @Column(name = "e_invoice_status")
    private Long eInvoiceStatus;
}
