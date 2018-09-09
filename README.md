# HtmlUnit - CSSParser

This is the code repository of the CSS parser used by HtmlUnit starting with version 1.30.

For a long time HtmlUnit uses the CSSParser (http://cssparser.sourceforge.net/). But as HtmlUnit has to support more
and more css related features a solution was required that offer more flexibility regarding the resulting object model.
This implementation starts based on CSSParser 0.9.25; we removed the sac dependency and did some cleanup.

We will try to stay in sync with CSSParser regarding the features in the future.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You simply only need a local maven installation.


### Building

Create a local clone of the repository and you are ready to start.

Open a command line window from the root folder of the project and call

```
mvn compile
```

### Running the tests

```
mvn test
```

## Contributing

TODO
Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Deployment and Versioning

This part is intended for committer who are packaging a release.

* Check all your files are checked in
* Execute "mvn clean test" to be sure all tests are passing
* Update the version number in pom.xml
* Execute "mvn clean test" to be sure all tests are passing
* Commit the changes


* Build and deploy the artifacts 

```
   mvn -up clean deploy
```

* Go to [Sonatype staging repositories](https://oss.sonatype.org/index.html#stagingRepositories) and process the deploy
  - select the repository and close it - wait until the close is processed
  - release the package and wait until it is processed

* Create the version on Github
    * login to Github and open project https://github.com/HtmlUnit/htmlunit-cssparser
    * click Releases > Draft new release
    * fill the tag and title field with the release number (e.g. 1.1.0)
    * append 
        * htmlunit-cssparser-1.x.x.jar
        * htmlunit-cssparser-1.x.x.jar.asc 
        * htmlunit-cssparser-1.x.x-javadoc.jar
        * htmlunit-cssparser-1.x.x-javadoc.jar.asc
    * and publish the release 

* Update the version number in pom.xml to start next snapshot development
* Update the htmlunit pom to use the new release

## Authors

* **RBRi**
* all the contributors to CSSParser

## License

This project is licensed under the Apache 2.0 License

## Acknowledgments

Many thanks to all of you contributing to HtmlUnit/CSSParser/Rhino in the past.