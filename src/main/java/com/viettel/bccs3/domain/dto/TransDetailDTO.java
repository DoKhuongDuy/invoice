package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransDetailDTO {
    private String prodOfferName;
    private Long prodOfferId;
    private Long quantity;
    private Double price;
    private Double priceNotTax;
    private Double amountNotTax;
    private Double vat;
    private Double vatAmount;
    private Double amountTax;
    private Double discountAmount;
    private Double discountNotTax;
    private Long saleTransId;
    private String unitName;
}
