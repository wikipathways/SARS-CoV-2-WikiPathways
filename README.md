# SARS-CoV-2-WikiPathways

Temporary repository of RDF of selected pathways from WikiPathways, supporting the Wikidata bot.
If you like this work and want to cite it, for now, please cite
[this paper](https://www.nature.com/articles/s41597-020-0477-8). If you use specific pathways,
please cite that pathway according to
[the guidelines](https://www.wikipathways.org/index.php/How_to_cite_WikiPathways).

The following steps can be taken to update the content of this repository.

## Step 1: Update the GPML files

Use the `getPathway.sh` bash script to download the pathways of interest:

```shell
bash getPathway.sh WP4846
bash getPathway.sh WP4853
bash getPathway.sh WP4860
bash getPathway.sh WP4861
bash getPathway.sh WP4863
bash getPathway.sh WP4864
bash getPathway.sh WP4868
bash getPathway.sh WP4799
bash getPathway.sh WP4876
bash getPathway.sh WP4877
bash getPathway.sh WP4880
bash getPathway.sh WP4883
bash getPathway.sh WP4884
bash getPathway.sh WP4891
bash getPathway.sh WP4904
bash getPathway.sh WP4912
bash getPathway.sh WP4927
bash getPathway.sh WP4936
```

## Step 2: Update the author information

This is currently a manual step. Check all pathways and see if the authors are updated,
and update or create the appropriate files in the `authors/` folder. Even the order of
the authors can have changed.

## Step 3: Create the RDF (Turtle)

The Turtle in the `wp/Human` folder is created with WPRDF from the
[SARS-CoV-2-WikiPathways](https://github.com/wikipathways/GPML2RDF/tree/SARS-CoV-2-WikiPathways)
branch. Check out the repository like this:

```shell
git clone git@github.com:wikipathways/GPML2RDF.git
git checkout -b SARS-CoV-2-WikiPathways
```

Then, create a single Java archive with dependencies with:

```shell
cd GPML2RDF/WP2RDF
mvn assembly:single
```

Copy the resulting `GPML2RDF-3.0.0-SNAPSHOT-jar-with-dependencies.jar` into the `libs` folder of
this repository. After that, you can run the Turtle generation with back in this repository:

```shell
javac -cp libs/GPML2RDF-3.0.0-SNAPSHOT-jar-with-dependencies.jar src/java/main/org/wikipathways/covid/CreateRDF.java
java -cp src/java/main/.:libs/GPML2RDF-3.0.0-SNAPSHOT-jar-with-dependencies.jar:libs/derby-10.5.3.0_1.jar org.wikipathways.covid.CreateRDF gpml/WP4846.gpml
```

There is a `Makefile` in this folder, that glues things together. Just type:

```shell
make fetch
```

To download the BridgeDb identifier mapping files, download them from
[here](https://bridgedb.github.io/data/gene_database/)
and save them in the `/path/to/where/the/bridge/files/are` folder, mathching what
you entered in the `config.properties` file above with the `bridgefiles=` parameter.
You also want to download the identifier mapping database for coronavirus
genes and proteins.

## Step 4: Make the zip files

Both the source files are in this repository, as are the `.zip` files to be used by the bot.
These zip files are created/updated with these commands:

```shell
rm wikipathways-SARS-CoV-2-rdf-*.zip
zip wikipathways-SARS-CoV-2-rdf-authors.zip authors/*
zip wikipathways-SARS-CoV-2-rdf-wp.zip wp/Human/*
zip wikipathways-SARS-CoV-2-rdf-gpml.zip wp/gpml/Human/*
```

## Step 4: run the Wikidata bot

Before you attempt to run the bot, make sure all publications are in Wikidata
and with statements to list their PubMed identifiers.

The bot is an adaption of the WikiPathways bot in the
[github.com/SuLab/scheduled-bots/](https://github.com/SuLab/scheduled-bots.git)
repository. You need, however, the `sars-cov-2-wikipathways-2`
branch:

```shell
git clone https://github.com/SuLab/scheduled-bots.git
cd scheduled-bots
git checkout -b sars-cov-2-wikipathways-2
```

In the `scheduled_bots/wikipathways` folder in that repository you
will find the bot:

```shell
python3 bot.py
```

## Step 5: Update the JSON/SVG for Wikidata

The JSON/SVG used on Scholia needs updating. A hook on this
repository has been put in place and each time a commit is made,
the JSON and SVG needed by Scholia are automatically updated.
Thanks to Anders!

## Step 6: Summary statistics

If you have the Debian/Ubuntu `rasqal-utils` package installed, you can run
the following queries to get some statistics:

```shell
cat wp/Human/WP*.ttl > all.ttl
echo "## Reactions"
roqet sparql/reactions.rq -D all.ttl > /dev/null
echo "## Publications"
roqet sparql/publications.rq -D all.ttl > /dev/null
echo "## Genes"
roqet sparql/genes.rq -D all.ttl > /dev/null
echo "## Proteins"
roqet sparql/proteins.rq -D all.ttl > /dev/null
echo "## Metabolites"
roqet sparql/metabolites.rq -D all.ttl > /dev/null
echo "## DataNodes"
roqet sparql/datanodes.rq -D all.ttl > /dev/null
rm all.ttl
```
