package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.ShopInvoiceListDTO;
import com.viettel.bccs3.domain.model.ShopInvoiceList;

public class ShopInvoiceListMapper {

    private ShopInvoiceListMapper() {
    }

    public static ShopInvoiceListDTO mapEntity2DTO(ShopInvoiceList shopInvoiceList) {
        return ShopInvoiceListDTO.builder()
                .shopId(shopInvoiceList.getShopId())
                .shopUsedId(shopInvoiceList.getShopUsedId())
                .build();
    }
}
