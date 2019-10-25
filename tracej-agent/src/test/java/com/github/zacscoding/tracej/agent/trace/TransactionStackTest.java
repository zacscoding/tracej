package com.github.zacscoding.tracej.agent.trace;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionStackTest {

    /*
    --stack of calling methods--
    method1("arg1", "arg2"):"retmethod1" (1)
        method2("arg11"):"retmethod2"    (2)
            method22():null              (3)
        method3("arg12"):null:           (4)
     */
    @Test
    public void pushAndPop() {
        TransactionContext transaction;
        MethodContext method;

        // when1-1) start method1
        TransactionStack.pushTransaction("method1");
        TransactionStack.appendParam("arg1");
        TransactionStack.appendParam("arg2");

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertTrue(transaction.hasTrace());
        assertNotNull(transaction);

        method = transaction.peek();
        assertThat(method.getId(), is("method1"));
        assertTrue(method.getParams().size() == 2);
        assertThat(method.getParams().get(0), is("arg1"));
        assertThat(method.getParams().get(1), is("arg2"));
        assertTrue(method.getDepth() == 0);

        // when2-1) start method2
        TransactionStack.pushTransaction("method2");
        TransactionStack.appendParam("arg11");

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertTrue(transaction.hasTrace());
        assertNotNull(transaction);

        method = transaction.peek();
        assertThat(method.getId(), is("method2"));
        assertTrue(method.getParams().size() == 1);
        assertThat(method.getParams().get(0), is("arg11"));
        assertTrue(method.getDepth() == 1);

        // when3-1) start method22
        TransactionStack.pushTransaction("method22");

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertTrue(transaction.hasTrace());
        assertNotNull(transaction);

        method = transaction.peek();
        assertThat(method.getId(), is("method22"));
        assertTrue(method.getParams().size() == 0);
        assertTrue(method.getDepth() == 2);

        // when3-2) end method22
        TransactionStack.appendReturnValue(null);
        TransactionStack.popTransaction();

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertTrue(transaction.hasTrace());
        assertNotNull(transaction);

        method = transaction.peek();
        assertThat(method.getId(), is("method2"));

        // when2-2) end method2
        TransactionStack.appendReturnValue("retmethod2");
        TransactionStack.popTransaction();

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertTrue(transaction.hasTrace());
        assertNotNull(transaction);

        method = transaction.peek();
        assertThat(method.getId(), is("method1"));

        // when 4-1) start method3
        TransactionStack.pushTransaction("method3");
        TransactionStack.appendParam("arg12");

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertTrue(transaction.hasTrace());
        assertNotNull(transaction);

        method = transaction.peek();
        assertThat(method.getId(), is("method3"));
        assertTrue(method.getParams().size() == 1);
        assertThat(method.getParams().get(0), is("arg12"));
        assertTrue(method.getDepth() == 1);

        // when 4-2) end method3
        TransactionStack.appendReturnValue(null);
        TransactionStack.popTransaction();

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertTrue(transaction.hasTrace());
        assertNotNull(transaction);

        method = transaction.peek();
        assertThat(method.getId(), is("method1"));

        // 1-2) end method1
        TransactionStack.appendReturnValue("retmethod1");
        TransactionStack.popTransaction();

        // then
        transaction = TransactionContextManager.getOrCreateContext();
        assertFalse(transaction.hasTrace());
        assertNotNull(transaction);

        assertFalse(transaction.hasTrace());
        assertTrue(transaction.getMethods().size() == 4);
        for (MethodContext methodContext : transaction.getMethods()) {
            String expectedRet = null;

            if (methodContext.getId().equals("method1")) {
                expectedRet = "retmethod1";
            } else if (methodContext.getId().equals("method2")) {
                expectedRet = "retmethod2";
            } else if (methodContext.getId().equals("method22")) {
                expectedRet = "null";
            } else if (methodContext.getId().equals("method3")) {
                expectedRet = "null";
            } else {
                fail();
            }

            assertThat(methodContext.getReturnValue(), is(expectedRet));
        }

    }
}
