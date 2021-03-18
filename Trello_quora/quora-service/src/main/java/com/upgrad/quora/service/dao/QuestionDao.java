package com.upgrad.quora.service.dao;

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

    public QuestionEntity create(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public UserAuthTokenEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<QuestionEntity> getAllQuestions() {
        Query query = entityManager.createQuery("SELECT allq FROM QuestionEntity allq");
        List<QuestionEntity> allQuestions = query.getResultList();
        return allQuestions;
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

    public QuestionEntity editQuestion(QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    public void deleteQuestion(final String questionUuid){
        QuestionEntity questionEntity = getQuestion(questionUuid);
        entityManager.remove(questionEntity);
    }
}