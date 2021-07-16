package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransInvoiceDTO {
    private Long transId;
    private Long transType;
    private LocalDateTime transDate;
    private Long transTypeDetail;
    private Long status;
    private Long invoiceUsedId;
    private LocalDateTime invoiceCreateDate;
    private Long shopId;
    private Long staffId;
    private String payMethod;
    private Double discount;
    private Double amountTax;
    private Double amountNotTax;
    private Double vat;
    private Double tax;
    private Double isdn;
    private String custName;
    private Long receiverId;
    private String contractNo;
    private String telNumber;
    private String company;
    private String address;
    private String tin;
    private String transCode;
    private Long custId;
    private String currency;
    private Long fromTransId;
    private Long numMonth;
    private Long receiverType;
    private String provinceName;
    private String districtName;
    private String precintName;
    private String email;
}
