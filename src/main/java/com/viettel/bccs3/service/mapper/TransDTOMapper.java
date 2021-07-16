package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.TransDTO;
import com.viettel.bccs3.domain.dto.TransDTOs;

public class TransDTOMapper {

    private TransDTOMapper() {
    }

    public static TransDTO mapTransDTOS2TransDTO(TransDTOs transDTOs) {
        return TransDTO.builder()
                .transId(transDTOs.getTransId())
                .transType(transDTOs.getTransType())
                .transTypeDetail(transDTOs.getTransTypeDetail())
                .build();
    }
}
