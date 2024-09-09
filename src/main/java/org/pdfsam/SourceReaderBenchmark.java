package org.pdfsam;
/*
 * This file is part of the PDF Black project
 * Created on 09/09/24
 * Copyright 2024 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l.
 * You are not permitted to modify it.
 *
 * PDF Black is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.sejda.io.SeekableSources;
import org.sejda.sambox.input.PDFParser;
import org.sejda.sambox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Andrea Vacondio
 */
public class SourceReaderBenchmark {

    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 25, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void small() throws IOException {
        try (PDDocument document = PDFParser.parse(SeekableSources.seekableSourceFrom(
                (new File("/home/torakiki/Scaricati/pdfa-delete/1.pdf"))))) {
        }
    }

    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 25, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void mixed() throws IOException {
        try (PDDocument document = PDFParser.parse(SeekableSources.seekableSourceFrom(
                (new File("/home/torakiki/Scaricati/pdfa-delete/2.pdf"))))) {
        }
    }

    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 25, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void allDrawings() throws IOException {
        try (PDDocument document = PDFParser.parse(SeekableSources.seekableSourceFrom(
                (new File("/home/torakiki/Scaricati/pdfa-delete/3.pdf"))))) {
        }
    }

    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 25, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public void allText() throws IOException {
        try (PDDocument document = PDFParser.parse(SeekableSources.seekableSourceFrom(
                (new File("/home/torakiki/Scaricati/pdfa-delete/4.pdf"))))) {
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
                .include(SourceReaderBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
