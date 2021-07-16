package com.viettel.bccs3.repository;

import com.viettel.bccs3.domain.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s from Staff s where s.staffId = :staffId")
    Staff getAllByStaffId(@Param("staffId") Long staffId);
}
