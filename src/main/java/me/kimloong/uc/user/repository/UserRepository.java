package me.kimloong.uc.user.repository;

import me.kimloong.uc.user.model.User;

/**
 * @author KimLoong
 */
public interface UserRepository {

    User findOne(String username);

    User add(User user);
}
