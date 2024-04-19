package com.aqarati.user.message;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqsMessage {
    private String id;
    private MultipartFile image;
    @Builder.Default
    private Date createdAt=new Date();
}
