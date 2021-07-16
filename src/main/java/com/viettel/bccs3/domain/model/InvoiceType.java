package com.viettel.bccs3.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "invoice_type")
public class InvoiceType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICE_TYPE_ID")
    private Long invoiceTypeId;
    @Column(name = "INVOICE_TYPE")
    private String invoiceType;
    @Column(name = "INVOICE_FORM")
    private String invoiceForm;
    @Column(name = "INVOICE_NAME")
    private String invoiceName;
    @Column(name = "BLOCK_NO_LENGTH")
    private Long blockNoLength;
    @Column(name = "INVOICE_NO_LENGTH")
    private Long invoiceNoLength;
    @Column(name = "NUM_INVOICE")
    private Long numInvoice;
    @Column(name = "BOOK_TYPE")
    private String bookType;
    @Column(name = "FORM_NO")
    private String formNo;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    private LocalDateTime createDateTime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    private LocalDateTime updateDateTime;
    @Column(name = "DESCRIPTION")
    private String description;

}
