package com.nuance.hackathon.models;

import com.azure.ai.textanalytics.models.CategorizedEntity;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;

import java.util.List;

public class Message {

    private String senderType;
    private String senderName;
    private String sendTime;
    private String content;
    private String senderId;
    private String sentiment;
    private SentimentConfidenceScores sentimentScore;
    List<CategorizedEntity> entities;
    List<String> keyPhrases;

    public List<String> getKeyPhrases() {
        return keyPhrases;
    }

    public void setKeyPhrases(List<String> keyPhrases) {
        this.keyPhrases = keyPhrases;
    }

    public List<CategorizedEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<CategorizedEntity> entities) {
        this.entities = entities;
    }

    public SentimentConfidenceScores getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(SentimentConfidenceScores sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderType() {
        return senderType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

}
