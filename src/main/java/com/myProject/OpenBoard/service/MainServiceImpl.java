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
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleDAO.findRoleByName(roleName);
    }

    @Override
    @Transactional
    public void createUser(User updatedUser) {
        updatedUser.setPassword("{bcrypt}" + bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        String username = updatedUser.getUsername();
        username = username.substring(0,1).toUpperCase() + username.substring(1);
        updatedUser.setUsername(username);
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
    @Transactional
    public void deletePublicPostById(int id) {
        postDAO.deletePublicPostById(id);
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
    @Transactional
    public void likePublicPost(int id) {
        String name = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = findUserByName(name);
        Post post = findPostById(id);
        int totalLikes = post.getLikes();

        if (post.getUserLikedSet().contains(user)) {
            post.getUserLikedSet().remove(user);
            user.getLikedPost().remove(post);
            totalLikes = Math.max(0, totalLikes - 1);
        } else {
            post.getUserLikedSet().add(user);
            user.getLikedPost().add(post);
            totalLikes += 1;
        }

        post.setLikes(totalLikes);

        postDAO.likePublicPost(post);
    }

    @Override
    @Transactional
    public void disLikePublicPost(int id) {
        String name = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = findUserByName(name);
        Post post = findPostById(id);
        int totalDislikes = post.getDislikes();

        if (post.getUserDislikedSet().contains(user)) {
            post.getUserDislikedSet().remove(user);
            user.getDislikedPost().remove(post);
            totalDislikes = Math.max(0, totalDislikes - 1);
        } else {
            post.getUserDislikedSet().add(user);
            user.getDislikedPost().add(post);
            totalDislikes += 1;
        }

        post.setDislikes(totalDislikes);
        postDAO.disLikePublicPost(post);
    }

    @Override
    public List<Post> topTenPost() {
        return postDAO.topTenPost();
    }

    @Override
    public Post findPostById(int id) {
        return postDAO.findPostById(id);
    }

    @Override
    public List<User> findAllMemberUser() {
        return userDAO.findAllMemberUser();
    }


    @Override
    public List<User> findAllModeratorUser() {
        return userDAO.findAllModeratorUser();
    }

    @Override
    public List<User> findDisabledUser() {
        return userDAO.findDisabledUser();
    }

    @Override
    @Transactional
    public void promoteUserById(int id) {
        userDAO.promoteUserById(id);
    }

    @Override
    @Transactional
    public void demoteUserById(int id) {
        userDAO.demoteUserById(id);
    }

    @Override
    @Transactional
    public void disableUserById(int id) {
        userDAO.disableUserById(id);
    }

    @Override
    @Transactional
    public void enableUserById(int id){
        userDAO.enableUserById(id);
    }

    @Override
    @Transactional
    public void deleteUserById(int id){
        User user = findUserById(id);


        List<Post> postList = user.getPostList();
        for(Post x : postList){
            postDAO.deleteById(x.getId());
        }

        userDAO.deleteUserById(id);
    }
}
