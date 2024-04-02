package com.Aqarati.document.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder@Data
@AllArgsConstructor@NoArgsConstructor
public class CreateDocumentRequest {
    private String name;
    private String description;
    private double price;
    private String imgUrl;
}
