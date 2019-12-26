package com.github.zacscoding.tracej.agent.trace;

import java.util.List;

import com.github.zacscoding.tracej.agent.LOGGER;
import com.github.zacscoding.tracej.agent.config.Config;

/**
 * Collector of traced transactions
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionStack {

    /**
     * Start to trace method with given id
     */
    public static void pushTransaction(String id) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            LOGGER.error("Cannot create a TransactionContext after called getOrCreateContext()");
            return;
        }

        if (ctx.getDepth() == 0) {
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            // first elt is a java.lang.Thread.getStackTrace
            // second elt is com.github.zacscoding.tracej.agent.trace.TransactionStack.pushTransaction
            ctx.setCaller(stackTrace[3].toString());

            if (Config.INSTANCE.getLogConfig().isTraceCallStack()) {
                StringBuilder jsonValue = new StringBuilder("[\"").append(stackTrace[3]).append('\"');

                for (int i = 4; i < stackTrace.length; i++) {
                    jsonValue.append(",\"")
                             .append(stackTrace[i].toString())
                             .append('\"');
                }

                ctx.setCallerStackTrace(jsonValue.append(']').toString());
            }
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

    public static void appendException(Throwable throwable) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            LOGGER.error("appendException(Throwable) is called although not exit tx ctx");
            return;
        }

        long endTime = System.currentTimeMillis();

        if (throwable == null) {
            ctx.appendReturnValue("Unknown Exception", endTime);
            return;
        }

        final StringBuilder result = new StringBuilder("Exception(")
                .append(throwable.getClass().getSimpleName())
                .append(") : ")
                .append(throwable.getMessage() == null ? "null" : throwable.getMessage());

        ctx.appendReturnValue(result.toString(), endTime);
    }

    /**
     * Stop to trace method
     */
    public static void popTransaction() {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            System.err.println("endTransaction() is called although not exist TransactionContext");
            return;
        }

        ctx.endMethod();

        if (ctx.hasTrace()) {
            return;
        }

        // TODO : flush transaction stack
        // TEMP for dev
        displayTracedCallStack(ctx);
    }

    private static void displayTracedCallStack(TransactionContext ctx) {
        // TODO : depth prefix
        synchronized (System.out) {
            List<MethodContext> methods = ctx.getMethods();
            StringBuilder sb = new StringBuilder("\n>> Trace method call stack from : ")
                    .append(ctx.getCaller())
                    .append('\n');

            if (Config.INSTANCE.getLogConfig().isTraceCallStack()) {
                sb.append("stack trace : ")
                  .append(ctx.getCallerStackTrace())
                  .append('\n');
            }

            for (int i = 0; i < methods.size(); i++) {
                MethodContext methodCtx = methods.get(i);

                // append depth
                int depth = methodCtx.getDepth();
                String depthPrefix = "";

                while (depth-- > 0) {
                    depthPrefix += "| ";
                }

                sb.append(depthPrefix)
                  .append("+--")
                  .append(methodCtx.getId())
                  .append('[')
                  .append(methodCtx.getEndTime() - methodCtx.getStartTime())
                  .append(" ms] : ")
                  .append(methodCtx.getReturnValue())
                  .append('\n');

                List<String> params = methodCtx.getParams();

                /*if (i == 0 && !params.isEmpty()) {
                    depthPrefix += "|";
                }*/

                for (int j = 0; j < params.size(); j++) {
                    sb.append(depthPrefix)
                      .append(" -- arg")
                      .append(j + 1)
                      .append(" : ")
                      .append(params.get(j))
                      .append('\n');
                }
            }

            sb.append("\n");
            System.out.println(sb);
        }
    }
}
