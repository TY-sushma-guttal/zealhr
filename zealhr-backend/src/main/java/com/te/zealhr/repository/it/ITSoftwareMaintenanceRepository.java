package com.te.zealhr.repository.it;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.zealhr.entity.it.PcLaptopSoftwareDetails;

@Repository
public interface ITSoftwareMaintenanceRepository extends JpaRepository<PcLaptopSoftwareDetails, Long> {

}
