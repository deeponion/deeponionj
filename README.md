[![Build Status](https://travis-ci.com/deeponion/deeponionj.svg?branch=master)](https://travis-ci.com/deeponion/deeponionj)   [![Coverage Status](https://coveralls.io/repos/deeponion/deeponionj/badge.png?branch=master)](https://coveralls.io/r/deeponion/deeponionj?branch=master) 

### Welcome to DeepOnionj

The DeepOnionj library is a is a Java implementation of the DeepOnion protocol, which allows it to maintain a wallet and send/receive transactions without needing a local copy of DeepOnion Core. It comes with full documentation and some example apps showing how to use it. It is a fork of the bitcoinj library. 

### Technologies

* Java 7+ and Gradle 4.4+ for the `core` module
* Java 8+ and Gradle 4.4+ for `tools` and `examples`
* Java 11+ and Gradle 4.10+ for the JavaFX-based `wallettemplate`
* [Gradle](https://gradle.org/) - for building the project
* [Google Protocol Buffers](https://github.com/google/protobuf) - for use with serialization and hardware communications

### Getting started

To get started, it is best to have the latest JDK and Gradle installed. The HEAD of the `master` branch contains the latest development code and various production releases are provided on feature branches.

#### Building from the command line

Official builds are currently using with JDK 8, even though the `core` module is compatible with JDK 7 and later.

To perform a full build (*including* JavaDocs and unit/integration *tests*) use JDK 8+
```
gradle clean build
```
If you are running JDK 11 or later and Gradle 4.10 or later, the build will automatically include the JavaFX-based `wallettemplate` module. The outputs are under the `build` directory.

To perform a full build *without* unit/integration *tests* use:
```
gradle clean assemble
```

#### Building from an IDE

Alternatively, just import the project using your IDE. [IntelliJ](http://www.jetbrains.com/idea/download/) has Gradle integration built-in and has a free Community Edition. Simply use `File | New | Project from Existing Sources` and locate the `build.gradle` in the root of the cloned project source tree.

### Example applications

These are found in the `examples` module.

### Where next?

Now you are ready to [follow the tutorial](https://bitcoinj.github.io/getting-started).

### Testing a SNAPSHOT build

Building apps with official releases of **bitcoinj** is covered in the [tutorial](https://bitcoinj.github.io/getting-started).

If you want to develop or test your app with a [Jitpack](https://jitpack.io)-powered build of the latest `master` or `release-0.15` branch of **bitcoinj** follow the dynamically-generated instructions for that branch by following the correct link.


* [master](https://jitpack.io/#deeponion/deeponionj/master-SNAPSHOT) branch
* [release-2.1.1](https://jitpack.io/#deeponion/deeponionj/2.1.1) branch
