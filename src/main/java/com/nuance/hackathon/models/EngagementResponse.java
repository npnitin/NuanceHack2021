package com.nuance.hackathon.models;

import java.util.List;
import java.util.Map;

public class EngagementResponse {
    List<Message> messages;
    int positiveSentimentCount;
    int negativeSentimentCount;
    Map<String,List<String>> ner;

    public int getNegativeSentimentCount() {
        return negativeSentimentCount;
    }


    public Map<String, List<String>> getNer() {
        return ner;
    }

    public void setNer(Map<String, List<String>> ner) {
        this.ner = ner;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setNegativeSentimentCount(int negativeSentimentCount) {
        this.negativeSentimentCount = negativeSentimentCount;
    }

    public int getPositiveSentimentCount() {
        return positiveSentimentCount;
    }

    public void setPositiveSentimentCount(int positiveSentimentCount) {
        this.positiveSentimentCount = positiveSentimentCount;
    }
}
