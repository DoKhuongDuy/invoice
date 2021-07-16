package com.viettel.bccs3.service.impl;

import com.viettel.bccs3.common.Constant;
import com.viettel.bccs3.config.Topic;
import com.viettel.bccs3.domain.dto.*;
import com.viettel.bccs3.domain.model.*;
import com.viettel.bccs3.repository.*;
import com.viettel.bccs3.service.InvoiceJobService;
import com.viettel.bccs3.service.ProducerService;
import com.viettel.bccs3.service.mapper.InvoiceProcessMapper;
import com.viettel.bccs3.service.mapper.ShopInvoiceListMapper;
import com.viettel.bccs3.service.mapper.TransDTOMapper;
import com.viettel.bccs3.service.mapper.TransDTOsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Log4j2
public class InvoiceJobServiceImpl implements InvoiceJobService {
    @Value("${mod}")
    private String mod;
    @Value("${partition}")
    private String partition;

    private final InvoiceProcessRepository invoiceProcessRepository;
    private final ShopInvoiceListRepository shopInvoiceListRepository;
    private final CustomRepository customRepository;
    private final SaleTransRepository saleTransRepository;
    private final SaleTransGeneralRepository saleTransGeneralRepository;
    private final ProducerService producerService;

    @Override
    public void getCurrentTransInvoice() {
        List<CreateInvoiceDTO> createInvoiceDTOS = new ArrayList<>();
        List<LstInvoiceProcess> lstInvoiceProcess = new ArrayList<>();
        List<ShopInvoiceListDTO> shopInvoiceLists;
        List<Long> listUsedId = new ArrayList<>();

        var existInvoiceError = false;

        var sysDate = LocalDateTime.now();
        var startOfDay = LocalDate.now().atStartOfDay();

        //list invoice process today
        List<InvoiceProcess> invoiceProcesses = invoiceProcessRepository.findAllByCreateDateIsAfter(startOfDay);

        if (!invoiceProcesses.isEmpty()) {
            //list invoice process after mapper
            lstInvoiceProcess = invoiceProcesses.stream().map(InvoiceProcessMapper::mapInvoiceProcess2LstInvoiceProcess).collect(toList());

            listUsedId = lstInvoiceProcess.stream().map(LstInvoiceProcess::getShopUsedId).collect(toList());
        }

        List<ShopInvoiceList> shopInvoiceListsOrigin = shopInvoiceListRepository.findAllByStatus(1L, Integer.parseInt(mod), Integer.parseInt(partition));
        if (shopInvoiceListsOrigin.isEmpty()) {
            var newInvoiceError = InvoiceError.builder()
                    .pkId(null)
                    .createDate(sysDate)
                    .shopUsedId(null)
                    .errorCode(Constant.NOT_CREATED_INVOICE)
                    .description("Không có thông tin sử dụng hóa đơn.")
                    .pkType(1L)
                    .retry(0L)
                    .build();
            customRepository.insertInvoiceError(newInvoiceError);
            log.info("Insert invoice error [" + newInvoiceError + "]");
            return;
        }
        shopInvoiceLists = shopInvoiceListsOrigin.stream().map(ShopInvoiceListMapper::mapEntity2DTO).collect(toList());

        if (shopInvoiceLists.size() > 0L) {
            for (ShopInvoiceListDTO shopInvoiceList : shopInvoiceLists) {
                var invoiceError = customRepository.existInvoiceError(shopInvoiceList, startOfDay, 1L);
                if (invoiceError != null) {
                    if (invoiceError.getRetry() >= Constant.CONFIG_RETRY) {
                        log.info("Invoice Error Retry = " + invoiceError.getRetry() + " > config : " + Constant.CONFIG_RETRY);
                        continue;
                    } else {
                        existInvoiceError = true;
                    }
                }

                //check invoice exist and has status = 1 -> ignore, if not -> handle
                if (!lstInvoiceProcess.contains(new LstInvoiceProcess(shopInvoiceList.getShopUsedId(), 1L))) {
                    List<SaleTrans> transNotInvoiced = customRepository.getTransNotInvoiced(shopInvoiceList.getShopUsedId(), startOfDay);

                    if (transNotInvoiced.size() > 0L) {
                        handleInvoiceProcessInvoiceError(listUsedId, shopInvoiceList, lstInvoiceProcess, sysDate, existInvoiceError, 1L, startOfDay, customRepository);
                        continue;
                    } else {
                        List<SaleTransGeneral> saleTransGenerals = customRepository.getSaleTransGeneralToCheck(shopInvoiceList.getShopUsedId());
                        if (saleTransGenerals.size() > 0L) {
                            handleInvoiceProcessInvoiceError(listUsedId, shopInvoiceList, lstInvoiceProcess, sysDate, existInvoiceError, 1L, startOfDay, customRepository);
                            continue;
                        } else {
                            var invoiceProcess = InvoiceProcess.builder()
                                    .shopUsedId(shopInvoiceList.getShopUsedId())
                                    .createDate(sysDate)
                                    .lastModify(sysDate)
                                    .status(1L)
                                    .build();

                            if (!listUsedId.contains(shopInvoiceList.getShopUsedId())) {
                                lstInvoiceProcess.add(new LstInvoiceProcess(shopInvoiceList.getShopUsedId(), 1L));
                                customRepository.insertInvoiceProcess(invoiceProcess);
                                log.info("Insert invoice process [" + invoiceProcess + "]");
                            } else {
                                lstInvoiceProcess.remove(new LstInvoiceProcess(shopInvoiceList.getShopUsedId(), 0L));
                                lstInvoiceProcess.add(new LstInvoiceProcess(shopInvoiceList.getShopUsedId(), 1L));
                                customRepository.updateInvoiceProcess(invoiceProcess, startOfDay);
                                log.info("Update invoice process [" + invoiceProcess + "]");
                            }
                        }
                    }
                }

                //handle wrap 2 list
                List<SaleTransDTO> saleTrans = customRepository.getSaleTrans(shopInvoiceList.getShopId(), startOfDay);
                List<SaleTransGeneralDTO> saleTransGenerals = customRepository.getSaleTransGeneral(shopInvoiceList.getShopId(), startOfDay);

                List<TransDTOs> transInvoiceDTOs = new ArrayList<>(wrapSaleTransAndSaleTransGeneral(saleTrans, saleTransGenerals));

                createInvoiceDTOS.addAll(classifyTransInvoiceDTOs(transInvoiceDTOs, shopInvoiceList.getShopId()));
                log.info("After grouping: [" + createInvoiceDTOS + "]");
                createInvoiceDTOS.forEach(invoice -> producerService.sendMessage(invoice, Topic.BCCS3_CREATE_INVOICE, Integer.parseInt(partition)));
            }
        }

    }

