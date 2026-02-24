package com.e_commerce.mapper;

import com.e_commerce.dto.UserAppRequestDTO;
import com.e_commerce.dto.UserAppResponseDTO;
import com.e_commerce.model.UserApp;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserAppMapper {

    public List<UserAppResponseDTO> toUserAppResponseDTOList(List<UserApp> userApp);
    public UserAppResponseDTO toUserAppResponseDTO(UserApp userApp);
    public UserApp toUserApp(UserAppRequestDTO userAppRequestDTO);
    // Este método toma un DTO y una entidad existente,
    // y actualiza la entidad existente con los datos del DTO
    public void updateUserAppFromDTO(UserAppRequestDTO userAppRequestDTO, @MappingTarget UserApp userApp);
}
