package com.viettel.bccs3.service.mapper;

import com.viettel.bccs3.domain.dto.TransInvoiceDTO;
import com.viettel.bccs3.domain.model.SaleTrans;
import com.viettel.bccs3.utils.DataMapper;

public class TransInvoiceDTOMapper {

    private TransInvoiceDTOMapper() {
    }

    public static TransInvoiceDTO mapFromObject(Object[] object) {
        return TransInvoiceDTO.builder()
                .transId(DataMapper.longValue(object[0]))
                .transType(DataMapper.longValue(object[1]))
                .transDate(DataMapper.localDateValue(object[2]))
                .transTypeDetail(DataMapper.longValue(object[3]))
                .status(DataMapper.longValue(object[4]))
                .invoiceUsedId(DataMapper.longValue(object[5]))
                .invoiceCreateDate(DataMapper.localDateValue(object[6]))
                .shopId(DataMapper.longValue(object[7]))
                .staffId(DataMapper.longValue(object[8]))
                .payMethod(DataMapper.stringValue(object[9]))
                .discount(DataMapper.doubleValue(object[10]))
                .amountNotTax(DataMapper.doubleValue(object[11]))
                .amountNotTax(DataMapper.doubleValue(object[12]))
                .vat(DataMapper.doubleValue(object[13]))
                .tax(DataMapper.doubleValue(object[14]))
                .isdn(DataMapper.doubleValue(object[15]))
                .custName(DataMapper.stringValue(object[16]))
                .receiverId(DataMapper.longValue(object[17]))
                .contractNo(DataMapper.stringValue(object[18]))
                .telNumber(DataMapper.stringValue(object[19]))
                .company(DataMapper.stringValue(object[20]))
                .address(DataMapper.stringValue(object[21]))
                .tin(DataMapper.stringValue(object[22]))
                .transCode(DataMapper.stringValue(object[23]))
                .custId(DataMapper.longValue(object[24]))
                .currency(DataMapper.stringValue(object[25]))
                .fromTransId(DataMapper.longValue(object[26]))
                .numMonth(DataMapper.longValue(object[27]))
                .build();
    }

    public static TransInvoiceDTO mapFromSaleTrans(SaleTrans saleTrans) {
        return TransInvoiceDTO.builder()
                .transId(saleTrans.getSaleTransId())
                .transType(1L)
                .transDate(saleTrans.getSaleTransDate())
                .transTypeDetail(saleTrans.getSaleTransType())
                .status(Long.valueOf(saleTrans.getStatus()))
                .invoiceUsedId(saleTrans.getInvoiceUsedId())
                .invoiceCreateDate(saleTrans.getInvoiceCreateDate())
                .shopId(saleTrans.getShopId())
                .staffId(saleTrans.getStaffId())
                .payMethod(saleTrans.getPayMethod())
                .discount(saleTrans.getDiscount())
                .amountTax(saleTrans.getAmountTax())
                .amountNotTax(saleTrans.getAmountNotTax())
                .vat(saleTrans.getVat())
                .tax(saleTrans.getTax())
                .isdn(saleTrans.getIsdn())
                .custName(saleTrans.getCustName())
                .receiverId(saleTrans.getReceiverId())
                .receiverType(saleTrans.getReceiverType())
                .contractNo(saleTrans.getContractNo())
                .telNumber(saleTrans.getTelNumber())
                .company(saleTrans.getCompany())
                .address(saleTrans.getAddress())
                .transCode(saleTrans.getSaleTransCode())
                .transCode(saleTrans.getSaleTransCode())
                .custId(saleTrans.getCustId())
                .currency(saleTrans.getCurrency())
                .fromTransId(saleTrans.getFromSaleTransId())
                .numMonth(null)
                .email(saleTrans.getEmail())
                .tin(saleTrans.getTin())
                .build();
    }
}
