package com.leapfin.assignment.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class Response {
    String responseCode;
    String message;
    HashMap<String, Object> objectMap;
}