    @Override
    public void getLastDayTransInvoice() {
        List<CreateInvoiceDTO> createInvoiceDTOS;
        List<LstInvoiceProcess> lstInvoiceProcess = new ArrayList<>();
        List<ShopInvoiceListDTO> shopInvoiceListDTOs;
        var existInvoiceError = false;

        var sysDate = LocalDateTime.now();
        var startOfDay = LocalDate.now().atStartOfDay();

        //get invoice process today
        List<InvoiceProcess> invoiceProcesses = invoiceProcessRepository.findAllByCreateDateIsAfter(startOfDay);
        if (invoiceProcesses.size() > 0L) {
            lstInvoiceProcess = invoiceProcesses.stream().map(InvoiceProcessMapper::mapInvoiceProcess2LstInvoiceProcess).collect(toList());
        }

        List<ShopInvoiceList> shopInvoiceLists = shopInvoiceListRepository.findAllByStatus(1L, Integer.parseInt(mod), Integer.parseInt(partition));
        if (shopInvoiceLists.isEmpty()) {
            var newInvoiceError = InvoiceError.builder()
                    .pkId(null)
                    .createDate(sysDate)
                    .shopUsedId(null)
                    .errorCode(Constant.NOT_CREATED_INVOICE)
                    .description("Không có thông tin sử dụng hóa đơn.")
                    .pkType(2L)
                    .retry(0L)
                    .build();
            customRepository.insertInvoiceError(newInvoiceError);
            log.info("Insert invoice error [" + newInvoiceError + "]");
            return;
        }
        shopInvoiceListDTOs = shopInvoiceLists.stream().map(ShopInvoiceListMapper::mapEntity2DTO).collect(toList());


        if (shopInvoiceLists.size() > 0L) {
            for (ShopInvoiceListDTO shopInvoiceListDTO : shopInvoiceListDTOs) {
                List<Long> lstShopUsedId = lstInvoiceProcess.stream().map(LstInvoiceProcess::getShopUsedId).collect(toList());

                var invoiceError = customRepository.existInvoiceError(shopInvoiceListDTO, startOfDay, 2L);
                if (invoiceError != null) {
                    if (invoiceError.getRetry() >= Constant.CONFIG_RETRY) {
                        log.info("Invoice Error Retry = " + invoiceError.getRetry() + " > config : " + Constant.CONFIG_RETRY);
                        continue;
                    } else {
                        existInvoiceError = true;
                    }
                }

                if (!lstInvoiceProcess.contains(new LstInvoiceProcess(shopInvoiceListDTO.getShopUsedId(), 1L))) {
                    List<SaleTrans> transNotInvoiced = customRepository.getTransNotInvoiced(shopInvoiceListDTO.getShopUsedId(), startOfDay);

                    if (transNotInvoiced != null && !transNotInvoiced.isEmpty()) {
                        handleInvoiceProcessInvoiceError(lstShopUsedId, shopInvoiceListDTO, lstInvoiceProcess, sysDate, existInvoiceError, 2L, startOfDay, customRepository);
                        continue;
                    } else {
                        List<SaleTransGeneral> saleTransGenerals = customRepository.getSaleTransGeneralToCheck(shopInvoiceListDTO.getShopUsedId());
                        if (saleTransGenerals != null && !saleTransGenerals.isEmpty()) {
                            handleInvoiceProcessInvoiceError(lstShopUsedId, shopInvoiceListDTO, lstInvoiceProcess, sysDate, existInvoiceError, 2L, startOfDay, customRepository);
                            continue;
                        } else {
                            var invoiceProcess = InvoiceProcess.builder()
                                    .shopUsedId(shopInvoiceListDTO.getShopUsedId())
                                    .createDate(sysDate)
                                    .lastModify(sysDate)
                                    .status(1L)
                                    .build();

                            if (!lstShopUsedId.contains(shopInvoiceListDTO.getShopUsedId())) {
                                lstInvoiceProcess.add(new LstInvoiceProcess(shopInvoiceListDTO.getShopUsedId(), 1L));
                                customRepository.insertInvoiceProcess(invoiceProcess);
                                log.info("Insert invoice process [" + invoiceProcess + "]");
                            } else {
                                lstInvoiceProcess.remove(new LstInvoiceProcess(shopInvoiceListDTO.getShopUsedId(), 0L));
                                lstInvoiceProcess.add(new LstInvoiceProcess(shopInvoiceListDTO.getShopUsedId(), 1L));
                                customRepository.updateInvoiceProcess(invoiceProcess, startOfDay);
                                log.info("Update invoice process [" + invoiceProcess + "]");
                            }
                        }
                    }
                }
                //get transactions sale_trans
                List<SaleTransDTO> saleTrans = customRepository.getSaleTrans(shopInvoiceListDTO.getShopId(), startOfDay);
                //get transactions sale_trans_general
                List<SaleTransGeneralDTO> saleTransGenerals = customRepository.getSaleTransGeneral(shopInvoiceListDTO.getShopId(), startOfDay);
                //wrap to a list
                List<TransDTOs> transInvoiceDTOs = new ArrayList<>(wrapSaleTransAndSaleTransGeneral(saleTrans, saleTransGenerals));

                createInvoiceDTOS = groupingTrans(transInvoiceDTOs, shopInvoiceListDTO.getShopId());
                log.info("After grouping: [" + createInvoiceDTOS + "]");
                createInvoiceDTOS.forEach(invoice -> producerService.sendMessage(invoice, Topic.BCCS3_CREATE_INVOICE, Integer.parseInt(partition)));

            }
        }
    }

