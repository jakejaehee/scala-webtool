## SQL Player

Implemented in Scala, Play Framework. Save SQL statement in XML files and request to execute query using them against database and get the result.
Diverse forms of request and response message formats are supported.

### Folders

- sqlplayer/ - SQL Player framework source project
- sqlplayer-sample/ - Sample source project running on Play Framework in development mode.
- sqlplayer-sample-prd/ - Sample binaries running on Play Framework in production mode
- sqlplayer-sample-servlet/ - Sample servlet project
- config/ - Configuration file and SQL XML files

### sqlplayer/

```
$ cd sqlplayer
$ sbt
> compile   -> generates target\scala-2.11\classes
> package   -> generates target\scala-2.11\sqlplayer_2.11-0.1.1.jar
```

### sqlplayer-sample/

In order to test this sample application, database should be initialized first.
H2 databse is used here therefore if you do not have H2 in your computer, please download H2 from http://www.h2database.com/html/cheatSheet.html. You can download jar file(such as h2-1.4.190.jar) from there then save it wherever you want and then in command line, move to that folder and run following command: 
```
  $ java -jar h2-1.4.190.jar
```

You will be connected with H2 console automatically in browser. On the console create database file. There are several ways to create it. Here we will explain the way in which the file is created in the OS user's home directory like following:
   - Fill in the JDBC URL 'jdbc:h2:~/test'
   - Press 'Connect' button
   - Check out test.mv.db file created in your home directory

Configure in conf/application.conf file like following:
```
      db.default.driver=org.h2.Driver
      db.default.url="jdbc:h2:~/test"
      #db.default.url="jdbc:h2:tcp://localhost/~/test"
      db.default.username= sa
      db.default.password=""
```

Then you have done all you have to do for using database.	  

Copy sqlplayer\target\scala-2.11\sqlplayer_2.11-0.1.1.jar to sqlplayer-sample\lib

```
$ cd sqlplayer-sample
$ activator
[sqlplayer-sample] $ compile
[sqlplayer-sample] $ ~ run
```

Then you are ready to test this sample application.
Connect with http://localhost:9000/assets/index.html

To shutdown, press Ctrl+D

### Making stage mode files

```
$ cd sqlplayer-sample
$ activator
```

To generate secret

```
[sqlplayer-sample] $ playGenerateSecret
[info] Generated new secret: QCYtAnfkaZiwrNwnxIlR6CTfG3gf90Latabg5241ABR5W1uDFNIkn
```

Copy the secret above and paste in conf/production.conf as the value of application.secret.
For example:
```
application.secret="QCYtAnfkaZiwrNwnxIlR6CTfG3gf90Latabg5241ABR5W1uDFNIkn"
```

```
$ activator clean stage
```

Command above generates target/universal/stage directory.

### sqlplayer-sample-prd/

Copy all files in target/universal/stage directory to in ..\sqlplayer-sample-prd

```
$ cd sqlplayer-sample-prd
$ bin\sqlplayer-sample.bat -Dconfig.resource=production.conf
```

To assign certain port and IP address. Default port is 9000.

```
$ bin\sqlplayer-sample.bat -Dconfig.resource=production.conf -Dhttp.port=9090 -Dhttp.address=127.0.0.1
```

To test, connect with http://localhost:9090/assets/index.html

To shutdown, press Ctrl+C

### sqlplayer-sample-servlet/

Copy all files in sqlplayer-sample\public into sqlplayer-sample-servlet\WebContent\assets.

Copy sqlplayer-sample\target\scala-2.11\classes into sqlplayer-sample-servlet\WebContent\WEB-INF.

Copy all files in sqlplayer-sample-prd\lib into sqlplayer-sample-servlet\WebContent\WEB-INF\lib.

Delete sqlplayer-sample.sqlplayer-sample-0.1-SNAPSHOT-assets.jar, sqlplayer-sample.sqlplayer-sample-0.1-SNAPSHOT-sans-externalized.jar files from sample-servlet\WebContent\WEB-INF\lib.

Deploy sample-servlet\WebContent directory in any Web Application Server.

### License

This software is licensed under the Apache 2 license, quoted below.

Copyright (C) 2016-2016 Jake (Jaehee) Lee.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
