package org.dasein.cloud.utils.retrypolicy;


import org.apache.commons.collections.Predicate;
import org.dasein.cloud.utils.retrypolicy.actions.Action;
import org.dasein.cloud.utils.retrypolicy.actions.Action1;
import org.dasein.cloud.utils.retrypolicy.actions.Func1;
import org.dasein.cloud.utils.retrypolicy.retry.Retry;
import org.dasein.cloud.utils.retrypolicy.retry.RetryState;
import org.dasein.cloud.utils.retrypolicy.retry.RetryStateWithCount;
import org.dasein.cloud.utils.retrypolicy.retry.RetryStateWithSleep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmunthiu on 11/19/2015.
 */
public class RetryPolicyBuilder {
    private List<Predicate> exceptionPredicates;

    RetryPolicyBuilder(Predicate exceptionPredicate) {
        exceptionPredicates = new ArrayList<Predicate>();
        exceptionPredicates.add(exceptionPredicate);
    }

    public <T extends Exception> RetryPolicyBuilder or(final Class<T> exceptionType) {
        Predicate exceptionPredicate = new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return object.getClass() == exceptionType;
            }
        };

        exceptionPredicates.add(exceptionPredicate);
        return this;
    }

    public <T extends Exception> RetryPolicyBuilder or(final Class<T> exceptionType, final Func1<T, Boolean> func) {
        Predicate exceptionPredicate = new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                try {
                    return object.getClass() == exceptionType && func.call((T)object);
                } catch (Exception e) {
                    return false;
                }
            }
        };

        exceptionPredicates.add(exceptionPredicate);
        return this;
    }

    public RetryPolicy retry() {
        return retry(1);
    }

    public RetryPolicy retry(final Integer retryCount) throws IllegalArgumentException {
        if(retryCount <= 0)
            throw new IllegalArgumentException("rertyCount should be a positive integer");

        return new RetryPolicy(new Action1<Action>() {
            @Override
            public void call(Action target) throws Exception {
                Retry.execute(target, exceptionPredicates, new RetryStateWithCount(retryCount));
            }
        });
    }

    public RetryPolicy retryAndWait(final Iterable<Long> sleepIntervals) {
        if(sleepIntervals == null)
            throw new IllegalArgumentException("sleepIntervals cannot be null");

        return new RetryPolicy(new Action1<Action>() {
            @Override
            public void call(Action target) throws Exception {
                Retry.execute(target, exceptionPredicates, new RetryStateWithSleep(sleepIntervals));
            }
        });
    }

    public RetryPolicy retryForever() {
        final RetryState retryForeverPolicyState = new RetryState() {
            @Override
            public boolean canRetry(Exception ex) {
                return true;
            }
        };

        return new RetryPolicy(new Action1<Action>() {
            @Override
            public void call(Action target) throws Exception {
                Retry.execute(target, exceptionPredicates, retryForeverPolicyState);
            }
        });
    }
}