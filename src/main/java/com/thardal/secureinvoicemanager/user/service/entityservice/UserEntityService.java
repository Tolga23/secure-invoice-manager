package com.thardal.secureinvoicemanager.user.service.entityservice;

import com.thardal.secureinvoicemanager.base.service.BaseEntityService;
import com.thardal.secureinvoicemanager.user.entity.User;
import com.thardal.secureinvoicemanager.user.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserEntityService extends BaseEntityService<User, UserRepository> {
    public UserEntityService(UserRepository dao) {
        super(dao);
    }

    public User getUserByEmail(String email) {
        return getDao().getUserByEmail(email);
    }

    public User getUserById(Long userId) {
        return getDao().getUserById(userId);
    }

    public User findUserByVerificationCode(String code){
        return getDao().findUserByVerificationCode(code);
    }

    public User findUserByResetPasswordVerification(String url){
        return getDao().findUserByResetPasswordVerification(url);
    }

    public void updatePasswordByUrl(String password, String url){
        getDao().updatePasswordByUrl(password,url);
    }

    public User findUserByVerificationUrl(String url){
        return getDao().findUserByVerificationUrl(url);
    }

    public void updateUserEnabledById(Long userId){
        getDao().updateUserEnabledById(userId);
    }

    public void updatePassword(Long userId, String password) {
        getDao().updatePassword(userId,password);
    }

}
