package com.viettel.bccs3.service.feign;

import com.viettel.bccs3.domain.dto.AreaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${ws.address.catalog.name}", url = "${ws.address.catalog.url}")
public interface CatalogService {
    @GetMapping(value = "/catalog/area-detail")
    AreaDTO getArea(@RequestParam("areaCode") String areaCode,
                    @RequestHeader(defaultValue = "vi-LA", value = "Accept-Language") String code);
}
