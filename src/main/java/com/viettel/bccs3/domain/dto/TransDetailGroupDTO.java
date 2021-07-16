package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransDetailGroupDTO {
    private String prodOfferName;
    private Long prodOfferId;
    private Double price;
    private Double priceNotTax;
    private Double vat;
}
