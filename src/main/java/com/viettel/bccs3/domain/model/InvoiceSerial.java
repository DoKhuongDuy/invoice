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
@Table(name = "INVOICE_SERIAL")
public class InvoiceSerial implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICE_SERIAL_ID")
    private Long invoiceSerialId;
    @Column(name = "INVOICE_TYPE_ID")
    private Long invoiceTypeId;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    private LocalDateTime createDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    private LocalDateTime updateDatetime;
    @Column(name = "SHOP_USED_ID")
    private Long shopUsedId;
    @Column(name = "INVOICE_TRANS")
    private Long invoiceTrans;
}
