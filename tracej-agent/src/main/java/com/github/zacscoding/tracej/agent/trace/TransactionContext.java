package com.github.zacscoding.tracej.agent.trace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.github.zacscoding.tracej.agent.LOGGER;

/**
 * Context of traced transaction
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionContext {

    // caller
    private String caller;

    // caller of stack traces in current thread e.g
    private String callerStackTrace;

    // called method
    private List<MethodContext> methods = new LinkedList<MethodContext>();

    // stack of calling method
    public Stack<MethodContext> callStack = new Stack<MethodContext>();

    /**
     * Returns traced methods
     */
    public List<MethodContext> getMethods() {
        return new ArrayList<MethodContext>(methods);
    }

    /**
     * Returns depth of stacks
     */
    public int getDepth() {
        return callStack.size();
    }

    /**
     * Returns a caller of this transaction
     */
    public String getCaller() {
        return caller;
    }

    /**
     * Settings a this transaction's caller
     */
    public void setCaller(String caller) {
        this.caller = caller;
    }

    /**
     * Returns stack trace json array string from caller
     */
    public String getCallerStackTrace() {
        return callerStackTrace;
    }

    /**
     * Setting a stack trace json array string
     */
    public void setCallerStackTrace(String callerStackTrace) {
        this.callerStackTrace = callerStackTrace;
    }

    /**
     * Adds a param value to peek of call stack i.e last called method
     */
    public void appendParam(Object value) {
        if (callStack.isEmpty()) {
            LOGGER.error("appendParam(Object) called although has no trace method call");
            return;
        }

        callStack.peek().appendParam(value);
    }

    /**
     * Adds a return value and sets a end time to peek of call stack method
     */
    public void appendReturnValue(Object value, long endTime) {
        if (callStack.isEmpty()) {
            LOGGER.error("appendReturnValue(Object,long) called although has no trace method call");
            return;
        }

        callStack.peek().appendReturnValue(value, endTime);
    }

    /**
     * Start to trace a new called method
     */
    public void startMethod(MethodContext methodContext) {
        if (methodContext == null) {
            LOGGER.error("received null method context");
            return;
        }

        methodContext.setDepth(callStack.size());
        callStack.push(methodContext);
        methods.add(methodContext);
    }

    /**
     * Complete to trace a peek method
     */
    public void endMethod() {
        if (callStack.isEmpty()) {
            LOGGER.error("endMethod called with empty stack.");
            return;
        }

        callStack.pop();
    }

    /**
     * Returns whether remain call stack or not.
     */
    public boolean hasTrace() {
        return !callStack.isEmpty();
    }

    // for tests
    MethodContext peek() {
        return callStack.isEmpty() ? null : callStack.peek();
    }
}
