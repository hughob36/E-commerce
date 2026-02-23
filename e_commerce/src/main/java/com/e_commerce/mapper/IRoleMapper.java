package com.e_commerce.mapper;

import com.e_commerce.dto.RoleRequestDTO;
import com.e_commerce.dto.RoleResponseDTO;
import com.e_commerce.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRoleMapper {

    public List<RoleResponseDTO> toRoleResponseDTOList(List<Role> roleList);

    public RoleResponseDTO toRoleResponseDTO(Role role);

    public Role toRole(RoleRequestDTO roleRequestDTO);

    // Este método toma un DTO y una entidad existente,
    // y actualiza la entidad existente con los datos del DTO
    public void updateRoleFromDTO(RoleRequestDTO roleRequestDTO,@MappingTarget Role role);
}
