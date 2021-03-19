package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity getUser(final String userUuid){
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class)
                    .setParameter("uuid", userUuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthTokenEntity getUserAuthToken(final String accessToken){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",UserAuthTokenEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public void deleteUser(final String userUuid){
        UserEntity userEntity = getUser(userUuid);
        entityManager.remove(userEntity);
    }

    public AnswerEntity createAnswer (AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity editAnswer (AnswerEntity answerEntity){
        entityManager.merge(answerEntity);
        return answerEntity;
    }
    public QuestionEntity getQuestion(final String questionUuid){
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class)
                    .setParameter("uuid", questionUuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public AnswerEntity getAnswer(final String answerUuid){
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class)
                    .setParameter("uuid", answerUuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }
    public void deleteAnswer(final String AnswerId){
        AnswerEntity answerEntity = getAnswer(AnswerId);
        entityManager.remove(answerEntity);
    }

    public List<AnswerEntity> getAllAnswers (final String questionId){
        QuestionEntity questionEntity = getQuestion(questionId);
        try {
            return entityManager.createNamedQuery("answersByQuesID", AnswerEntity.class)
                    .setParameter("question_id", questionEntity)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }
}

