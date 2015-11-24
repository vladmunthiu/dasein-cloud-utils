package org.dasein.cloud.utils.retrypolicy.retry;

import java.util.Iterator;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public class RetryStateWithSleep implements RetryState {
    private Iterator<Long> sleepDurations;

    public RetryStateWithSleep(Iterable<Long> sleepDurations) {
        this.sleepDurations = sleepDurations.iterator();
    }

    @Override
    public boolean canRetry(Exception ex) {
        if(!this.sleepDurations.hasNext())
            return false;

        Long currentTimeStamp = sleepDurations.next();
        try {
            Thread.sleep(currentTimeStamp);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
