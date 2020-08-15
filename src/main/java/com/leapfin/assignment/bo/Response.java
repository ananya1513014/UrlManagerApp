package com.leapfin.assignment.bo;

import lombok.Data;

import java.util.HashMap;

@Data
public class Response {
    String responseCode;
    String message;
    HashMap<String, Object> objectMap;
}
