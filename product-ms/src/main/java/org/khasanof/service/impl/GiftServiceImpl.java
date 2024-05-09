package org.khasanof.service.impl;

import java.util.Optional;
import org.khasanof.domain.Gift;
import org.khasanof.repository.GiftRepository;
import org.khasanof.service.GiftService;
import org.khasanof.service.dto.GiftDTO;
import org.khasanof.service.mapper.GiftMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.khasanof.domain.Gift}.
 */
@Service
@Transactional
public class GiftServiceImpl implements GiftService {

    private final Logger log = LoggerFactory.getLogger(GiftServiceImpl.class);

    private final GiftRepository giftRepository;

    private final GiftMapper giftMapper;

    public GiftServiceImpl(GiftRepository giftRepository, GiftMapper giftMapper) {
        this.giftRepository = giftRepository;
        this.giftMapper = giftMapper;
    }

    @Override
    public GiftDTO save(GiftDTO giftDTO) {
        log.debug("Request to save Gift : {}", giftDTO);
        Gift gift = giftMapper.toEntity(giftDTO);
        gift = giftRepository.save(gift);
        return giftMapper.toDto(gift);
    }

    @Override
    public GiftDTO update(GiftDTO giftDTO) {
        log.debug("Request to update Gift : {}", giftDTO);
        Gift gift = giftMapper.toEntity(giftDTO);
        gift = giftRepository.save(gift);
        return giftMapper.toDto(gift);
    }

    @Override
    public Optional<GiftDTO> partialUpdate(GiftDTO giftDTO) {
        log.debug("Request to partially update Gift : {}", giftDTO);

        return giftRepository
            .findById(giftDTO.getId())
            .map(existingGift -> {
                giftMapper.partialUpdate(existingGift, giftDTO);

                return existingGift;
            })
            .map(giftRepository::save)
            .map(giftMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GiftDTO> findOne(Long id) {
        log.debug("Request to get Gift : {}", id);
        return giftRepository.findById(id).map(giftMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gift : {}", id);
        giftRepository.deleteById(id);
    }
}
