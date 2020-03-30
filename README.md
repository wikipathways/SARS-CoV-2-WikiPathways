# SARS-CoV-2-WikiPathways

Temporary repository of RDF of selected pathways from WikiPathways, supporting the Wikidata bot.

The following steps can be taken to update the content of this repository.

## Step 1: Update the GPML files

Use the `getPathway.sh` bash script to download the pathways of interest:

```shell
bash getPathway.sh WP4846
bash getPathway.sh WP4853
bash getPathway.sh WP4860
bash getPathway.sh WP4862
bash getPathway.sh WP4861
bash getPathway.sh WP4863
bash getPathway.sh WP4864
bash getPathway.sh WP4868
bash getPathway.sh WP4869
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

Generate the Turtle with these two Maven commands and manually copy/paste the content
into the `.ttl` files in this repository (GNU/Linux):

```shell
cd GPML2RDF
mkdir -p /tmp/OPSBRIDGEDB
echo "bridgefiles=/path/to/where/the/bridge/files/are" > /tmp/OPSBRIDGEDB/config.properties
cp WP2RDF
cp ../../SARS-CoV-2-WikiPathways/gpml/* resources/.
```

If you added new pathways to the `SARS-CoV-2-WikiPathways` repository, then you need to
add a new test for each of them, which includes a GPML file you just copied, and a
copy of `org.wikipathways.wp2rdf.WP4846Test` for the new pathway.

After that, you can run the JUnit test for each pathway to create the Turtle, that
you need to copy/paste from the Maven command line output into the appropriate
Turtle file in the `SARS-CoV-2-WikiPathways` repository. With a `createTurtle.sh`
helper script, we can do:

```shell
rm WP*.ttl
bash createTurtle.sh WP4846
bash createTurtle.sh WP4853
bash createTurtle.sh WP4860
bash createTurtle.sh WP4862
bash createTurtle.sh WP4861
bash createTurtle.sh WP4863
bash createTurtle.sh WP4864
bash createTurtle.sh WP4868
bash createTurtle.sh WP4869
cp WP*.ttl ../../SARS-CoV-2-WikiPathways/wp/Human/.
```

To download the BridgeDb identifier mapping files, download them from
[here](https://bridgedb.github.io/data/gene_database/)
and save them in the `/path/to/where/the/bridge/files/are` folder, mathching what
you entered in the `config.properties` file above.

## Step 4: Make the zip files

Both the source files are in this repository, as are the `.zip` files to be used by the bot.
These zip files are created/updated with these commands:

```shell
zip wikipathways-SARS-CoV-2-rdf-authors.zip authors/*
zip wikipathways-SARS-CoV-2-rdf-wp.zip wp/Human/*
```

## Step 4: run the Wikidata bot

Before you attempt to run the bot, make sure all publications are in Wikidata
and with statements to list their PubMed identifiers.

To do...

## Step 5: Update the JSON/SVG for Wikidata

The JSON/SVG used on Scholia needs updating. Check ... to ...
