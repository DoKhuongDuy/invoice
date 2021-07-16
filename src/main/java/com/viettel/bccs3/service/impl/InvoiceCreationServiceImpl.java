package com.viettel.bccs3.service.impl;

import com.viettel.bccs3.common.Constant;
import com.viettel.bccs3.common.JSonMapper;
import com.viettel.bccs3.domain.dto.*;
import com.viettel.bccs3.domain.dto.map.IsdnNumMonthVat;
import com.viettel.bccs3.domain.dto.map.IsdnVat;
import com.viettel.bccs3.domain.dto.map.MapTransInvoiceDetail;
import com.viettel.bccs3.domain.dto.map.MapTransInvoiceDetailDiscount;
import com.viettel.bccs3.domain.model.*;
import com.viettel.bccs3.repository.*;
import com.viettel.bccs3.service.InvoiceCreationService;
import com.viettel.bccs3.service.feign.CatalogService;
import com.viettel.bccs3.service.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.allegro.finance.tradukisto.MoneyConverters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Log4j2
public class InvoiceCreationServiceImpl implements InvoiceCreationService {
    private final ShopInvoiceListRepository shopInvoiceListRepository;
    private final CustomRepository customRepository;
    private final InvoiceProcessRepository invoiceProcessRepository;
    private final InvoiceErrorRepository invoiceErrorRepository;
    private final ShopRepository shopRepository;
    private final StaffRepository staffRepository;
    private final InvoiceUsedRepository invoiceUsedRepository;
    private final InvoiceListRepository invoiceListRepository;
    private final SaleTransRepository saleTransRepository;
    private final SaleTransGeneralRepository saleTransGeneralRepository;
    private final InvoiceInfoRepository invoiceInfoRepository;
    private final InvoiceInfoItemsRepository invoiceInfoItemsRepository;
    private final InvoiceInfoDiscountRepository invoiceInfoDiscountRepository;
    private final InvoiceTaxAmountRepository invoiceTaxAmountRepository;
    private final CatalogService catalogService;

