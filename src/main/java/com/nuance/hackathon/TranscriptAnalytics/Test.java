package com.nuance.hackathon.TranscriptAnalytics;// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.


import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.AnalyzeSentimentOptions;
import com.azure.ai.textanalytics.models.DocumentSentiment;
import com.azure.ai.textanalytics.models.SentimentConfidenceScores;
import com.azure.ai.textanalytics.models.TextDocumentBatchStatistics;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.ai.textanalytics.util.AnalyzeSentimentResultCollection;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nuance.hackathon.models.Message;
import com.nuance.hackathon.services.AnalyticsService;
import org.json.JSONException;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sample demonstrates how to analyze the sentiments of {@link TextDocumentInput} documents.
 */
public class Test {
    /**
     * Main method to invoke this demo about how to analyze the sentiments of {@link TextDocumentInput} documents.
     *
     * @param args Unused arguments to the program.
     */
    public static void main(String[] args) throws JsonProcessingException, JSONException, URISyntaxException, InterruptedException {/*
        // Instantiate a client that will be used to call the service.
        TextAnalyticsClient client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential("0512d4eb0910412d9df13ec24586757b"))
                .endpoint("https://transcriptsentiment.cognitiveservices.azure.com/")
                .buildClient();

        // The texts that need be analyzed.
        List<TextDocumentInput> documents = Arrays.asList(
                new TextDocumentInput("A", "The hotel was dark and unclean. I wouldn't recommend staying there.").setLanguage("en"),
                new TextDocumentInput("B", "The restaurant had amazing gnocchi! The waiters were excellent.").setLanguage("en"),
                new TextDocumentInput("C", "The hotel was dark and unclean. The restaurant had amazing gnocchi!").setLanguage("en"),
                new TextDocumentInput("D", "The hotel was dark and unclean. The restaurant had amazing gnocchi!").setLanguage("en")
        );

        AnalyzeSentimentOptions requestOptions = new AnalyzeSentimentOptions().setIncludeStatistics(true).setModelVersion("latest");

        // Analyzing sentiment for each document in a batch of documents
        Response<AnalyzeSentimentResultCollection> sentimentBatchResultResponse =
                client.analyzeSentimentBatchWithResponse(documents, requestOptions, Context.NONE);


        // Response's status code
        System.out.printf("Status code of request response: %d%n", sentimentBatchResultResponse.getStatusCode());
        AnalyzeSentimentResultCollection sentimentBatchResultCollection = sentimentBatchResultResponse.getValue();

        // Model version
        System.out.printf("Results of Azure Text Analytics \"Sentiment Analysis\" Model, version: %s%n", sentimentBatchResultCollection.getModelVersion());

        // Batch statistics
        TextDocumentBatchStatistics batchStatistics = sentimentBatchResultCollection.getStatistics();
        System.out.printf("Documents statistics: document count = %s, erroneous document count = %s, transaction count = %s, valid document count = %s.%n",
                batchStatistics.getDocumentCount(), batchStatistics.getInvalidDocumentCount(), batchStatistics.getTransactionCount(), batchStatistics.getValidDocumentCount());

        // Analyzed sentiment for each document in a batch of documents
        AtomicInteger counter = new AtomicInteger();
        sentimentBatchResultCollection.forEach(analyzeSentimentResult -> {
            System.out.printf("%n%s%n", documents.get(counter.getAndIncrement()));
            if (analyzeSentimentResult.isError()) {
                // Erroneous document
                System.out.printf("Cannot analyze sentiment. Error: %s%n", analyzeSentimentResult.getError().getMessage());
            } else {
                // Valid document
                DocumentSentiment documentSentiment = analyzeSentimentResult.getDocumentSentiment();
                SentimentConfidenceScores scores = documentSentiment.getConfidenceScores();
                System.out.printf("Analyzed document sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                        documentSentiment.getSentiment(), scores.getPositive(), scores.getNeutral(), scores.getNegative());
                documentSentiment.getSentences().forEach(sentenceSentiment -> {
                    SentimentConfidenceScores sentenceScores = sentenceSentiment.getConfidenceScores();
                    System.out.printf(
                            "\tAnalyzed sentence sentiment: %s, positive score: %f, neutral score: %f, negative score: %f.%n",
                            sentenceSentiment.getSentiment(), sentenceScores.getPositive(), sentenceScores.getNeutral(), sentenceScores.getNegative());
                });
            }
        });*/
        AnalyticsService analyticsService = new AnalyticsService();
        List<Message> messages = analyticsService.getEngagement("12").getMessages();
        for (int i=0;i<messages.size();i++){
            System.out.println(messages.get(i).getContent()+":"+messages.get(i).getSentiment()+":"+messages.get(i).getEntities());
        }
    }
}