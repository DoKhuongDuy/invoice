package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.InvoiceListDTO;
import com.viettel.bccs3.utils.DataMapper;

public class InvoiceListDTOMapper {

    public static InvoiceListDTO mapFromObject(Object[] object) {
        return InvoiceListDTO.builder()
                .invoiceListId(DataMapper.longValue(object[0]))
                .invoiceTypeId(DataMapper.longValue(object[1]))
                .invoiceForm(DataMapper.longValue(object[2]))
                .invoiceSerialId(DataMapper.longValue(object[3]))
                .serialNo(DataMapper.stringValue(object[4]))
                .formNo(DataMapper.stringValue(object[5]))
                .fromInvoice(DataMapper.longValue(object[6]))
                .toInvoice(DataMapper.longValue(object[7]))
                .currInvoice(DataMapper.longValue(object[8]))
                .staffId(DataMapper.longValue(object[10]))
                .status(DataMapper.longValue(object[11]))
                .createUser(DataMapper.stringValue(object[12]))
                .createDatetime(DataMapper.localDateValue(object[13]))
                .invoiceNoLength(DataMapper.longValue(object[14]))
                .invoiceTrans(DataMapper.longValue(object[15]))
                .build();
    }
}