    static void handleInvoiceProcessInvoiceError(List<Long> listUsedId, ShopInvoiceListDTO shopInvoiceList, List<LstInvoiceProcess> listInvoiceProcess,
                                                 LocalDateTime sysDate, boolean existInvoiceError,
                                                 Long pkType, LocalDateTime startOfDay, CustomRepository customRepository) {
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
        if (existInvoiceError) {
            customRepository.updateInvoiceError(shopInvoiceList.getShopId(), shopInvoiceList.getShopUsedId(), sysDate, startOfDay, pkType);
            log.info("Update invoice error retry = retry + 1 ! ");
        } else {
            var newInvoiceError = InvoiceError.builder()
                    .pkId(shopInvoiceList.getShopId())
                    .createDate(sysDate)
                    .shopUsedId(shopInvoiceList.getShopUsedId())
                    .errorCode(Constant.NOT_CREATED_INVOICE)
                    .description("Đơn vị còn hóa đơn ngày hôm trước chưa lập")
                    .pkType(pkType)
                    .retry(0L)
                    .build();

            customRepository.insertInvoiceError(newInvoiceError);
            log.info("Insert invoice error [" + newInvoiceError + "]");
        }
    }

    private List<TransDTOs> wrapSaleTransAndSaleTransGeneral(List<SaleTransDTO> saleTransDTOS, List<SaleTransGeneralDTO> saleTransGeneralDTOS) {
        List<TransDTOs> transDTOsSaleTrans = saleTransDTOS.stream()
                .map(TransDTOsMapper::mapFromSaleTransDTO)
                .collect(toList());
        List<TransDTOs> transDTOsSaleTransGeneral = saleTransGeneralDTOS.stream()
                .map(TransDTOsMapper::mapFromSaleTransDTOGeneral)
                .collect(toList());
        transDTOsSaleTransGeneral.forEach(transDTOs -> {
            if (!transDTOsSaleTrans.contains(transDTOs)) {
                transDTOsSaleTrans.add(transDTOs);
            }
        });
        return transDTOsSaleTrans;
    }

