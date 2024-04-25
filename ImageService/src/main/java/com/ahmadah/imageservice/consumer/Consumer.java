package com.ahmadah.imageservice.consumer;

import com.ahmadah.imageservice.message.SqsMessage;
import com.ahmadah.imageservice.service.ImageService;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.io.*;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class Consumer {
    @Value("${aws.queue.name}")
    private String queueName;
    private final ImageService imageService;
    private final AmazonSQS amazonSQSClient;
    private final ObjectMapper objectMapper;

    private Map<String, List<byte[]>> imageChunksMap=new HashMap<>();


//
//    @Scheduled(fixedDelay = 5000) // It runs every 5 seconds.
//    public void consumeMessages() {
//        try {
//            String queueUrl = amazonSQSClient.getQueueUrl(queueName).getQueueUrl();
//
//            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(queueUrl);
//
//            if (!receiveMessageResult.getMessages().isEmpty()) {
//                com.amazonaws.services.sqs.model.Message message = receiveMessageResult.getMessages().get(0);
//                log.info("Read Message from queue: {}", message.getBody());
//                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
//            }
//
//        } catch (Exception e) {
//            log.error("Queue Exception Message: {}", e.getMessage());
//        }
//    }
    @Scheduled(fixedDelay = 5000)
    public void consumeImageChunks() {
        log.info("running consumer");
        log.info("imageChunksMap {}",imageChunksMap.isEmpty());
        while (true) {
            String queueUrl = amazonSQSClient.getQueueUrl(queueName).getQueueUrl();
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
            List<Message> messages = amazonSQSClient.receiveMessage(receiveMessageRequest).getMessages();

            for (Message message : messages) {
                try {
                    SqsMessage sqsMessage = objectMapper.readValue(message.getBody(), SqsMessage.class);
                    log.info("Meessage index:{} total {} , id: {}",sqsMessage.getChunkIndex(),sqsMessage.getTotalChunks(),sqsMessage.getId(),sqsMessage.getFileExt());
                    String imageId = sqsMessage.getId();

                    if (!imageChunksMap.containsKey(imageId)) {
                        imageChunksMap.put(imageId, new ArrayList<>());
                    }

                    byte[] chunkBytes = Base64.getDecoder().decode(sqsMessage.getImageChunk());
                    imageChunksMap.get(imageId).add(chunkBytes);

                    // Check if all chunks for the image have been received
                    if (imageChunksMap.get(imageId).size() == sqsMessage.getTotalChunks()) {
                        // All chunks for this image have been received
                        // Now you can reconstruct the image using the chunks in order
                        List<byte[]> chunks = imageChunksMap.get(imageId);
                        byte[] reconstructedImage = reconstructImage(chunks);
                        log.info("finish reading {} ,related to id {},",sqsMessage.getTotalChunks(),sqsMessage.getId());
                        // Process or save the reconstructed image as needed
                        processReconstructedImage(reconstructedImage,sqsMessage.getFolderName(),sqsMessage.getId(),sqsMessage.getFileExt());

                        // Remove the entry from the map
                        imageChunksMap.remove(imageId);

                    }
                    // Delete the message from the queue after processing
                    amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
                } catch (IOException e) {
                    // Handle JSON parsing error
                    e.printStackTrace();
                } catch (Exception e) {
                    // Handle other exceptions
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] reconstructImage(List<byte[]> chunks) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (byte[] chunk : chunks) {
                outputStream.write(chunk);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
            return null;
        }
    }

    private void processReconstructedImage(byte[] reconstructedImage,String folderName,String imageName,String fileExt) {
        log.info("calling imageService from consumer ");
        imageService.sqsPutObject(reconstructedImage,folderName,imageName,fileExt);
//
//        try (FileOutputStream fos = new FileOutputStream(imageName+fileExt)) {
//            fos.write(reconstructedImage);
//        } catch (IOException e) {
//            // Handle IOException
//            e.printStackTrace();
//        }
    }

}