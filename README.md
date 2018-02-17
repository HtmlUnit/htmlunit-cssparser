# HtmlUnit - CSSParser

This is the code repository of the CSS parser used by HtmlUnit strarting with version 1.30.
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

## Running the tests

```
mvn test
```

## Deployment

TODO

## Contributing

TODO
Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

TODO
We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **RBRi**
* all the contributors to CSSParser

## License

This project is licensed under the Apache 2.0 License

## Acknowledgments

Many thanks to all of you contributing to HtmlUnit/CSSParser/Rhino in the past.