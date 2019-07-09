package cn.itcast.store.dao;

import cn.itcast.store.domain.User;

import java.sql.SQLException;

public interface UserDao {
    public int register(User user) throws SQLException;
    public User active(String code) throws SQLException;
    public void update(User user) throws SQLException;
    public User login(User user) throws SQLException;
}
