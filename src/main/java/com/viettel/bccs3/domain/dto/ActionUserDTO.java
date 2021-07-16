package com.viettel.bccs3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionUserDTO implements Serializable {
    Long staffId;//d user đăng nhập
    String staffCode;// mã user đăng nhập
    String staffName;// tên user đăng nhập
    Long channelTypeId;// kênh nhân viên thực hiện
    String areaCode;// mã địa bàn của user đăng nhập
    Long shopId;// id shop user đăng nhập.
    String shopCode;// mã shop của user đăng nhập
    Long shopChanelTypeId;// mã kênh cửa hàng user đăng nhập
    String shopProvince;// mã tỉnh của shop user đăng nhập
    String shopPath;// đường dẫn shop user đăng nhập
    String systemType;// mã hệ thống user đăng nhập.
    String ipAddress;// địa chỉ IP user đăng nhập
    Long pointOfSale;// Đánh giấu là Điểm bán hay nhân viên Địa bàn.
}
