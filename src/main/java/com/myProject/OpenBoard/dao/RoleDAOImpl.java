package com.myProject.OpenBoard.dao;

import com.myProject.OpenBoard.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl implements RoleDAO{

    private EntityManager entityManager;
    public RoleDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Role findRoleByName(String roleName) {
        TypedQuery<Role> role = entityManager.createQuery("SELECT r FROM Role r WHERE r.role=:x ", Role.class);
        role.setParameter("x", roleName);
        return role.getSingleResult();
    }

    @Override
    public Role updateRole(Role role) {
        return entityManager.merge(role);
    }
}
