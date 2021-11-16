# OCL Verification of IMDB Data

This project creates an Ecore model based on the IMDB data set and executes a time measurment of the following verification rules:

- Do all the persons have a name? `context Root inv: self.persons->forAll(a | a.name <> null)`
- Are all persons part of a relationship? `context Root inv: self.persons->forAll(a | self.partOf->any(aIm | a = aIm.id) <> null)`
- Are all persons who have "actor" as their primary profession also part of a relationship in which they take on the role of an actor. `context Root inv: self.persons->select(a | a.primaryProfession->includes('actor'))->forAll(a | self.partOf->any(aIm | a = aIm.id and aIm.category = 'actor') <> null)`

## IMDB-data

According to the license model of the imdb dataset, the used source files are not included in this repository. To be 
able to use the provided importer the mentioned files (`name.basics.tsv`, `title.principals.tsv`, `title.basics.tsv`) 
have to be included in the resources `IMDB-OCL-Verification/src/main/resources` folder of this project.

The imdb data is available under the following link [https://datasets.imdbws.com/](https://datasets.imdbws.com/), and 
the documentation to the interfaces can be found here [https://www.imdb.com/interfaces/](https://www.imdb.com/interfaces/).

## How to run

To start the import process, run the main function in the `OclVerificationTest` class.

## Contributing

**First make sure to read our [general contribution guidelines](https://fhooeaist.github.io/CONTRIBUTING.html).**

## License

Copyright (c) 2020 the original author or authors. DO NOT ALTER OR REMOVE COPYRIGHT NOTICES.

This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.

## Research

If you are going to use this project as part of a research paper, we would ask you to reference this project by citing
it.

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.5705170.svg)](https://doi.org/10.5281/zenodo.5705170)
