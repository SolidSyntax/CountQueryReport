/*
 * 
 *Copyright 2015 Solid Syntax (www.SolidSyntax.net)
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package net.solidsyntax.jdbc.pool.interceptor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CountQueryReportTest {

    @Before
    public void clearReportForTests() {
        CountQueryReport.resetCount();
    }

    @Test
    public void prepareStatement_increasesNumberOfStatements() {
        CountQueryReport reportInstance = new CountQueryReport();

        Assert.assertEquals("Expected 0 statements to have run",
                0, CountQueryReport.numberOfStatements());

        reportInstance.prepareStatement("", 0);
        reportInstance.prepareStatement("", 0);

        Assert.assertEquals("Expected 2 statements after invoking 'prepareStatement'",
                2, CountQueryReport.numberOfStatements());
    }

    @Test
    public void resetCount_SetNumberOfStatementsTo0() {
        CountQueryReport reportInstance = new CountQueryReport();

        reportInstance.prepareStatement("", 0);
        reportInstance.prepareStatement("", 0);

        Assert.assertEquals(2, CountQueryReport.numberOfStatements());

        CountQueryReport.resetCount();

        Assert.assertEquals(0, CountQueryReport.numberOfStatements());

    }

    @Test
    public void numberOfStatements_countsForEachThread() throws InterruptedException, ExecutionException {
        final CountQueryReport reportInstance = new CountQueryReport();

        reportInstance.prepareStatement("", 0);
        reportInstance.prepareStatement("", 0);

        Future<?> worker = Executors.newFixedThreadPool(1).submit(new Runnable() {
            @Override
            public void run() {
                reportInstance.prepareStatement("", 1);
                reportInstance.prepareStatement("", 1);
                reportInstance.prepareStatement("", 1);
                Assert.assertEquals("Expected 2 statement in the worker thread",
                        3, CountQueryReport.numberOfStatements());
            }
        });

        worker.get();

        Assert.assertEquals("Expected 2 statements in the main thread",
                2, CountQueryReport.numberOfStatements());
    }

}
