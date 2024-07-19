# Interactive Drawing Board with Real-Time WebSocket Communication

An interactive drawing board developed with React and Spring Boot that allows multiple users to draw on a shared board in real-time using WebSockets and Redis for session management and ticket-based authorization.
![Demo GIF](https://github.com/alexandrac1420/RealTimeInteractiveBoardRedis/blob/master/Pictures/Dise%C3%B1o%20sin%20t%C3%ADtulo.gif)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to install the following tools and configure their dependencies:

1. **Java** (versions 7 or 8)
    ```sh
    java -version
    ```
    Should return something like:
    ```sh
    java version "1.8.0"
    Java(TM) SE Runtime Environment (build 1.8.0-b132)
    Java HotSpot(TM) 64-Bit Server VM (build 25.0-b70, mixed mode)
    ```

2. **Maven**
    - Download Maven from [here](http://maven.apache.org/download.html)
    - Follow the installation instructions [here](http://maven.apache.org/download.html#Installation)

    Verify the installation:
    ```sh
    mvn -version
    ```
    Should return something like:
    ```sh
    Apache Maven 3.2.5 (12a6b3acb947671f09b81f49094c53f426d8cea1; 2014-12-14T12:29:23-05:00)
    Maven home: /Users/dnielben/Applications/apache-maven-3.2.5
    Java version: 1.8.0, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/jre
    Default locale: es_ES, platform encoding: UTF-8
    OS name: "mac os x", version: "10.10.1", arch: "x86_64", family: "mac"
    ```

3. **Git**
    - Install Git by following the instructions [here](http://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

    Verify the installation:
    ```sh
    git --version
    ```
    Should return something like:
    ```sh
    git version 2.2.1
    ```

4. **Docker**
    - Install Docker by following the instructions [here](https://docs.docker.com/get-docker/)

    Verify the installation:
    ```sh
    docker --version
    ```
    Should return something like:
    ```sh
    Docker version 20.10.7, build f0df350
    ```

5. **Redis (via Docker)**
    - Run Redis in a Docker container:
    ```sh
    docker run --name some-redis -p 45000:6379 -d redis
    ```

    Verify the installation:
    ```sh
    docker ps
    ```
    Should return something like:
    ```sh
    CONTAINER ID   IMAGE     COMMAND                  CREATED         STATUS         PORTS                     NAMES
    e7e2f46385d1   redis     "docker-entrypoint.s…"   2 minutes ago   Up 2 minutes   0.0.0.0:45000->6379/tcp   some-redis
    ```
    ![alt text](https://github.com/alexandrac1420/RealTimeInteractiveBoardRedis/blob/master/Pictures/image.png)

### Installing

1. Clone the repository and navigate into the project directory:
    ```sh
    git clone https://github.com/alexandrac1420/RealTimeInteractiveBoardRedis
    cd BoardWebSocketRedis1
    ```

2. Build the Spring Boot backend:
    ```sh
    mvn package
    ```

    Should display output similar to:
    ```sh
    [INFO] Building jar: C:\Users\alexandra.cortes\Downloads\BoardWebSocket\target\BoardWebSocket-0.0.1-SNAPSHOT.jar       
    [INFO] The original artifact has been renamed to C:\Users\alexandra.cortes\Downloads\BoardWebSocket\target\BoardWebSocket-0.0.1-SNAPSHOT.jar.original
    [INFO] BUILD SUCCESS
    ```

### Running the Application

To run the backend and frontend, follow these steps:

1. **Ensure Redis is running** (if not already running):
    ```sh
    docker start some-redis
    ```

2. **Run the Spring Boot backend:**
    ```sh
    cd demo
    mvn spring-boot:run
    ```

    The backend will start on `https://localhost:8443`, and it will store the drawing board's data (i.e., the points that are drawn) and session information in Redis.

   ![image](https://github.com/user-attachments/assets/d8fa60cd-37c5-4a14-9d13-07eeb359c477)
   ![image](https://github.com/user-attachments/assets/ee717d4e-0ac1-4abc-9fc0-28286c622174)
   ![image](https://github.com/user-attachments/assets/611c38d7-09eb-4988-911b-c73f46bb0fac)

## Architectural Design

![image](https://github.com/user-attachments/assets/d9d8271b-ee93-4747-a725-32ac669c1f4d)

### BBCanvas Component (React)

The BBCanvas component uses P5.js to manage interactions on the canvas and WebSocket communication:

**Features:**
- Uses P5.js to draw and handle interactions on the HTML5 canvas.
- Manages WebSocket connection (comunicationWS) to send and receive real-time data from the server.
- Renders points on the canvas based on coordinates received from the server, allowing real-time updates.

### Editor Component (React)

The Editor component provides the main structure of the application and contains the BBCanvas:

**Features:**
- Defines the user interface structure, integrating the BBCanvas component for direct interaction with the interactive board.

### WSBBChannel Class (JavaScript)

The WSBBChannel class facilitates WebSocket communication between the client and server:

**Features:**
- Manages WebSocket connection with the Spring Boot server.
- Sends JSON messages representing coordinates of points drawn by the client to the server and distributes these updates to all connected clients.

### BBAppStarter (@SpringBootApplication)

BBAppStarter is the entry point of the Spring Boot application:

**Features:**
- Configures and starts the Spring Boot application, defining server settings such as the execution port.
- Handles incoming requests from the client through REST controllers and WebSocket endpoints.

### BBConfigurator (@Configuration)

BBConfigurator provides configuration to the Spring application context:

**Features:**
- Configures the export of the WebSocket endpoint (/bbService), allowing WebSocket connections between the client and server.

### BBEndpoint (@ServerEndpoint)

BBEndpoint acts as a server endpoint for WebSocket connections:

**Features:**
- Manages bidirectional communication between the client and server via WebSocket.
- Receives and distributes JSON messages (coordinates of drawn points) among all connected clients to maintain synchronization on the interactive board.

### DrawingServiceController (@RestController)

DrawingServiceController is a REST controller handling incoming HTTP requests:

**Features:**
- Provides REST endpoints for the client to obtain information about the server's status and other operations unrelated to drawing on the interactive board.

### Redis and Ticket-Based Authorization

The application uses Redis for session management and ticket-based authorization to enhance security and performance:

**Features:**
- **Redis Configuration**: Managed by `RedisConfig`, which sets up the connection to the Redis server using LettuceConnectionFactory. It retrieves connection parameters from `application.properties`.
- Stores session data in Redis to manage user sessions efficiently.
- Utilizes ticket-based authorization for secure WebSocket connections, ensuring that only authorized users can interact with the drawing board.

## Testing

The project uses JaCoCo for code coverage and SonarQube for continuous inspection. The code coverage is maintained above 85%. 

### Test Cases

1. **WebSecurityConfigTest**
   - **Purpose**: Validates the application's security configuration.
   - **Tests**:
     - `accessToUnsecuredEndpointShouldBeAllowed()`: Ensures that unsecured endpoints are accessible.
     - `accessToSecuredEndpointShouldRequireAuthentication()`: Verifies that secured endpoints require authentication.
     - `loginWithValidCredentialsShouldSucceed()`: Tests successful login with valid credentials.
     - `logoutShouldInvalidateSession()`: Ensures that logout invalidates the session.

2. **TicketRepositoryTest**
   - **Purpose**: Tests the functionality of the `TicketRepository` class.
   - **Tests**:
     - `testGetTicket()`: Checks ticket generation and retrieval.
     - `testCheckTicketValid()`: Validates the checking of a valid ticket.
     - `testCheckTicketInvalid()`: Validates the checking of an invalid ticket.

3. **DrawingServiceControllerTest**
   - **Purpose**: Tests the `DrawingServiceController` class.
   - **Tests**:
     - `testStatus()`: Verifies the status endpoint response.
     - `testGetTicket()`: Tests ticket retrieval through the controller.

### SonarQube Integration

To ensure code quality and coverage, SonarQube is integrated into the project:

1. **Setup SonarQube**:
   - Pull the SonarQube Docker image and run it:
     ```sh
     docker pull sonarqube
     docker run -d -p 9000:9000 --name sonarqube sonarqube
     ```
   - Access SonarQube at `http://localhost:9000` and log in with the default credentials (`admin`/`admin`).

2. **Configure Maven**:
   - Add the SonarQube plugin to your `pom.xml`:
     ```xml
     <build>
       <plugins>
         <plugin>
           <groupId>org.sonarsource.scanner.maven</groupId>
           <artifactId>sonar-maven-plugin</artifactId>
           <version>3.9.1.2184</version>
         </plugin>
       </plugins>
     </build>
     ```

3. **Run SonarQube Analysis**:
   - Use the following Maven command to perform SonarQube analysis:
     ```sh
     mvn verify sonar:sonar -D sonar.token=<your-sonar-token>
     ```

   Replace `<your-sonar-token>` with the token generated from the SonarQube interface.

4. **View Results**:
   - After the analysis is complete, view the results in the SonarQube dashboard. The analysis results include code quality metrics, security issues, and code coverage details.

### JaCoCo Integration

JaCoCo is used for code coverage reporting:

1. **Add JaCoCo Plugin**:
   - Add the JaCoCo plugin to your `pom.xml`:
     ```xml
     <build>
       <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.12</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                        <configuration>
                            <excludes>
                                <exclude>**/BBConfigurator.class</exclude>
								<exclude>**/MvcConfig.class</exclude>
								<exclude>**/BBAppStarter.class</exclude>
								<exclude>**/BBEndpoint.class</exclude>
								<exclude>**/RedisConfig.class</exclude>
								<exclude>**/WebSecurityConfig.class</exclude>
                         </excludes>
                        </configuration>
                    </execution>
                    <execution>
                        <id>jacoco-check</id>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <configuration>
                            <rules>
                                <rule>
                                    <element>PACKAGE</element>
									<excludes>
										<exclude>**/BBConfigurator.class</exclude>
										<exclude>**/MvcConfig.class</exclude>
										<exclude>**/BBAppStarter.class</exclude>
										<exclude>**/BBEndpoint.class</exclude>
										<exclude>**/RedisConfig.class</exclude>
										<exclude>**/WebSecurityConfig.class</exclude>
									</excludes>
                                    <limits>
                                        <limit>
                                            <counter>CLASS</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>0.85</minimum><!--Porcentaje mínimo de cubrimiento para construir el proyecto-->
                                        </limit>
                                    </limits>
                                </rule>
                            </rules>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
     </build>
     ```

2. **Generate Coverage Report**:
   - Run Maven to generate the coverage report:
     ```sh
     mvn clean verify
     ```

   - View the generated coverage report at `target/site/jacoco/index.html`.


## Deployment on AWS

Follow these steps to deploy the application on AWS:

1. **Start the virtual machine**

    Launch an EC2 instance with your preferred configuration.

    ![alt text](https://github.com/alexandrac1420/CalculadoraWeb/blob/master/Pictures/image.png)

2. **Transfer dependencies and the JAR file**

    Upload the dependencies.zip (containing necessary dependencies) and the built JAR file to the created virtual machine.
    ![alt text](https://github.com/alexandrac1420/CalculadoraWeb/blob/master/Pictures/image-1.png)
    ![alt text](https://github.com/alexandrac1420/CalculadoraWeb/blob/master/Pictures/image-2.png)

3. **Execute the following command**

    Navigate to the directory where you uploaded the files and run:
    ```sh
     java -jar demo-0.0.1-SNAPSHOT.jar
    ```
    This will start the Spring service.

4. Start the Spring service

    Ensure the Spring Boot application starts without errors.
    ![alt text](https://github.com/alexandrac1420/CalculadoraWeb/blob/master/Pictures/image-3.png)

5. Verify the deployment

    Check the application's availability using the public DNS of the EC2 instance on port 8443, e.g.,
    ![image](https://github.com/user-attachments/assets/6868a796-e3d3-4254-a2cb-db2caa3ba3fd)
    ![image](https://github.com/user-attachments/assets/0a9a809f-aa5f-41b4-b2ce-e658233cf892)
    ![image](https://github.com/user-attachments/assets/6d6e5d85-abfa-470a-88bd-18e49936f2d2)
    ![image](https://github.com/user-attachments/assets/f53742a3-c846-4c16-a58c-564f49134448)


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management for backend
* [Spring Boot](https://spring.io/projects/spring-boot) - Backend framework
* [React](https://reactjs.org/) - Frontend framework
* [Redis](https://redis.io/) - Session management and caching
* [Docker](https://www.docker.com/) - Containerization
* [Git](http://git-scm.com/) - Version Control System

## Versioning

I use [GitHub](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/alexandrac1420/RealTimeInteractiveBoardRedis).

## Authors

* **Alexandra Cortes Tovar** - [alexandrac1420](https://github.com/alexandrac1420)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
