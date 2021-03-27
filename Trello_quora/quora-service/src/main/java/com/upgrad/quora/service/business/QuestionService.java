package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    // This method is for Creating the Question
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity create(QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);

        // Validating if User has Signed-In or not
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validating whether the user is Signed-Out or not
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        questionEntity.setUser(userAuthTokenEntity.getUser());
        return questionDao.create(questionEntity);
    }

    // This method is for Getting All Questions for any user
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(final String authorizationToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);

        // Validating if the User has Signed-In or not
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validating whether the user is Signed-Out or not
        else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get all questions");
        }

        else {
            List<QuestionEntity> allQuestions = questionDao.getAllQuestions();
            return allQuestions;
        }
    }

    // This method is for Editing the Question
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(QuestionEntity questionEntity, final String questionId, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);

        // Validating if User has Signed-In or not
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validating whether the user is Signed-Out or not
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }


        // Validating if the Question with corresponding Uuid exist in the DataBase or  not
        QuestionEntity question_Entity0 = questionDao.getQuestion(questionId);
        if (question_Entity0 == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        // Validating the Owner of the Question
        UserEntity userEntity = questionDao.getQuestion(questionId).getUser();
        if (!userEntity.getUuid().equals(userAuthTokenEntity.getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        else {
            questionEntity.setUser(userAuthTokenEntity.getUser());
            questionDao.editQuestion(questionEntity);
            return questionEntity;
        }
    }

    // This method is for Deleting the Question
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteQuestion(final String questionId, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);

        // Validating if User has Signed-In or not
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validating whether the user is Signed-Out or not
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }


        // Validating if the Question with corresponding Uuid exist in the DataBase or  not
        QuestionEntity questionEntity = questionDao.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        //Validating whether user is Admin or Owner of the question
        String role = userAuthTokenEntity.getUser().getRole();
        UserEntity userEntity = questionDao.getQuestion(questionId).getUser();
        if (role.equals("admin") || userEntity.getUuid().equals(userAuthTokenEntity.getUuid())) {
            questionDao.deleteQuestion(questionId);
        }
        else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
    }

    // This method is for Getting All the Questions posted by respective user
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(final String userId, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        //Validate if user is signed in or not
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        //Validate if user has signed out
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
        }

        //Validate if user exist in the data base or not.
        UserEntity userEntity = userDao.getUserByUuid(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        else {
            List<QuestionEntity> allQuestions = questionDao.getAllQuestionsByUser(userId);
            return allQuestions;
        }
    }
}