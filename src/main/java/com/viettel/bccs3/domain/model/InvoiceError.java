package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "INVOICE_ERROR")
public class InvoiceError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "PK_ID")
    private Long pkId;
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @Column(name = "LAST_MODIFY")
    private LocalDateTime lastModify;
    @Column(name = "SHOP_USED_ID")
    private Long shopUsedId;
    @Column(name = "ERROR_CODE")
    private String errorCode;
    @Column(name = "INVOICE_REQUEST_ID")
    private Long invoiceRequestId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PK_TYPE")
    private Long pkType;
    @Column(name = "DATA_JSON")
    private String dataJson;
    @Column(name = "RETRY")
    private Long retry;
}
