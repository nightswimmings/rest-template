### Thesis REST server Test

The app is provided as a Java jar library, with two entry points, ClientLauncher and ServerLauncher.
ServerLauncher is the component that runs and populates with mock data the actual REST Backend, whereas 
ClientLauncher is responsible for querying programatically the REST server frontend in a business functional 
way and showing the results data.
 
If the library is executed through the GUI (equaling "Open With..." Java JVM [javaw]) the ServerLauncher is 
executed conveniently by default in background listening at localhost:8080, and outputing all logs to a server.log file 
created on-the-fly in the execution folder. Multiple servers and clients can be executed concurrently,
as long as the ports are properly configured to not concur ever as explained in detail later.

This library has been tested ONLY ON WINDOWS.

## ServerLauncher

The ServerLauncher can also be executed manually through a shell/console. 
In that case, logs are outputted in the shell instead of a file, and it is possible to 
close app gracefully with standard CTRL+C.
To run it manually, open a shell in the folder of the library and execute:

	$> java -jar ThesisAPIServer.jar, (which is by default equivalent to
	$> java -jar ThesisAPIServer.jar start 8080)

As you can see, ServerLauncher accepts optional parameters; start/stop, to indicate the operation to perform, and in case those are provided,
an optional port number as a second parameter. Following commands, would for example start the server and try to stop it later.

	$> java -jar ThesisAPIServer.jar start 9090
	$> java -jar ThesisAPIServer.jar stop 9090

You should see the server screen shutdowning gracefully if another shell tries a stop operation over the same port.

Since the server is embedded in the library and runs indefinitely in its own process, there is no way to stop it remotely but 
signaling over the interface it listens. That is why the stop operation is not guaranteed. It is synchronous to a certain degree,
because it tries to connect to the server by establishing a connection, and failing if not capable of doing so, but it returns asynchronously 
without any notice of the actual response to signaling. A shutdown handler is configured in every server run in a way that 
the launcher class shares a secret signaling token between both start and stop operations, kind of a RSP request ot a private ping of death if you prefer. This operation is especially interesting when running server in background mode, as we cannot shutdown it with a 
typical CTRL+C in the console. Please, notice shutdown secret practice is discouraged for security reasons in production scenarios, but it's useful in our test case.

# Advanced Configuration

All the configuration defaults reside within the jar in META-INF/application.properties. 
Please, inspect the file opening the JAR with a conventional unzipper for a complete list of settings.
However, as those are not easily modifiable, the app comes with a couple of extra ways to override those default settings.
The properties fallback mechanism is as follows:

	1.Property is appended as an option (--name=value) at the end of the server run execution command in the shell
	2.Property is set within a custom "application.properties" file right next to the library jar
	3.Property is read from the internal application.properties (default setting).

This is especially IMPORTANT when configuring multiple servers as detailed below.

Also, as we are using JPA internally, almost any imaginable database can be set up through these properties
with minimal effort as soon as its JDBC driver is found in the classpath.

# Concurrency of processes

Both server and client can be run many times as long as the ports don't match, as it was requested in the spec. 
But in our current implementation, this includes the database ports as well. 
By default, server comes pre-configured with an in-memory database (H2) in server-mode, i.e, including 
a web interface for browsing internal data on-the-fly conveniently and in a visual way and therefore know its state. 
But this features forces us to keep a second port per instance, next to the very server one.
Thus, for each run of the server, even if we alternate ports, consequent runs will fail unless we provide to the n-th
instance a new default db-port. This is an internal setting so it must be overriden by means of an
extra option flag in the command line as explained in the previous section.

	$> java -jar ThesisAPIServer.jar start 8080 --db.webport=8090
	$> java -jar ThesisAPIServer.jar start 8081 --db.webport=8092

This example would run two instances of the server listening at localhost:8080 and localhost:8081, 
their databases browsable at localhost:8090 and localhost:8091 respectively. 
Important fact: Default port for the in-memory database is 8082.  


# Troubleshooting SideNotes

Please disable any firewall in case the server is not browsable at localhost:port after
running stage is finished (see the logs or wait moderately if in background)

