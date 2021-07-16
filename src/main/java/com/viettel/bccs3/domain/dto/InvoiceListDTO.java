package com.viettel.bccs3.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceListDTO implements Serializable {
    private Long invoiceTypeId;
    private String invoiceType;
    private String invoiceName;
    private String formNo;
    private Long invoiceListId;
    private Long invoiceSerialId;
    private String serialNo;
    private String blockNo;
    private Long fromInvoice;
    private Long toInvoice;
    private Long currInvoice;
    private Long shopId;
    private String shopName;
    private Long staffId;
    private Long status;
    private String createUser;
    private LocalDateTime createDatetime;
    private String approveUser;
    private LocalDateTime approveDatetime;
    private String destroyUser;
    private LocalDateTime destroyDate;
    private String SerialNo;
    private LocalDateTime approveDate;
    private String destroyReasonId;
    private String updateUser;
    private LocalDateTime updateDatetime;
    private Boolean isSuccess;
    private String result;
    private Long invoiceNoLength;
    private Long invoiceTrans;
    private Long showInvoiceTransType;
    private String invoiceTypeName;
    private String shopCode;
    //    khong dung dinh dang validate upfile
    private String fromInvoiceString;
    private String toInvoiceString;
    private String currInvoiceString;
    private String showInvoiceTransTypeString;
    private Long invoiceForm;
}
