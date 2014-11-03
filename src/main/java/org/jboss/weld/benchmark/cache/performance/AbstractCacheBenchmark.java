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
package org.jboss.weld.benchmark.cache.performance;

import java.math.BigInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public abstract class AbstractCacheBenchmark<C> {

    protected final Long complexity;

    private final C cache;

    protected AbstractCacheBenchmark() {
        this.complexity = Long
                .getLong("org.jboss.weld.benchmark.cache.complexity");
        this.cache = getCache();
    }

    protected abstract C getCache();

    protected abstract Foo getValue(C cache, Foo key);

    protected Foo compute(Foo key) {
        if (complexity != null) {
            factorial((complexity + (key.getValue() % 10)));
        }
        return key;
    }

    protected int getMaxSize() {
        return 1 << 16;
    }

    @State(Scope.Thread)
    public static class Counter {
        long counter = 0;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public boolean testGetValue(Counter counter) {
        Foo key = new Foo(counter.counter++);
        Foo value = getValue(cache, key);
        return key == value;
    }

    private BigInteger factorial(long input) {
        BigInteger result = BigInteger.ONE;
        for (long i = input; i > 1; i--) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
