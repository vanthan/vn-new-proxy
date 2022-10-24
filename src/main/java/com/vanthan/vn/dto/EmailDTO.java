package com.vanthan.vn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private String recipient;
    private String message;
    private String subject;
    private List<Object> attachments;
    private Map<String, Object> props;
}
