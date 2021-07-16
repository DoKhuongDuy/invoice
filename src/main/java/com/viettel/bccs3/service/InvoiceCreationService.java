package com.viettel.bccs3.service;

import com.viettel.bccs3.domain.dto.CreateInvoiceDTO;

public interface InvoiceCreationService {
    void createInvoice(CreateInvoiceDTO message);
}
