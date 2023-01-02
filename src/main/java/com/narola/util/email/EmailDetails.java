package com.narola.util.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
    private String templateName;
    private Map<String,Object> templateData;

}