package cn.itcast.store.service;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserService {
    public int register(User user) throws SQLException;
    public boolean active(String active) throws SQLException;
    public User login(User user) throws SQLException;
}
