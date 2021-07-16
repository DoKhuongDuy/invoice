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
public class BranchInvoiceListDTO {

    private Long id;
    private Long branchId;
    private Long branchInvoiceId;
    private Date createDate;
    private Long createUserId;
    private Date lastUpdateDate;
    private Long lastUpdateUserId;
    private Long levelType;
    private Long invoiceType;
}
