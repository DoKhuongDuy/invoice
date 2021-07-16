package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransDTOs {
    private Long transId;
    private Long transType;
    private Long transTypeDetail;
    private Date transDate;
    private Long receiverId;
    private String custName;
    private Long custId;
    private String tin;
    private String currency;
    private Long invoiceRequestId;
    private Long receiverType;

}
