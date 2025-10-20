package com.e_commerce.mapper;


import com.e_commerce.dto.PermissionRequestDTO;
import com.e_commerce.dto.PermissionResponseDTO;
import com.e_commerce.model.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {


    public PermissionResponseDTO toPermissionResponseDTO(Permission permission);

    public Permission toPermission(PermissionRequestDTO permissionRequestDTO);

    public List<PermissionResponseDTO> toPermissionResponseDTOList(List<Permission> permissionList);

}
