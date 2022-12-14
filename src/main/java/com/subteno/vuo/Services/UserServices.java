package com.subteno.vuo.Services;

import java.util.concurrent.ExecutionException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.cloud.FirestoreClient;
import com.subteno.vuo.Interface.UserInterfaceServices;
import com.subteno.vuo.Model.CustomUserDetails;
import com.subteno.vuo.Model.Result;
import com.subteno.vuo.Model.Role;
import com.subteno.vuo.Model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServices implements UserInterfaceServices {

    private static final String COLLECTION_NAME = "vuoStaff";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private CollectionReference collection = FirestoreClient.getFirestore().collection(COLLECTION_NAME);

    @Override
    public Result AddUser(User user) throws InterruptedException, ExecutionException {
        CustomUserDetails reqUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        DocumentReference doc = collection.document(user.getUsername());
        User resUser = doc.get().get().toObject(User.class);
        if (resUser == null) {
            user.setRole(new Role(1, "ROLE_USER"));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return new Result("success", "Success. Timestamp : " + doc.set(user).get().getUpdateTime().toString());
        }
        if (reqUser.getRole().getId() < resUser.getRole().getId()) {
            return new Result("error", "Cannot change upper authority");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(resUser.getRole());
        return new Result("success",
                "Update user. Timestamp : " + doc.set(user).get()
                        .getUpdateTime().toString());
    }

    @Override
    public Result GetUser(String username) throws InterruptedException, ExecutionException {
        if (username.isEmpty()) {
            return new Result("error", "User cannot be empty");
        }
        User user = collection.document(username).get().get()
                .toObject(User.class);
        if (user == null) {
            return new Result("error", "User cannot be found");
        }
        return new Result("user", user);
    }

    @Override
    public Result DeleteUser(User user) throws InterruptedException, ExecutionException {
        if (user.getUsername().isBlank())
            return new Result("error", "User not found");
        DocumentReference doc = collection.document(user.getUsername());
        if (!doc.get().get().exists())
            return new Result("error", "User not found");

        return new Result("success", "Success. Timestamp : " + doc.delete().get().getUpdateTime().toString());

    }

    @Override
    public Result AssignRoleUser(String username, Role role)
            throws InterruptedException, ExecutionException {
        if (username.equals("")) {
            return new Result("error", "Username cannot be empty");
        }
        User user = null;
        try {
            user = (User) GetUser(username).getObject();
        } catch (ClassCastException e) {
            return new Result("error", "Username not found");
        }
        switch (role.getRole()) {
            case "ROLE_USER":
                role.setId(1);
                break;
            case "ROLE_MANAGER":
                role.setId(2);
                break;
            case "ROLE_ADMIN":
                role.setId(3);
                break;
            default:
                break;
        }
        user.setRole(role);
        return new Result("success",
                "Assigned user. Timestamp : "
                        + collection.document(username)
                                .set(user).get().getUpdateTime().toString());
    }

}
