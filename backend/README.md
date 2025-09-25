# About this project

This project is for personal training of server backend technologies, focusing on 
using a database with JPA access and spring.

The provided service from this server is of an app where users can borrow physical books from each other temporarily.
Anyone is free to announce to the server books they are willing to lend, which other users can request for a limited 
time. Some capabilities to browse books are to be added.

Admin user capabilities may also be added, allowing special operations like suspending/banning users, such as 
harassment through any communication in the server or adding fake books. The damaging books or failing to return them
to their owners after the time limit will also be added, even if in this person to person lending framework may not 
allow any realistic way for these cases to be actually verified.

## Database

Postgres is the chosen database. Its related files can be found [here](./src/main/resources/sql).

