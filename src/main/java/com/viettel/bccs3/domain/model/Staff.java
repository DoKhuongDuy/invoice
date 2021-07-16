package com.viettel.bccs3.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
@Table(name = "STAFF")
public class Staff implements Serializable {

    @Id
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "BIRTHDAY")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;
    @Column(name = "ID_ISSUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date idIssueDate;
    @Column(name = "TEL")
    private String tel;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "STAFF_OWN_TYPE")
    private String staffOwnType;
    @Column(name = "STAFF_OWNER_ID")
    private Long staffOwnerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Column(name = "PRICE_POLICY")
    private String pricePolicy;
    @Column(name = "DISCOUNT_POLICY")
    private String discountPolicy;
    @Column(name = "POINT_OF_SALE")
    private String pointOfSale;
    @Column(name = "LOCK_STATUS")
    private Long lockStatus;
    @Column(name = "LAST_LOCK_TIME")
    @Temporal(TemporalType.DATE)
    private Date lastLockTime;
    @Column(name = "CONTRACT_METHOD")
    private Long contractMethod;
    @Column(name = "HAS_EQUIPMENT")
    private Long hasEquipment;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "BUSINESS_METHOD")
    private Long businessMethod;
    @Column(name = "HAS_TIN")
    private Long hasTin;
    @Column(name = "TIN")
    private String tin;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "PRECINCT")
    private String precinct;
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Column(name = "STREET_BLOCK")
    private String streetBlock;
    @Column(name = "STREET")
    private String street;
    @Column(name = "HOME")
    private String home;
    @Column(name = "POINT_OF_SALE_TYPE")
    private Long pointOfSaleType;
    @Column(name = "STOCK_NUM_IMP")
    private Long stockNumImp;
    @Column(name = "STOCK_NUM")
    private Long stockNum;
    @Column(name = "TTNS_CODE")
    private String ttnsCode;
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "CONTRACT_FROM_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractFromDate;
    @Column(name = "CONTRACT_TO_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractToDate;
    @Column(name = "DEPOSIT_VALUE")
    private Long depositValue;
    @Column(name = "BANKPLUS_MOBILE")
    private String bankplusMobile;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "BUSINESS_LICENCE")
    private String businessLicence;
    @Column(name = "SUB_OWNER_TYPE")
    private Long subOwnerType;
    @Column(name = "SUB_OWNER_ID")
    private Long subOwnerId;
    @Column(name = "LAST_MODIFIED")
    @Temporal(TemporalType.DATE)
    private Date lastModified;
    @Column(name = "SHOP_OWNER_ID")
    private Long shopOwnerId;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "TENANT_ID")
    private Long tenantId;

}
