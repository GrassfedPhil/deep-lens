This project is written in Kotlin using Spring Boot as an underlying framework. I've chosen these tools because of my familiarity with them (used them most of my career), and because I've found Spring Boot to be an easy framework to add features to a project quickly.

To build the project, run
```
./gradlew build
```

This will run the tests in the project and produce a runnable jar. You can then run the jar file by executing the following command

```
java -jar build/libs/deep-lens-0.0.1-SNAPSHOT.jar <absolute-path-to-clinical-file> <absolute-path-to-patient-file>
```

There were a few assumptions I made with this kata. I opted to have the application fail if data ended up getting in to a bad way. Since this is a first pass at an application, with a small example data set, I'd rather the whole thing fail. In a normal work environment, I'd make sure to speak with an analyst or project manager to verify what kind of behavior they'd like to see.

Secondly, I noticed that the diagnosis and anatomic site data between the patient and client files varied sometimes. For example a patient anatomic site might be "left lower lung", while the trial anatomic site would just say "lung." I wasn't sure whether to be strict here, or play a little more loosely with the string matching. I'm handling case insensitivity, but decided to opt for the more strict route on string comparison. This would be a pretty simple change to make, should the business requirements want a more loose comparison.