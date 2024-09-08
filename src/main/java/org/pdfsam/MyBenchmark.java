/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pdfsam;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import org.sejda.sambox.cos.COSFloat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
public class MyBenchmark {


    List<String> data = Arrays.asList(
            // 80% "Good" strings
            "1.234", "0.0", "-123.456", "123.456", "999.99", "-999.99", "1.0", "0.123", "10.01",
            "-10.01", "100.12345", "-100.12345", "1234.56789", "0.0001", "-0.0001", "987654.321",
            "-987654.321", "3.14159", "-3.14159", "0.9999", "1E2", "1.23E4", "-1.23E4", "4.56e2",
            "-4.56e2", "5.0E-6", "-5.0E-6", "7E10", "-7E10", "0.333", "-0.333", "1.00000001",
            "-1.00000001", "123456789.123456789", "0.0000001", "2.7182818", "-2.7182818", "0.7777777",
            "-0.7777777", "1000000.0", "1.23456789", "-1.23456789", "1.9", "-1.9", "6.022E23",
            "-6.022E23", "1234567890.0987654321", "9.87654321", "0.5", "0.25", "0.125", "-0.125",
            "0.0625", "123.4567890123456789", "-123.4567890123456789", "987654321.123456789",
            "-987654321.123456789", "0.0000000001", "-0.0000000001", "0.9999999999", "0.1010101010",
            "-0.1010101010", "1.0101010101", "123.999999999", "-123.999999999", "0.9999999999E-5",
            "1E-5", "3.5", "-3.5", "456789.999", "9876543210.987654321", "-9876543210.987654321",
            "0.000000987654", "5.5555555555", "-5.5555555555", "0.010101", "8.888888888",
            "-8.888888888", "0.123456789", "-0.123456789"

            // 20% "Bad" strings (for exceptions)
            //not handled by pdfbox "123.45.67","999E", "1.23E", "-++123.45", "123e45e", "12e3e","-+123.45",
            //"415.75.795", "1.23.45", "-123.45.67", "-123..456",
            //handled by pdfbox
          //  "0.-345",  "0.00000-123",  "--123.45",
          //  "0.-1", "0.0000-567",
          //  "0.0-5678", "0.-456", "0.0000-123", "0.-999999", "0.0-98765"
    );

    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 25, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void sambox() throws IOException {
        for(String s : data) {
            var cosFloat = new COSFloat(s);
        }
    }

    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 25, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void pdfbox() throws IOException {
        for(String s : data) {
            var cosFloat = new org.apache.pdfbox.cos.COSFloat(s);
        }
    }

    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 25, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void samboxFloat() throws IOException {
        for(String s : data) {
            var cosFloat = new org.pdfsam.COSFloat(s);
        }
    }

    /*
     * ============================== HOW TO RUN THIS TEST: ====================================
     *
     * Note JMH honors the default annotation settings. You can always override
     * the defaults via the command line or API.
     *
     * You can run this test:
     *
     * a) Via the command line:
     *    $ mvn clean install
     *    $ java -jar target/benchmarks.jar JMHSample_20
     *
     * b) Via the Java API:
     *    (see the JMH homepage for possible caveats when running from IDE:
     *      http://openjdk.java.net/projects/code-tools/jmh/)
     */

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

}
