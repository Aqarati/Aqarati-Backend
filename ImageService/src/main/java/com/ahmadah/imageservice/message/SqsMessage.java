package com.ahmadah.imageservice.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqsMessage implements Serializable {
    private String id;
    private String folderName;
    private String fileExt;
    private int chunkIndex;
    private int totalChunks;
    private String imageChunk;

    @Builder.Default
    private Date createdAt=new Date();
}
