package com.viettel.bccs3.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
@Table(name = "SHOP")
public class Shop implements Serializable {
    @Id
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PARENT_SHOP_ID")
    private Long parentShopId;
    @Column(name = "ACCOUNT")
    private String account;
    @Column(name = "BANK_NAME")
    private String bankName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TEL")
    private String tel;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "SHOP_TYPE")
    private String shopType;
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Column(name = "CONTACT_TITLE")
    private String contactTitle;
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "PAR_SHOP_CODE")
    private String parShopCode;
    @Column(name = "CENTER_CODE")
    private String centerCode;
    @Column(name = "OLD_SHOP_CODE")
    private String oldShopCode;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "TIN")
    private String tin;
    @Column(name = "SHOP")
    private String shop;
    @Column(name = "PROVINCE_CODE")
    private String provinceCode;
    @Column(name = "PAY_COMM")
    private String payComm;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Column(name = "DISCOUNT_POLICY")
    private String discountPolicy;
    @Column(name = "PRICE_POLICY")
    private String pricePolicy;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "SHOP_PATH")
    private String shopPath;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "PRECINCT")
    private String precinct;
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;
    @Column(name = "ID_ISSUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date idIssueDate;
    @Column(name = "STREET_BLOCK")
    private String streetBlock;
    @Column(name = "STREET")
    private String street;
    @Column(name = "HOME")
    private String home;
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "CONTRACT_FROM_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractFromDate;
    @Column(name = "CONTRACT_TO_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractToDate;
    @Column(name = "DEPOSIT_VALUE")
    private Long depositValue;
    @Column(name = "BUSINESS_LICENCE")
    private String businessLicence;
    @Column(name = "BANKPLUS_MOBILE")
    private String bankplusMobile;
    @Column(name = "STOCK_NUM")
    private Long stockNum;
    @Column(name = "STOCK_NUM_IMP")
    private Long stockNumImp;
    @Column(name = "TENANT_ID")
    private Long tenantId;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "sunnat_code")
    private String sunnatCode;

}
