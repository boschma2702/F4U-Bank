package com.bank.util.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.InvocationListener;

import java.lang.reflect.Method;
import java.util.*;

public class JsonRpcInvocationListener implements InvocationListener {

    public static final Set<String> METHODS_TO_NOT_LOG = new HashSet<>(Arrays.asList("reset"));

    @Override
    public void willInvoke(Method method, List<JsonNode> arguments) {
    }

    @Override
    public void didInvoke(Method method, List<JsonNode> arguments, Object result, Throwable t, long duration) {
        if(!METHODS_TO_NOT_LOG.contains(method.getName())) {
            Logger.info("Invoked method=%s, arguments=%s, duration=%sms", method.getName(), JsonArgumentsParser.getJsonRpcArgumentsMap(method, arguments), duration);
        }
    }


}
