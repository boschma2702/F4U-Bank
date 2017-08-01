package com.bank.service.session;

import com.bank.bean.person.PersonBean;
import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.session.SessionAuthTokenProjection;
import com.bank.service.AuthenticationService;
import com.bank.service.person.PersonService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionLoginService {

    @Autowired
    private PersonService personService;


    public SessionAuthTokenProjection getAuthToken(String username, String password) throws AuthenticationException {
        Logger.info("Retrieving authentication token of username=%s", username);
        try {
            PersonBean personBean = personService.getPersonBeanByUsername(username);
            if (!personBean.getPassword().equals(password)) {
                throw new AuthenticationException("Invalid username password combination");
            }
            SessionAuthTokenProjection projection = new SessionAuthTokenProjection();
            if (personBean.getCustomerBean() != null) {
                projection.setAuthToken(AuthenticationService.instance.customerLogin(personBean.getCustomerBean().getCustomerId()));
            } else if (personBean.getEmployeeBean() != null) {
                projection.setAuthToken(AuthenticationService.instance.employeeLogin(personBean.getEmployeeBean().getEmployeeId(), true));
            }
            return projection;
        } catch (InvalidParamValueException e) {
            throw new AuthenticationException("Invalid username password combination");
        }
    }
}
