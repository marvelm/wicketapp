This webapp makes a few assumptions about the environment in which it is run.

#### postgres
- server: 'localhost:5432'.
- username: 'postgres'
- password: ''
- database: 'wicketdb'

These settings can be changed in the `sessionFactory` bean in 'applicationContext.xml' under 'hibernateProperties'.
The app will destroy all previous tables in the database. The 'applicationContext.xml' is loaded from 'src/main/resources/applicationContext.xml'.

To use the recently played games feature in offline mode, set the value of
the `offlineMode` bean to `true` in 'applicationContext.xml'.

The app will create a demo user on start.
- username: 'john'
- password: 'hunter2'

It will also randomly generate 10 users to demonstrate the user search
functionality. These users all have the same password: 'hunter2'.

#### Running the application
Import the project into your favourite IDE and run
'src/test/java/com/tf2center/Start.java'.
