package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceInfoDTO implements Serializable {
    private Long shopId;
    private String shopCode;
    private Long staffId;
    private String staffCode;
    private Long invoiceType;
    private String serialNo;
    private Long invoiceSerialId;
    private Long shopUsedId;
    private Long payMethod;
}
