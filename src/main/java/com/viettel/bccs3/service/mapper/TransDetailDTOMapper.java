package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.TransDetailDTO;
import com.viettel.bccs3.domain.model.SaleTransDetail;

public class TransDetailDTOMapper {

    private TransDetailDTOMapper() {
    }

    public static TransDetailDTO mapEntity2DTO(SaleTransDetail saleTransDetail) {
        double price = saleTransDetail.getPrice() == null ? 0 : saleTransDetail.getPrice();
        double discountAmount = saleTransDetail.getDiscountAmount() == null ? 0 : saleTransDetail.getDiscountAmount();
        double vat = saleTransDetail.getVat() == null ? 0 : saleTransDetail.getVat();
        return TransDetailDTO.builder()
                .prodOfferName(saleTransDetail.getProdOfferName())
                .prodOfferId(saleTransDetail.getProdOfferId())
                .quantity(saleTransDetail.getQuantity())
                .price(saleTransDetail.getPrice())
                .priceNotTax(vat != 0 ? (price / (1 + vat / 100)) : price)
                .amountTax(saleTransDetail.getAmountTax())
                .amountNotTax(saleTransDetail.getAmountNotTax())
                .vat(vat)
                .vatAmount(saleTransDetail.getVatAmount())
                .discountAmount(saleTransDetail.getDiscountAmount() == null ? 0 : saleTransDetail.getDiscountAmount())
                .discountNotTax(vat != 0 ? Math.round(discountAmount / (1 + vat / 100)) : discountAmount)
                .saleTransId(saleTransDetail.getSaleTransId())
                .build();

    }
}
