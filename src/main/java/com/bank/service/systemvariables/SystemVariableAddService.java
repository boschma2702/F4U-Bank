package com.bank.service.systemvariables;

import com.bank.bean.systemvariables.SystemVariableBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.service.time.TimeService;
import com.bank.util.logging.Logger;
import com.bank.util.systemvariable.Money;
import com.bank.util.systemvariable.SystemVariableChangeObject;
import com.bank.util.systemvariable.SystemVariableFieldChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Iterator;

@Service
public class SystemVariableAddService {

    @Autowired
    private SystemVariableEditorService systemVariableEditorService;

    public void setValue(String key, BigDecimal value, java.sql.Date effectDate) throws InvalidParamValueException {
        Logger.info("Setting system variable with key=%s, value=%s, effectDate=%s", key, value, effectDate);
        if (effectDate.after(TimeService.TIMESIMULATOR.getCurrentDate())) {
            if (checkValue(key, value)) {
                if(checkIfAlreadyPresent(key, effectDate)){
                    systemVariableEditorService.addSystemVariableChangeObject(new SystemVariableChangeObject(key, value, effectDate));
                }
            }
        } else {
            Logger.error("Could not add set value request, date already passed of key=%s value=%s", key, value);
            throw new InvalidParamValueException("Effect date already passed");
        }
    }

    private boolean checkValue(String key, BigDecimal value) throws InvalidParamValueException {
        try {
//            double d = Double.parseDouble(value);
            Field field = SystemVariableBean.class.getDeclaredField(key);
            if (field.getType().getSimpleName().equals("int")) {
//                BigDecimal bigDecimal = new BigDecimal(d);
                if (value.scale() > 0) {
                    throw new NumberFormatException();
                }
            }
            if (field.isAnnotationPresent(Money.class)) {
//                BigDecimal bigDecimal = new BigDecimal(d);
                if (value.scale() > 2) {
                    throw new NumberFormatException();
                }
            }
            return true;
        } catch (NoSuchFieldException e) {
            Logger.error("Could not add set value request, unknown system variable key=%s", key);
            throw new InvalidParamValueException("Unknown system variable");
        } catch (NumberFormatException e) {
            Logger.error("Could not add set value request, invalid value=%s of key=%s", value, key);
            throw new InvalidParamValueException("Invalid value format");
        }
    }

    private boolean checkIfAlreadyPresent(String key, Date date) throws InvalidParamValueException {
        Iterator<SystemVariableChangeObject> queue = systemVariableEditorService.getChangesQueue().iterator();
        while (queue.hasNext()){
            SystemVariableChangeObject object = queue.next();
            if(object.getEffectDate().equals(date) && object.getKey().equals(key)){
                Logger.error("Key on given date already present");
                throw new InvalidParamValueException("Key already present on given day");
            }
            if(date.after(object.getEffectDate())){
                break;
            }
        }
        return true;
    }

}
