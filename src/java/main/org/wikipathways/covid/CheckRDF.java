package org.wikipathways.covid;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;

import nl.unimaas.bigcat.wikipathways.curator.assertions.*;
import nl.unimaas.bigcat.wikipathways.curator.tests.GeneTests;
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
        assertions.addAll(GeneTests.entrezGeneIdentifiersNotNumber(helper));
        
        int failed = 0;
        for (IAssertion assertion : assertions) {
            if (assertion instanceof AssertEquals) {
                AssertEquals typedAssertion = (AssertEquals)assertion;
                if (!typedAssertion.getExpectedValue().equals(typedAssertion.getValue())) {
                    System.out.println(typedAssertion.getMessage());
                    failed++;
                }
            } else if (assertion instanceof AssertNotNull) {
                AssertNotNull typedAssertion = (AssertNotNull)assertion;
                if (typedAssertion.getValue() == null) {
                    System.out.println("Unexpected null found");
                    failed++;
                }
            } else {
                System.out.println("Unrecognized assertion type: " + assertion.getClass().getName());
                failed++;
            }
        }
        System.out.println("## Summary\n");
        System.out.println("* Number of assertions: " + assertions.size());
        System.out.println("* Number of fails: " + failed);
    }

}
