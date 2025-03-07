package com.myProject.OpenBoard.dao;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.Role;
import com.myProject.OpenBoard.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDAOImpl implements UserDAO{
    private EntityManager entityManager;
    public UserDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void createUser(User newUser) {
        entityManager.persist(newUser);
    }

    @Override
    public void updateUser(User user) {
        entityManager.clear();
        entityManager.merge(user);
    }

    @Override
    public void updatePassword(User user) {
        entityManager.merge(user);
    }

    @Override
    public User findUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findUserByName(String name) {
        TypedQuery<User> userTypedQuery = entityManager.createQuery(
          "FROM User WHERE username=:x", User.class
        );
        userTypedQuery.setParameter("x", name);
        return userTypedQuery.getSingleResult();
    }

    @Override
    public List<Post> findAllUserPost() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = findUserByName(username);
        if(user != null){
            return user.getPostList();
        }
        return null;
    }

    @Override
    public List<User> findAllMemberUser() {
        TypedQuery<User> userList = entityManager.createQuery(
                "SELECT u FROM User u " +
                        "JOIN u.roleList r " +
                        "WHERE r.role = 'ROLE_MEMBER' " +
                        "AND u.active = true", User.class);
        return userList.getResultList();
    }

    @Override
    public List<User> findAllModeratorUser() {
        TypedQuery<User> userList = entityManager.createQuery(
                "SELECT u FROM User u " +
                        "JOIN u.roleList r " +
                        "WHERE r.role = 'ROLE_MODERATOR' "+
                        "AND u.active = true", User.class);
        return userList.getResultList();
    }

    @Override
    public List<User> findDisabledUser(){
        TypedQuery<User> typedQuery = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.active=:x ", User.class).setParameter("x", false);
        return  typedQuery.getResultList();
    }


    @Override
    public void promoteUserById(int id) {
        User user = entityManager.find(User.class, id);
        List<Role> roleList = user.getRoleList();
        for(Role x: roleList){
            x.removeUser(user);
        }
        user.removeRole();
        user.addRole(entityManager.find(Role.class, 2));

        entityManager.merge(user);
    }

    @Override
    public void demoteUserById(int id) {
        User user = entityManager.find(User.class, id);
        List<Role> roleList = user.getRoleList();
        for(Role x : roleList){
            x.removeUser(user);
        }
        user.removeRole();
        user.addRole(entityManager.find(Role.class, 3));

        entityManager.merge(user);
    }

    @Override
    public void disableUserById(int id){
        User user = entityManager.find(User.class, id);
        user.setActive(false);
        entityManager.merge(user);
    }

    @Override
    public void enableUserById(int id){
        User user = entityManager.find(User.class, id);
        user.setActive(true);
        entityManager.merge(user);
    }


    @Override
    public void deleteUserById(int id){
        User user = findUserById(id);
        List<Role> roleList = user.getRoleList();
        for(Role x : roleList){
            x.removeUser(user);
        }
        int deleteUser = entityManager.createQuery("DELETE FROM User WHERE id=:x")
                .setParameter("x", id).executeUpdate();

    }


}
