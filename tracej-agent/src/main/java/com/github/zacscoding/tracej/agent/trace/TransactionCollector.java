package com.github.zacscoding.tracej.agent.trace;

import java.util.List;

import com.github.zacscoding.tracej.agent.LOGGER;

/**
 * Collector of traced transactions
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionCollector {

    /**
     * Start to trace method with given id
     */
    public static void startTransaction(String id) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            LOGGER.error("Cannot create a TransactionContext after called getOrCreateContext()");
            return;
        }

        MethodContext methodCtx = new MethodContext(id, System.currentTimeMillis());
        ctx.startMethod(methodCtx);
    }

    /**
     * Adds a param value to current tracing method context
     */
    public static void appendParam(Object value) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            LOGGER.error("appendParam(Object) is called although not exit tx ctx");
            return;
        }

        ctx.appendParam(value);
    }

    /**
     * Adds a return value to current tracing method context
     */
    public static void appendReturnValue(Object value) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            LOGGER.error("appendReturnValue(Object) is called although not exit tx ctx");
            return;
        }

        ctx.appendReturnValue(value, System.currentTimeMillis());
    }

    /**
     * Stop to trace method
     */
    public static void endTransaction() {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            System.err.println("endTransaction() is called although not exist TransactionContext");
            return;
        }

        ctx.endMethod();

        if (ctx.hasTrace()) {
            return;
        }

        // TEMP for dev
        displayTracedCallStack(ctx);
    }

    private static void displayTracedCallStack(TransactionContext ctx) {
        synchronized (System.out) {
            List<MethodContext> methods = ctx.getMethods();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < methods.size(); i++) {
                MethodContext methodCtx = methods.get(i);

                // append depth
                int depth = methodCtx.getDepth();
                String depthPrefix = "";

                while (depth-- > 0) {
                    depthPrefix += "|  ";
                }

                sb.append(depthPrefix)
                  .append("+--")
                  .append(methodCtx.getId())
                  .append('[')
                  .append(methodCtx.getEndTime() - methodCtx.getStartTime())
                  .append("ms] : ")
                  .append(methodCtx.getReturnValue())
                  .append('\n');

                List<String> params = methodCtx.getParams();

                /*if (i == 0 && !params.isEmpty()) {
                    depthPrefix += "|  ";
                }*/

                for (int j = 0; j < params.size(); j++) {
                    sb.append(depthPrefix)
                      .append(" -- ")
                      .append(j + 1)
                      .append(" : ")
                      .append(params.get(j))
                      .append('\n');
                }
            }

            System.out.println(sb);
        }
    }
}