    @Override
    public void createInvoice(CreateInvoiceDTO createInvoiceDTO) {

        if (createInvoiceDTO == null || createInvoiceDTO.getLstTransDTO().isEmpty() ||
                (createInvoiceDTO.getInvoiceRequestId() == null && createInvoiceDTO.getShopId() == null && createInvoiceDTO.getLstTransDTO() == null)) {
            return;
        }
        String dataJson = JSonMapper.toJson(createInvoiceDTO);

        var existInvoiceError = false;
        List<LstInvoiceProcess> lstInvoiceProcess = new ArrayList<>();
        List<Long> listUsedId = new ArrayList<>();

        var sysDate = LocalDateTime.now();
        var startOfDay = LocalDate.now().atStartOfDay();
        var fromInvoiceUsedId = 0L;

        //1.1.1
        InvoiceError invoiceError = invoiceErrorRepository.getByPkIdIsAndCreateDateAfter(createInvoiceDTO.getShopId(), startOfDay);
        if (invoiceError != null) {
            if (invoiceError.getRetry() >= Constant.CONFIG_RETRY) {
                log.info("Invoice Error Retry = " + invoiceError.getRetry() + " > config : " + Constant.CONFIG_RETRY);
                return;
            } else {
                existInvoiceError = true;
            }
        }

        //1.1.2
        var shopInvoiceList = shopInvoiceListRepository.findByStatusAndShopId(1L, createInvoiceDTO.getShopId());

        if (shopInvoiceList == null || shopInvoiceList.getInvoiceType() == null || shopInvoiceList.getShopUsedId() == null) {
            Long shopId = null;
            if (shopInvoiceList != null) {
                shopId = shopInvoiceList.getShopId();
            }
            insertUpdateInvoiceError(shopId, sysDate, startOfDay, null,
                    Constant.NO_SHOP_INVOICE_LIST,
                    dataJson,
                    "Chưa có thông tin đơn vị sử dụng hóa đơn.",
                    createInvoiceDTO.getInvoiceRequestId(),
                    existInvoiceError, createInvoiceDTO.getShopId());
            return;
        }

        var shopInvoiceListDTO = ShopInvoiceListMapper.mapEntity2DTO(shopInvoiceList);

        //1.1.3
        List<InvoiceProcess> invoiceProcesses = invoiceProcessRepository.findAllByCreateDateIsAfter(startOfDay);
        if (!invoiceProcesses.isEmpty()) {
            lstInvoiceProcess = invoiceProcesses.stream().map(InvoiceProcessMapper::mapInvoiceProcess2LstInvoiceProcess).collect(Collectors.toList());
            listUsedId = lstInvoiceProcess.stream().map(LstInvoiceProcess::getShopUsedId).collect(toList());
        }

        if (!lstInvoiceProcess.contains(new LstInvoiceProcess(shopInvoiceListDTO.getShopUsedId(), 1L))) {
            List<SaleTrans> transNotInvoiced = customRepository.getTransNotInvoiced(shopInvoiceList.getShopUsedId(), startOfDay);

            if (!transNotInvoiced.isEmpty()) {
                handleAddInvoiceProcess(listUsedId, shopInvoiceListDTO, lstInvoiceProcess, sysDate,
                        existInvoiceError, startOfDay, createInvoiceDTO, Constant.NOT_CREATED_INVOICE, "Đơn vị còn hóa đơn ngày hôm trước chưa lập");
                return;
            } else {
                List<SaleTransGeneral> saleTransGenerals = customRepository.getSaleTransGeneralToCheck(shopInvoiceList.getShopUsedId());
                if (!saleTransGenerals.isEmpty()) {
                    handleAddInvoiceProcess(listUsedId, shopInvoiceListDTO, lstInvoiceProcess, sysDate,
                            existInvoiceError, startOfDay, createInvoiceDTO, Constant.NOT_CREATED_INVOICE, "Đơn vị còn hóa đơn ngày hôm trước chưa lập");
                    return;
                } else {
                    if (listUsedId.contains(shopInvoiceList.getShopUsedId())) {
                        lstInvoiceProcess.remove(new LstInvoiceProcess(shopInvoiceList.getShopUsedId(), 0L));
                    }
                    lstInvoiceProcess.add(new LstInvoiceProcess(shopInvoiceList.getShopUsedId(), 1L));
                }
            }
        }

        //1.2
        List<TransInvoiceDTO> transInvoiceDTOs;
        List<TransInvoiceDTO> saleTransGeneralInvoiceDTOs = new ArrayList<>();
        List<TransInvoiceDTO> saleTransInvoiceDTOs = new ArrayList<>();
        List<InvoiceInfoItemsDTO> invoiceInfoItemsDTOs = new ArrayList<>();
        List<InvoiceInfoDiscountDTO> invoiceInfoDiscountDTOs = new ArrayList<>();
        List<TransDetailDTO> transInvoiceDetailDTOs;

        List<TransDTO> transDTOSType1 = createInvoiceDTO.getLstTransDTO().stream()
                .filter(transDTO -> transDTO.getTransType().equals(1L))
                .collect(Collectors.toList());
        List<TransDTO> transDTOSType2 = createInvoiceDTO.getLstTransDTO().stream()
                .filter(transDTO -> transDTO.getTransType().equals(2L))
                .collect(Collectors.toList());

        //1.2.1.1
        //case transType = 1
        if (!transDTOSType1.isEmpty()) {
            //get transaction from sale trans
            saleTransInvoiceDTOs = customRepository.getTransactionFromSaleTrans(transDTOSType1);
            if (saleTransInvoiceDTOs.isEmpty()) {
                insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                        Constant.NOT_FOUND_SALE_TRANS, dataJson,
                        "Không tìm thấy thông tin giao dịch trong sale_trans",
                        createInvoiceDTO.getInvoiceRequestId(),
                        existInvoiceError, shopInvoiceList.getShopId());
                return;
            }

            var transInvoiceDTOIndex0 = saleTransInvoiceDTOs.get(0);
            // if transTypeDetail = 41
            if (transInvoiceDTOIndex0.getTransTypeDetail() == 41) {
                if (transInvoiceDTOIndex0.getFromTransId() == null) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.FROM_TRANS_ID_IS_NULL, dataJson,
                            "Id giao dịch gốc null",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                InvoiceRootTransactionDTO invoiceRootFromSaleTrans = customRepository.getInvoiceRootFromSaleTrans(transInvoiceDTOIndex0.getFromTransId());
                if (invoiceRootFromSaleTrans == null) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.NOT_FOUND_ORIGINAL_SALE_TRANS, dataJson,
                            "Không tìm thấy thông tin giao dịch gốc trong sale_trans",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                Long invoiceNo = invoiceRootFromSaleTrans.getInvoiceNo();
                LocalDateTime invoiceDateTime = invoiceRootFromSaleTrans.getInvoiceDateTime();
                fromInvoiceUsedId = invoiceRootFromSaleTrans.getInvoiceUsedId();

                invoiceInfoItemsDTOs.add(InvoiceInfoItemsDTO.builder().price(1L).priceNotTax(1L)
                        .quantity(Math.round((saleTransInvoiceDTOs.get(0).getAmountNotTax() == null ? 0D : saleTransInvoiceDTOs.get(0).getAmountNotTax()) / (1 + (saleTransInvoiceDTOs.get(0).getVat() == null ? 0D : saleTransInvoiceDTOs.get(0).getVat()) / 100)))
                        .itemName("Adjustment decrease the amount of goods and tax rate for in voice number " + invoiceNo + " date " + invoiceDateTime)
                        .vat(transInvoiceDTOIndex0.getVat())
                        .build());
            } else {
                //if transTypeDetail <> 41
                List<Long> transIds = saleTransInvoiceDTOs.stream().map(TransInvoiceDTO::getTransId).collect(toList());
                List<SaleTransDetail> saleTransDetails = customRepository.getSaleTransDetail(transIds);
                saleTransDetails.forEach(saleTransDetail -> {
                    if (saleTransDetail.getProdOfferName() == null)
                        saleTransDetail.setProdOfferName(saleTransDetail.getSaleServicesName());
                    Double extValue = customRepository.getExtValue(saleTransDetail.getSaleTransDetailId());
                    if (extValue == null) extValue = saleTransDetail.getSaleServicesPriceVat();
                    if (saleTransDetail.getVat() == null) saleTransDetail.setVat(extValue);
                });
                if (saleTransDetails.isEmpty()) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.NOT_FOUND_SALE_TRANS_DETAIL, dataJson,
                            "Không tìm thấy thông tin chi tiết giao dịch",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                } else {
                    List<Long> saleTransIds = saleTransDetails.stream().map(SaleTransDetail::getSaleTransId).collect(toList());
                    if (!saleTransIds.containsAll(transIds)) {
                        insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                                Constant.NOT_FOUND_SALE_TRANS_DETAIL, dataJson,
                                "Không tìm thấy thông tin chi tiết giao dịch",
                                createInvoiceDTO.getInvoiceRequestId(),
                                existInvoiceError, shopInvoiceList.getShopId());
                        return;
                    } else {
                        transInvoiceDetailDTOs = saleTransDetails.stream().map(TransDetailDTOMapper::mapEntity2DTO).collect(toList());
                    }
                }

                invoiceInfoItemsDTOs = group2InvoiceInfoItemsDTOs(transInvoiceDetailDTOs);
                log.info("Group Invoice Info Items: [" + invoiceInfoItemsDTOs + "]");

