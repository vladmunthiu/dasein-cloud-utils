package org.dasein.cloud.utils.retrypolicy.retry;

import org.dasein.cloud.utils.retrypolicy.actions.Action;

import java.util.Iterator;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public class RetryStateWithSleep implements RetryState {
    private Iterator<Long> sleepDurations;
    private Action onRetryAction;

    public RetryStateWithSleep(Iterable<Long> sleepDurations) {
        this.sleepDurations = sleepDurations.iterator();
    }

    public RetryStateWithSleep(Iterable<Long> sleepDurations, Action onRetryAction) {
        this.sleepDurations = sleepDurations.iterator();
        this.onRetryAction = onRetryAction;
    }

    @Override
    public boolean canRetry(Exception ex) throws Exception {
        if(!this.sleepDurations.hasNext())
            return false;

        Long currentTimeStamp = sleepDurations.next();
        try {
            Thread.sleep(currentTimeStamp);
        } catch (InterruptedException e) {
            return false;
        }

        if(onRetryAction != null) {
            onRetryAction.call();
        }

        return true;
    }
}
