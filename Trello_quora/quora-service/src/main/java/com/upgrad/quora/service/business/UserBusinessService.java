package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signUp(UserEntity userEntity) throws SignUpRestrictedException {

        //Checking username exists in db or not
        UserEntity userByUsername = userDao.getUserByUsername(userEntity.getUserName());
        if(userByUsername != null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }

        //Checking email exists in db or not
        UserEntity userByEmail = userDao.getUserByEmail(userEntity.getEmail());
        if(userByEmail != null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }

        //Encrypt salt and password
        String[] encrypt = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encrypt[0]);
        userEntity.setPassword(encrypt[1]);

        UserEntity createdUser = userDao.createUser(userEntity);
        return createdUser;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signOut(final String authorizationToken) throws SignOutRestrictedException {
        UserAuthTokenEntity userAuthToken = userDao.getUserAuthToken(authorizationToken);

        if(userAuthToken == null){
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }

        final ZonedDateTime now = ZonedDateTime.now();
        userAuthToken.setLogoutAt(now);

        return userAuthToken.getUser();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUserDetails(final String userUuid, final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthToken = userDao.getUserAuthToken(authorization);

        //Validate if user is signed in or not
        if(userAuthToken == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        //Validate if user has signed out
        if(userAuthToken.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }

        UserEntity userEntity = userDao.getUserByUuid(userUuid);

        //validate if user exists or not
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }

        return userEntity;
    }
}
