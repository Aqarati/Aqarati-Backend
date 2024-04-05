package com.aqarati.document.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder@Data
@AllArgsConstructor@NoArgsConstructor
public class CreateDocumentRequest {
    private String imgUrl;
    private double price;
}
