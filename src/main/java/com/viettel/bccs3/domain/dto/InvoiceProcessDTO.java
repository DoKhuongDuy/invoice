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
public class InvoiceProcessDTO {
    private Long id;
    private Long branchId;
    private Date invoiceDate;
    private Date createDate;
    private Date lastModify;
    private Long status;
}
