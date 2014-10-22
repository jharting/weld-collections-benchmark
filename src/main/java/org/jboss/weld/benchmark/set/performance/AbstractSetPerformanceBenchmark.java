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
package org.jboss.weld.benchmark.set.performance;

import java.util.Iterator;
import java.util.Set;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
abstract class AbstractSetPerformanceBenchmark {

    private static final String[] MISS_OBJECTS;

    static {
        MISS_OBJECTS = new String[100];
        for (int i = 0; i < MISS_OBJECTS.length; i++) {
            MISS_OBJECTS[i] = AbstractSetPerformanceBenchmark.class.getName() + (7 * i);
        }
    }

    private final Set<Object> instance;
    private final Object[] data;

    protected AbstractSetPerformanceBenchmark() {
        this.instance = getInstance();
        this.data = getData();
    }

    protected abstract Set<Object> getInstance();

    protected Object[] getData() {
        final int setSize = Integer.getInteger("org.jboss.weld.benchmark.setSize");
        System.out.println("Generating test set. Size: " + setSize);
        Object[] objects = new Object[setSize];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = "foobarbazqux" + (i * 7);
        }
        return objects;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public boolean testSetHit() {
        for (Object object : data) {
            if (!instance.contains(object)) {
                throw new IllegalStateException();
            }
        }
        return true;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public boolean testSetMiss() {
        for (String string : MISS_OBJECTS) {
            if (instance.contains(string)) {
                throw new IllegalStateException();
            }
        }
        return true;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public boolean testIterator() {
        Iterator<Object> iterator = instance.iterator();
        Object object = null;
        for (int i = 0; i < instance.size(); i++) {
            if (!iterator.hasNext()) {
                throw new IllegalStateException(object.toString());
            }
            object = iterator.next();
        }
        return !iterator.hasNext();
    }
}
