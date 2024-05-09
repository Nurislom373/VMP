package org.khasanof.repository;

import org.khasanof.domain.Gift;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Gift entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GiftRepository extends JpaRepository<Gift, Long>, JpaSpecificationExecutor<Gift> {}
