package org.dasein.cloud.utils.retrypolicy;

import org.apache.commons.collections.Predicate;
import org.dasein.cloud.utils.retrypolicy.actions.Action;
import org.dasein.cloud.utils.retrypolicy.actions.Action1;
import org.dasein.cloud.utils.retrypolicy.actions.Func;
import org.dasein.cloud.utils.retrypolicy.actions.Func1;

/**
 * Created by vmunthiu on 11/19/2015.
 */
public class Policy {
    private Action1<Action> exceptionPolicy;

    Policy(final Action1<Action> exceptionPolicy) {
        if(exceptionPolicy == null)
            throw new IllegalArgumentException("exceptionPolicy argument cannot be null");

        this.exceptionPolicy = exceptionPolicy;
    }

    public void execute(final Action action) throws Exception {
        exceptionPolicy.call(action);
    }

    public <T> T execute(final Func<T> func) throws Exception {
        final Object[] result = new Object[1];

        exceptionPolicy.call(new Action() {
            @Override
            public void call() throws Exception {
                result[0] = func.call();
            }
        });

        return (T)result[0];
    }

    public static <T extends Exception> PolicyBuilder handle(final Class<T> exceptionType){
        Predicate exceptionPredicate = new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return object.getClass() == exceptionType;
            }
        };

        return new PolicyBuilder(exceptionPredicate);
    }

    public static <T extends Exception> PolicyBuilder handle(final Class<T> exceptionType, final Func1<T, Boolean> func ) throws Exception {
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

        return new PolicyBuilder(exceptionPredicate);
    }
}
