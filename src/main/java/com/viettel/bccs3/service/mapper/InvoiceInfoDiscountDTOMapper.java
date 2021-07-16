package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.InvoiceInfoDiscountDTO;
import com.viettel.bccs3.domain.dto.TransDetailDTO;

import java.util.List;

public class InvoiceInfoDiscountDTOMapper {

    private InvoiceInfoDiscountDTOMapper() {
    }

    public static InvoiceInfoDiscountDTO mapFromTransDetailDTODiscount(List<TransDetailDTO> transDetailDTOs) {
        double quantity = 0;
        double taxDiscount = 0;
        double discountNotTax = 0;
        double priceNotTax = 0;
        double discountPrice = 0;
        var transDetailDTOIndex0 = transDetailDTOs.get(0);

        for (TransDetailDTO transDetailDTO : transDetailDTOs) {
            quantity += (transDetailDTO.getQuantity() == null ? 0 : transDetailDTO.getQuantity());
            discountNotTax += transDetailDTO.getDiscountNotTax();
            taxDiscount += ((transDetailDTO.getDiscountAmount() == null ? 0 : transDetailDTO.getDiscountAmount()) -
                    (transDetailDTO.getDiscountNotTax() == null ? 0 : transDetailDTO.getDiscountNotTax()));
        }

        if (quantity != 0) {
            priceNotTax = (transDetailDTOIndex0.getDiscountAmount() == null ? 0 : transDetailDTOIndex0.getDiscountAmount()) /
                    (1 + (transDetailDTOIndex0.getVat() == null ? 0 : transDetailDTOIndex0.getVat()) / 100) / quantity;
            discountPrice = (transDetailDTOIndex0.getDiscountAmount() == null ? 0 : transDetailDTOIndex0.getDiscountAmount()) / quantity;
        }

        return InvoiceInfoDiscountDTO.builder()
                .discountPrice(discountPrice)
                .priceNotTax(priceNotTax)
                .quantity(Math.round(quantity))
                .unitName(transDetailDTOIndex0.getUnitName())
                .taxDiscount(taxDiscount)
                .discountNotTax(discountNotTax)
                .itemId(transDetailDTOIndex0.getProdOfferId())
                .itemName(transDetailDTOIndex0.getProdOfferName())
                .taxPercentage(transDetailDTOIndex0.getVat() == null ? null : transDetailDTOIndex0.getVat().toString())
                .build();
    }
}
