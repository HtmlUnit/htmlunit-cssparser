# AGENTS.md — HtmlUnit-CSSParser

## Project Overview

HtmlUnit-CSSParser is a **CSS parser for Java** that reads CSS source text and produces a DOM-style object tree. It is the CSS parser powering [HtmlUnit](https://www.htmlunit.org/) since version 1.30. The project originated as a fork of [CSSParser 0.9.25](http://cssparser.sourceforge.net/), with the SAC (`org.w3c.css.sac`) dependency removed and a more flexible object model introduced.

- **Group/Artifact:** `org.htmlunit:htmlunit-cssparser`
- **License:** Apache License 2.0
- **Default branch:** `master`
- **Java version:** JDK 17+ (version 5.x, current development); JDK 8+ for 4.x releases
- **Build system:** Maven

## Repository Structure

```
htmlunit-cssparser/
├── pom.xml                          # Maven build configuration
├── checkstyle.xml                   # Checkstyle rules (enforced on build)
├── checkstyle_suppressions.xml      # Checkstyle suppression rules
├── README.md
├── LICENSE                          # Apache 2.0
├── .github/
│   ├── workflows/
│   │   └── codeql.yml               # CodeQL security scanning (Java)
│   ├── dependabot.yml               # Dependabot dependency updates
│   └── FUNDING.yml                  # Sponsorship info
├── src/
│   ├── main/
│   │   ├── java/org/htmlunit/cssparser/
│   │   │   ├── dom/                 # CSS DOM implementation classes
│   │   │   ├── parser/              # Core parser classes
│   │   │   │   ├── condition/       # CSS selector conditions
│   │   │   │   ├── selector/        # CSS selector model
│   │   │   │   └── media/           # Media query support
│   │   │   └── util/                # Utility classes
│   │   └── javacc/
│   │       └── CSS3Parser.jj        # JavaCC grammar file (generates the parser)
│   └── test/
│       ├── java/                    # JUnit 5 test classes
│       └── resources/               # CSS test fixture files
└── target/                          # Build output (not committed)
```

## Build and Test

### Prerequisites

- **Maven 3.6.3+**
- **JDK 17+** (for current master / version 5.x)

### Commands

```bash
# Compile (this also runs JavaCC to generate the parser from CSS3Parser.jj)
mvn compile

# Run all tests
mvn test

# Full build with checkstyle verification
mvn -U clean test

# Check for dependency/plugin updates
mvn versions:display-plugin-updates
mvn versions:display-dependency-updates
```

### Generated Code

The CSS parser is generated from a **JavaCC grammar file** at `src/main/javacc/CSS3Parser.jj`. During the `generate-sources` phase, the `ph-javacc-maven-plugin` generates Java source files into `target/generated-sources/javacc/org/htmlunit/cssparser/parser/javacc/`. A post-processing step using the `maven-replacer-plugin` cleans up the generated code (removes dead code patterns produced by JavaCC).

**Do not manually edit files in `target/generated-sources/`** — they are regenerated on every build. If parser behavior needs to change, edit `src/main/javacc/CSS3Parser.jj`.

## Architecture and Key Packages

### `org.htmlunit.cssparser.parser` — Core Parser

The main entry point for users. Key classes:

| Class | Purpose |
|---|---|
| `CSSOMParser` | High-level parser that produces a DOM-style tree from CSS input. Main public API. |
| `AbstractCSSParser` | Base class with shared parsing logic; `CSS3Parser` (generated) extends this. |
| `InputSource` | Wraps a `Reader` to feed CSS text to the parser. Replaces the old SAC `InputSource`. |
| `LexicalUnit` / `LexicalUnitImpl` | Represents CSS values (lengths, colors, functions, etc.) as a linked list of lexical tokens. |
| `CSSErrorHandler` | Interface for custom error handling during parsing. Replaces the old SAC `ErrorHandler`. |
| `CSSException` / `CSSParseException` | Exception types for parse errors. |
| `DocumentHandler` / `HandlerBase` | Event-based (SAX-like) callback interface for streaming CSS parsing. |
| `Locator` / `Locatable` | Source location tracking (line/column numbers). |

### `org.htmlunit.cssparser.parser.selector` — Selector Model

Represents CSS selectors as an object model:

- `Selector`, `SimpleSelector` — base types
- `ElementSelector` — type selectors (`h1`, `div`, `*`)
- `DescendantSelector`, `ChildSelector` — combinators (` `, `>`)
- `DirectAdjacentSelector`, `GeneralAdjacentSelector` — combinators (`+`, `~`)
- `PseudoElementSelector` — pseudo-elements (`::before`, `::after`)
- `RelativeSelector` — for `:has()` relative selectors
- `SelectorList` / `SelectorListImpl` — ordered list of selectors
- `SelectorSpecificity` — calculates selector specificity
- `Combinator` — enum of CSS combinator types

### `org.htmlunit.cssparser.parser.condition` — Selector Conditions

Conditions attached to selectors (class, id, attribute, pseudo-class matching):

- `ClassCondition` (`.foo`), `IdCondition` (`#bar`)
- `AttributeCondition` (`[attr=val]`), `PrefixAttributeCondition` (`[attr^=val]`), `SuffixAttributeCondition` (`[attr$=val]`), `SubstringAttributeCondition` (`[attr*=val]`), `OneOfAttributeCondition` (`[attr~=val]`), `BeginHyphenAttributeCondition` (`[attr|=val]`)
- `PseudoClassCondition` (`:hover`, `:nth-child()`, etc.)
- `NotPseudoClassCondition` (`:not()`), `IsPseudoClassCondition` (`:is()`), `HasPseudoClassCondition` (`:has()`), `WherePseudoClassCondition` (`:where()`)
- `LangCondition` (`:lang()`)

### `org.htmlunit.cssparser.parser.media` — Media Queries

- `MediaQuery` — a single media query (`screen and (min-width: 768px)`)
- `MediaQueryList` — a list of media queries

### `org.htmlunit.cssparser.dom` — CSS DOM Implementation

Implements a CSS object model (style sheets, rules, values):

- `CSSStyleSheetImpl` — represents a complete stylesheet
- `CSSStyleRuleImpl` — a style rule (`selector { declarations }`)
- `CSSStyleDeclarationImpl` — a set of property declarations
- `CSSMediaRuleImpl`, `CSSImportRuleImpl`, `CSSPageRuleImpl`, `CSSFontFaceRuleImpl`, `CSSCharsetRuleImpl`, `CSSUnknownRuleImpl` — at-rule implementations
- `CSSRuleListImpl` — ordered list of rules
- `CSSValueImpl` — wraps parsed CSS values
- `Property` — a single CSS property with name, value, and priority
- Color classes: `RGBColorImpl`, `HSLColorImpl`, `HWBColorImpl`, `LABColorImpl`, `LCHColorImpl` (plus `AbstractColor` base)
- `RectImpl`, `CounterImpl` — CSS `rect()` and `counter()` value types
- `MediaListImpl`, `CSSStyleSheetListImpl` — list types
- `DOMExceptionImpl` — DOM exception handling

### `org.htmlunit.cssparser.util` — Utilities

- `ParserUtils` — string processing helpers used by the generated parser (trimming, unescaping)

## Code Style and Quality

### Checkstyle

Checkstyle is **strictly enforced** via `checkstyle.xml` and runs during the build. Key rules:

- **Line length:** 120 characters max
- **Indentation:** 4-space tabs
- **Braces:** opening brace on same line (`eol`), closing brace on its own line (`alone`)
- **Naming conventions:**
  - Member fields: `camelCase_` (trailing underscore)
  - Static fields: `CamelCase_` (capital start, trailing underscore)
  - Constants: `UPPER_SNAKE_CASE` (exception: `log`)
  - Methods: `camelCase` (test methods may use underscores: `test[A-Z][a-zA-Z0-9_]+`)
  - Catch parameters: `e`, `ex`, `ignored`, or `expected`
- **Javadoc:** Required on all public/protected methods, types, and packages. Author tag format: `@author Firstname Lastname`
- **Imports:** No star imports, no unused imports, no redundant imports
- **License header:** Required on every source file:
  ```
  /*
   * Copyright (c) 2019-2026 Ronald Brill.
   *
   * Licensed under the Apache License, Version 2.0 ...
   */
  ```
- **No `serialVersionUID`** fields
- **No `@version`** tags
- **No `System.out`/`System.err`** in production code
- **Final local variables** and parameters are enforced
- **No trailing whitespace**, no tab characters, no double blank lines
- Single empty line after package declaration, none before it

Checkstyle suppressions (`checkstyle_suppressions.xml`):
- Test files are exempt from `JavadocPackage`, `JavadocMethod`, and `LineLength`
- Generated files in `target/generated-sources/javacc` are fully exempt
- `CssCharStream.java` is fully exempt (special character stream handling)

### Testing

- **Framework:** JUnit Jupiter (JUnit 5), version 6.x
- **Test dependency:** `commons-io` (test scope only)
- **Test resources:** CSS fixture files in `src/test/resources/`
- **Run tests:** `mvn test` (uses `maven-surefire-plugin`)

## CI/CD

- **CodeQL:** GitHub Actions workflow (`.github/workflows/codeql.yml`) runs security analysis on pushes/PRs to `master` and weekly (Mondays 23:34 UTC). Analyzes Java code only.
- **Dependabot:** Configured via `.github/dependabot.yml` for automated dependency update PRs.
- **Jenkins:** Primary CI runs on an external Jenkins server at `https://jenkins.wetator.org/job/HtmlUnit%20-%20CSS%20Parser/`.

## Making Changes

### Modifying Parser Behavior

1. Edit the JavaCC grammar: `src/main/javacc/CSS3Parser.jj`
2. Run `mvn compile` to regenerate and compile
3. Add/update tests to cover the change
4. Run `mvn test` to verify

### Adding Support for New CSS Features

New CSS features typically require changes in multiple layers:

1. **Grammar** (`CSS3Parser.jj`) — add token definitions and production rules
2. **Lexical units** (`LexicalUnit.java`, `LexicalUnitImpl.java`) — add new `LexicalUnitType` enum values if needed
3. **Conditions** (`parser/condition/`) — for new pseudo-classes or attribute selectors
4. **Selectors** (`parser/selector/`) — for new selector types or combinators
5. **DOM** (`dom/`) — for new at-rule types or value types
6. **Tests** — comprehensive tests for parsing, serialization, and error handling

### Code Conventions for PRs

- Run `mvn -U clean test` and ensure all tests pass
- Run checkstyle: it's part of the build; fix all violations
- Follow the naming conventions (especially trailing underscores on fields)
- Add Javadoc to all new public/protected API
- Keep the license header on all new files
- Do not modify generated files in `target/`

## Versioning and Releases

- **Current development:** 5.0.0-SNAPSHOT (requires JDK 17+)
- **Latest stable:** 4.21.0 (December 2025, JDK 8+)
- **Artifacts:** Published to Maven Central via Sonatype Central Publishing
- **Release process:** (from README)
  1. Ensure all tests pass
  2. Update version in `pom.xml` and `README.md`
  3. Commit, build, and deploy: `mvn -up clean deploy`
  4. Publish on Maven Central Portal
  5. Create GitHub release with signed JARs
  6. Bump to next SNAPSHOT version

## Dependencies

### Runtime

**None.** The library has zero runtime dependencies — it is completely self-contained.

### Test Only

- `org.junit.jupiter:junit-jupiter-engine`
- `org.junit.platform:junit-platform-launcher`
- `commons-io:commons-io`

## Key Design Decisions

1. **No SAC dependency:** The `org.w3c.css.sac` API (stalled since 2008) was removed. All interfaces are built-in, giving the project full control over the object model.
2. **JavaCC-based parser:** The CSS grammar is defined in `CSS3Parser.jj` and compiled by JavaCC. This provides robust, specification-aligned tokenization and parsing.
3. **Event-based + DOM-based API:** The parser supports both SAX-like streaming (`DocumentHandler`) and tree-building (`CSSOMParser`) usage patterns.
4. **Zero runtime dependencies:** Makes the library safe to embed anywhere without dependency conflicts.

## Links

- **Repository:** https://github.com/HtmlUnit/htmlunit-cssparser
- **Maven Central:** https://central.sonatype.com/artifact/org.htmlunit/htmlunit-cssparser
- **HtmlUnit:** https://www.htmlunit.org/
- **Developer Blog:** https://htmlunit.github.io/htmlunit-blog/
- **CI:** https://jenkins.wetator.org/job/HtmlUnit%20-%20CSS%20Parser/
- **Sponsor:** https://github.com/sponsors/rbri
- **Predecessor:** http://cssparser.sourceforge.net/