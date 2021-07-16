package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.SaleTransDTO;
import com.viettel.bccs3.domain.dto.SaleTransGeneralDTO;
import com.viettel.bccs3.domain.dto.TransDTOs;

public class TransDTOsMapper {

    private TransDTOsMapper() {
    }

    public static TransDTOs mapFromSaleTransDTO(SaleTransDTO saleTransDTO) {
        return TransDTOs.builder()
                .transId(saleTransDTO.getTransId())
                .transType(saleTransDTO.getTransType())
                .transTypeDetail(saleTransDTO.getTransTypeDetail())
                .invoiceRequestId(saleTransDTO.getInvoiceRequestId())
                .build();
    }

    public static TransDTOs mapFromSaleTransDTOGeneral(SaleTransGeneralDTO saleTransGeneralDTO) {
        return TransDTOs.builder()
                .transId(saleTransGeneralDTO.getTransId())
                .transType(saleTransGeneralDTO.getTransType())
                .transTypeDetail(saleTransGeneralDTO.getTransTypeDetail())
                .invoiceRequestId(saleTransGeneralDTO.getInvoiceRequestId())
                .build();
    }
}
