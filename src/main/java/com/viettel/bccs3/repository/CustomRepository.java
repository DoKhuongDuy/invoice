package com.viettel.bccs3.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.bccs3.common.Constant;
import com.viettel.bccs3.domain.dto.*;
import com.viettel.bccs3.domain.model.*;
import com.viettel.bccs3.service.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    public InvoiceError existInvoiceError(ShopInvoiceListDTO shopInvoiceListDTO, LocalDateTime startOfDay, Long pkType) {
        String sql = "  select * from invoice_error where pk_id = ?1 " +
                "   and shop_used_id = ?2 " +
                "   and create_date >= ?3 " +
                "   and error_code = ?4 " +
                "   and pk_type = ?5";
        var query = entityManager.createNativeQuery(sql, InvoiceError.class);
        query.setParameter(1, shopInvoiceListDTO.getShopId());
        query.setParameter(2, shopInvoiceListDTO.getShopUsedId());
        query.setParameter(3, startOfDay);
        query.setParameter(4, Constant.NOT_CREATED_INVOICE);
        query.setParameter(5, pkType);

        var resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return (InvoiceError) resultList.get(0);
    }

    public List<SaleTrans> getTransNotInvoiced(Long shopUsedId, LocalDateTime startOfDay) {
        long value = getOptionSetValue();

        String sql = "select st.* from SALE_TRANS st, SHOP sh" +
                "   where 1 = 1 and st.status = 2" +
                "   and st.sale_trans_date >= ?1 - interval ?2 day" +
                "   and st.sale_trans_date < ?1" +
                "   and st.invoice_used_id is null" +
                "   and st.shop_id = sh.shop_id" +
                "   and exists ( select 1 from shop_invoice_list bil" +
                "   where bil.shop_id = sh.shop_id and bil.status = 1 and bil.shop_used_id = ?3" +
                "   and st.sale_trans_type not in ( select osv.VALUE from OPTION_SET_VALUE osv, " +
                "   OPTION_SET os where osv.option_set_id = os.id" +
                "   and os.code = ?4 and osv.name = ?5" +
                "   and os.status = 1 and osv.status = 1)" +
                "   and (st.sale_trans_type <> 41 or ( st.sale_trans_type = 41 and (" +
                "   select st1.sale_trans_type from SALE_TRANS st1" +
                "   where st1.sale_trans_id = st.from_sale_trans_id) not in ( " +
                "   select osv.VALUE from OPTION_SET_VALUE osv, OPTION_SET os" +
                "   where osv.option_set_id = os.id" +
                "   and os.code = ?6" +
                "   and osv.name = ?7" +
                "   and os.status = 1" +
                "   and osv.status = 1)))) limit 1 ";

        var query = entityManager.createNativeQuery(sql, SaleTrans.class);
        query.setParameter(1, startOfDay);
        query.setParameter(2, value);
        query.setParameter(3, shopUsedId);
        query.setParameter(4, Constant.SALE_TRANS_TYPE_NOT_INVOICE);
        query.setParameter(5, Constant.SALE_TRANS);
        query.setParameter(6, Constant.SALE_TRANS_TYPE_NOT_INVOICE);
        query.setParameter(7, Constant.SALE_TRANS);

        return query.getResultList();
    }

    @Transactional
    public void insertInvoiceProcess(InvoiceProcess invoiceProcess) {
        var sql = "insert into invoice_process(shop_used_id, create_date, last_modify, status) values (?1, ?2, ?3, ?4)";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, invoiceProcess.getShopUsedId());
        query.setParameter(2, invoiceProcess.getCreateDate());
        query.setParameter(3, invoiceProcess.getLastModify());
        query.setParameter(4, invoiceProcess.getStatus());
        query.executeUpdate();
    }

    @Transactional
    public void updateInvoiceProcess(InvoiceProcess invoiceProcess, LocalDateTime startOfDay) {
        var sql = "update invoice_process set status = 1, last_modify = ?1" +
                "   where shop_used_id = ?2 and create_date >= ?3";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, invoiceProcess.getLastModify());
        query.setParameter(2, invoiceProcess.getShopUsedId());
        query.setParameter(3, startOfDay);
        query.executeUpdate();
    }

    @Transactional
    public void insertInvoiceError(InvoiceError invoiceError) {
        var sql = "insert into invoice_error(pk_id, create_date, shop_used_id, " +
                "error_code, description, data_json, pk_type, retry) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, invoiceError.getPkId());
        query.setParameter(2, invoiceError.getCreateDate());
        query.setParameter(3, invoiceError.getShopUsedId());
        query.setParameter(4, invoiceError.getErrorCode());
        query.setParameter(5, invoiceError.getDescription());
        query.setParameter(6, invoiceError.getDataJson());
        query.setParameter(7, invoiceError.getPkType());
        query.setParameter(8, invoiceError.getRetry());
        query.executeUpdate();
    }

    @Transactional
    public void updateInvoiceError(Long shopId, Long shopUsedId, LocalDateTime sysDate, LocalDateTime startOfDay, Long pkType) {
        var sql = " update invoice_error set retry = retry + 1, " +
                " last_modify = ?6 where pk_id = ?1 " +
                " and shop_used_id = ?2 and create_date >= ?3 " +
                " and error_code = ?4 and pk_type = ?5 ";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, shopId);
        query.setParameter(2, shopUsedId);
        query.setParameter(3, startOfDay);
        query.setParameter(4, Constant.NOT_CREATED_INVOICE);
        query.setParameter(5, pkType);
        query.setParameter(6, sysDate);
        query.executeUpdate();
    }

    @Transactional
    public void updateMultiInvoiceError(InvoiceError invoiceError, Long invoiceRequestId, Long shopId,
                                        LocalDateTime sysDate, LocalDateTime startOfDay) {
        String sql = "update invoice_error" +
                " set last_modify = ?1, " +
                " pk_type = ?2, " +
                " error_code = ?3, " +
                " description = ?4, " +
                " data_json = ?5, " +
                " invoice_request_id = ?6 " +
                " where pk_id = ?7" +
                " and create_date >= ?8";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, sysDate);
        query.setParameter(2, invoiceError.getPkType());
        query.setParameter(3, invoiceError.getErrorCode());
        query.setParameter(4, invoiceError.getDescription());
        query.setParameter(5, invoiceError.getDataJson());
        query.setParameter(6, invoiceRequestId);
        query.setParameter(7, shopId);
        query.setParameter(8, startOfDay);

        query.executeUpdate();

    }

    public List<SaleTransGeneral> getSaleTransGeneralToCheck(Long shopUsedId) {
        long optionSetValue = getOptionSetValue();

        String sql = "select stg.* from SALE_TRANS_general stg, SHOP sh" +
                "   where 1 = 1 and stg.invoice_status <> 1" +
                "   and stg.create_date>= ?1 - interval ?2 day" +
                "   and stg.create_date < ?3" +
                "   and stg.invoice_used_id is null and stg.shop_code = sh.shop_code" +
                "   and exists (select 1 from shop_invoice_list bil" +
                "   where bil.shop_id = sh.shop_id and bil.status = 1" +
                "   and bil.shop_used_id = ?4" +
                "   and stg.type not in (select osv.VALUE from OPTION_SET_VALUE osv," +
                "   OPTION_SET os where osv.option_set_id = os.id" +
                "   and os.code = ?5" +
                "   and os.status = 1 and osv.status = 1 and osv.name = ?6)" +
                "   and (stg.type <> 7 or (stg.type = 7 and (select stg1.type from SALE_TRANS_general stg1" +
                "   where stg1.sale_trans_general_id = stg.from_sale_general_id) " +
                "   not in (select osv.VALUE from OPTION_SET_VALUE osv," +
                "   OPTION_SET os where osv.option_set_id = os.id" +
                "   and os.code = ?7 and os.status = 1" +
                "   and osv.status = 1 and osv.name = ?8))))" +
                "   limit 1 ";

        var query = entityManager.createNativeQuery(sql, SaleTransGeneral.class);
        query.setParameter(1, new Date());
        query.setParameter(2, optionSetValue);
        query.setParameter(3, new Date());
        query.setParameter(4, shopUsedId);
        query.setParameter(5, Constant.SALE_TRANS_TYPE_NOT_INVOICE);
        query.setParameter(6, Constant.SALE_TRANS_GENERAL);
        query.setParameter(7, Constant.SALE_TRANS_TYPE_NOT_INVOICE);
        query.setParameter(8, Constant.SALE_TRANS_GENERAL);


        return query.getResultList();
    }

    public List<SaleTransDTO> getSaleTrans(Long shopId, LocalDateTime startOfDay) {
        String sql = "select * " +
                "   from sale_Trans st " +
                "   where st.check_create_invoice = 1 " +
                "   and st.status = 2" +
                "   and st.sale_trans_date < :current_date" +
                "   and st.invoice_used_id is null" +
                "   and st.sale_trans_type not in ( select osv.VALUE" +
                "   from" +
                "   OPTION_SET_VALUE osv," +
                "   OPTION_SET os" +
                "   where osv.option_set_id = os.id" +
                "   and os.code = :saleTransNotInvoice " +
                "   and os.status = 1" +
                "   and osv.status = 1" +
                "   and osv.name = :saleTransGeneral)" +
                "   and st.shop_id = :shopId";

        var query = entityManager.createNativeQuery(sql, SaleTrans.class);
        query.setParameter("current_date", startOfDay);
        query.setParameter("saleTransNotInvoice", Constant.SALE_TRANS_TYPE_NOT_INVOICE);
        query.setParameter("saleTransGeneral", Constant.SALE_TRANS_GENERAL);
        query.setParameter("shopId", shopId);

        List<SaleTrans> resultList = query.getResultList();
        return resultList.stream().map(SaleTransDTOMapper::mapSaleTrans2DTO).collect(Collectors.toList());
    }

    public List<SaleTransGeneralDTO> getSaleTransGeneral(Long shopId, LocalDateTime startOfDay) {
        String sql = "select *" +
                "   from sale_Trans_general stg " +
                "   where stg.check_create_invoice = 1" +
                "   and stg.invoice_status <> 1" +
                "   and stg.create_date < :current_date" +
                "   and stg.invoice_used_id is null" +
                "   and stg.type not in ( select osv.VALUE" +
                "   from OPTION_SET_VALUE osv," +
                "   OPTION_SET os" +
                "   where osv.option_set_id = os.id" +
                "   and os.code = :saleTransNotInvoice" +
                "   and os.status = 1 " +
                "   and osv.status = 1 " +
                "   and osv.name = :saleTransGeneral)" +
                "   and ((stg.owner_type= 2 and stg.shop_code = :shopId) OR (stg.owner_type= 1 and stg.owner_id = :shopId))";

        var query = entityManager.createNativeQuery(sql, SaleTransGeneral.class);
        query.setParameter("current_date", startOfDay);
        query.setParameter("saleTransNotInvoice", Constant.SALE_TRANS_TYPE_NOT_INVOICE);
        query.setParameter("saleTransGeneral", Constant.SALE_TRANS_GENERAL);
        query.setParameter("shopId", shopId);

        List<SaleTransGeneral> resultList = query.getResultList();
        return resultList.stream().map(SaleTransGeneralDTOMapper::mapSaleTransGeneral2DTO).collect(Collectors.toList());
    }

    @Transactional
    public void updateInvoiceRequestId(TransDTOs transDTOs) {
        String sql = null;
        if (transDTOs.getTransType() == 1L) {
            sql = "update sale_trans st set st.invoice_request_id = ?1 where st.SALE_TRANS_ID = ?2 ";
        }
        if (transDTOs.getTransType() == 2L) {
            sql = "update sale_trans_general stg set stg.invoice_request_id = ?1 where stg.SALE_TRANS_ID = ?2 ";
        }
        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, transDTOs.getInvoiceRequestId());
        query.setParameter(2, transDTOs.getTransId());
        query.executeUpdate();
    }

    public List<TransInvoiceDTO> getTransactionFromSaleTrans(List<TransDTO> transDTOS) {
        List<Long> transIds = transDTOS.stream().map(TransDTO::getTransId).collect(Collectors.toList());
        String sql = "select * " +
                " from sale_Trans st " +
                " where st.sale_trans_id in (?1)";

        var query = entityManager.createNativeQuery(sql, SaleTrans.class);
        query.setParameter(1, transIds);

        List<SaleTrans> resultList = query.getResultList();
        return resultList.stream().map(TransInvoiceDTOMapper::mapFromSaleTrans).collect(Collectors.toList());
    }

    public InvoiceRootTransactionDTO getInvoiceRootFromSaleTrans(Long fromTransId) {
        String sql = "select iu.invoice_no, " +
                " iu.invoice_datetime, " +
                " st.invoice_used_id " +
                " from sale_trans st, invoice_used iu " +
                " where st.invoice_used_id = iu.invoice_used_id " +
                " and st.sale_trans_id = ?1 " +
                " and st.status = 3";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, fromTransId);

        List<Object[]> resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        Object[] item = resultList.get(0);
        return InvoiceRootTransactionDTOMapper.mapFromObject(item);
    }

    public InvoiceRootTransactionDTO getInvoiceRootFromSaleTransGeneral(Long fromTransId) {
        String sql = "select iu.invoice_no, " +
                " iu.invoice_datetime, " +
                " st.invoice_used_id" +
                " from sale_trans_general st, invoice_used iu" +
                " where st.invoice_used_id = iu.invoice_used_id and st.sale_trans_general_id = ?1 " +
                " and st.invoice_status = 1 ";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, fromTransId);

        List<Object[]> resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        Object[] item = resultList.get(0);
        return InvoiceRootTransactionDTOMapper.mapFromObject(item);
    }

    public List<SaleTransDetail> getSaleTransDetail(List<Long> transIds) {
        String sql = "SELECT * " +
                " FROM" +
                " SALE_TRANS_DETAIL std" +
                " WHERE" +
                " 1 = 1" +
                " AND std.sale_trans_id in ?1"; //(:saleTransInvoiceDTOs.tranId)

        var query = entityManager.createNativeQuery(sql, SaleTransDetail.class);
        query.setParameter(1, transIds);

        return query.getResultList();
    }

    public Double getExtValue(Long saleTransDetailId) {
        String sql = "SELECT stx.ext_value " +
                " FROM SALE_TRANS_DETAIL_EXT stx " +
                " WHERE stx.sale_trans_detail_id = ?1 " +
                " AND ext_key = 'VAT_TYPE' ";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, saleTransDetailId);

        var resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return ((Number) resultList.get(0)).doubleValue();
    }

    public String getSystemVAT() {
        String sql = "select a.value " +
                " from OPTION_SET_VALUE a, " +
                "     OPTION_SET b " +
                " where a.option_set_id = b.id " +
                "  and b.code = 'VAT_DEFAULT' " +
                "  and a.status = 1 " +
                "  and b.status = 1 ";

        var query = entityManager.createNativeQuery(sql);
        var resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return (String) resultList.get(0);
    }

    public String getSystemCurrency() {
        String sql = "select a.value " +
                " from OPTION_SET_VALUE a, " +
                "     OPTION_SET b " +
                " where a.option_set_id = b.id " +
                "  and b.code = 'CURRENCY_DEFAULT' " +
                "  and a.status = 1 " +
                "  and b.status = 1 ";

        var query = entityManager.createNativeQuery(sql);

        var resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return (String) resultList.get(0);
    }

    public List<TransInvoiceDTO> getListTransaction(String vatDefault, String currencyDefault, List<Long> transIds) {
        String sql = "select stg.sale_Trans_general_id trans_id, " +
                " 2 trans_type, " +
                " stg.create_date trans_date, " +
                " stg.type trans_type_detail, " +
                " stg.invoice_status status, " +
                " stg.invoice_used_id, " +
                " stg.invoice_datetime invoice_create_date, " +
                " (select sh.shop_id from shop sh where sh.shop_code = stg.shop_code limit 1 ) shop_id, " +
                " (case when stg.owner_id = 1 then null else (select sf.staff_id from staff sf " +
                " where sf.staff_code = stg.staff_code limit 1) end ) staff_id, " +
                " stg.pay_method, " +
                " CAST(null as char) discount, " +
                " stg.amount amount_Tax, " +
                " (case when stg.amount_not_tax is not null then stg.amount_not_tax else (stg.amount / (1 + (?1/100))) end) amount_not_tax, " +
                " (case when stg.vat is not null then stg.vat else ?1 end) vat, " +
                " (case when  stg.vat_amount is not null then stg.vat_amount else (stg.amount - (stg.amount/(1+(?1/100)))) end) tax, " +
                " stg.isdn, " +
                " stg.cust_name, " +
                " CAST(null as char) receiver_id, " +
                " stg.contract_no, " +
                " CAST(null as char) tel_number, " +
                " stg.company, " +
                " stg.address, " +
                " CAST(null as char) tin, " +
                " stg.sale_trans_general_code trans_code, " +
                " CAST(null as char) cust_id, " +
                " ?2 currency , " +
                " stg.from_sale_general_id from_trans_id, " +
                " stg.num_month " +
                " from sale_Trans_general stg where stg.sale_trans_general_id in (?3)";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, vatDefault);
        query.setParameter(2, currencyDefault);
        query.setParameter(3, transIds);

        List<Object[]> objects = query.getResultList();

        List<TransInvoiceDTO> listTransInvoiceDTO = new ArrayList<>();

        for (Object[] item : objects) {
            listTransInvoiceDTO.add(TransInvoiceDTOMapper.mapFromObject(item));
        }

        return listTransInvoiceDTO;
    }

    public List<Long> getTransNotInvoice(String type) {
        String sql = "select " +
                " osv.VALUE " +
                " from" +
                " OPTION_SET_VALUE osv, " +
                " OPTION_SET os " +
                " where osv.option_set_id = os.id " +
                " and os.code = 'SALE_TRANS_TYPE_NOT_INVOICE' " +
                " and osv.name = ?1 " +
                " and os.status = 1 " +
                " and osv.status = 1 ";
        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, type);

        return query.getResultList();
    }

    public Long getByShopId(Long shopId) {
        String sql = "SELECT shop_id" +
                " FROM  SHOP " +
                " WHERE 1 = 1 " +
                " AND shop_id = ?1 " +
                " AND channel_type_id IN (SELECT channel_type_id " +
                " FROM CHANNEL_TYPE " +
                " WHERE is_vt_unit = 2 AND object_type = 1) ";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, shopId);

        var resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return ((Number) resultList.get(0)).longValue();
    }

    public Long getStaffId(Long staffId) {
        String sql = "SELECT staff_id " +
                " FROM   staff " +
                " WHERE 1 = 1 " +
                " AND staff_id = ?1 " +
                " AND channel_type_id IN (SELECT   channel_type_id " +
                " FROM CHANNEL_TYPE " +
                " WHERE   is_vt_unit = 2 AND object_type = 2) ";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, staffId);

        var resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return ((Number) resultList.get(0)).longValue();
    }

    public InvoiceListDTO getInvoiceList(Long invoiceType, Long shopInvoiceListInvoiceType, Long shopUsedId) {
        String sql = " SELECT A.INVOICE_LIST_ID, " +
                "       C.INVOICE_TYPE_ID, " +
                "       C.INVOICE_FORM, " +
                "       A.INVOICE_SERIAL_ID, " +
                "       A.SERIAL_NO, " +
                "       C.FORM_NO, " +
                "       A.FROM_INVOICE, " +
                "       A.TO_INVOICE, " +
                "       A.CURR_INVOICE, " +
                "       (CASE " +
                "            WHEN D.NAME = B.SHOP_USED_ID THEN D.VALUE " +
                "            ELSE B.SHOP_USED_ID " +
                "           END) SHOP_USED_ID, " +
                "       A.STAFF_ID, " +
                "       A.STATUS, " +
                "       A.CREATE_USER, " +
                "       DATE_FORMAT(A.CREATE_DATETIME, '%d/%m/%Y') CREATE_DATETIME, " +
                "       C.INVOICE_NO_LENGTH, " +
                "       B.INVOICE_TRANS " +
                " FROM INVOICE_SERIAL B " +
                "         left JOIN ( " +
                "    SELECT X.* " +
                "    FROM OPTION_SET_VALUE X, " +
                "         OPTION_SET Y " +
                "    WHERE X.OPTION_SET_ID = Y.ID " +
                "      AND Y.CODE = 'DISPLAY_INVOICE_INFORMATION_BY_SHOP' " +
                "      AND X.STATUS = 1 " +
                "      AND Y.STATUS = 1 ) D ON " +
                "    B.SHOP_USED_ID = D.NAME " +
                "         inner JOIN INVOICE_TYPE C ON " +
                "    C.INVOICE_TYPE_ID = B.INVOICE_TYPE_ID " +
                "         inner JOIN INVOICE_LIST A ON " +
                "    A.INVOICE_SERIAL_ID = B.INVOICE_SERIAL_ID " +
                " WHERE 1 = 1 " +
                "  AND A.STATUS = 1 " +
                "  AND B.STATUS = 1 " +
                "  AND C.INVOICE_TYPE = ?1 " +
                "  and c.INVOICE_FORM = ?2 " +
                "  AND C.STATUS = 1 " +
                "  AND A.CURR_INVOICE < A.TO_INVOICE " +
                "  AND C.FORM_NO is not null " +
                "  AND A.SHOP_ID = ?3 " +
                "  AND B.INVOICE_TRANS is null " +
                " ORDER BY B.SERIAL_NO, " +
                "         A.FROM_INVOICE DESC";

        var query = entityManager.createNativeQuery(sql);
        query.setParameter(1, invoiceType);
        query.setParameter(2, shopInvoiceListInvoiceType);
        query.setParameter(3, shopUsedId);

        List<Object[]> resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        Object[] item = resultList.get(0);

        return InvoiceListDTOMapper.mapFromObject(item);

    }

    public List<InvoiceList> lockInvoiceList(Long invoiceListIds) {
        String sql = "select * " +
                " from INVOICE_LIST " +
                " where invoice_list_id = ?1";

        var query = entityManager.createNativeQuery(sql, InvoiceList.class);
        query.setParameter(1, invoiceListIds);

        return query.getResultList();
    }

    private Long getOptionSetValue() {
        String sql = "select a.value from OPTION_SET_VALUE a, OPTION_SET b" +
                "   where a.option_set_id = b.id and b.code = 'CREATE_INVOICE_AFTER_DATE' and a.status = 1" +
                "   and b.status = 1";
        var query = entityManager.createNativeQuery(sql);
        var result = query.setMaxResults(1).getResultList();
        if (result.isEmpty()) {
            return 30L;
        } else {
            return ((Number) result.get(0)).longValue();
        }
    }

}
