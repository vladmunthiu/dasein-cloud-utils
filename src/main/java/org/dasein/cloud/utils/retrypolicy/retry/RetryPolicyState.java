package org.dasein.cloud.utils.retrypolicy.retry;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public interface RetryPolicyState {
    boolean canRetry(Exception ex);
}
