# dasein-cloud-utils
Home for a variety of helper and utility classes

*RetryPolicy usage*

 Utility classes for retry policies

```
Action doSomethingAction = new Action() {
    @Override
    public void call() throws Exception {
        doSomething();
    }
};

//if doSomething method throws SocketException or CloudException, retry executing doSomething 2 more times;
RetryPolicy.handle(SocketException.class).or(CloudException.class).retry(2).execute(doSomethingAction);
//if doSomething method throws SockectException or CloudException, retry executing doSomething 3 more times with 1s, 2s and 3s wait in between retries
RetryPolicy.handle(SocketException.class).or(CloudException.class).retryAndWait(Arrays.asList(1000L, 2000L, 3000L)).execute(doSomethingAction);

Func1<CloudException, Boolean> cloudExceptionFunc = new Func1<CloudException, Boolean>() {
    @Override
    public Boolean call(CloudException target) throws Exception {
        return target.getHttpCode() == 500;
    }
};

//if doSomething throws CloudException and on that CloudException instance getHttpCode() == 500, retry executing doSomething 2 more times
RetryPolicy.handle(CloudException.class, cloudExceptionFunc).retry(2).execute(doSomethingAction);
```