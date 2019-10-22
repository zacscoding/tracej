package com.github.zacscoding.tracej.agent.trace;

import java.util.ArrayList;
import java.util.List;

/**
 * Context of traced method
 *
 * @GitHub : https://github.com/zacscoding
 */
public class MethodContext {

    // trace id
    private String id;
    // start time of method
    private long startTime;
    // end time of method
    private long endTime;
    // param values in calling method
    private List<String> params = new ArrayList<String>();
    // return value by called method
    private String returnValue;
    // thrown exception by called method
    private Throwable exception;
    // depth in call stack
    private int depth;

    public MethodContext(String id, long startTime) {
        this.id = id;
        this.startTime = startTime;
    }

    public void appendParam(Object obj) {
        params.add(obj == null ? "null" : obj.toString());
    }

    public void appendReturnValue(Object obj, long endTime) {
        returnValue = obj == null ? "null" : obj.toString();
        this.endTime = endTime;
    }

    // getters, setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
