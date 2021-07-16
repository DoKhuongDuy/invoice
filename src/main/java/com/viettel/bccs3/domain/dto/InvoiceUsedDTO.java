package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceUsedDTO implements Serializable {
    private Long invoiceUsedId;
    private Long invoiceListId;
    private LocalDateTime invoiceDatetime;
    private Long shopId;
    private Long staffId;
    private Long receiverId;
    private Long receiverType;
    private String serialNo;
    private Long invoiceId;
    private String invoiceNo;
    private Long invoiceType;
    private Long fromInvoiceUsedId;
    private Long subId;
    private Long custId;
    private String custIsdn;
    private String custName;
    private String custAddress;
    private String custCompany;
    private String custIdentityNo;
    private String custIdentityType;
    private String custIdentityTypeName;
    private String payMedthod;
    private String note;
    private Long status;
    private LocalDateTime createDate;
    private Long telecomServiceId;
    private String currency;
    private Double amountNotTax;
    private Double tax;
    private Double discount;
    private Double vat;
    private Double amountTax;
    private Long eInvoiceStatus;
}
