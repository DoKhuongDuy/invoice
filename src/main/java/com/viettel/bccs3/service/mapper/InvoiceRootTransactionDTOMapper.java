package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.InvoiceRootTransactionDTO;
import com.viettel.bccs3.utils.DataMapper;

public class InvoiceRootTransactionDTOMapper {

    public static InvoiceRootTransactionDTO mapFromObject(Object[] object) {
        return InvoiceRootTransactionDTO.builder()
                .invoiceNo(DataMapper.longValue(object[0]))
                .invoiceDateTime(DataMapper.localDateValue(object[1]))
                .invoiceUsedId(DataMapper.longValue(object[2]))
                .build();
    }
}
