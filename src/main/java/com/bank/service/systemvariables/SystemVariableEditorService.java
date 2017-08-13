package com.bank.service.systemvariables;

import com.bank.bean.systemvariables.SystemVariableBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.systemvariable.SystemVariableRepository;
import com.bank.service.time.TimeService;
import com.bank.util.logging.Logger;
import com.bank.util.systemvariable.SystemVariableChangeObject;
import com.bank.util.systemvariable.SystemVariableFieldChecker;
import com.bank.util.time.DayPassedListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SystemVariableEditorService extends DayPassedListener {

    private SystemVariableRepository systemVariableRepository;

    private PriorityQueue<SystemVariableChangeObject> changesQueue = new PriorityQueue<>();

    private SystemVariableBean systemVariableBean;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public SystemVariableEditorService(SystemVariableRepository systemVariableRepository) {
        systemVariableBean = systemVariableRepository.getSystemVariableBean();
        if (systemVariableBean == null) {
            systemVariableRepository.save(new SystemVariableBean());
        }
        this.systemVariableRepository = systemVariableRepository;
    }

    public void setValue(String key, String value, java.sql.Date effectDate) throws InvalidParamValueException {
        Logger.info("Setting system variable with key=%s, value=%s, effectDate=%s", key, value, effectDate);
        if (effectDate.after(TimeService.TIMESIMULATOR.getCurrentDate())) {
            if (SystemVariableFieldChecker.hasObject(key)) {
                try {
                    Double.parseDouble(value);
                    changesQueue.add(new SystemVariableChangeObject(key, value, effectDate));
                } catch (NumberFormatException e) {
                    Logger.error("Could not add set value request, invalid value=%s of key=%s", value, key);
                    throw new InvalidParamValueException("Invalid value format");
                }

            } else {
                Logger.error("Could not add set value request, unknown system variable key=%s", key);
                throw new InvalidParamValueException("Unknown system variable");
            }
        } else {
            Logger.error("Could not add set value request, date already passed of key=%s value=%s", key, value);
            throw new InvalidParamValueException("Effect date already passed");
        }
    }

    @Override
    public void onDayPassed(Date start, Date end) {
        List<SystemVariableChangeObject> toApply = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        while (changesQueue.size() > 0) {
            SystemVariableChangeObject systemVariableChangeObject = changesQueue.peek();
            if (sdf.format(calendar.getTime()).equals(sdf.format(systemVariableChangeObject.getEffectDate()))) {
                toApply.add(changesQueue.poll());
            } else {
                break;
            }
        }
        for (SystemVariableChangeObject object : toApply) {
            applyValue(object.getKey(), object.getValue());
        }
        if (toApply.size() > 0) {
            systemVariableRepository.save(systemVariableBean);
        }
    }

    public void applyValue(String key, String value) {
        Logger.info("Applying system variable change key=%s, value=%s", key, value);
        try {
            Field field = SystemVariableBean.class.getDeclaredField(key);
            field.setAccessible(true);
            switch (field.getType().getSimpleName()) {
                case "double":
                    field.set(systemVariableBean, Double.parseDouble(value));
                    break;
                case "int":
                    field.set(systemVariableBean, (int) Double.parseDouble(value));
                    break;
                case "BigDecimal":
                    field.set(systemVariableBean, new BigDecimal(Double.parseDouble(value)));
                    break;
                default:
                    //should not happen
                    throw new IllegalStateException("Unknown type:" + field.getType().getSimpleName());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //should not happen
            e.printStackTrace();
            Logger.error("Illegal state reached with key=%s, value=%s", key, value);
            throw new IllegalStateException("Illegal state: could not retrieve type of field");
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }

    public SystemVariableBean getSystemVariableBean() {
        return systemVariableBean;
    }
}
