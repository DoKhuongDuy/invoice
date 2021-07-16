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
public class InvoiceErrorDTO {
    private Long id;
    private Long pkId;
    private Date issueDate;
    private Long branchId;
    private String description;
    private String errorMessage;
    private Long pkType;
}
