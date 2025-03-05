package com.myProject.OpenBoard.dao;

import com.myProject.OpenBoard.entity.Role;

public interface RoleDAO {
    Role findRoleByName(String roleName);
    Role updateRole(Role role);
}
