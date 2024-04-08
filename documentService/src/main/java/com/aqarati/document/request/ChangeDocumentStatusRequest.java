package com.aqarati.document.request;

import com.aqarati.document.model.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDocumentStatusRequest {
    private Long id;
    private DocumentStatus status;
}
