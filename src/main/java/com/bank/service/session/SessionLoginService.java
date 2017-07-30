package com.bank.service.session;

import com.bank.exception.AuthenticationException;
import com.bank.projection.session.SessionAuthTokenProjection;
import com.bank.projection.session.SessionPasswordProjection;
import com.bank.repository.session.LoginRepository;
import com.bank.service.AuthenticationService;
import com.bank.service.customer.CustomerGetService;
import com.bank.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionLoginService {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private CustomerGetService customerGetService;

    private boolean checkLogin(String username, String password) {
        Logger.info("Checking login of username=%s", username);
        SessionPasswordProjection sessionPasswordProjection = loginRepository.findByUsername(username);
        return sessionPasswordProjection != null && sessionPasswordProjection.getPassword() != null && sessionPasswordProjection.getPassword().equals(password);
    }

    public SessionAuthTokenProjection getAuthToken(String username, String password) throws AuthenticationException {
        Logger.info("Retrieving authentication token of username=%s", username);
        if (checkLogin(username, password)) {
            SessionAuthTokenProjection projection = new SessionAuthTokenProjection();
            projection.setAuthToken(AuthenticationService.instance.login(customerGetService.getCustomerId(username)));
            return projection;
        } else {
            Logger.error("Invalid username and password combination of username=%s", username);
            throw new AuthenticationException("Invalid username password combination");
        }
    }
}