If a performance boost is desired in the server, one can allocate more memory to the process by 
PREPPENDING -Xmx2048m (or other RAM Mb value) flag as in:

	$> java -Xmx2048m -jar ThesisAPIServer.jar start 8080h  

The server is configured to use IPv4 stack in a hardcoded way due to some problems in IPv6-ready systems 
giving the bad stack when establishing loopback connections

## Business Model

At this point, an already started server is hosting 4 endpoints, 2 POST methods (the production ones only accessible by POSTing a Request message) 
and 2 GET methods, which are exposed for convenient and easy web-interaction testing).

	- POST http://localhost:[port]/api/thesis/subject (search by subject) 
	- POST http://localhost:[port]/api/thesis/target  (search by thesisTarget)

	- GET http://localhost:8080/api/thesis/1  (find thesis, where 1 is the mandatory thesis id)
	- GET http://localhost:8080/api/thesis/ES/subject/00000000?page=0 (findByTarget, where /ES/ is an optional subpath representing any registered country [ES|ITA|BRA] to filter by)
								
Pagination of results is implemented by query parameter page=x. It can be used by both POST and GET endpoints.
Results Page Size can be configured by the internal setting --rest.pagination_size=x (see Advanced Configuration section)

## ClientLauncher

The Client is not the default entry point of the library, so we need to ask for the specific class 
to execute it:

	$> java -cp ThesisAPIServer.jar org.nightswimming.thesis.ClientLauncher

Executing this shows a usage message detailing the shape of the operations. It basically
simplifies the process of requesting information to the two POST methods of the Provider
by asking the relevant data in each case, and building internally a whole standard Request JSON message
Thus, we need to provide the URL of the endpoint, either the expected Subject or ThesisTarget JSON
entity containing all the criteria to match (depending if we are addressing the SearchBySubject or SearchByTarget endpoint),
and finally the country filter. As always, filtering by country is optional, so ANY can be used to specify no filtering.
Lastly, an optional parameter with the JSON format of the Requester is accepted as a parameter, to test our
rudimentary authentication filter. By default, client will use "{\"id\":\"NSM\",\"name\":\"Nightswimming\"}" which is the
identification included in the spec and one of the "accounts" registered in the system pre-populated database. 

	$> ClientLauncher 	http://localhost:8080/api/thesis/subject 00000000 ES
	$> ClientLauncher 	http://localhost:8080/api/thesis/subject 00000000 ANY "{\"id\":\"NSM\",\"name\":\"Nightswimming\"}"
	$> ClientLauncher 	http://localhost:8080/api/thesis/target "{\"gender\":\"M\",\"age\":[20,40],\"income\":{\"currency\":\"EUR\",\"range\":[25000,40000]}}" ITA

Escaping through the console is really ugly, but it's easy to copy-paste those literals.
Client will also intepret the resulting message, whether a successful ResponseMsg or a 
custom error message, in both cases it's pretty printed to the console.
 
### On pre-populated mock data in the System

As already explained, the applications come with a in-memory database, prepopulated by means of 
an import script (you can locate it inside the jar library using a conventional unzipper on /scripts/MockDBData.sql).
The H2 database accesible browser interface explained before comes very handy to play with the queries and 
check the effect and state of the database after them

========================================================================================

### Architectural and Technical Implementation

My first concern was choosing the storage engine. Since thesis are distributed in a format-agnostic way and we have not 
precise idea of the structure of the thesis indexing data, I thought a NoSQL database could be a good match. 
Specially a mainly JSON database, like MongoDB, due to the nature of the requests/responses and the text-based
property matching for criteria, quite unstructured feature per se.
But there was a handicap: this app is expected to be portable in a certain degree, at least 
for demonstration purposes given the executable nature that is requested. 
As I assume, sending an OS executable installing every needed component is out of current 
asignment's scope, that meant distributing MongoDB in a fairly lightweight, fast to use, 
and integrated way. The typical way to do that seems to be by means of using the 
third-party de.flapdoodle.embed.mongo maven plugin, which one would expect it starts an 
in-memory instance (a la h2), but instead it downloads, uncompresses and installs each time 
about 140 Mb of a managed distribution. I could not find any other portable way to distribute 
MongoDB (a la sqllite) neither, so I concluded that for portability and demonstration purposes
it's not the best option. Anyways, the plugin does not seem to work properly once installing 
the correct downloaded mongodb-win32-i386-3.2.0.zip distribution in the background, as it 
tries to execute a not unzipped installer. We are discussing about an unofficial plugin. 

