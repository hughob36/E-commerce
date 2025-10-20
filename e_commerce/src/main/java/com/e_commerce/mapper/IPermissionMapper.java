package com.e_commerce.mapper;


import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {


    public PermissionResponseDTO toPermissionResponseDTO(Permission permission);

    public Permission toPermission(PermissionRequestDTO permissionRequestDTO);

    public List<PermissionResponseDTO> toPermissionResponseDTOList(List<Permission> permissionList);

    // Este método toma un DTO y una entidad existente,
    // y actualiza la entidad existente con los datos del DTO.
    public void updatePermissionFromDto(PermissionRequestDTO dto, @MappingTarget Permission permission);
}
