# LIMESGui
GUI for the LIMES Link Discovery Framework (http://aksw.org/Projects/LIMES.html)


**This is an old version! The current one can be found at (https://github.com/AKSW/LIMES-dev/)**

## Usage
Run the following commands to start the desktop GUI for LIMES
```
    cd swp15-ld
    mvn -U clean compile -DskipTests=true assembly:assembly
    cd target
    unzip swp15-ld-0.0.1-SNAPSHOT-with-dependencies.zip 
    cd swp15-ld-0.0.1-SNAPSHOT
    java -jar swp15-ld-0.0.1-SNAPSHOT.jar
```

## Notes
Works on Java8 from Oracle
