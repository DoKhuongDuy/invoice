package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "INVOICE_LIST")
public class InvoiceList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICE_LIST_ID")
    private Long invoiceListId;
    @Column(name = "INVOICE_SERIAL_ID")
    private Long invoiceSerialId;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "BLOCK_NO")
    private String blockNo;
    @Column(name = "FROM_INVOICE")
    private Long fromInvoice;
    @Column(name = "TO_INVOICE")
    private Long toInvoice;
    @Column(name = "CURR_INVOICE")
    private Long currInvoice;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    private LocalDateTime createDateTime;
    @Column(name = "APPROVE_USER")
    private String approveUser;
    @Column(name = "APPROVE_DATETIME")
    private LocalDateTime approveDateTime;
    @Column(name = "DESTROY_USER")
    private String destroyUser;
    @Column(name = "DESTROY_DATE")
    private LocalDateTime destroyDate;
    @Column(name = "DESTROY_REASON_ID")
    private String destroyReasonId;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    private LocalDateTime updateDateTime;
}
