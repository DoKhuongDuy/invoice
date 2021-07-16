package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BRANCH_INVOICE_LIST")
public class BranchInvoiceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "BRANCH_ID")
    private Long branchId;
    @Column(name = "BRANCH_INVOICE_ID")
    private Long branchInvoiceId;
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "CREATE_USER_ID")
    private Long createUserId;
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;
    @Column(name = "LAST_UPDATE_USER_ID")
    private Long lastUpdateUserId;
    @Column(name = "LEVEL_TYPE")
    private Long levelType;
    @Column(name = "INVOICE_TYPE")
    private Long invoiceType;
}
