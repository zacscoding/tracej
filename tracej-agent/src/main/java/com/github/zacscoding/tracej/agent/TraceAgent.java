package com.github.zacscoding.tracej.agent;

import java.lang.instrument.Instrumentation;

import com.github.zacscoding.tracej.agent.config.Config;

/**
 * Agent for premain.
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TraceAgent {
    private static Instrumentation instrumentation;

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        if (instrumentation != null) {
            return;
        }

        try {
            instrumentation = inst;
            instrumentation.addTransformer(new TraceAgentTransformer());
        } catch (Throwable t) {
            LOGGER.error("failed to premain in Agent", t);
            Config.INSTANCE.setError(true);
        }
    }
}
