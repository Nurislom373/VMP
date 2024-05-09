package org.khasanof.repository;

import org.khasanof.domain.OptionVariant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OptionVariant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionVariantRepository extends JpaRepository<OptionVariant, Long>, JpaSpecificationExecutor<OptionVariant> {}
