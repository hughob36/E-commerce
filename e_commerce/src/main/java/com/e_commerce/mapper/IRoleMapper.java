package com.e_commerce.mapper;

import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.model.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRoleMapper {

    public List<RoleResponseDTO> toRoleResponseDTOList(List<Role> roleList);

    public RoleResponseDTO toRoleResponseDTO(Role role);
}
