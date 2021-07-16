package com.viettel.bccs3.domain.dto.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IsdnNumMonthVat {
    private Double isdn;
    private Double vat;
    private Long numMonth;
}
