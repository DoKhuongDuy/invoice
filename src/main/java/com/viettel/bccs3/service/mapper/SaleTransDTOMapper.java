package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.SaleTransDTO;
import com.viettel.bccs3.domain.model.SaleTrans;

public class SaleTransDTOMapper {

    private SaleTransDTOMapper() {
    }

    public static SaleTransDTO mapSaleTrans2DTO(SaleTrans saleTrans) {
        return SaleTransDTO.builder()
                .transId(saleTrans.getSaleTransId())
                .transType(1L)
                .transTypeDetail(saleTrans.getSaleTransType())
                .transDate(saleTrans.getSaleTransDate())
                .receiverId(saleTrans.getReceiverId())
                .custName(saleTrans.getCustName())
                .custId(saleTrans.getCustId())
                .tin(saleTrans.getTin())
                .currency(saleTrans.getCurrency())
                .invoiceRequestId(saleTrans.getInvoiceRequestId())
                .build();
    }

}