                invoiceInfoDiscountDTOs = group2InvoiceInfoDiscountDTOs(transInvoiceDetailDTOs);
                log.info("Group Invoice Info Discount: [" + invoiceInfoDiscountDTOs + "]");
            }
        }

        //1.2.1.2
        //case transType = 2
        if (!transDTOSType2.isEmpty()) {
            String systemVATDefault = customRepository.getSystemVAT();
            String currencyDefault = customRepository.getSystemCurrency();
            List<Long> transIds = transDTOSType2.stream().map(TransDTO::getTransId).collect(toList());
            saleTransGeneralInvoiceDTOs = customRepository.getListTransaction(systemVATDefault, currencyDefault, transIds);
            if (saleTransGeneralInvoiceDTOs.isEmpty()) {
                insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                        Constant.NOT_FOUND_SALE_TRANS_GENERAL, dataJson,
                        "Không tìm thấy thông tin giao dịch trong sale_trans_general",
                        createInvoiceDTO.getInvoiceRequestId(),
                        existInvoiceError, shopInvoiceList.getShopId());
                return;
            }
            TransInvoiceDTO transGeneralInvoiceDTOIndex0 = saleTransGeneralInvoiceDTOs.get(0);
            //case transTypeDetail = 7
            if (transGeneralInvoiceDTOIndex0.getTransTypeDetail() == 7L) {
                if (transGeneralInvoiceDTOIndex0.getFromTransId() == null) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.FROM_TRANS_ID_IS_NULL, dataJson,
                            "Id giao dịch gốc null",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                InvoiceRootTransactionDTO invoiceRootFromSaleTransGeneral = customRepository.getInvoiceRootFromSaleTransGeneral(transGeneralInvoiceDTOIndex0.getFromTransId());
                if (invoiceRootFromSaleTransGeneral == null) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.NOT_FOUND_ORIGINAL_SALE_TRANS_GENERAL, dataJson,
                            "Không tìm thấy thông tin giao dịch gốc trong sale_trans_general",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                Long invoiceNo = invoiceRootFromSaleTransGeneral.getInvoiceNo();
                LocalDateTime invoiceDateTime = invoiceRootFromSaleTransGeneral.getInvoiceDateTime();
                fromInvoiceUsedId = invoiceRootFromSaleTransGeneral.getInvoiceUsedId();

                invoiceInfoItemsDTOs.add(InvoiceInfoItemsDTO.builder().price(1L).priceNotTax(1L)
                        .quantity(Math.round((saleTransGeneralInvoiceDTOs.get(0).getAmountNotTax() == null ? 0D : saleTransGeneralInvoiceDTOs.get(0).getAmountNotTax()) / (1 + (saleTransGeneralInvoiceDTOs.get(0).getVat() == null ? 0D : saleTransGeneralInvoiceDTOs.get(0).getVat()) / 100)))
                        .itemName("Adjustment decrease the amount of goods and tax rate for in voice number " + invoiceNo + " date " + invoiceDateTime)
                        .vat(transGeneralInvoiceDTOIndex0.getVat())
                        .build());

            } else {
                //case transTypeDetail <> 7
                //Lấy ra các giao dịch trong saleTransGeneralInvoiceDTOs có transTypeDetail = 2 --> Group giao dịch trong saleTransGeneralInvoiceDTOs có cùng các tiêu chí sau: isdn, vat set vào invoiceInfoItemsDTO
                List<TransInvoiceDTO> dtoListType2 = saleTransGeneralInvoiceDTOs.stream()
                        .filter(transInvoiceDTO -> transInvoiceDTO.getTransTypeDetail() == 2L).collect(toList());
                Map<IsdnVat, List<TransInvoiceDTO>> mapType2 = dtoListType2.stream()
                        .collect(groupingBy(transInvoiceDTO -> new IsdnVat(transInvoiceDTO.getIsdn(), transInvoiceDTO.getVat())));
                for (List<TransInvoiceDTO> list : mapType2.values()) {
                    invoiceInfoItemsDTOs.add(InvoiceInfoItemsDTOMapper.mapFromTransInvoiceDTOsType2(list));
                }
                //Lấy ra các giao dịch trong saleTransGeneralInvoiceDTOs có transTypeDetail = 3 --> Group giao dịch trong saleTransGeneralInvoiceDTOs có cùng các tiêu chí sau: isdn, numMonth, vat set vào invoiceInfoItemsDTO
                List<TransInvoiceDTO> dtoListType3 = saleTransGeneralInvoiceDTOs.stream()
                        .filter(transInvoiceDTO -> transInvoiceDTO.getTransTypeDetail() == 3L).collect(toList());
                Map<IsdnNumMonthVat, List<TransInvoiceDTO>> mapType3 = dtoListType3.stream()
                        .collect(groupingBy(transInvoiceDTO -> new IsdnNumMonthVat(transInvoiceDTO.getIsdn(), transInvoiceDTO.getVat(), transInvoiceDTO.getNumMonth())));
                for (List<TransInvoiceDTO> list : mapType3.values()) {
                    invoiceInfoItemsDTOs.add(InvoiceInfoItemsDTOMapper.mapFromTransInvoiceDTOsType3(list));
                }
            }
        }

        //1.2.1.3

        for (InvoiceInfoItemsDTO invoiceInfoItemsDTO : invoiceInfoItemsDTOs) {
            var priceNotTax = invoiceInfoItemsDTO.getPriceNotTax() == null ? 0 : invoiceInfoItemsDTO.getPriceNotTax();
            invoiceInfoItemsDTO.setTotalAmountNotTax((double) (invoiceInfoItemsDTO.getQuantity() == null ? 0L : invoiceInfoItemsDTO.getQuantity() * priceNotTax));
        }

        //Group invoiceInfoItemsDTOs's element has same vat set into List<invoiceTaxAmountDTO> invoiceTaxAmountDTOs
        List<InvoiceTaxAmountDTO> invoiceTaxAmountDTOs = group2InvoiceTaxAmountByVAT(invoiceInfoItemsDTOs);

        //Group invoiceInfoDiscountDTOs's element has same vat set into List<invoiceTaxAmountDTO> invoiceTaxAmountDTOs
        groupInvoiceInfoDiscount2InvoiceTaxAmount(invoiceTaxAmountDTOs, invoiceInfoDiscountDTOs);

        //1.2.1.4

        transInvoiceDTOs = wrapSaleTransDTOSaleTransGeneralDTO(saleTransGeneralInvoiceDTOs, saleTransInvoiceDTOs);
        log.info("Wrap SaleTrans vs SaleTransGeneral [" + transInvoiceDTOs + "]");

        if (transInvoiceDTOs.size() != createInvoiceDTO.getLstTransDTO().size()) {
            insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                    Constant.TOTAL_TRANS_NOT_MAP, dataJson,
                    "Tổng giao dịch trên hệ thống và giao dịch truyền vào không khớp nhau",
                    createInvoiceDTO.getInvoiceRequestId(),
                    existInvoiceError, shopInvoiceList.getShopId());
            return;
        }

        //1.2.2
        for (TransInvoiceDTO transInvoiceDTO : transInvoiceDTOs) {
            //1.2.2.1
            boolean isInvoiced = ((transInvoiceDTO.getStatus() == 3L || transInvoiceDTO.getStatus() == 1L) && (transInvoiceDTO.getTransType() == 1L))
                    || (transInvoiceDTO.getStatus() == 1L && transInvoiceDTO.getTransType() == 2L);
            if (isInvoiced) {
                insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                        Constant.INVALID_INVOICE_STATUS, dataJson,
                        "Giao dịch có trạng thái không hợp lệ",
                        createInvoiceDTO.getInvoiceRequestId(),
                        existInvoiceError, shopInvoiceList.getShopId());
                return;
            }

            //1.2.2.2
            String type = null;
            if (transInvoiceDTO.getTransType() == 1L) type = "SALE_TRANS";
            if (transInvoiceDTO.getTransType() == 2L) type = "SALE_TRANS_GENERAL";
            List<Long> listTransNotInvoice = customRepository.getTransNotInvoice(type);
            if (listTransNotInvoice.contains(transInvoiceDTO.getTransTypeDetail())) {
                insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                        Constant.TRANS_TYPE_NOT_INVOICE, dataJson,
                        "Loại giao dịch không được lập hóa đơn",
                        createInvoiceDTO.getInvoiceRequestId(),
                        existInvoiceError, shopInvoiceList.getShopId());
                return;
            }

            //1.2.2.3
            Long staffId;
            if (transInvoiceDTO.getStaffId() == null) {
                staffId = customRepository.getByShopId(createInvoiceDTO.getShopId());
            } else {
                staffId = customRepository.getStaffId(transInvoiceDTO.getStaffId());
            }
            if (staffId == null) {
                insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                        Constant.FORBIDDEN_INVOICE_FOR_OUTSIDE_COMPANY, dataJson,
                        "Không được lập hóa đơn cho giao dịch đại lý/cộng tác viên thực hiện",
                        createInvoiceDTO.getInvoiceRequestId(),
                        existInvoiceError, shopInvoiceList.getShopId());
                return;
            }
        }

        //1.2.3
        if (transInvoiceDTOs.get(0).getTransType() == 2L && transInvoiceDTOs.get(0).getTransTypeDetail() == 2L) {
            for (TransInvoiceDTO transInvoiceDTO : transInvoiceDTOs) {
                if (transInvoiceDTO.getTransTypeDetail() != 2L) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.TRANS_PAYMENT_INVALID, dataJson,
                            "Không được lập gộp giao dịch gạch nợ cùng giao dịch khác",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }
            }
        } else {
            for (TransInvoiceDTO transInvoiceDTO : transInvoiceDTOs) {
                if ((transInvoiceDTO.getTransType() == 1L && transInvoiceDTO.getTransTypeDetail() == 41L) ||
                        (transInvoiceDTO.getTransType() == 2L && transInvoiceDTO.getTransTypeDetail() == 7L)) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.MERGE_INVOICE_LIST_CONTAINS_ADJUST_INVOICE, dataJson,
                            "Danh sách giao dich lâp hóa đơn có giao dịch điều chỉnh giảm. Bạn không được phép lập gộp hóa đơn",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }
            }
            for (var i = 0; i < transInvoiceDTOs.size() - 1; i++) {
                if (!transInvoiceDTOs.get(i).getTransDate().equals(transInvoiceDTOs.get(i + 1).getTransDate())) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.MERGE_INVOICE_LIST_DATE_ERROR, dataJson,
                            "Các giao dịch không cùng ngày lập giao dịch. Bạn không được phép lập gộp hóa đơn",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                if ((!transInvoiceDTOs.get(i).getReceiverId().equals(transInvoiceDTOs.get(i + 1).getReceiverId()))
                        || (!transInvoiceDTOs.get(i).getReceiverType().equals(transInvoiceDTOs.get(i + 1).getReceiverType()))) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.MERGE_INVOICE_LIST_RECEIVER_ERROR, dataJson,
                            "Danh sách giao dịch có giao dịch bán cho kênh không cùng kênh mua hàng. Bạn không được phép lập gộp hóa đơn",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                if (!transInvoiceDTOs.get(i).getCustId().equals(transInvoiceDTOs.get(i + 1).getCustId())) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.MERGE_INVOICE_LIST_CUSTOMER_ID_ERROR, dataJson,
                            "Danh sách giao dịch không cùng ID khách hàng. Bạn không được phép lập gộp hóa đơn",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                if (!transInvoiceDTOs.get(i).getCustName().equals(transInvoiceDTOs.get(i + 1).getCustName())) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.MERGE_INVOICE_LIST_CUSTOMER_NAME_ERROR, dataJson,
                            "Danh sách giao dịch không cùng tên khách hàng. Bạn không được phép lập gộp hóa đơn",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }

                if (!transInvoiceDTOs.get(i).getTin().equals(transInvoiceDTOs.get(i + 1).getTin())) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.MERGE_INVOICE_LIST_TIN_ERROR, dataJson,
                            "Danh sách giao dịch không cùng thông tin số giấy tờ. Bạn không được phép lập gộp hóa đơn",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }
            }
            for (var i = 0; i < transInvoiceDTOs.size() - 1; i++) {
                if (!transInvoiceDTOs.get(i).getCurrency().equals(transInvoiceDTOs.get(i + 1).getCurrency())) {
                    insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                            Constant.MERGE_INVOICE_LIST_CURRENCY_ERROR, dataJson,
                            "Danh sách giao dịch không cùng thông tin loại tiền tệ. Bạn không được phép lập gộp hóa đơn",
                            createInvoiceDTO.getInvoiceRequestId(),
                            existInvoiceError, shopInvoiceList.getShopId());
                    return;
                }
            }
        }

        //1.3
        var invoiceType = 1L;
        if (transInvoiceDTOs.get(0).getTransType() == 2L && transInvoiceDTOs.get(0).getTransTypeDetail() == 2L) {
            invoiceType = 2L;
        }

        var invoiceListDTOS = customRepository.getInvoiceList(invoiceType, shopInvoiceList.getInvoiceType(), shopInvoiceList.getShopUsedId());
        if (invoiceListDTOS == null) {
            insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                    Constant.NO_INVOICE_LIST, dataJson,
                    "Đơn vị  không còn đủ hóa đơn",
                    createInvoiceDTO.getInvoiceRequestId(),
                    existInvoiceError, shopInvoiceList.getShopId());
            return;
        }
        Long invoiceListIds = invoiceListDTOS.getInvoiceListId();
        List<InvoiceList> invoiceLists = customRepository.lockInvoiceList(invoiceListIds);
        if (invoiceLists.isEmpty()) {
            insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                    Constant.INVOICE_LIST_IS_BEING_PROCESSED, dataJson,
                    "Dải hóa đơn của số hóa đơn  đang được xử lý trong giao dịch khác. Bạn vui lòng thực hiện lại",
                    createInvoiceDTO.getInvoiceRequestId(),
                    existInvoiceError, shopInvoiceList.getShopId());
            return;
        }
        insertData(transInvoiceDTOs, invoiceListDTOS, shopInvoiceList, existInvoiceError, invoiceInfoItemsDTOs,
                invoiceInfoDiscountDTOs, invoiceTaxAmountDTOs, lstInvoiceProcess,
                sysDate, startOfDay, dataJson, createInvoiceDTO, fromInvoiceUsedId);
    }

    private void insertUpdateInvoiceError(Long pkId, LocalDateTime sysDate, LocalDateTime startOfDay,
                                          Long shopUsedId, String errorCode, String dataJson, String description,
                                          Long invoiceRequestId,
                                          boolean existInvoiceError, Long shopId) {
        var invoiceError = InvoiceError.builder()
                .pkId(pkId)
                .createDate(sysDate)
                .shopUsedId(shopUsedId)
                .errorCode(errorCode)
                .dataJson(dataJson)
                .description(description)
                .invoiceRequestId(invoiceRequestId)
                .pkType(3L)
                .retry(0L)
                .build();
        if (existInvoiceError) {
            customRepository.updateMultiInvoiceError(invoiceError, invoiceRequestId, shopId, sysDate, startOfDay);
            log.info("Update invoice error [" + invoiceError + "]");
        } else {
            customRepository.insertInvoiceError(invoiceError);
            log.info("Insert invoice error [" + invoiceError + "]");
        }
    }

    private void handleAddInvoiceProcess(List<Long> listUsedId, ShopInvoiceListDTO shopInvoiceList, List<LstInvoiceProcess> listInvoiceProcess,
                                         LocalDateTime sysDate, boolean existInvoiceError, LocalDateTime startOfDay,
                                         CreateInvoiceDTO createInvoiceDTO, String errorCode, String description) {
        if (!listUsedId.contains(shopInvoiceList.getShopUsedId())) {
            listInvoiceProcess.add(new LstInvoiceProcess(shopInvoiceList.getShopUsedId(), 0L));

            var invoiceProcess = InvoiceProcess.builder()
                    .shopUsedId(shopInvoiceList.getShopUsedId())
                    .createDate(sysDate)
                    .lastModify(sysDate)
                    .status(0L)
                    .build();

            customRepository.insertInvoiceProcess(invoiceProcess);
            log.info("Insert invoice process [" + invoiceProcess + "]");
        }
        insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, null,
                errorCode,
                JSonMapper.toJson(createInvoiceDTO), description,
                createInvoiceDTO.getInvoiceRequestId(),
                existInvoiceError, createInvoiceDTO.getShopId());
    }

    private List<InvoiceInfoItemsDTO> group2InvoiceInfoItemsDTOs(List<TransDetailDTO> transInvoiceDetailDTOs) {
        List<InvoiceInfoItemsDTO> invoiceInfoItemsDTOS = new ArrayList<>();
        if (transInvoiceDetailDTOs == null) return invoiceInfoItemsDTOS;
        Map<MapTransInvoiceDetail, List<TransDetailDTO>> map = transInvoiceDetailDTOs.stream()
                .collect(groupingBy(transDetailDTO -> new MapTransInvoiceDetail(transDetailDTO.getProdOfferName(), transDetailDTO.getProdOfferId(), transDetailDTO.getPrice(),
                        transDetailDTO.getPriceNotTax(), transDetailDTO.getVat())));

        for (List<TransDetailDTO> list : map.values()) {
            invoiceInfoItemsDTOS.add(InvoiceInfoItemsDTOMapper.mapFromTransDetailDTO(list));
        }
        return invoiceInfoItemsDTOS;
    }

    private List<InvoiceInfoDiscountDTO> group2InvoiceInfoDiscountDTOs(List<TransDetailDTO> transInvoiceDetailDTOs) {
        List<InvoiceInfoDiscountDTO> invoiceInfoDiscountDTOS = new ArrayList<>();
        if (transInvoiceDetailDTOs == null) return invoiceInfoDiscountDTOS;
        List<TransDetailDTO> transDetailDTODiscount = transInvoiceDetailDTOs.stream()
                .filter(transDetailDTO -> transDetailDTO.getDiscountAmount() > 0).collect(toList());

        Map<MapTransInvoiceDetailDiscount, List<TransDetailDTO>> map = transDetailDTODiscount.stream()
                .collect(groupingBy(transDetailDTO -> new MapTransInvoiceDetailDiscount(transDetailDTO.getProdOfferName(), transDetailDTO.getProdOfferId(),
                        transDetailDTO.getVat(), transDetailDTO.getDiscountAmount())));

        for (List<TransDetailDTO> list : map.values()) {
            invoiceInfoDiscountDTOS.add(InvoiceInfoDiscountDTOMapper.mapFromTransDetailDTODiscount(list));
        }
        return invoiceInfoDiscountDTOS;
    }

    private List<InvoiceTaxAmountDTO> group2InvoiceTaxAmountByVAT(List<InvoiceInfoItemsDTO> invoiceInfoItemsDTOs) {
        HashMap<Double, InvoiceTaxAmountDTO> mapInvoiceInfoItems = new HashMap<>();
        invoiceInfoItemsDTOs.forEach(invoiceInfoItemsDTO -> {
            if (!mapInvoiceInfoItems.containsKey(invoiceInfoItemsDTO.getVat())) {
                var invoiceTaxAmountDTO = InvoiceTaxAmountDTO.builder()
                        .taxPercentage(invoiceInfoItemsDTO.getTaxPercentage())
                        .totalTaxAmount(invoiceInfoItemsDTO.getTaxAmount())
                        .totalAmountNotTax(invoiceInfoItemsDTO.getItemTotalAmountNotTax())
                        .build();
                mapInvoiceInfoItems.put(invoiceInfoItemsDTO.getVat(), invoiceTaxAmountDTO);
            } else {
                var invoiceTaxAmountDTO = mapInvoiceInfoItems.get(invoiceInfoItemsDTO.getVat());
                invoiceTaxAmountDTO.setTotalTaxAmount(invoiceTaxAmountDTO.getTotalTaxAmount() + invoiceInfoItemsDTO.getTaxAmount());
                invoiceTaxAmountDTO.setTotalAmountNotTax(invoiceTaxAmountDTO.getTotalAmountNotTax() + invoiceInfoItemsDTO.getItemTotalAmountNotTax());
            }
        });
        return new ArrayList<>(mapInvoiceInfoItems.values());
    }

    private void groupInvoiceInfoDiscount2InvoiceTaxAmount(List<InvoiceTaxAmountDTO> invoiceTaxAmountDTOS, List<InvoiceInfoDiscountDTO> invoiceInfoDiscountDTOs) {
        HashMap<Double, InvoiceTaxAmountDTO> mapInvoiceInfoDiscount = new HashMap<>();
        invoiceInfoDiscountDTOs.forEach(invoiceInfoDiscountDTO -> {
            if (!mapInvoiceInfoDiscount.containsKey(invoiceInfoDiscountDTO.getVat())) {
                var invoiceTaxAmountDTO = InvoiceTaxAmountDTO.builder()
                        .totalTaxAmount(invoiceInfoDiscountDTO.getTaxDiscount())
                        .totalAmountNotTax(invoiceInfoDiscountDTO.getDiscountNotTax())
                        .build();
                mapInvoiceInfoDiscount.put(invoiceInfoDiscountDTO.getVat(), invoiceTaxAmountDTO);
            } else {
                var invoiceTaxAmountDTO = mapInvoiceInfoDiscount.get(invoiceInfoDiscountDTO.getVat());
                invoiceTaxAmountDTO.setTotalTaxAmount(invoiceTaxAmountDTO.getTotalTaxAmount() + invoiceInfoDiscountDTO.getTaxDiscount());
                invoiceTaxAmountDTO.setTotalAmountNotTax(invoiceTaxAmountDTO.getTotalAmountNotTax() + invoiceInfoDiscountDTO.getDiscountNotTax());
            }
        });
        invoiceTaxAmountDTOS.addAll(new ArrayList<>(mapInvoiceInfoDiscount.values()));
    }

    public List<TransInvoiceDTO> wrapSaleTransDTOSaleTransGeneralDTO(List<TransInvoiceDTO> saleTransGeneralInvoiceDTOs,
                                                                     List<TransInvoiceDTO> saleTransInvoiceDTOs) {
        List<TransInvoiceDTO> result = new ArrayList<>();
        result.addAll(saleTransInvoiceDTOs);
        result.addAll(saleTransGeneralInvoiceDTOs);
        return result;
    }

    public void insertData(List<TransInvoiceDTO> transInvoiceDTOs, InvoiceListDTO invoiceListDTO, ShopInvoiceList shopInvoiceList, Boolean existInvoiceError,
                           List<InvoiceInfoItemsDTO> invoiceInfoItemsDTOs, List<InvoiceInfoDiscountDTO> invoiceInfoDiscountDTOS, List<InvoiceTaxAmountDTO> invoiceTaxAmountDTOs,
                           List<LstInvoiceProcess> invoiceProcesses, LocalDateTime sysDate, LocalDateTime startOfDay, String dataJson, CreateInvoiceDTO createInvoiceDTO, Long fromInvoiceUsedId) {
        //1
        List<Long> listUsedId = invoiceProcesses.stream().map(LstInvoiceProcess::getShopUsedId).collect(toList());

        var invoiceProcess = InvoiceProcess.builder()
                .shopUsedId(shopInvoiceList.getShopUsedId())
                .shopId(shopInvoiceList.getShopId())
                .createDate(LocalDateTime.now())
                .lastModify(LocalDateTime.now())
                .status(1L)
                .build();

        if (!listUsedId.contains(shopInvoiceList.getShopUsedId())) {
            customRepository.insertInvoiceProcess(invoiceProcess);
            log.info("Insert invoice process = [" + invoiceProcess + "]");
        } else {
            customRepository.updateInvoiceProcess(invoiceProcess, startOfDay);
            log.info("Update invoice process = [" + invoiceProcess + "]");
        }
        //2
        var transInvoiceDTOIndex0 = transInvoiceDTOs.get(0);
        if (transInvoiceDTOIndex0.getReceiverId() != null && transInvoiceDTOIndex0.getReceiverType() != null) {
            if (transInvoiceDTOIndex0.getReceiverType() == 1) {
                var shop = shopRepository.getShopByShopId(transInvoiceDTOs.get(0).getReceiverId());
                String areCode = shop.getProvince() + shop.getDistrict() + shop.getPrecinct();
                AreaDTO catalog = catalogService.getArea(areCode, "vi-LA");
                transInvoiceDTOIndex0.setCustName(shop.getContactName());
                transInvoiceDTOIndex0.setAddress(shop.getAddress());
                transInvoiceDTOIndex0.setTin(shop.getTin());
                transInvoiceDTOIndex0.setTelNumber(shop.getTel());
                transInvoiceDTOIndex0.setCompany(shop.getName());
                transInvoiceDTOIndex0.setProvinceName(catalog == null ? null : catalog.getProvinceName());
                transInvoiceDTOIndex0.setDistrictName(catalog == null ? null : catalog.getDistrictName());
                transInvoiceDTOIndex0.setPrecintName(catalog == null ? null : catalog.getPrecinctName());
            }
            if (transInvoiceDTOIndex0.getReceiverType() == 2) {
                var staff = staffRepository.getAllByStaffId(transInvoiceDTOs.get(0).getReceiverId());
                String areCode = staff.getProvince() + staff.getDistrict() + staff.getPrecinct();
                AreaDTO catalog = catalogService.getArea(areCode, "vi-LA");
                transInvoiceDTOIndex0.setCustName(staff.getName());
                transInvoiceDTOIndex0.setAddress(staff.getAddress());
                transInvoiceDTOIndex0.setTin(staff.getTin());
                transInvoiceDTOIndex0.setTelNumber(staff.getTel());
                transInvoiceDTOIndex0.setCompany(staff.getName());
                transInvoiceDTOIndex0.setProvinceName(catalog == null ? null : catalog.getProvinceName());
                transInvoiceDTOIndex0.setDistrictName(catalog == null ? null : catalog.getDistrictName());
                transInvoiceDTOIndex0.setPrecintName(catalog == null ? null : catalog.getPrecinctName());
            }

        }
        var shop = shopRepository.getShopByShopId(shopInvoiceList.getShopUsedId());
        String shopTin = shop.getTin();
        String shopName = shop.getName();
        String shopAddress = shop.getAddress();
        Double amountNotTax = transInvoiceDTOs.stream().filter(item -> Objects.nonNull(item.getAmountNotTax())).mapToDouble(TransInvoiceDTO::getAmountNotTax).sum();
        Double amountTax = transInvoiceDTOs.stream().filter(item -> Objects.nonNull(item.getAmountTax())).mapToDouble(TransInvoiceDTO::getAmountTax).sum();
        Double tax = transInvoiceDTOs.stream().filter(item -> Objects.nonNull(item.getTax())).mapToDouble(TransInvoiceDTO::getTax).sum();
        Double totalDiscount = transInvoiceDTOs.stream().filter(item -> Objects.nonNull(item.getDiscount())).mapToDouble(TransInvoiceDTO::getDiscount).sum();
        boolean checkVat = transInvoiceDTOs.stream().filter(item -> Objects.nonNull(item.getVat()))
                .mapToDouble(TransInvoiceDTO::getVat)
                .distinct()
                .count() == 1;
        Long invoiceId = invoiceListDTO.getCurrInvoice() + 1;
        String invoiceNo = generateInvoiceNo(invoiceListDTO.getSerialNo(), invoiceListDTO.getInvoiceNoLength(), invoiceId);

        if (invoiceNo == null) {
            insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                    Constant.INVOICE_LENGTH_NOT_EXIST, dataJson,
                    "Invoice length không tồn tại",
                    createInvoiceDTO.getInvoiceRequestId(),
                    existInvoiceError, shopInvoiceList.getShopId());
            return;
        }

        var saveFromUsedId = false;
        long adjustmentType;
        if ((transInvoiceDTOs.get(0).getTransType().equals(1L) && transInvoiceDTOs.get(0).getTransTypeDetail().equals(Long.valueOf("41")))
                || (transInvoiceDTOs.get(0).getTransType().equals(2L) && transInvoiceDTOs.get(0).getTransTypeDetail().equals(Long.valueOf("7")))) {
            saveFromUsedId = true;
            adjustmentType = 1L;
        } else {
            adjustmentType = 2L;
        }

        var invoiceUsed = InvoiceUsed.builder()
                .invoiceUsedId(invoiceUsedRepository.getNextInvoiceUsedId())
                .invoiceListId(invoiceListDTO.getInvoiceListId())
                .invoiceDatetime(transInvoiceDTOIndex0.getTransDate())
                .shopId(transInvoiceDTOIndex0.getShopId())
                .staffId(transInvoiceDTOIndex0.getStaffId())
                .receiverId(transInvoiceDTOIndex0.getReceiverId())
                .receiverType(transInvoiceDTOIndex0.getReceiverType())
                .serialNo(invoiceListDTO.getSerialNo())
                .invoiceId(invoiceId)
                .invoiceNo(invoiceNo)
                .invoiceLength(invoiceListDTO.getInvoiceNoLength())
                .shopName(shopName)
                .shopAddress(shopAddress)
                .shopTin(shopTin)
                .custId(transInvoiceDTOIndex0.getCustId())
                .custIsdn(transInvoiceDTOIndex0.getTelNumber())
                .custName(transInvoiceDTOIndex0.getCustName())
                .custAddress(transInvoiceDTOIndex0.getAddress())
                .custCompany(transInvoiceDTOIndex0.getCompany())
                .custIdentityNo(transInvoiceDTOIndex0.getTin())
                .status(1L)
                .note(null)
                .createDate(sysDate)
                .currency(transInvoiceDTOIndex0.getCurrency())
                .amountNotTax(amountNotTax)
                .amountTax(amountTax)
                .tax(tax)
                .discount(totalDiscount)
                .vat(checkVat ? transInvoiceDTOIndex0.getVat() : null)
                .eInvoiceStatus(invoiceListDTO.getInvoiceForm() == 2 ? 0L : null)
                .fromInvoiceUsedId(saveFromUsedId ? fromInvoiceUsedId : null)
                .build();
        var insertInvoiceUsed = invoiceUsedRepository.save(invoiceUsed);
        log.info("Insert invoice used = [" + invoiceUsed + "]");
        //3
        try {
            var invoiceList = invoiceListRepository.findFirstByCurrInvoiceAndInvoiceListId(invoiceListDTO.getCurrInvoice(), invoiceListDTO.getInvoiceListId());
            invoiceList.setCurrInvoice(invoiceList.getCurrInvoice() + 1);
            if (Objects.equals(invoiceList.getCurrInvoice() + 1, invoiceList.getToInvoice()))
                invoiceList.setStatus(3L);
            invoiceListRepository.save(invoiceList);
            log.info("Update invoice list = [" + invoiceList + "]");
        } catch (Exception e) {
            insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                    Constant.NOT_UPDATE_INVOICE_LIST, dataJson,
                    "Không update được vào bảng invoice_list",
                    createInvoiceDTO.getInvoiceRequestId(),
                    existInvoiceError, shopInvoiceList.getShopId());
        }

        //4
        transInvoiceDTOs.forEach(item -> {
            if (Objects.equals(item.getTransType(), 1L)) {
                var saleTrans = saleTransRepository.findFirstBySaleTransId(item.getTransId());

                saleTrans.setStatus("3");
                saleTrans.setInvoiceUsedId(insertInvoiceUsed.getInvoiceUsedId());
                saleTrans.setInvoiceCreateDate(insertInvoiceUsed.getCreateDate());
                saleTrans.setInvoiceNo(insertInvoiceUsed.getInvoiceNo());
                saleTrans.setInvoiceRequestId(createInvoiceDTO.getInvoiceRequestId());
                saleTrans.setInvoiceType(1L);

                saleTransRepository.save(saleTrans);
                log.info("Update sale trans list = [" + saleTrans + "]");
            }
            if (Objects.equals(item.getTransType(), 2L)) {
                var saleTransGeneral = saleTransGeneralRepository.findFirstBySaleTransGeneralId(item.getTransId());

                saleTransGeneral.setInvoiceStatus(1L);
                saleTransGeneral.setInvoiceUsedId(insertInvoiceUsed.getInvoiceUsedId());
                saleTransGeneral.setInvoiceDateTime(insertInvoiceUsed.getCreateDate());
                saleTransGeneral.setInvoiceNo(insertInvoiceUsed.getInvoiceNo());
                saleTransGeneral.setInvoiceRequestId(createInvoiceDTO.getInvoiceRequestId());
                saleTransGeneral.setInvoiceType(1L);

                saleTransGeneralRepository.save(saleTransGeneral);
                log.info("Update sale trans general list = [" + saleTransGeneral + "]");
            }
        });
        //5
        //5.1
        Double totalDiscountNotTax = invoiceInfoItemsDTOs.stream().filter(item -> Objects.nonNull(item.getDiscountNotTax())).mapToDouble(InvoiceInfoItemsDTO::getDiscountNotTax).sum();
        double totalTaxAmount = invoiceInfoItemsDTOs.stream().filter(item -> Objects.nonNull(item.getTotalTaxAmount())).mapToDouble(InvoiceInfoItemsDTO::getTotalTaxAmount).sum();
        Double totalAmountWithoutTax = invoiceInfoItemsDTOs.stream().filter(item -> Objects.nonNull(item.getTotalAmountNotTax())).mapToDouble(InvoiceInfoItemsDTO::getTotalAmountNotTax).sum();
        String invoiceNumber = generateInvoiceNumber(invoiceListDTO.getCurrInvoice().toString(), invoiceListDTO.getInvoiceNoLength());

        if (invoiceNumber == null) {
            insertUpdateInvoiceError(shopInvoiceList.getShopId(), sysDate, startOfDay, shopInvoiceList.getShopUsedId(),
                    Constant.INVOICE_LENGTH_NOT_EXIST, dataJson,
                    "Invoice length không tồn tại",
                    createInvoiceDTO.getInvoiceRequestId(),
                    existInvoiceError, shopInvoiceList.getShopId());
            return;
        }
        var invoiceInfo = InvoiceInfo.builder()
                .invoiceInfoId(invoiceInfoRepository.getNextInvoiceInfoId())
                .invoiceUsedId(insertInvoiceUsed.getInvoiceUsedId())
                .createDate(sysDate)
                .templateCode(invoiceListDTO.getFormNo())
                .invoiceNote(null)
                .buyerName(transInvoiceDTOIndex0.getCustName())
                .buyerAddress(transInvoiceDTOIndex0.getAddress())
                .buyerProvince(transInvoiceDTOIndex0.getProvinceName())
                .buyerDistrict(transInvoiceDTOIndex0.getDistrictName())
                .buyerVillage(transInvoiceDTOIndex0.getPrecintName())
                .buyerEmail(transInvoiceDTOIndex0.getEmail())
                .buyerPhoneNumber(transInvoiceDTOIndex0.getTelNumber())
                .buyerTaxCode(transInvoiceDTOIndex0.getTin())
                .sellerTaxCode(shopTin)
                .sellerAddress(shopAddress)
                .sellerLegalName(shopName)
                .invoiceSystem(invoiceListDTO.getInvoiceForm())
                .invoiceNumber(invoiceNumber)
                .invoiceName(null)
                .invoiceSeries(invoiceListDTO.getSerialNo())
                .invoiceNo(insertInvoiceUsed.getInvoiceNo())
                .invoiceType(null)
                .totalDiscountNotTax(totalDiscountNotTax)
                .totalTaxAmount(totalTaxAmount)
                .totalAmountWithoutTax(totalAmountWithoutTax)
                .totalAmountWithTax(totalAmountWithoutTax - totalDiscountNotTax + totalTaxAmount)
                .totalAmountWithTaxInWord(moneyToWord(String.valueOf(totalAmountWithoutTax - totalDiscountNotTax + totalTaxAmount)))
                .sumTotalLineAmountNotTax(totalAmountWithoutTax - totalDiscountNotTax)
                .processType(1L)
                .adjustmentType(adjustmentType)
                .contractNumber(transInvoiceDTOIndex0.getContractNo())
                .currencyCode(transInvoiceDTOIndex0.getCurrency())
                .originalInvoiceId(null)
                .originalInvoiceIssueDate(null)
                .build();
        var insertInvoiceInfo = invoiceInfoRepository.save(invoiceInfo);
        log.info("Insert invoice infor = [" + invoiceInfo + "]");
        //5.2
        invoiceInfoItemsDTOs.forEach(invoiceInfoItemsDTO -> {
            var invoiceInfoItems = InvoiceInfoItems.builder()
                    .invoiceInfoItemsId(invoiceInfoItemsRepository.getNextInvoiceInfoItems())
                    .invoiceInfoId(insertInvoiceInfo.getInvoiceInfoId())
                    .price(invoiceInfoItemsDTO.getPrice())
                    .priceNotTax(invoiceInfoItemsDTO.getPriceNotTax())
                    .amountNotTax(invoiceInfoItemsDTO.getTotalAmountNotTax())
                    .quantity(invoiceInfoItemsDTO.getQuantity())
                    .unitName(invoiceInfoItemsDTO.getUnitName())
                    .itemId(invoiceInfoItemsDTO.getItemId())
                    .itemName(invoiceInfoItemsDTO.getItemName())
                    .taxAmount(invoiceInfoItemsDTO.getTaxAmount())
                    .taxPercentage(invoiceInfoItemsDTO.getTaxPercentage())
                    .build();
            invoiceInfoItemsRepository.save(invoiceInfoItems);
            log.info("Insert invoice infor items = [" + invoiceInfoItems + "]");
        });
        //5.3
        if (invoiceInfoDiscountDTOS.size() > 0L) {
            invoiceInfoDiscountDTOS.forEach(invoiceInfoDiscountDTO -> {
                var invoiceInfoDiscount = InvoiceInfoDiscount.builder()
                        .invoiceInfoDiscountId(invoiceInfoDiscountRepository.getNextInvoiceInfoDiscount())
                        .invoiceInfoId(insertInvoiceInfo.getInvoiceInfoId())
                        .discountPrice(invoiceInfoDiscountDTO.getDiscountPrice())
                        .priceNotTax(invoiceInfoDiscountDTO.getPriceNotTax())
                        .amountNotTax(invoiceInfoDiscountDTO.getAmountNotTax())
                        .quantity(invoiceInfoDiscountDTO.getQuantity())
                        .unitName(invoiceInfoDiscountDTO.getUnitName())
                        .itemId(invoiceInfoDiscountDTO.getItemId())
                        .itemName(invoiceInfoDiscountDTO.getItemName())
                        .taxDiscount(invoiceInfoDiscountDTO.getTaxDiscount())
                        .taxPercentage(invoiceInfoDiscountDTO.getTaxPercentage())
                        .build();
                invoiceInfoDiscountRepository.save(invoiceInfoDiscount);
                log.info("Insert invoice infor discount = [" + invoiceInfoDiscount + "]");
            });
        }
        //5.4
        if (invoiceTaxAmountDTOs.size() > 0L) {
            invoiceTaxAmountDTOs.forEach(invoiceTaxAmountDTO -> {
                var invoiceTaxAmount = InvoiceTaxAmount.builder()
                        .invoiceTaxAmountId(invoiceTaxAmountRepository.getNextInvoiceTaxAmountId())
                        .invoiceInfoId(insertInvoiceInfo.getInvoiceInfoId())
                        .taxPercentage(invoiceTaxAmountDTO.getTaxPercentage())
                        .totalAmountNotTax(invoiceTaxAmountDTO.getTotalAmountNotTax())
                        .totalDiscountNotTax(invoiceTaxAmountDTO.getTotalDiscountNotTax())
                        .totalTaxAmount(invoiceTaxAmountDTO.getTotalTaxAmount())
                        .totalTaxDiscount(invoiceTaxAmountDTO.getTotalTaxDiscount())
                        .build();
                invoiceTaxAmountRepository.save(invoiceTaxAmount);
                log.info("Insert invoice tax amount = [" + invoiceTaxAmount + "]");
            });
        }
    }

    private String generateInvoiceNo(String serialNo, Long invoiceNoLength, Long invoiceId) {
        if (invoiceNoLength == null) {
            return null;
        }
        return serialNo + "0".repeat(Math.max(0, (int) (invoiceNoLength - String.valueOf(invoiceId).length()))) + invoiceId;
    }

    private String generateInvoiceNumber(String currInvoice, Long invoiceNoLength) {
        if (invoiceNoLength == null) {
            return null;
        }
        String result = currInvoice + 1;
        return "0".repeat(Math.max(0, (int) (invoiceNoLength - result.length()))) + result;
    }

    private String moneyToWord(String input) {
        MoneyConverters converter = MoneyConverters.ENGLISH_BANKING_MONEY_VALUE;
        return converter.asWords(new BigDecimal(input));
    }
}