So I changed my mind, and I find more convenient to use a traditional tested in-memory SQL 
solution like H2 for our particular situation. Indeed, in practical terms, the actual thing 
that catch my eyes was reusing same serialization mechanism for both Java Domain Objects<->REST 
and Java Domain Objects<->DB, homogenizing the annotations. But as I am not using a pure 
javascript or JSON-integrated language backend, because I prefer to build the foundation with 
the language that I am more used to for this short-term task, we are still not avoiding the 
overhead of dealing with JSON parsing and unparsing, and it seems to me quite unnatural to 
write all the business logic in the service in terms of JSON parsing.
Anyway, MongoDB has a JPA adapter to use it as a relational transparent layer, which is the 
classical, transversal and enterprise way to deal with all kinds of ORM and database engines. 
I discarded other JSON/BSON databases, since they are not that popular for production uses. 
At the end, relational databases are starting to support JSON storage so I'm going to go that 
way and study if it is standard enough. 

To create DAOs and Persistance Layer in a modern way, I maximized the use of 
standard JPA (Hibernate as Provider), but I did not resist to the 
convenient and simplificated mechanisms Spring-data-jpa brings on the table, which can be clearly 
stated by checking my sources.

Likewise, I tried to minimize Spring in all other areas out of the dependency injection (CDI and JEE7 still
not paired in all-cases), using for instance, standard JAX-RS (JEE7) always, or loading Jetty and Jersey 
programatically instead of using SpringBoot or Dropdown Wizard. I am not saying it is bad alternative,
but I had experience on this field and preferred doing it by code. I probably should have done the same for
Spring, but the annotationConfig.xml is really simple and convenient, as I don't manage the whole app through it,
to simplify things.
 
The big fail of this delivery was an attempt to configure Spring-Security-Oauth2 for the REST API
as an extra feature (you can see relevant classes commented out in the sources), but due to Jetty+Jersey servlet containers
failing to read SPIs (on behalf of Java's recent JAR packaging alongwith Servlet 3.x (JEE) spec), initializers 
for Spring-Security were not registered properly, and I did not want to spend much time mimic-izing all the stuff 
they did (web.xml-like) manually. Also, Spring-Security pushes really hard to use SpringMVC and SpringBoot,
so at that point I did not assess to refactor the whole architecture. Just for emulating a bit the 
idea, I used the users database to authenticate on the available Requester data, just as a eye-candy. 

Another interesting feature is the architecture I built for mapping Exceptions, to deal with
some issues encountered in the past: 
Some business-layer functional exceptions are provided as a sample. 
They all extend ThesisException, constructed by an ErrorCode and an errorMessage.
ErrorCode is an enum class that registers all business-functional exceptions expected in the workflow.
A custom ExceptionMapper is registered in the context of the ServletContainer. 
This captures all ThesisExceptions in the flow, strips its ErrorCode, look up in another Enum that
maps ErrorCodes with HTTP status responses for security reasons and builds an ErrorMsg, which is the 
centralized and controlled conventional response message containing all the relevant data about the failure. 
This feature can be disabled through the internal setting --rest.emptyEntitiesAsServerErrors=false,
if security is a concern and we don't want to show the failures transparently, so everything maps to a 500 INTERNAL_SERVER_ERROR
with no message at all.

Finally, I provide some JUnit tests to cover the whole cycle. Those are indeed End2End tests.
Providing actual Unit Tests, and Mocks for Integration tests between the infinite components seemed to me 
a bit ouf of the scope for the current task, but it's indeed the first thing I would started with 
in a real scenario with a bit more time for securing everything, where performance is critical, 
and working alongwith teammates. I am more of a BDD lover, of them all, though.  
