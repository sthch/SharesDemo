package com.learnice.sharesdemo.ui.login.model;

import com.learnice.sharesdemo.FunInterface.Idone;
import com.learnice.sharesdemo.bean.tb_user;
import com.learnice.sharesdemo.ui.login.contract.LoginContract;


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Xuebin He on 2017/5/23.
 * e-mail:learnice.he@gmail.com.
 */

public class LoginModel implements LoginContract.Model {

    public static final String OK = "登录成功";
    public static final String PASS_ERROR = "密码错误";
    public static final String USER_NO_EXITS = "用户名不存在";
    public static final String OCCUR_EXCEPTION = "发生异常";

    @Override
    public void toLogin(String name, final String pass, final Idone idone) {
        BmobQuery<tb_user> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("name", name);
        bmobQuery.findObjects(new FindListener<tb_user>() {
            @Override
            public void done(List<tb_user> list, BmobException e) {
                if (e == null) {
                    //用户不存在
                    if (list.size() == 0) {
                        idone.done(USER_NO_EXITS);
                    }
                    //存在
                    else {
                        tb_user user = list.get(0);
                        //验证密码
                        if (user.getPass().equals(pass)) {
                            idone.done(OK);
                        } else {
                            idone.done(PASS_ERROR);
                        }
                    }
                    //发生错误
                } else {
                    idone.done(OCCUR_EXCEPTION);
                }
            }
        });
    }
}
