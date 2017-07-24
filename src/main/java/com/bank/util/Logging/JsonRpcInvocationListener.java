package com.bank.util.Logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.InvocationListener;

import java.lang.reflect.Method;
import java.util.*;

public class JsonRpcInvocationListener implements InvocationListener {


    @Override
    public void willInvoke(Method method, List<JsonNode> arguments) {
    }

    @Override
    public void didInvoke(Method method, List<JsonNode> arguments, Object result, Throwable t, long duration) {
        Logger.info("Invoked method=%s, arguments=%s, duration=%sms", method.getName(), JsonArgumentsParser.getJsonRpcArgumentsMap(method, arguments), duration);
    }


}
