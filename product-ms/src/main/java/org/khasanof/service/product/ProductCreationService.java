package org.khasanof.service.product;

import org.khasanof.service.dto.product.ProductCreationDTO;

/**
 * @author Nurislom
 * @see org.khasanof.service.product
 * @since 6/8/2024 11:17 AM
 */
public interface ProductCreationService {

    /**
     *
     * @param product
     */
    void creation(ProductCreationDTO product);
}
