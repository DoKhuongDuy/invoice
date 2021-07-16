package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.ShopInvoiceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopInvoiceListRepository extends JpaRepository<ShopInvoiceList, Long> {
    @Query("select sil from ShopInvoiceList sil where sil.status=:status and mod(sil.shopId,:mod) = :partition ")
    List<ShopInvoiceList> findAllByStatus(@Param("status") Long status, @Param("mod") Integer mod, @Param("partition") Integer partition);

    ShopInvoiceList findByStatusAndShopId(Long status, Long shopId);
}
