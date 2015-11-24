package org.dasein.cloud.utils.retrypolicy.retry;


import org.apache.commons.collections.Predicate;
import org.dasein.cloud.utils.retrypolicy.actions.Action;

import java.util.List;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public class Retry {
    public static void execute(Action action, List<Predicate> exceptionPredicates, RetryState retryState) throws Exception{
        while(true) {
            try {
                action.call();
                return;
            } catch (Exception ex) {
                if(!shouldRetry(ex, exceptionPredicates)) {
                    throw ex;
                }
                if(!retryState.canRetry(ex)) {
                    throw ex;
                }
            }
        }
    }

    private static boolean shouldRetry(Exception exception, List<Predicate> retryPredicates) {
        for (Predicate retryPredicate : retryPredicates ) {
            if(retryPredicate.evaluate(exception)) {
                return true;
            }
        }
        return false;
    }
}
