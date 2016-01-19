# Setting up the development environment

Based on [Gimer's 101](https://forum.nem.io/t/nem-development-101-episode-01-java-git-maven-nem-core/1656)
but using [sdkman](http://sdkman.io/).

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
