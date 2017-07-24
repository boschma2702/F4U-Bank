package com.bank.configuration;

import com.bank.util.Logging.JsonRpcErrorResolver;
import com.bank.util.Logging.JsonRpcInvocationListener;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonRpcConfiguration {
    @Bean
    public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
        AutoJsonRpcServiceImplExporter exp = new AutoJsonRpcServiceImplExporter();
        exp.setAllowLessParams(false);
        exp.setAllowExtraParams(false);
        exp.setErrorResolver(new JsonRpcErrorResolver());
        exp.setInvocationListener(new JsonRpcInvocationListener());
        return exp;
    }
}
