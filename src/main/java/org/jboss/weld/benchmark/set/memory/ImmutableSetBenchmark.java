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
package org.jboss.weld.benchmark.set.memory;

import java.util.Arrays;

import org.jboss.weld.benchmark.Benchmark;

import com.javamex.classmexer.MemoryUtil;

public class ImmutableSetBenchmark implements Benchmark {

    public static void main(String... args) {
        new ImmutableSetBenchmark().run();
    }

    public void run() {
        System.out.println("\nWeld ImmutableSet memory benchmark. Sizes are in bytes.\n");
        for (int i = 0; i < 10; i++) {
            System.out.println("Collection size: " + i);
            testSize(i);
            System.out.println();
        }
        for (int i = 10; i < 100; i+=10) {
            System.out.println("Collection size: " + i);
            testSize(i);
            System.out.println();
        }
        for (int i = 100; i < 1000; i+=100) {
            System.out.println("Collection size: " + i);
            testSize(i);
            System.out.println();
        }
        for (int i = 1000; i <= 10000; i+=1000) {
            System.out.println("Collection size: " + i);
            testSize(i);
            System.out.println();
        }
    }

    private void testSize(int size) {
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++) {
            array[i] = new Object();
        }
        Result[] results = new Result[] { compute("JDK HashSet default", Utils.newHashSet(false, array)),
                compute("JDK HashSet optimized", Utils.newHashSet(true, array)), compute("Weld", org.jboss.weld.util.collections.ImmutableSet.of(array)),
                compute("Guava", com.google.common.collect.ImmutableSet.copyOf(array)), };
        Arrays.sort(results);
        for (Result result : results) {
            System.out.print(result + " | ");
        }
    }

    private Result compute(String name, Object object) {
        return new Result(name, MemoryUtil.deepMemoryUsageOf(object));
    }

    private static class Result implements Comparable<Result> {
        private final String name;
        private final long size;

        private Result(String name, long size) {
            super();
            this.name = name;
            this.size = size;
        }

        @Override
        public int compareTo(Result o) {
            return (int) (size - o.size);
        }

        @Override
        public String toString() {
            return name + ": " + size;
        }

    }
}
