# alfresco-repo-events-example

Example use of Camel in an Alfresco repository (5.x upwards) to send and receive messages/events. Messaging is built in to Alfresco 5.x upwards and is exposed via the Messaging subsystem. Camel is used under the covers to send events to a Camel endpoint such as a topic or queue.

Build using

    mvn clean install

which will generate an amp in target/amps. Install in the usual way. Alternatively, build using

    mvn -DskipTests clean install -DapplyAmps -P wars-from-maven

to generate an amp and apply it to a vanilla repository war file. Change the pom file properties to choose the version of Alfresco you require:

    <alfresco.version>5.1</alfresco.version>
    <alfrescoRepoVersionMin>5.0.0</alfrescoRepoVersionMin>
    <alfrescoRepoVersionMax>5.99</alfrescoRepoVersionMax>