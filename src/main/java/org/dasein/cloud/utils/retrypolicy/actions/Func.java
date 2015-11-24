package org.dasein.cloud.utils.retrypolicy.actions;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public interface Func<TResult> {
    TResult call() throws Exception;
}
