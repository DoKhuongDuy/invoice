package com.viettel.bccs3.domain.dto.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapTransInvoiceDetail {
    private String prodOfferName;
    private Long prodOfferId;
    private Double price;
    private Double priceNotTax;
    private Double vat;
}
