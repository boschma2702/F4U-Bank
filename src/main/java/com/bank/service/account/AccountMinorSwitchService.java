package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.bean.customer.CustomerBean;
import com.bank.bean.customeraccount.CustomerAccount;
import com.bank.exception.InvalidParamValueException;
import com.bank.projection.customer.CustomerAccountAccessProjection;
import com.bank.repository.account.AccountRepository;
import com.bank.repository.customer.CustomerRepository;
import com.bank.repository.customeraccount.CustomerAccountRepository;
import com.bank.service.customer.CustomerAccessService;
import com.bank.service.customeraccount.CustomerAccountService;
import com.bank.service.time.TimeService;
import com.bank.service.transaction.TransactionService;
import com.bank.util.AmountFormatter;
import com.bank.util.Constants;
import com.bank.util.time.DayPassedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AccountMinorSwitchService extends DayPassedListener {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAccessService customerAccessService;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void onDayPassed(Date start, Date end) {
        List<CustomerBean> customerBeans = customerRepository.getMinorBirthdays(TimeService.TIMESIMULATOR.getCurrentDate(), Constants.AGE_MINOR_MAX);
        for (CustomerBean customerBean : customerBeans) {
            List<AccountBean> accounts = customerAccessService.getMainAccountAcces(customerBean.getCustomerId());
            for (AccountBean accountBean : accounts) {
                BigDecimal amount = AmountFormatter.format(accountBean.getBuildUpInterest());
                //remove guardians
                customerAccountRepository.removeNoneMainAccountHolders(accountBean.getAccountId());
                //set isMinor to false
                accountBean.setMinorAccount(false);
                accountBean.setBuildUpInterest(0);
                accountRepository.save(accountBean);
                //transfer build up interest
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    try {
                        transactionService.doSingleTransaction(accountBean, null, amount, "Happy Birthday, remaining interest of child account");
                    } catch (InvalidParamValueException e) {
                        // do nothing
                    }
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
