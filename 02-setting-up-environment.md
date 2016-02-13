# Setting up the development environment

In this guide we will work with [Groovy](http://www.groovy-lang.org/), a language perfectly wel integrated 
in the Java ecosystem, and easy to fiddle with, providing a [groovysh](http://www.groovy-lang.org/groovysh.html) 
shell.

The currently recommended way to setup the development environment to follow this guide is to run a docker
container configure with all that's needed. However, instructions of the [Dockerfile](https://github.com/rb2nem/nem-dev-docker/blob/master/Dockerfile)
should give you an idea of how to set it up manually should you wish to, and the script [test.groovy](https://github.com/rb2nem/nem-dev-docker/blob/master/test.groovy) 
used here is also available.

The code in this section is based on [Gimer's 101](https://forum.nem.io/t/nem-development-101-episode-01-java-git-maven-nem-core/1656)
but using [sdkman](http://sdkman.io/).

## Docker

A docker image with a Groovy development environment set up (nem.core and its dependencies installed and
available to groovy) is available at [Docker Hub](https://hub.docker.com/r/rb2nem/nem-dev-docker).

Once docker installed on your computer, you can start a nem development container:
```
  $ docker run -it rb2nem/nem-dev-docker bash
  Unable to find image 'rb2nem/nem-dev-docker:latest' locally
  latest: Pulling from rb2nem/nem-dev-docker

  bc4d5aff843e: Pull complete 
  21bb4f61db95: Pull complete 
  09bb106b74ae: Pull complete 
  c835c196856d: Pull complete 
  badd7cb5036d: Pull complete 
  2c76457cbcf0: Pull complete 
  3f5adf7add74: Pull complete 
  7e10b0c12840: Pull complete 
  90e733a231a7: Pull complete 
  3f5d92fc5690: Pull complete 
  a7e03f1cd708: Pull complete 
  8ca85d9c35bd: Pull complete 
  89cb76074d3a: Pull complete 
  9cbae61e389f: Pull complete 
  4178073883a6: Pull complete 
  Digest: sha256:fa614d260bddc57b9bf6f6aefba76ae843669326a08921d5f220fa13d5a8dbb5
  Status: Downloaded newer image for rb2nem/nem-dev-docker:latest
  [root@659c2287c036 nem.core]# 
```

As I didn't have the docker image locally, it was first downloaded. As I asked to run the `bash` command, I get
a bach prompt in the container. The current working directory is the source of nem.core. You can see what was built
in the `target` directory:
```
[root@659c2287c036 nem.core]# ls target
classes  generated-sources  generated-test-sources  maven-status  nem-core-0.6.73-BETA-tests.jar  nem-core-0.6.73-BETA.jar  test-classes
```
The `nem.core` jar that was built is at `/root/nem.core/target/nem-core-0.6.73-BETA.jar`.
This jar and all its dependencies have been copied over to `/root/.groovy/lib` so that it can be used from Groovy 
without having to specify anything.                 

You can validate that you all is functional with the `/root/test.groovy`, which will generate a random account info (private key, public key, 
account):
```
[root@659c2287c036 nem.core]# cd ..
[root@659c2287c036 ~]# groovy ./test.groovy 
Private key: 56e75c55f3579e40706862205c533b56a690997209f054b1cdb8045f8eaf18b5
 Public key: 40056cdae02542fa3624fdd72c21bb548706289faeb66d837d667ceeaec2582c
    Address: TC44JKPXMDFO4PB7AZ6T4JRMKWNMCBY7XP3R4XIL

```


Groovy is a language that is very close to Java, and actually the test.groovy script is a copy of the Java code
from [Gimer's example](https://forum.nem.io/t/nem-development-101-episode-02-idea-intellij-nem-core-vanity-gen/1665):

```
import org.nem.core.crypto.KeyPair;
import org.nem.core.model.Address;
import org.nem.core.model.NetworkInfos;


final KeyPair someKey = new KeyPair();
System.out.println(String.format("Private key: %s", someKey.getPrivateKey()));
System.out.println(String.format(" Public key: %s", someKey.getPublicKey()));

final Address anAddress = Address.fromPublicKey(
              NetworkInfos.getTestNetworkInfo().getVersion(),
                                                        someKey.getPublicKey());
System.out.println(String.format("    Address: %s", anAddress));

```

As static typing in Groovy is optional, you get the same result with this version:
```
import org.nem.core.crypto.KeyPair;
import org.nem.core.model.Address;
import org.nem.core.model.NetworkInfos;


someKey = new KeyPair();
println(String.format("Private key: %s", someKey.getPrivateKey()));
println(String.format(" Public key: %s", someKey.getPublicKey()));

anAddress = Address.fromPublicKey(
              NetworkInfos.getTestNetworkInfo().getVersion(),
                                                        someKey.getPublicKey());
println(String.format("    Address: %s", anAddress));

``` 

If this works fine, you're all set to go. We will take a closer look at this code and build on it in the coming chapters.

If you want to know more about the classes used here, you can look at the Javadoc for [KeyPair](http://www.nem.ninja/org.nem.core/org/nem/core/crypto/KeyPair.html),[Address](http://www.nem.ninja/org.nem.core/org/nem/core/model/Address.html) and [NetworkInfos](http://www.nem.ninja/org.nem.core/org/nem/core/model/NetworkInfos.html).

## Windows

See [Gimer's
101](https://forum.nem.io/t/nem-development-101-episode-01-java-git-maven-nem-core/1656).
You should be able to use [sdkman](http://sdkman.io/) to install all
dependencies. Foe example, installing Maven can be done with `sdk install
maven`.

## Linux

### Installing Java

NEM requires Java 8. You can either download it [from
Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html),
just be sure you download the JDK (not the JRE).
If you use an RPM based distro, you can download the RPM. For ther distros, download the .tar.gz
and uncompress it in `/usr/local`. This will result in a path like `/usr/local/jdk1.8.0_66` being
created. I created a soft link `/usr/local/jdk8` with the command `cd /usr/local && ln -s jdk1.8.0_66 jdk8`.
Finally, modify your `PATH`to use that version: `PATH=/usr/local/jdk8/bin:$PATH`. To make it persistent 
across reboots, add that line at the end of `~/.bashrc`.
Now check you use the correct version of java:
```
~$ java -version
java version "1.8.0_66"
Java(TM) SE Runtime Environment (build 1.8.0_66-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.66-b17, mixed mode)
```

Then you can install [sdkman](http://sdkman.io) and activate it for your current shell (new shells will get sdkman automatically):
```
$ curl -s get.sdkman.io | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Now you can install Maven:
```
sdk install maven
```
and possibly groovy:
```
sdk install groovy
```
From there you can clone the nem.core repo: 
```
$ git clone https://github.com/NewEconomyMovement/nem.core.git
```
and compile it:
```
$ cd nem.core
$ mvn clean compile install
```
If you want to copy all dependencies of nem.core to `/root/.groovy/lib` to make it availeble to groovy without classpath manipulations,
issue the command:
```
$ mvn dependency:copy-dependencies -DoutputDirectory=/root/.groovy/lib/
```
You are now ready to test your setup with the [test.groovy](https://github.com/rb2nem/nem-dev-docker/blob/master/test.groovy)!
