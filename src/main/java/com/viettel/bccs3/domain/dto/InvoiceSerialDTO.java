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
public class InvoiceSerialDTO implements Serializable {
    private Long invoiceSerialId;
    private String serialNo;
    private Long shopUsedId;
    private String shopUsedName;
    private String invoiceTypeName;
    private Long invoiceTrans;
    private Long invoiceTypeId;
    private Long invoiceType;
    private String formNo;
    private String description;
    private Long status;
    private Long shopId;
    private String createUser;
    private LocalDateTime createDatetime;
    private String updateUser;
    private LocalDateTime updateDatetime;
    private Long listShopUsedId;
    private String errMessage;
    private Long isSuccess;
    private Boolean isValidate;
}
