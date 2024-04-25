package com.ahmadah.imageservice.consumer;

import com.ahmadah.imageservice.message.SqsMessage;
import com.ahmadah.imageservice.service.ImageService;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    private Map<String, List<SqsMessage>> imageSqsMessageMap=new HashMap<>();

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
                    String imageId = sqsMessage.getId();

                    log.info("Meessage index:{} total {} , id: {} ",sqsMessage.getChunkIndex(),sqsMessage.getTotalChunks(),sqsMessage.getId());

                    if (!imageChunksMap.containsKey(imageId)) {
                        imageChunksMap.put(imageId, new ArrayList<>());
                        imageSqsMessageMap.put(imageId, new ArrayList<SqsMessage>());
                    }
                    imageSqsMessageMap.get(imageId).add(sqsMessage);


                    byte[] chunkBytes = Base64.getDecoder().decode(sqsMessage.getImageChunk());
                    imageChunksMap.get(imageId).add(chunkBytes);

                    if (imageChunksMap.get(imageId).size() == sqsMessage.getTotalChunks()) {
                        List<SqsMessage> chunks = imageSqsMessageMap.get(imageId);
                        byte[] reconstructedImage = reconstructImage(chunks);
                        log.info("finish reading {} ,related to id {},",sqsMessage.getTotalChunks(),sqsMessage.getId());
                        processReconstructedImage(reconstructedImage,sqsMessage.getFolderName(),sqsMessage.getId(),sqsMessage.getFileExt());

                        imageChunksMap.remove(imageId);

                    }
                    amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] reconstructImage(List<SqsMessage> chunks) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Collections.sort(chunks, Comparator.comparingInt(SqsMessage::getChunkIndex));

            for (SqsMessage chunk : chunks) {
                log.info( "Merging chunk with index {} and size {} bytes", new Object[]{chunk.getChunkIndex(), chunk.getImageChunk().length()});
                byte[] decodedChunk = Base64.getDecoder().decode(chunk.getImageChunk());
                outputStream.write(decodedChunk);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error( "Error while reconstructing image: " + e.getMessage(), e);
            return null;
        }
    }

    private void processReconstructedImage(byte[] reconstructedImage,String folderName,String imageName,String fileExt) {
        log.info("calling imageService from consumer ");
        imageService.sqsPutObject(reconstructedImage,folderName,imageName,fileExt);
    }
}