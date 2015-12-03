package org.dasein.cloud.utils.retrypolicy.retry;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public interface RetryState {
    boolean canRetry(Exception ex) throws Exception;
}
