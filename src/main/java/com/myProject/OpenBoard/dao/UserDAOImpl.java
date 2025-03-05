package com.myProject.OpenBoard.dao;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.User;
import jakarta.persistence.EntityManager;
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

}
