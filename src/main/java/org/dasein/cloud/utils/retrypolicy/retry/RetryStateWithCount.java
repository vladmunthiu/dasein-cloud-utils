package org.dasein.cloud.utils.retrypolicy.retry;

import org.dasein.cloud.utils.retrypolicy.actions.Action;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public class RetryStateWithCount implements RetryState {
    private int errorCount;
    private int retryCount;
    private Action onRetryAction;

    public RetryStateWithCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public RetryStateWithCount(int retryCount, Action onRetryAction) {
        this.retryCount = retryCount;
        this.onRetryAction = onRetryAction;
    }

    @Override
    public boolean canRetry(Exception ex) throws Exception {
        this.errorCount += 1;

        boolean canRetry = errorCount <= retryCount;
        if(canRetry && this.onRetryAction != null) {
            this.onRetryAction.call();
        }

        return canRetry;
    }
}
