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
public class InvoiceTypeDTO implements Serializable {
    private Long invoiceTypeId;
    private String formNo;
    private String invoiceType;
    private String invoiceName;
    private String invoiceTypeName;
    private String invoiceForm;
    private String invoiceFormName;
    private Long invoiceNoLength;
    private String createUser;
    private LocalDateTime createDateTime;
    private String description;
    private String staffName;
    private Long isSuccess;
    private String errMessage;
    private Boolean isValidate;
}
