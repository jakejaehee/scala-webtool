## Scala Webtool

Implemented in Scala, Play Framework. Save SQL statement in XML files and request to execute query using them against database and get the result.
Diverse forms of request and response message formats are supported.

### Folders

- scala-webtool/ - SQL Player framework source project
- scala-webtool-sample/ - Sample source project running on Play Framework in development mode.
- scala-webtool-sample-prd/ - Sample binaries running on Play Framework in production mode
- scala-webtool-sample-servlet/ - Sample servlet project
- config/ - Configuration file and SQL XML files

### scala-webtool/

```
$ cd scala-webtool
$ sbt
> compile   -> generates target\scala-2.11\classes
> package   -> generates target\scala-2.11\scala-webtool_2.11-0.1.1.jar
```

### scala-webtool-sample/

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

Copy scala-webtool\target\scala-2.11\scala-webtool_2.11-0.1.1.jar to scala-webtool-sample\lib

```
$ cd scala-webtool-sample
$ activator
[scala-webtool-sample] $ compile
[scala-webtool-sample] $ ~ run
```

Then you are ready to test this sample application.
Connect with http://localhost:9000/assets/index.html

To shutdown, press Ctrl+D

### Making stage mode files

```
$ cd scala-webtool-sample
$ activator
```

To generate secret

```
[scala-webtool-sample] $ playGenerateSecret
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

### scala-webtool-sample-prd/

Copy all files in target/universal/stage directory to in ..\scala-webtool-sample-prd

```
$ cd scala-webtool-sample-prd
$ bin\scala-webtool-sample.bat -Dconfig.resource=production.conf
```

To assign certain port and IP address. Default port is 9000.

```
$ bin\scala-webtool-sample.bat -Dconfig.resource=production.conf -Dhttp.port=9090 -Dhttp.address=127.0.0.1
```

To test, connect with http://localhost:9090/assets/index.html

To shutdown, press Ctrl+C

### scala-webtool-sample-servlet/

Copy all files in scala-webtool-sample\public into scala-webtool-sample-servlet\WebContent\assets.

Copy scala-webtool-sample\target\scala-2.11\classes into scala-webtool-sample-servlet\WebContent\WEB-INF.

Copy all files in scala-webtool-sample-prd\lib into scala-webtool-sample-servlet\WebContent\WEB-INF\lib.

Deploy sample-servlet\WebContent directory in any Web Application Server.

### License

This software is licensed under the Apache 2 license, quoted below.

Copyright (C) 2016-2016 Jake (Jaehee) Lee.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
