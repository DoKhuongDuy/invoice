package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaleTransDTO implements Serializable {
    private Long transId;
    private Long transType;
    private Long transTypeDetail;
    private LocalDateTime transDate;
    private Long receiverId;
    private String custName;
    private Long custId;
    private String tin;
    private String currency;
    private Long invoiceRequestId;
    private Long receiverType;
}
