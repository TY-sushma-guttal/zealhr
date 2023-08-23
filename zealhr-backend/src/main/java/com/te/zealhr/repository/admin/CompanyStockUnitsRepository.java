package com.te.zealhr.repository.admin;
/**
 * @author Tapas
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.admin.CompanyStockCategories;
import com.te.zealhr.entity.admin.CompanyStockUnits;

public interface CompanyStockUnitsRepository extends JpaRepository<CompanyStockUnits, Long> {

}
