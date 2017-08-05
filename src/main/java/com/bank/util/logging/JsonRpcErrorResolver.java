package com.bank.util.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorResolver;
import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;

import java.lang.reflect.Method;
import java.util.List;

public class JsonRpcErrorResolver implements ErrorResolver {
    @Override
    public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {
        Logger.error("Error in JSON RPC method=%s, throwable=%s, arguments=%s", method.getName(), t, JsonArgumentsParser.getJsonRpcArgumentsMap(method, arguments));
        JsonRpcError[] errors = ((JsonRpcErrors) method.getDeclaredAnnotations()[0]).value();
        for(int i=0;i<errors.length; i++){
            if(errors[i].exception().equals(t.getClass())){
                return new JsonError(errors[i].code(), t.getMessage(), null);
            }
        }
        return null;
    }
}
