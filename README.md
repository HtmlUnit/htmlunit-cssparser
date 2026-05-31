# HtmlUnit - CSSParser

This is the code repository of the CSS parser used by HtmlUnit starting with version 1.30.

For a long time HtmlUnit uses the CSSParser (http://cssparser.sourceforge.net/). But as HtmlUnit has to support more
and more css related features a solution was required that offer more flexibility regarding the resulting object model.
This implementation starts based on CSSParser 0.9.25; we removed the sac dependency and did some cleanup.

We will try to stay in sync with CSSParser regarding the features in the future.

[![Maven Central Version](https://img.shields.io/maven-central/v/org.htmlunit/htmlunit-cssparser)](https://central.sonatype.com/artifact/org.htmlunit/htmlunit-cssparser)

:heart: [Sponsor](https://github.com/sponsors/rbri)

### Project News

**[Developer Blog](https://htmlunit.github.io/htmlunit-blog/)**

[HtmlUnit@mastodon](https://fosstodon.org/@HtmlUnit) | [HtmlUnit@bsky](https://bsky.app/profile/htmlunit.bsky.social) | [HtmlUnit@Twitter](https://twitter.com/HtmlUnit)

#### Version 5

Starting with version 5.0.0, **JDK 17 or higher is required**.  
If you are still on JDK 8, see [Legacy Support (JDK 8)](#legacy-support-jdk-8) below.


### Latest release Version 5.0.0 / May 31, 2026

## Get it!

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.htmlunit</groupId>
    <artifactId>htmlunit-cssparser</artifactId>
    <version>5.1.0</version>
</dependency>
```

### Gradle

Add to your `build.gradle`:

```groovy
implementation group: 'org.htmlunit', name: 'htmlunit-cssparser', version: '5.1.0'
```


<a name="legacy-support-jdk-8"></a>
### Legacy Support (JDK 8)

If you need to continue using **JDK 8**, versions 4.x remain available as-is.
Ongoing maintenance and fixes for JDK 8 are only available through sponsorship —
please contact me via email to discuss options. Sponsorship provides:

- Backporting security and bug fixes to the 4.x branch
- Compatibility maintenance with older Java versions
- Timely releases for critical issues

Without sponsorship, the 4.x branch will not receive further updates.


### Latest CI build
The latest builds are available from our
[Jenkins CI build server](https://jenkins.wetator.org/job/HtmlUnit%20-%20CSS%20Parser/ "HtmlUnit - CSS Parser CI")

[![Build Status](https://jenkins.wetator.org/buildStatus/icon?job=HtmlUnit+-+CSS+Parser)](https://jenkins.wetator.org/job/HtmlUnit%20-%20CSS%20Parser/)

If you use maven please add:

    <dependency>
        <groupId>org.htmlunit</groupId>
        <artifactId>htmlunit-cssparser</artifactId>
        <version>5.2.0-SNAPSHOT</version>
    </dependency>

You have to add the sonatype-central snapshot repository to your pom `repositories` section also:

    <repositories>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>


## Start HtmlUnit - CSSParser Development

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

Pull Requests and and all other Community Contributions are essential for open source software.
Every contribution - from bug reports to feature requests, typos to full new features - are greatly appreciated.

## Deployment and Versioning

This part is intended for committer who are packaging a release.

* Check all your files are checked in
* Execute these mvn commands to be sure all tests are passing and everything is up to data

```
   mvn versions:display-plugin-updates
   mvn versions:display-dependency-updates
   mvn -U clean test
```

* Update the version number in pom.xml and README.md
* Commit the changes


* Build and deploy the artifacts 

```
   mvn -up clean deploy
```

* Go to [Maven Central Portal](https://central.sonatype.com/) and process the deploy
  - publish the package and wait until it is processed

* Create the version on Github
    * login to Github and open project https://github.com/HtmlUnit/htmlunit-cssparser
    * click Releases > Draft new release
    * fill the tag and title field with the release number (e.g. 5.0.0)
    * append 
        * htmlunit-cssparser-5.x.x.jar
        * htmlunit-cssparser-5.x.x.jar.asc 
        * htmlunit-cssparser-5.x.x.pom
        * htmlunit-cssparser-5.x.x.pom.asc 
        * htmlunit-cssparser-5.x.x-javadoc.jar
        * htmlunit-cssparser-5.x.x-javadoc.jar.asc
        * htmlunit-cssparser-5.x.x-sources.jar
        * htmlunit-cssparser-5.x.x-sources.jar.asc
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

## Development Tools

Special thanks to:

<a href="https://www.jetbrains.com/community/opensource/"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg" alt="JetBrains" width="42"></a>
<a href="https://www.jetbrains.com/idea/"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA_icon.svg" alt="IntelliJ IDEA" width="42"></a>  
**[JetBrains](https://www.jetbrains.com/)** for providing IntelliJ IDEA under their [open source development license](https://www.jetbrains.com/community/opensource/) and

<a href="https://www.eclipse.org/"><img src="https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-foundation-grey-orange.svg" alt="Eclipse Foundation" width="80"></a>  
Eclipse Foundation for their Eclipse IDE

<a href="https://www.syntevo.com/smartgit/"><img src="https://www.syntevo.com/assets/images/logos/smartgit-8c1aa1e2.svg" alt="SmartGit" width="54"></a>  
to **[Syntevo](https://www.syntevo.com/)** for their excellent [SmartGit](https://www.smartgit.dev/)!
