package com.aqarati.user.publisher;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.aqarati.user.message.SqsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
@RequiredArgsConstructor
public class Publisher {

    @Value("${aws.queue.name}")
    private String queueName;

    private final AmazonSQS amazonSQSClient;
    private final ObjectMapper objectMapper;

        public void publishMessage(String id, MultipartFile image) {
            try {
                GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(queueName);
                var message = SqsMessage.builder().
                        id(id).
                        image(image).
                        build();
                var result = amazonSQSClient.sendMessage(queueUrl.getQueueUrl(), objectMapper.writeValueAsString(message));
                log.info("massage was publish");
            } catch (Exception e) {
                log.error("Queue Exception Message: {}", e.getMessage());
            }
        }

}