package com.bank.util.Logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorResolver;

import java.lang.reflect.Method;
import java.util.List;

public class JsonRpcErrorResolver implements ErrorResolver {
    @Override
    public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {
        Logger.error("Error in JSON RPC method=%s, throwable=%s, arguments=%s", method.getName(), t, JsonArgumentsParser.getJsonRpcArgumentsMap(method, arguments));
        return null;
    }
}
