package org.wikipathways.covid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.bio.DataSourceTxt;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.wikipathways.wp2rdf.io.PathwayReader;
import org.wikipathways.wp2rdf.GpmlConverter;
import org.wikipathways.wp2rdf.WPREST2RDF;

import com.hp.hpl.jena.rdf.model.Model;

public class CreateRDF {

    public static void main(String[] args) throws Exception {
        DataSourceTxt.init();
        // the next line is needed until BridgeDb gets updated
        DataSource.register("Cpx", "Complex Portal")
          .identifiersOrgBase("http://identifiers.org/complexportal/")
          .asDataSource();
        DataSource.register("Pbd", "Digital Object Identifier").asDataSource();
        DataSource.register("Pbm", "PubMed").asDataSource();
        DataSource.register("Gpl", "Guide to Pharmacology Targets").asDataSource();
        InputStream input = new FileInputStream(args[0]);
        Pathway pathway = PathwayReader.readPathway(input);
        IDMapperStack stack = WPREST2RDF.maps();
        Model model = GpmlConverter.convertWp(pathway, "WP1111", "111111", stack, Collections.<String>emptyList());
        input.close();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        model.write(output, "TURTLE");
        System.out.print(new String(output.toByteArray()));
    }

}
