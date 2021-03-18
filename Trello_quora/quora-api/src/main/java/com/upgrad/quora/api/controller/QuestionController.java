package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.now());

        final QuestionEntity createdQuestionEntity = questionService.create(questionEntity, authorization);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        final List<QuestionEntity> allQuestions = questionService.getAllQuestions(authorization);
        List<QuestionDetailsResponse> questionDetailsResponse = new ArrayList<>();

        for (QuestionEntity aq : allQuestions) {
            questionDetailsResponse.add(new QuestionDetailsResponse().id(aq.getUuid()).content(aq.getContent()));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(final QuestionEditRequest questionEditRequest, @PathVariable("questionId") final String id, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = new QuestionEntity();

        questionEntity.setContent(questionEditRequest.getContent());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.now());

        final QuestionEntity createdQuestionEntity = questionService.editQuestion(questionEntity, id, authorization);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(createdQuestionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String qid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        questionService.deleteQuestion(qid, authorization);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(qid).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>>  getAllQuestionsByUser(@PathVariable("userId") final String uid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {
        final List<QuestionEntity> allQuestions = questionService.getAllQuestionsByUser(uid, authorization);
        List<QuestionDetailsResponse> questionDetailsResponse = new ArrayList<>();

        for (QuestionEntity aq : allQuestions) {
            questionDetailsResponse.add(new QuestionDetailsResponse().id(aq.getUuid()).content(aq.getContent()));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponse, HttpStatus.OK);
    }
}