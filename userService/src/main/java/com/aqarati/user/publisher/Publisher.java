package com.aqarati.user.publisher;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.aqarati.user.message.SqsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Base64;


@Service
@Log4j2
@RequiredArgsConstructor
public class Publisher {

    @Value("${aws.queue.name}")
    private String queueName;

    private final AmazonSQS amazonSQSClient;
    private final ObjectMapper objectMapper;

    public void publishImageChunks(MultipartFile image,String folderName,String id,String fileExt ) {
        try {
            GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(queueName);

            // Read image bytes
            byte[] imageBytes = image.getBytes();

            long imageSizeKB = image.getSize() / 1024; // Convert to KB
            log.info("Total size of the image ({}): {} KB", id, imageSizeKB);
            // Calculate chunk size (e.g., 256KB)
            int chunkSize = 128 * 1024; // 256KB
            int numChunks = (int) Math.ceil((double) imageBytes.length / chunkSize);

            // Send each chunk as a separate message
            for (int i = 0; i < numChunks; i++) {
                int start = i * chunkSize;
                int end = Math.min((i + 1) * chunkSize, imageBytes.length);
                byte[] chunkBytes = Arrays.copyOfRange(imageBytes, start, end);

                // Encode chunk bytes to Base64 string
                String base64Chunk = Base64.getEncoder().encodeToString(chunkBytes);

                // Build your message
                var message = SqsMessage.builder()
                        .id(id)
                        .folderName(folderName)
                        .fileExt(fileExt)
                        .chunkIndex(i)
                        .totalChunks(numChunks)
                        .imageChunk(base64Chunk) // Store image chunk as Base64 string
                        .build();
                log.info("Sending chunk {} of {} for image name: {}, imageExt  {}", i + 1, numChunks, id,fileExt);

                // Serialize message
                SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl.getQueueUrl(), objectMapper.writeValueAsString(message));

                // Send the message
                SendMessageResult result = amazonSQSClient.sendMessage(sendMessageRequest);
            }

            log.info("Image chunks were published");
        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage());
        }
    }

}