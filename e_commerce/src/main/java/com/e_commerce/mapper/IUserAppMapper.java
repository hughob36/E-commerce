package com.e_commerce.mapper;

import com.e_commerce.dto.UserAppResponseDTO;
import com.e_commerce.model.UserApp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserAppMapper {

    public List<UserAppResponseDTO> toUserAppResponseDTOList(List<UserApp> userApp);
}
