package com.nuance.hackathon.services;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.*;
import com.azure.ai.textanalytics.util.AnalyzeSentimentResultCollection;
import com.azure.ai.textanalytics.util.ExtractKeyPhrasesResultCollection;
import com.azure.ai.textanalytics.util.RecognizeEntitiesResultCollection;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.nuance.hackathon.models.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BatchSentimentProcessor implements Runnable {

    List<Message> messages;

    BatchSentimentProcessor(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        TextAnalyticsClient client = new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential("002752714e8d441d96feeb6e37c048b2"))
                .endpoint("https://hackathon-nuance.cognitiveservices.azure.com/")
                .buildClient();
        List<TextDocumentInput> documents = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            documents.add(new TextDocumentInput(String.valueOf(i), messages.get(i).getContent()));
        }
        //doing sentimental analysis
        AnalyzeSentimentOptions requestOptions = new AnalyzeSentimentOptions().setIncludeStatistics(true).setModelVersion("latest");
        Response<AnalyzeSentimentResultCollection> sentimentBatchResultResponse =
                client.analyzeSentimentBatchWithResponse(documents, requestOptions, Context.NONE);
        AnalyzeSentimentResultCollection sentimentBatchResultCollection = sentimentBatchResultResponse.getValue();
        AtomicInteger counter = new AtomicInteger();
        List<AnalyzeSentimentResult> documentSentimentList = sentimentBatchResultCollection.stream().collect(Collectors.toList());
        sentimentBatchResultCollection.forEach(analyzeSentimentResult -> {
            DocumentSentiment documentSentiment = analyzeSentimentResult.getDocumentSentiment();
            SentimentConfidenceScores scores = documentSentiment.getConfidenceScores();
            messages.get(counter.get()).setSentiment(documentSentiment.getSentiment().toString());
            messages.get(counter.getAndIncrement()).setSentimentScore(documentSentiment.getConfidenceScores());
        });

        //NER
        Response<RecognizeEntitiesResultCollection> entitiesBatchResultResponse = client.recognizeEntitiesBatchWithResponse(documents, requestOptions, Context.NONE);
        RecognizeEntitiesResultCollection recognizeEntitiesResultCollection = entitiesBatchResultResponse.getValue();
        AtomicInteger counter1 = new AtomicInteger();
        for (RecognizeEntitiesResult entitiesResult : recognizeEntitiesResultCollection) {
            if (entitiesResult.isError()) {
                System.out.printf("Cannot recognize entities. Error: %s%n", entitiesResult.getError().getMessage());
            } else {
                List<CategorizedEntity> entities = entitiesResult.getEntities().stream().collect(Collectors.toList());
                messages.get(counter1.getAndIncrement()).setEntities(entities);
            }
        }
            //KEY PHRASES
            Response<ExtractKeyPhrasesResultCollection> keyPhrasesBatchResultResponse =
                    client.extractKeyPhrasesBatchWithResponse(documents, requestOptions, Context.NONE);
            ExtractKeyPhrasesResultCollection keyPhrasesBatchResultCollection = keyPhrasesBatchResultResponse.getValue();
            AtomicInteger counter2 = new AtomicInteger();
                for (ExtractKeyPhraseResult extractKeyPhraseResult :  keyPhrasesBatchResultCollection){
                if (extractKeyPhraseResult.isError()) {
                    System.out.printf("Cannot extract key phrases. Error: %s%n", extractKeyPhraseResult.getError().getMessage());
                } else {
                    List<String> keyPhrases = extractKeyPhraseResult.getKeyPhrases().stream().collect(Collectors.toList());
                    messages.get(counter2.getAndIncrement()).setKeyPhrases(keyPhrases);
                }
            }
        }
    }

