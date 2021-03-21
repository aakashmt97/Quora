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
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    // This method is for Creating the Question in the Database
    public QuestionEntity create(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    // This method is for Getting User_Token from the Database
    public UserAuthTokenEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    // This method is for Getting All Questions for any user from Database
    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    // This method is for Getting a Question by using the Question_Id, from the Database
    public QuestionEntity getQuestion(final String questionUuid){
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class)
                    .setParameter("uuid", questionUuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    // This method is for Editing the Question in the Database
    public QuestionEntity editQuestion(QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    // This method is for Deleting the Question in the Database
    public void deleteQuestion(final String questionUuid){
        QuestionEntity questionEntity = getQuestion(questionUuid);
        entityManager.remove(questionEntity);
    }

    // This method id for Getting All the Questions posted by respective user from the Database
    public List<QuestionEntity> getAllQuestionsByUser(final String userId) {
        Query query = entityManager.createQuery("SELECT alq FROM QuestionEntity alq WHERE alq.user.uuid = :userId").setParameter("userId", userId);
        List<QuestionEntity> allQuestions = query.getResultList();
        return allQuestions;
    }
}