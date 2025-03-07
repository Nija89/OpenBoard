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

    List<User> findAllMemberUser();

    List<User> findAllModeratorUser();

    List<User> findDisabledUser();

    void promoteUserById(int id);

    void deleteUserById(int id);

    void disableUserById(int id);

    void enableUserById(int id);

    void demoteUserById(int id);

}
