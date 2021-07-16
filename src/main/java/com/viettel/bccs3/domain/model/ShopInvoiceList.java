package com.viettel.bccs3.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SHOP_INVOICE_LIST")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopInvoiceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "SHOP_USED_ID")
    private Long shopUsedId;
    @Column(name = "CREATE_USER_ID")
    private Long createUsedId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;
    @Column(name = "LAST_UPDATE_USER_ID")
    private Long lastUpdateUserId;
    @Column(name = "LEVEL_TYPE")
    private Long levelType;
    @Column(name = "INVOICE_TYPE")
    private Long invoiceType;
}
