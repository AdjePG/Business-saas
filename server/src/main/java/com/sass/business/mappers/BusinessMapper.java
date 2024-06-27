package com.sass.business.mappers;

import com.sass.business.dtos.business.BusinessDTO;
import com.sass.business.models.Business;
import com.sass.business.models.User;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {

    public BusinessDTO toDto(Business business) {
        BusinessDTO dto = new BusinessDTO();
        //dto.setUuid(business.getUuid());
        dto.setName(business.getName());
        dto.setImagePath(business.getImagePath());
        dto.setUuidUser(business.getUser().getUuid());
        return dto;
    }

    public Business toModal(BusinessDTO dto, User user) {
        Business business = new Business();
        //business.setUuid(dto.getUuid());
        business.setImagePath(dto.getImagePath());
        business.setName(dto.getName());
        business.setUser(user);
        return business;
    }
}
