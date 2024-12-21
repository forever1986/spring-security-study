package com.demo.lesson12.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhiteDTO {

    private String httpMethod;
    private String url;
}
