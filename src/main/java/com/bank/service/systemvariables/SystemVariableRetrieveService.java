package com.bank.service.systemvariables;

import com.bank.bean.systemvariables.SystemVariableBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class SystemVariableRetrieveService {

    @Autowired
    private SystemVariableEditorService systemVariableEditorService;

    public Object getObject(String key) throws InvalidParamValueException {
        SystemVariableBean systemVariableBean = systemVariableEditorService.getSystemVariableBean();
        try {
            Field field = systemVariableBean.getClass().getDeclaredField(key);
            field.setAccessible(true);
            return field.get(systemVariableBean);
        }catch (NoSuchFieldException e){
            Logger.error("Could not find system variable=%s", key);
            throw new InvalidParamValueException("Could not find variable");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException("Exception in retrieving system variable");
        }
    }


    public Object getObjectInternally(String key){
        try {
            return getObject(key);
        } catch (InvalidParamValueException e) {
            //This function should only be used internally, field should always be present
            throw new IllegalStateException("Could not retrieve system variable object, key="+key);
        }
    }


}
