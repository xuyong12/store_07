package cn.itcast.store.service.ServiceImp;

import cn.itcast.store.dao.DaoImp.UserDaoImp;
import cn.itcast.store.dao.UserDao;
import cn.itcast.store.domain.User;
import cn.itcast.store.service.UserService;
import com.sun.org.apache.bcel.internal.classfile.Code;

import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;

public class UserServiceImp implements UserService {
    @Override
    public int register(User user) throws SQLException {
        UserDao dao = new UserDaoImp();
        return dao.register(user);
    }

    @Override
    public boolean active(String active) throws SQLException {
        UserDao dao = new UserDaoImp();
        User user = dao.active(active);
        if (user != null) {
            //当存在该用户时，激活成功，改变code为null，把state变为1
            user.setCode(null);
            user.setState(1);
            //跟新数据库的表
            dao.update(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User user) throws SQLException,RuntimeException {
        UserDao dao = new UserDaoImp();
        User login = dao.login(user);
        //当用户不存在时
        if (login == null) {
            throw new RuntimeException("密码错误");
        } else if (login.getState() == 0) {
            throw new RuntimeException("用户未激活，请先激活用户");
        } else {
            return login;
        }
    }
}
