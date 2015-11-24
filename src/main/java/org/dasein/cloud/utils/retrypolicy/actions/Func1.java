package org.dasein.cloud.utils.retrypolicy.actions;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public interface Func1<TInput, TResult> {
    TResult call(TInput target) throws Exception;
}
