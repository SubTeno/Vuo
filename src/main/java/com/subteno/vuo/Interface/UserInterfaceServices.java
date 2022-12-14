package com.subteno.vuo.Interface;

import com.subteno.vuo.Model.Result;
import com.subteno.vuo.Model.Role;
import com.subteno.vuo.Model.User;

public interface UserInterfaceServices {
    Result AddUser(User user) throws Exception;

    Result GetUser(String username) throws Exception;

    Result AssignRoleUser(String username, Role role) throws Exception;

    Result DeleteUser(User user) throws Exception;

}
