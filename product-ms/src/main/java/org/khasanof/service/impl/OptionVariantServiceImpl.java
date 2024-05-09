package org.khasanof.service.impl;

import java.util.Optional;
import org.khasanof.domain.OptionVariant;
import org.khasanof.repository.OptionVariantRepository;
import org.khasanof.service.OptionVariantService;
import org.khasanof.service.dto.OptionVariantDTO;
import org.khasanof.service.mapper.OptionVariantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.khasanof.domain.OptionVariant}.
 */
@Service
@Transactional
public class OptionVariantServiceImpl implements OptionVariantService {

    private final Logger log = LoggerFactory.getLogger(OptionVariantServiceImpl.class);

    private final OptionVariantRepository optionVariantRepository;

    private final OptionVariantMapper optionVariantMapper;

    public OptionVariantServiceImpl(OptionVariantRepository optionVariantRepository, OptionVariantMapper optionVariantMapper) {
        this.optionVariantRepository = optionVariantRepository;
        this.optionVariantMapper = optionVariantMapper;
    }

    @Override
    public OptionVariantDTO save(OptionVariantDTO optionVariantDTO) {
        log.debug("Request to save OptionVariant : {}", optionVariantDTO);
        OptionVariant optionVariant = optionVariantMapper.toEntity(optionVariantDTO);
        optionVariant = optionVariantRepository.save(optionVariant);
        return optionVariantMapper.toDto(optionVariant);
    }

    @Override
    public OptionVariantDTO update(OptionVariantDTO optionVariantDTO) {
        log.debug("Request to update OptionVariant : {}", optionVariantDTO);
        OptionVariant optionVariant = optionVariantMapper.toEntity(optionVariantDTO);
        optionVariant = optionVariantRepository.save(optionVariant);
        return optionVariantMapper.toDto(optionVariant);
    }

    @Override
    public Optional<OptionVariantDTO> partialUpdate(OptionVariantDTO optionVariantDTO) {
        log.debug("Request to partially update OptionVariant : {}", optionVariantDTO);

        return optionVariantRepository
            .findById(optionVariantDTO.getId())
            .map(existingOptionVariant -> {
                optionVariantMapper.partialUpdate(existingOptionVariant, optionVariantDTO);

                return existingOptionVariant;
            })
            .map(optionVariantRepository::save)
            .map(optionVariantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OptionVariantDTO> findOne(Long id) {
        log.debug("Request to get OptionVariant : {}", id);
        return optionVariantRepository.findById(id).map(optionVariantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OptionVariant : {}", id);
        optionVariantRepository.deleteById(id);
    }
}
