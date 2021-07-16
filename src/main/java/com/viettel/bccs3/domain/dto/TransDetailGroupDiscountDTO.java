package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransDetailGroupDiscountDTO {
    private String prodOfferName;
    private Long prodOfferId;
    private Double discountAmount;
    private Double vat;
}
