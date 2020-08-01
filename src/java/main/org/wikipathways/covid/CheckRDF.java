package org.wikipathways.covid;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;

import nl.unimaas.bigcat.wikipathways.curator.assertions.*;
import nl.unimaas.bigcat.wikipathways.curator.tests.*;
import nl.unimaas.bigcat.wikipathways.curator.SPARQLHelper;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class CheckRDF {

    public static void main(String[] args) throws Exception {
        String gpmlFile = args[0];
        String wpid     = gpmlFile.substring(9,gpmlFile.indexOf(".ttl"));
        System.out.println("# WikiPathways " + wpid + "\n");
        System.out.println("GPML file: [" + gpmlFile + "](" + gpmlFile + ")\n");
        List<IAssertion> assertions = new ArrayList<IAssertion>();
        Model loadedData = ModelFactory.createDefaultModel();
        loadedData.read(new FileInputStream(new File(gpmlFile)), "", "TURTLE");

        SPARQLHelper helper = new SPARQLHelper(loadedData);
        assertions.addAll(GeneTests.all(helper));
        assertions.addAll(InteractionTests.all(helper));

        System.out.println("## Tests");

        int failed = 0;
        int testClasses = 0;
        int tests = 0;
        String currentTestClass = "";
        String currentTest = "";
        String message = "";
        String errors = "";
        for (IAssertion assertion : assertions) {
            if (assertion.getTestClass() != currentTestClass) {
                currentTestClass = assertion.getTestClass();
                currentTest = "";
                testClasses++;
                if (!errors.isEmpty()) message += "\n" + errors;
                if (!message.isEmpty()) System.out.println(message);
                message = "";
                System.out.println("\n* " + currentTestClass);
            }
            if (assertion.getTest() != currentTest) {
                currentTest = assertion.getTest();
                if (!message.isEmpty()) System.out.println(message);
                message = "** " + currentTest + ": ";
                errors = "";
                tests++;
            }
            if (assertion instanceof AssertEquals) {
                AssertEquals typedAssertion = (AssertEquals)assertion;
                if (!typedAssertion.getExpectedValue().equals(typedAssertion.getValue())) {
                   message += "x";
                   errors += "*** " + typedAssertion.getMessage();
                   failed++;
                } else {
                    message += ".";
                }
            } else if (assertion instanceof AssertNotNull) {
                AssertNotNull typedAssertion = (AssertNotNull)assertion;
                if (typedAssertion.getValue() == null) {
                   message += "x";
                   errors += "*** Unexpected null found";
                   failed++;
                } else {
                    message += ".";
                }
            } else {
                errors += "*** Unrecognized assertion type: " + assertion.getClass().getName();
                failed++;
            }
        }
        if (!message.isEmpty()) System.out.println(message);
        System.out.println("\n## Summary\n");
        System.out.println("* Number of test classes: " + testClasses);
        System.out.println("* Number of tests: " + tests);
        System.out.println("* Number of assertions: " + assertions.size());
        System.out.println("* Number of fails: " + failed);
    }

}
