package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.SaleTransGeneralDTO;
import com.viettel.bccs3.domain.model.SaleTransGeneral;

public class SaleTransGeneralDTOMapper {

    private SaleTransGeneralDTOMapper() {
    }

    public static SaleTransGeneralDTO mapSaleTransGeneral2DTO(SaleTransGeneral saleTransGeneral) {
        return SaleTransGeneralDTO.builder()
                .transId(saleTransGeneral.getSaleTransGeneralId())
                .transType(2L)
                .transTypeDetail(saleTransGeneral.getType())
                .transDate(saleTransGeneral.getCreateDate())
                .invoiceRequestId(saleTransGeneral.getInvoiceRequestId())
                .custName(saleTransGeneral.getCustName())
                .receiverId(null)
                .tin(null)
                .custId(null)
//                .currency()
                .build();
    }
}