    private List<CreateInvoiceDTO> classifyTransInvoiceDTOs(List<TransDTOs> transInvoiceDTOs, Long shopId) {
        List<CreateInvoiceDTO> createInvoiceDTOS = new ArrayList<>();
        List<TransDTOs> listTransDTOsToRemove = new ArrayList<>();

        transInvoiceDTOs.forEach(transInvoice -> {
            if (transInvoice.getInvoiceRequestId() == null) {
                List<TransDTO> lstTransDTO = new ArrayList<>();
                lstTransDTO.add(TransDTOMapper.mapTransDTOS2TransDTO(transInvoice));
                createInvoiceDTOS.add(new CreateInvoiceDTO(lstTransDTO, transInvoice.getInvoiceRequestId(), shopId));
                listTransDTOsToRemove.add(transInvoice);
            }
        });
        transInvoiceDTOs.removeAll(listTransDTOsToRemove);

        Map<Long, List<TransDTOs>> mapTransDTOs = transInvoiceDTOs.stream().collect(groupingBy(TransDTOs::getInvoiceRequestId));
        Set<Long> keys = mapTransDTOs.keySet();
        keys.forEach(key -> {
            List<TransDTO> lstTransDTO = new ArrayList<>();
            lstTransDTO.add(TransDTOMapper.mapTransDTOS2TransDTO(mapTransDTOs.get(key).get(0)));
            createInvoiceDTOS.add(new CreateInvoiceDTO(lstTransDTO, mapTransDTOs.get(key).get(0).getInvoiceRequestId(), shopId));
        });

        return createInvoiceDTOS;
    }

