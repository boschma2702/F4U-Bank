package com.bank.service.account;

import com.bank.bean.account.AccountBean;
import com.bank.exception.AccountFrozenException;
import com.bank.exception.InvalidParamValueException;
import com.bank.repository.account.AccountRepository;
import com.bank.service.time.TimeService;
import com.bank.util.logging.Logger;
import com.bank.util.time.DayPassedListener;
import com.bank.util.time.DayPassedRemoveAfterUseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountTransferLimitService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    public void setTransferLimit(int accountId, BigDecimal transferLimit) throws InvalidParamValueException, AccountFrozenException {
        if (transferLimit.compareTo(BigDecimal.ZERO) < 0) {
            Logger.error("Could not set transfer limit of accountId=%s, invalid transfer limit", accountId);
            throw new InvalidParamValueException("Invalid transferLimit");
        }
        AccountBean accountBean = accountService.getAccountBeanByAccountIdCheckOnFrozen(accountId);

        DayPassedListener dayPassedListener = new DayPassedRemoveAfterUseListener() {
            @Override
            public void onDayPassed(Date start, Date end) {
                Logger.info("Setting transfer limit of accountId=%s", accountId);
                AccountBean accountBean = null;
                try {
                    accountBean = accountService.getAccountBeanByAccountIdCheckOnFrozen(accountId);
                    accountBean.setTransferLimit(transferLimit);
                    accountRepository.save(accountBean);
                } catch (AccountFrozenException e) {
                    Logger.error("AccountFrozenException while setting day passed limit on accountId=%s", accountId);
                } catch (InvalidParamValueException e) {
                    Logger.error("InvalidParamError while setting day passed limit on accountId=%s", accountId);
                }
            }

            @Override
            public int getOrder() {
                return -100;
            }
        };
    }

}
