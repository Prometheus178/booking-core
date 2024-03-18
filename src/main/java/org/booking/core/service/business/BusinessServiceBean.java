package org.booking.core.service.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.booking.core.domain.entity.business.Business;
import org.booking.core.domain.request.BusinessRequest;
import org.booking.core.domain.response.BusinessResponse;
import org.booking.core.mapper.BusinessMapper;
import org.booking.core.repository.BusinessRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BusinessServiceBean implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    @Override
    public BusinessResponse create(BusinessRequest request) {
        Business business = businessMapper.dtoTo(request);
        Business saved = businessRepository.save(business);
        return businessMapper.toDto(business);
    }

    @Override
    public BusinessResponse update(Long id, BusinessRequest request) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        Business existed = optionalBusiness.orElseThrow(EntityNotFoundException::new);
        Business business = businessMapper.dtoTo(request);
            existed.setType(business.getType());
            existed.setDescription(business.getDescription());
            existed.setName(business.getName());
            existed.setAddress(business.getAddress());
            Business saved = businessRepository.save(existed);
            return businessMapper.toDto(saved);
    }

    @Override
    public boolean delete(Long id) {
        try {
            businessRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public BusinessResponse getById(Long id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        Business existed = optionalBusiness.orElseThrow(EntityNotFoundException::new);
            return businessMapper.toDto(optionalBusiness.get());
    }

}
