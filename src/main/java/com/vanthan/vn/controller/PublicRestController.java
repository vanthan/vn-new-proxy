package com.vanthan.vn.controller;

import com.vanthan.vn.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicRestController {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    public static final String TOPIC = "NewTopic";

    @GetMapping("spring-kafka/{message}")
    public String publicMessage(@PathVariable String message){
//        kafkaTemplate.send(TOPIC, message);
        return "public message success";
    }

    @PostMapping("spring-kafka/data")
    public String publicMessage(@RequestBody Student message){
        kafkaTemplate.send(TOPIC, message);
        return "public message success";
    }
}
