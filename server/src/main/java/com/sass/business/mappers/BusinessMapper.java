package com.sass.business.mappers;

import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.models.business.Business;
import com.sass.business.models.User;
import com.sass.business.others.UuidConverterUtil;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {
    // region INJECTED DEPENDENCIES

    private UuidConverterUtil uuidConverterUtil;

    public BusinessMapper(
            UuidConverterUtil uuidConverterUtil
    ) {
        this.uuidConverterUtil = uuidConverterUtil;
    }

    // endregion

    public BusinessDTO toDto(Business business) {
        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setUuid(uuidConverterUtil.binaryToUuid(business.getUuid()));
        businessDTO.setName(business.getName());
        businessDTO.setDescription(business.getDescription());
        businessDTO.setImagePath(business.getImagePath());
        businessDTO.setUserUuid(uuidConverterUtil.binaryToUuid(business.getUser().getUuid()));

        return businessDTO;
    }

    public Business toModel(BusinessDTO dto, User user) {
        Business business = new Business();
        business.setName(dto.getName());
        business.setDescription(dto.getDescription());
        business.setImagePath(dto.getImagePath());
        business.setUser(user);

        return business;
    }

    public Business toModel(Business originalBusiness, BusinessDTO dto, User user) {
        originalBusiness.setName(dto.getName());
        originalBusiness.setDescription(dto.getDescription());
        originalBusiness.setImagePath(dto.getImagePath());
        originalBusiness.setUser(user);

        return originalBusiness;
    }
}
