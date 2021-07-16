package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT s from Shop s where s.shopId = :shopId")
    Shop getShopByShopId(@Param("shopId") Long shopId);
}
