package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.LstInvoiceProcess;
import com.viettel.bccs3.domain.model.InvoiceProcess;

public class InvoiceProcessMapper {

    private InvoiceProcessMapper() {
    }

    public static LstInvoiceProcess mapInvoiceProcess2LstInvoiceProcess(InvoiceProcess invoiceProcess) {
        return LstInvoiceProcess.builder()
                .shopUsedId(invoiceProcess.getShopUsedId())
                .status(invoiceProcess.getStatus())
                .build();
    }
}
