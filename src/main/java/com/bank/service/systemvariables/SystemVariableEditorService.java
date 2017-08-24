package com.bank.service.systemvariables;

import com.bank.bean.systemvariables.SystemVariableBean;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.systemvariable.SystemVariableRepository;
import com.bank.service.time.TimeService;
import com.bank.util.logging.Logger;
import com.bank.util.systemvariable.SystemVariableChangeObject;
import com.bank.util.systemvariable.SystemVariableFieldChecker;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SystemVariableApplyService systemVariableApplyService;

    public SystemVariableEditorService(SystemVariableRepository systemVariableRepository) {
        this.systemVariableRepository = systemVariableRepository;
        resetSystemVariableBean();
    }

    public void resetSystemVariableBean(){
        systemVariableBean = systemVariableRepository.getSystemVariableBean();
        if (systemVariableBean == null) {
            systemVariableBean = new SystemVariableBean();
            systemVariableRepository.save(systemVariableBean);
        }
    }

    public void addSystemVariableChangeObject(SystemVariableChangeObject systemVariableChangeObject){
        changesQueue.add(systemVariableChangeObject);
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

    public void applyValue(String key, BigDecimal value) {
        Logger.info("Applying system variable change key=%s, value=%s", key, value);
        try {
            Field field = SystemVariableBean.class.getDeclaredField(key);
            field.setAccessible(true);
            Object oldValue = SystemVariableRetrieveService.getObjectInternally(systemVariableBean, key);
            switch (field.getType().getSimpleName()) {
                case "double":
                    field.set(systemVariableBean, value.doubleValue());
                    break;
                case "int":
                    field.set(systemVariableBean, (int)value.doubleValue());
                    break;
                case "BigDecimal":
                    field.set(systemVariableBean, value);
                    break;
                default:
                    //should not happen
                    throw new IllegalStateException("Unknown type:" + field.getType().getSimpleName());
            }
            systemVariableApplyService.applySystemVariable(key, value, oldValue);
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

    public PriorityQueue<SystemVariableChangeObject> getChangesQueue() {
        return changesQueue;
    }

}
