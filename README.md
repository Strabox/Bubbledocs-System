# Bubble Docs

Project to 2014/2015 Software Engineering and Distributed Systems courses of Bachelors Degree in Computer Science and Engineering 3rd Year @ Instituto Superior Técnico

## Goals

- Develop an application to manage users that create and manage documents
- Integrate the above application with a distributed system of authentication (using kerberos) and a other of document storage

## Project Group

- https://github.com/carboned4

The initial version of the repository contains the following structure:

    .
    |__ .gitignore
    |__ .travis.yml
    |__ README.md
    |__ pom.xml
    |__ bubbledocs-appserver
    |   |__ .gitignore
    |   |__ README.md
    |   |__ pom.xml
    |   \__ src
    |       \__ main
    |           |__ dml
    |           |   \__ bubbledocs.dml
    |           \__ resources
    |               |__ fenix-framework-jvstm-ojb.properties
    |               \__ log4j.properties
    |__ sd-id
    |   \__ README.md
    \__ sd-store
        \__ README.md

