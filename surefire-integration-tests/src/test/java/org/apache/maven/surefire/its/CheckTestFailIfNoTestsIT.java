package org.apache.maven.surefire.its;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.List;

/**
 * Test failIfNoTests
 *
 * @author <a href="mailto:dfabulich@apache.org">Dan Fabulich</a>
 */
public class CheckTestFailIfNoTestsIT
    extends AbstractSurefireIntegrationTestClass
{
    public void testFailIfNoTests()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/default-configuration-noTests" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        List<String> goals = this.getInitialGoals();
        goals.add( "test" );
        goals.add( "-DfailIfNoTests" );

        try
        {
            executeGoals( verifier, goals );
            verifier.resetStreams();
            verifier.verifyErrorFreeLog();
            fail( "Build didn't fail, but it should" );
        }
        catch ( VerificationException e )
        {
            // as expected
        }
        finally
        {
            verifier.resetStreams();
        }

    }

    public void testDontFailIfNoTests()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/default-configuration-noTests" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        this.executeGoal( verifier, "test" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        File reportsDir = new File( testDir, "target/surefire-reports" );
        assertFalse( "Unexpected reports directory", reportsDir.exists() );
    }

}
