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
@Table(name = "invoice_process")
public class InvoiceProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "shop_used_id")
    private Long shopUsedId;
    @Column(name = "shop_id")
    private Long shopId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_modify")
    private LocalDateTime lastModify;
    @Column(name = "status")
    private Long status;
}
