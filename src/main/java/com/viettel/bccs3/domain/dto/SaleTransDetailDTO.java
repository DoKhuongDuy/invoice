package com.viettel.bccs3.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleTransDetailDTO implements Serializable {
    private Long saleTransDetailId;
    private Long saleTransId;
    private LocalDateTime saleTransDate;
    private Long prodOfferId;
    private Long stateId;
    private Long priceId;
    private Double price;
    private Double vat;
    private Long quantity;
    private Double amountTax;
    private Double amountNotTax;
    private Double discountAmountTax;
    private Double discountAmount;
    private Long discountId;
    private Double vatAmount;
    private Long prodOfferTypeId;
    private String prodOfferTypeName;
    private String prodOfferCode;
    private String prodOfferName;
    private String accountingModelCode;
    private String accountingModelName;
    private Long saleServicesId;
    private Long saleServicesPriceId;
    private String saleServicesCode;
    private String saleServicesName;
    private Double saleServicesPrice;
    private Double saleServicesPriceVat;
    private Long prodPackId;
    private Long depositPriceId;
    private Double depositPrice;
    private Long discountFeeId;
    private Double feeAmount;
    private Long revenueVtt;
    private String revenueType;
    private String transferGood;
    private String note;
    private String account;
    private Long posId;
    private String currency;

    private Long saleTransSerialId;
    private String fromSerial;
    private String toSerial;
    private Long quantitySerial;
}
