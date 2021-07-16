package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.InvoiceInfoItemsDTO;
import com.viettel.bccs3.domain.dto.TransDetailDTO;
import com.viettel.bccs3.domain.dto.TransInvoiceDTO;

import java.util.List;

public class InvoiceInfoItemsDTOMapper {

    private InvoiceInfoItemsDTOMapper() {
    }

    public static InvoiceInfoItemsDTO mapFromTransDetailDTO(List<TransDetailDTO> transDetailDTOs) {
        double quantity = 0;
        double taxAmount = 0;
        for (TransDetailDTO transDetailDTO : transDetailDTOs) {
            quantity = quantity + (transDetailDTO.getQuantity() == null ? 0 : transDetailDTO.getQuantity());
            taxAmount = taxAmount + (transDetailDTO.getVatAmount() == null ? 0 : transDetailDTO.getVatAmount());

        }
        var transDetailDTOIndex0 = transDetailDTOs.get(0);
        return InvoiceInfoItemsDTO.builder()
                .price(transDetailDTOIndex0.getPrice() == null ? null : transDetailDTOIndex0.getPrice().longValue())
                .priceNotTax(transDetailDTOIndex0.getPriceNotTax() == null ? null : transDetailDTOIndex0.getPriceNotTax().longValue())
                .quantity(Math.round(quantity))
                .unitName(transDetailDTOIndex0.getUnitName())
                .taxAmount(taxAmount)
                .itemId(transDetailDTOIndex0.getProdOfferId())
                .itemName(transDetailDTOIndex0.getProdOfferName())
                .taxPercentage(transDetailDTOIndex0.getVat() == null ? null : transDetailDTOIndex0.getVat().toString())
                .build();
    }

    public static InvoiceInfoItemsDTO mapFromTransInvoiceDTOsType2(List<TransInvoiceDTO> saleTransGeneralInvoiceDTOs) {
        double quantity = 0;
        for (TransInvoiceDTO transInvoiceDTO : saleTransGeneralInvoiceDTOs) {
            quantity = quantity + ((transInvoiceDTO.getAmountNotTax() == null ? 0D : transInvoiceDTO.getAmountNotTax()) / (1 + (transInvoiceDTO.getVat() == null ? 0D : transInvoiceDTO.getVat()) / 100));
        }

        return InvoiceInfoItemsDTO.builder()
                .price(1L)
                .priceNotTax(1L)
                .quantity(Math.round(quantity))
                .itemName("Pay post-paid fee to account " + saleTransGeneralInvoiceDTOs.get(0).getIsdn())
                .vat(saleTransGeneralInvoiceDTOs.get(0).getVat())
                .build();
    }


    public static InvoiceInfoItemsDTO mapFromTransInvoiceDTOsType3(List<TransInvoiceDTO> saleTransGeneralInvoiceDTOs) {
        double quantity = 0;
        for (TransInvoiceDTO transInvoiceDTO : saleTransGeneralInvoiceDTOs) {
            quantity = quantity + ((transInvoiceDTO.getAmountNotTax() == null ? 0D : transInvoiceDTO.getAmountNotTax()) / (1 + (transInvoiceDTO.getVat() == null ? 0D : transInvoiceDTO.getVat()) / 100));
        }

        return InvoiceInfoItemsDTO.builder()
                .price(1L)
                .priceNotTax(1L)
                .quantity(Math.round(quantity))
                .itemName("Prepaid " + saleTransGeneralInvoiceDTOs.get(0).getNumMonth() + " month for account " + saleTransGeneralInvoiceDTOs.get(0).getIsdn())
                .vat(saleTransGeneralInvoiceDTOs.get(0).getVat())
                .build();
    }
}
