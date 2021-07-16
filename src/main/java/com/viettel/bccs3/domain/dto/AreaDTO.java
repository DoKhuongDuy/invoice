package com.viettel.bccs3.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaDTO implements Serializable {

    public static enum FILTER {area_code, name, status}

    public static enum COLUMNS {areaCode, areaGroup, center, createDatetime, createUser, district, fullName, name, parentCode, precinct, province, provinceCode, pstnCode, regionId, status, streetBlock, updateDatetime, updateUser, vtMapCode, EXCLUSE_ID_LIST}

    @JsonProperty(value = "area_code")
    private String areaCode;
    private String areaGroup;
    private String center;
    private Date createDatetime;
    private String createUser;
    private String district;
    private String districtName;

    private String fullName;
    @JsonProperty(value = "name")
    private String name;
    private String parentCode;
    private String precinct;
    private String precinctName;

    private String province;
    private String provinceCode;
    private String pstnCode;
    private Long regionId;
    @JsonProperty(value = "status")
    private String status;
    private String streetBlock;
    private String streetBlockName;

    private Date updateDatetime;
    private String updateUser;
    private String vtMapCode;

    private String provinceName;

    private boolean checked = false;
    private String areaGroupCode;

    private String actionMapping = null;
    private Long square;
    private Long population;
    private Long households;
    private String areaType;

    private String vnCode;

    private int type;
    private String vnName;
    private String vnCodeLv;

    private String areaAddress;

}
