package com.myProject.OpenBoard.service;

import com.myProject.OpenBoard.dao.PostDAO;
import com.myProject.OpenBoard.dao.RoleDAO;
import com.myProject.OpenBoard.dao.UserDAO;
import com.myProject.OpenBoard.entity.Post;
import com.myProject.OpenBoard.entity.Role;
import com.myProject.OpenBoard.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    private final RoleDAO roleDAO;
    private final UserDAO userDAO;
    private final PostDAO postDAO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MainServiceImpl(RoleDAO roleDAO, UserDAO userDAO, PostDAO postDAO) {
        this.roleDAO = roleDAO;
        this.userDAO = userDAO;
        this.postDAO = postDAO;
        this.bCryptPasswordEncoder= new BCryptPasswordEncoder();
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleDAO.findRoleByName(roleName);
    }

    @Override
    @Transactional
    public void createUser(User updatedUser) {
        updatedUser.setPassword("{bcrypt}" + bCryptPasswordEncoder.encode(updatedUser.getPassword()));

        updatedUser.addRole(roleDAO.findRoleByName("ROLE_MEMBER"));

        userDAO.createUser(updatedUser);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userDAO.findUserByName(username);

        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());
        currentUser.setDescription(user.getDescription());
        currentUser.setRoleList(user.getRoleList());

        userDAO.updateUser(user);
    }

    @Override
    @Transactional
    public boolean updatePassword(User user, String currentPassword, String newPassword) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User currentUser = userDAO.findUserByName(username);

        String currentUserPassword = currentUser.getPassword();
        if (currentUserPassword.startsWith("{bcrypt}")) {
            currentUserPassword = currentUserPassword.substring(8);
        }


        if (bCryptPasswordEncoder.matches(currentPassword, currentUserPassword)) {
            String encodedNewPassword = "{bcrypt}" + bCryptPasswordEncoder.encode(newPassword);
            currentUser.setPassword(encodedNewPassword);  // Set the new encoded password
            userDAO.updatePassword(currentUser);
            return true;
        }

        System.out.println("False");
        return false;
    }


    @Override
    public User findUserById(int id) {
        return userDAO.findUserById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userDAO.findUserByName(name);
    }

    @Override
    @Transactional
    public void createPost(Post post) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userDAO.findUserByName(username);

        post.setUser(user);
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        postDAO.createPost(post);
    }
    @Override
    @Transactional
    public void updatePost(Post post) {
        Post currentPost = postDAO.findPostById(post.getId());

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userDAO.findUserByName(username);

        currentPost.setTitle(post.getTitle());
        currentPost.setDescription(post.getDescription());
        currentPost.setShowPost(post.getShowPost());

        currentPost.setUser(user);

        currentPost.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        postDAO.updatePost(currentPost);
    }

    @Override
    @Transactional
    public void deletePostById(int id) {
        postDAO.deleteById(id);
    }

    @Override
    public List<Post> findAllUserPost() {
        return userDAO.findAllUserPost();
    }

    @Override
    public List<Post> findPublicPost() {
        return postDAO.findPublicPost();
    }

    @Override
    public Post findPostById(int id) {
        return postDAO.findPostById(id);
    }




}
