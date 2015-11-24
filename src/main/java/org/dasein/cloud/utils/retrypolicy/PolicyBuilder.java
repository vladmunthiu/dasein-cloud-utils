package org.dasein.cloud.utils.retrypolicy;


import org.apache.commons.collections.Predicate;
import org.dasein.cloud.utils.retrypolicy.actions.Action;
import org.dasein.cloud.utils.retrypolicy.actions.Action1;
import org.dasein.cloud.utils.retrypolicy.actions.Func1;
import org.dasein.cloud.utils.retrypolicy.retry.RetryPolicy;
import org.dasein.cloud.utils.retrypolicy.retry.RetryPolicyState;
import org.dasein.cloud.utils.retrypolicy.retry.RetryPolicyStateWithCount;
import org.dasein.cloud.utils.retrypolicy.retry.RetryPolicyStateWithSleep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmunthiu on 11/19/2015.
 */
public class PolicyBuilder {
    private List<Predicate> exceptionPredicates;

    PolicyBuilder(Predicate exceptionPredicate) {
        exceptionPredicates = new ArrayList<Predicate>();
        exceptionPredicates.add(exceptionPredicate);
    }

    public <T extends Exception> PolicyBuilder or(final Class<T> exceptionType) {
        Predicate exceptionPredicate = new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return object.getClass() == exceptionType;
            }
        };

        exceptionPredicates.add(exceptionPredicate);
        return this;
    }

    public <T extends Exception> PolicyBuilder or(final Class<T> exceptionType, final Func1<T, Boolean> func) {
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

    public Policy retry() {
        return retry(1);
    }

    public Policy retry(final Integer retryCount) throws IllegalArgumentException {
        if(retryCount <= 0)
            throw new IllegalArgumentException("rertyCount should be a positive integer");

        return new Policy(new Action1<Action>() {
            @Override
            public void call(Action target) throws Exception {
                RetryPolicy.execute(target, exceptionPredicates, new RetryPolicyStateWithCount(retryCount));
            }
        });
    }

    public Policy retryAndWait(final Iterable<Long> sleepIntervals) {
        if(sleepIntervals == null)
            throw new IllegalArgumentException("sleepIntervals cannot be null");

        return new Policy(new Action1<Action>() {
            @Override
            public void call(Action target) throws Exception {
                RetryPolicy.execute(target, exceptionPredicates, new RetryPolicyStateWithSleep(sleepIntervals));
            }
        });
    }

    public Policy retryForever() {
        final RetryPolicyState retryForeverPolicyState = new RetryPolicyState() {
            @Override
            public boolean canRetry(Exception ex) {
                return true;
            }
        };

        return new Policy(new Action1<Action>() {
            @Override
            public void call(Action target) throws Exception {
                RetryPolicy.execute(target, exceptionPredicates, retryForeverPolicyState);
            }
        });
    }
}