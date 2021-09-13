package com.nuance.hackathon.services;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.models.CategorizedEntity;
import com.azure.ai.textanalytics.models.EntityCategory;
import com.azure.ai.textanalytics.models.TextDocumentInput;
import com.azure.core.credential.AzureKeyCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuance.hackathon.models.EngagementResponse;
import com.nuance.hackathon.models.Message;

import org.json.JSONException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${apiservice.host}")
    private String apiServiceHost;

    public EngagementResponse getEngagement(String engagementId) throws URISyntaxException, JSONException, JsonProcessingException, InterruptedException {

      /*  String token =  getToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+token);
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<?> response =
                restTemplate.exchange("https://api.touchcommerce.com/realtime/transcripts/engagement?site=306&engagementID="+engagementId+"&encrypted=false&output=json",
                        HttpMethod.GET, entity,String.class);
*/
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("C:\\temp\\TranscriptAnalytics\\TranscriptAnalytics\\src\\main\\java\\com\\nuance\\hackathon\\TranscriptAnalytics\\test1.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject)obj;
        jsonObject = (JSONObject) jsonObject.get("transcript-response");
        jsonObject = (JSONObject) jsonObject.get("engagements");
        jsonObject = (JSONObject) jsonObject.get("engagement");
        jsonObject = (JSONObject) jsonObject.get("transcript");
        JSONArray jsonArray = (JSONArray) jsonObject.get("transcriptLine");
        List<Message> messages = new ArrayList<>();
        for(int i=0;i<jsonArray.size();i++){
            ObjectMapper objectMapper = new ObjectMapper();
            Message message = objectMapper.readValue(jsonArray.get(i).toString(), Message.class);
            messages.add(message);
        }
        List<List<Message>> messageBatches = getMessageBatches(messages);
        ExecutorService executorService = Executors.newFixedThreadPool(messageBatches.size());

        for(int i=0;i<messageBatches.size();i++){
         BatchSentimentProcessor batchSentimentProcessor = new BatchSentimentProcessor(messageBatches.get(i));
          executorService.submit(batchSentimentProcessor);
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        List<Message> messagesToReturn =  messageBatches.stream().flatMap(messages1 -> messages1.stream()).collect(Collectors.toList());
        int countNegative = 0;
        int countPositive = 0 ;
        for(int i=0;i<messagesToReturn.size();i++){
            if(messagesToReturn.get(i).getSentiment().equalsIgnoreCase("Negative")){
                countNegative++;
            }else if(messagesToReturn.get(i).getSentiment().equalsIgnoreCase("Positive")){
                countPositive++;
            }
        }
        EngagementResponse engagementResponse = new EngagementResponse();
        engagementResponse.setMessages(messagesToReturn);
        engagementResponse.setNegativeSentimentCount(countNegative);
        engagementResponse.setPositiveSentimentCount(countPositive);
        engagementResponse = getNER(engagementResponse);
        return engagementResponse;
    }

    private EngagementResponse getNER(EngagementResponse engagementResponse) {
        Map<String,List<String>> ner = new HashMap<>();
        for(int i=0;i<engagementResponse.getMessages().size();i++){
            Message message = engagementResponse.getMessages().get(i);
            List<CategorizedEntity> entities = message.getEntities();
            if(entities != null && !entities.isEmpty()){
                for(int j=0;j<entities.size();j++){
                    if(ner.containsKey(entities.get(j).getCategory().toString())){
                        ner.get(entities.get(j).getCategory().toString()).add(entities.get(j).getText());
                    }else{
                        List<String> list = new ArrayList<>();
                        list.add(entities.get(j).getText());
                        ner.put(entities.get(j).getCategory().toString(),list);
                    }
                }
            }
        }
        engagementResponse.setNer(ner);
        return engagementResponse;
    }

    private List<List<Message>> getMessageBatches(List<Message> messages) {
        List<List<Message>> messageBatches = new ArrayList<>();
        List<Message> messagesTemp = new ArrayList<>();
        for(int i=0;i<messages.size();i++){
            if(messagesTemp.size()<5){
                messagesTemp.add(messages.get(i));
            }else{
                messageBatches.add(messagesTemp);
                messagesTemp = new ArrayList<>();
                messagesTemp.add(messages.get(i));
            }
        }
        if(!messagesTemp.isEmpty()){
            messageBatches.add(messagesTemp);
        }
        return messageBatches;
    }

    private String getToken() throws URISyntaxException, JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic YWVhcGlDbGllbnRJZDphZWFwaUNsaWVudFNlY3JldA==");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","password");
        map.add("password","Hadapsar@123");
        map.add("username","ag1");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<?> response =
                restTemplate.exchange("https://auth.touchcommerce.com/oauth-server/oauth/token",
                        HttpMethod.POST,
                        entity,
                        String.class);

        //JSONObject jsonObject = new JSONObject(response.getBody().toString());
       // return jsonObject.getString("access_token");
        return null;
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
