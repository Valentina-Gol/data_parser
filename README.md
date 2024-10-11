# Requirements
- Java 17
- Gradle
- Git

# How to run it on Linux

Clone repository and go to project root:
```
git clone git@github.com:Valentina-Gol/data_parser.git
cd data_parser/
```

Build application:
```
./gradlew build
```

Run it with command:
```
java -Xmx1G -jar data_parser.jar <file-path>
```

Result will appear in current directory in file named `output.txt`.
