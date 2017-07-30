package com.bank.util.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.JsonRpcParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class JsonArgumentsParser {

    public static final Set<String> ARGUMENTS_TO_NOT_LOG = new HashSet<>(Arrays.asList("password", "pinCode", "authToken"));

    public static HashMap<String, Object> getJsonRpcArgumentsMap(Method method, List<JsonNode> arguments){
        Annotation[][] jsonRpcAnnotations = method.getParameterAnnotations();
        HashMap<String, Object> map = new HashMap<>();
        for(int i=0; i<jsonRpcAnnotations.length; i++){
            String value = ((JsonRpcParam) jsonRpcAnnotations[i][0]).value();
            if(ARGUMENTS_TO_NOT_LOG.contains(value)){
                map.put(value, "\"****\"");
            }else{
                map.put(value, arguments.get(i));
            }
        }
        return map;
    }

}
