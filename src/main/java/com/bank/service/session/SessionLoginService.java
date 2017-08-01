package com.bank.service.session;

import com.bank.bean.person.PersonBean;
import com.bank.exception.AuthenticationException;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.session.SessionAuthTokenProjection;
import com.bank.projection.session.SessionPasswordProjection;
import com.bank.repository.person.PersonRepository;
import com.bank.repository.session.LoginRepository;
import com.bank.service.AuthenticationService;
import com.bank.service.customer.CustomerGetService;
import com.bank.service.customer.CustomerService;
import com.bank.service.person.PersonService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionLoginService {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private CustomerGetService customerGetService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private CustomerService customerService;

    private boolean checkLogin(String username, String password) {
        Logger.info("Checking login of username=%s", username);
        PersonBean personBean;
        try {
            personBean = personService.getPersonBeanByUsername(username);
        } catch (InvalidParamValueException e) {
            return false;
        }
        return personBean.getPassword().equals(password);
    }

    public SessionAuthTokenProjection getAuthToken(String username, String password) throws AuthenticationException {
        Logger.info("Retrieving authentication token of username=%s", username);
        if (checkLogin(username, password)) {
            SessionAuthTokenProjection projection = new SessionAuthTokenProjection();
            try {
                projection.setAuthToken(AuthenticationService.instance.login(customerService.getCustomerBeanByUsername(username).getCustomerId()));
            } catch (InvalidParamValueException e) {
                Logger.error("Could not find customer_id of username=%s", username);
                throw new AuthenticationException("Could not link username to customer");
            }
            return projection;
        } else {
            Logger.error("Invalid username and password combination of username=%s", username);
            throw new AuthenticationException("Invalid username password combination");
        }
    }
}
