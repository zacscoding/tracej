package com.github.zacscoding.tracej.agent.trace;

/**
 * Manage transaction context in current thread
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionContextManager {
    // trace context
    private static ThreadLocal<TransactionContext> contexts = new ThreadLocal<TransactionContext>();

    /**
     * Return a transaction context.
     *
     * If not exist, then create a new TransactionContext instance.
     */
    public static TransactionContext getOrCreateContext() {
        TransactionContext ctx = contexts.get();

        if (ctx == null) {
            ctx = new TransactionContext();
            contexts.set(ctx);
        }

        return contexts.get();
    }

    /**
     * Return a transaction context in current thread
     *
     * @return ctx or null if not exist
     */
    public static TransactionContext getContext() {
        return contexts.get();
    }

    /**
     * Remove transaction context in current thread.
     *
     * @return prev context or null
     */
    public static TransactionContext dispose() {
        TransactionContext ctx = contexts.get();

        if (ctx != null) {
            contexts.set(null);
        }

        return ctx;
    }
}
