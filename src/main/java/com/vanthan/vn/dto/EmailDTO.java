package com.vanthan.vn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private String recipient;
    private String message;
    private String subject;
    private String attachment;
}
