package com.phegon.FoodApp.role.services;

import com.phegon.FoodApp.response.Response;
import com.phegon.FoodApp.role.dtos.RoleDTO;

import java.util.List;

public interface RoleService {

    Response<RoleDTO> createRole(RoleDTO roleDTO); //Take a RoleDTO as input, create a role, and return a Response object that contains the created RoleDTO.
    //This method takes a RoleDTO, creates a role, and returns a Response whose data field contains a RoleDTO.

    Response<RoleDTO> updateRole(RoleDTO roleDTO);

    Response<List<RoleDTO>> getAllRoles();

    Response<?> deleteRole(Long id);


}
