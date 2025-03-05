package com.myProject.OpenBoard.dao;

import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.User;

import java.util.List;


public interface UserDAO {

    void createUser(User user);

    void updateUser(User user);

    void updatePassword(User user);

    User findUserById(int id);

    User findUserByName(String name);

    List<Post> findAllUserPost();

}