    private List<CreateInvoiceDTO> groupingTrans(List<TransDTOs> transDTOs, Long shopId) {
        List<CreateInvoiceDTO> createInvoiceDTOS = new ArrayList<>();
        Map<String, List<TransDTOs>> groupingTransDetail2 = transDTOs.stream()
                .filter(t -> (t.getTransTypeDetail() == 2L))
                .collect(groupingBy(this::getGroupByKey, Collectors.mapping((TransDTOs p) -> p, toList())));

        if (groupingTransDetail2.size() > 0L) {
            Set<String> keys = groupingTransDetail2.keySet();
            keys.forEach(key -> {
                long maxInvoiceRequestIdST = saleTransRepository.getMaxInvoiceRequestId() == null ? 0L : saleTransRepository.getMaxInvoiceRequestId();
                long maxInvoiceRequestIdSTG = saleTransGeneralRepository.getMaxInvoiceRequestId() == null ? 0L : saleTransGeneralRepository.getMaxInvoiceRequestId();

                List<TransDTO> lstTransDTO = new ArrayList<>();
                List<TransDTOs> transDTOsList = groupingTransDetail2.get(key);
                transDTOsList.forEach(transDTO -> {
                    lstTransDTO.add(TransDTOMapper.mapTransDTOS2TransDTO(transDTO));
                    if (transDTO.getTransType() == 1L) {
                        transDTO.setInvoiceRequestId(maxInvoiceRequestIdST + 1L);
                    }
                    if (transDTO.getTransType() == 2L) {
                        transDTO.setInvoiceRequestId(maxInvoiceRequestIdSTG + 1L);
                    }
                    customRepository.updateInvoiceRequestId(transDTO);
                    log.info("Insert(Update) invoice request id [" + transDTO + "]");
                });
                createInvoiceDTOS.add(new CreateInvoiceDTO(lstTransDTO, transDTOsList.get(0).getInvoiceRequestId(), shopId));
                transDTOsList.forEach(transDTOs::remove);
            });
        }
        Map<String, List<TransDTOs>> groupingTrans = transDTOs.stream()
                .filter(g -> (g.getTransTypeDetail() != 41L && g.getTransType() == 1L)
                        || (g.getTransTypeDetail() != 7L && g.getTransType() == 2L))
                .collect(groupingBy(this::getGroupByKey, Collectors.mapping((TransDTOs p) -> p, toList())));
        if (groupingTrans.size() > 0L) {
            Set<String> keys = groupingTrans.keySet();
            keys.forEach(key -> {
                long maxInvoiceRequestIdST = saleTransRepository.getMaxInvoiceRequestId() == null ? 0L : saleTransRepository.getMaxInvoiceRequestId();
                long maxInvoiceRequestIdSTG = saleTransGeneralRepository.getMaxInvoiceRequestId() == null ? 0L : saleTransGeneralRepository.getMaxInvoiceRequestId();

                List<TransDTO> lstTransDTO = new ArrayList<>();
                List<TransDTOs> transDTOsList = groupingTrans.get(key);
                transDTOsList.forEach(transDTO -> {
                    lstTransDTO.add(TransDTOMapper.mapTransDTOS2TransDTO(transDTO));
                    if (transDTO.getTransType() == 1L) {
                        transDTO.setInvoiceRequestId(maxInvoiceRequestIdST + 1L);
                    }
                    if (transDTO.getTransType() == 2L) {
                        transDTO.setInvoiceRequestId(maxInvoiceRequestIdSTG + 1L);
                    }
                    customRepository.updateInvoiceRequestId(transDTO);
                    log.info("Insert(Update) invoice request id [" + transDTO + "]");
                });
                createInvoiceDTOS.add(new CreateInvoiceDTO(lstTransDTO, transDTOsList.get(0).getInvoiceRequestId(), shopId));
                transDTOsList.forEach(transDTOs::remove);
            });
        }
        transDTOs.forEach(transDTO -> {
            List<TransDTO> lstTransDTO = new ArrayList<>();
            lstTransDTO.add(TransDTOMapper.mapTransDTOS2TransDTO(transDTO));
            createInvoiceDTOS.add(new CreateInvoiceDTO(lstTransDTO, null, shopId));
        });

        return createInvoiceDTOS;
    }

    private String getGroupByKey(TransDTOs transDTOs) {
        return transDTOs.getTransDate() + "-" + transDTOs.getReceiverId()
                + "-" + transDTOs.getCustId() + "-" + transDTOs.getCustName()
                + "-" + transDTOs.getTin() + "-" + transDTOs.getCurrency()
                + "-" + transDTOs.getReceiverType();
    }
}
