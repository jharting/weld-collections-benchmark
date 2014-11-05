/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Performance {

    public static void main(String[] args) throws RunnerException {
        String benchmark = System.getProperty("org.jboss.weld.benchmark");
        if (benchmark == null) {
            throw new IllegalArgumentException("You need to specify -Dorg.jboss.weld.benchmark");
        }

        int numberOfThreads = Integer.getInteger("org.jboss.weld.threads", Runtime.getRuntime().availableProcessors());

        switch (benchmark) {
            case "cache": {
                Options opt = new OptionsBuilder().include("org.jboss.weld.benchmark.cache").forks(1).threads(numberOfThreads)
                        .build();
                new Runner(opt).run();
                return;
            }
            case "set": {
                Options opt = new OptionsBuilder().include("org.jboss.weld.benchmark.set").forks(1).threads(1).build();
                new Runner(opt).run();
                return;
            }
            default: throw new IllegalArgumentException("Unknown benchmark: " + benchmark);
        }

    }
}
