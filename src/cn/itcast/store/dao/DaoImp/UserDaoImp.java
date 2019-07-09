package cn.itcast.store.dao.DaoImp;

import cn.itcast.store.dao.UserDao;
import cn.itcast.store.domain.User;
import cn.itcast.store.utils.JDBCUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class UserDaoImp implements UserDao {
    private static QueryRunner runner;

    static {
        runner=new QueryRunner(JDBCUtils.getDataSource());
    }
    @Override
    public int register(User user) throws SQLException {
        String sql="insert into user values(?,?,?,?,?,?,?,?,?,?)";
        Object[] param={user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode()};
        int update = runner.update(sql, param);
        return update;
    }

    @Override
    public User active(String code) throws SQLException {
        String sql="select * from user where code=?";
        Object[] param={code};
        return runner.query(sql,new BeanHandler<>(User.class),param);
    }

    @Override
    public void update(User user) throws SQLException {
        String sql="update user set uid=?,username=?,password=?,name=?,email=?,telephone=?,birthday=?,sex=?,state=?,code=? where uid=?";
        Object[] param={user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode(),user.getUid()};
        runner.update(sql,param);
    }

    @Override
    public User login(User user) throws SQLException {
        String sql="select * from user where username=? and password=?";
        Object[] param={user.getUsername(),user.getPassword()};
        return runner.query(sql,new BeanHandler<>(User.class),param);
    }
}
