CREATE TABLE Region(
    name varchar(50) primary key
);


CREATE TABLE App_User(
    id serial primary key NOT NULL,
    region varchar(50) references Region(name) NOT NULL,
    email varchar(70) UNIQUE NOT NULL,
    hash varchar(256) NOT NULL,
    salt varchar(2) NOT NULL
);

-- not necessary if we limit amount of sessions for an account to one. Then delete table and change values to user
CREATE TABLE Token(
    token_validation varchar(256) primary key,
    user_id int references App_User(id),
    created_at timestamp not null DEFAULT now(),
    last_used_at timestamp not null DEFAULT now()
);

CREATE type LANGUAGE as ENUM(
 'English',
 'Portuguese',
 'Spanish',
 'French',
 'German',
 'Russian',
 'Japanese',
 'Italian',
 'Hebrew',
 'Hungarian',
 'Danish'
);

-- https://stackoverflow.com/questions/63967873/postgresql-regex-constraint-so-that-varchar-contains-only-digits
--- either isbn 10 or isbn_13 may be required to not be null
CREATE TABLE Book(
    id serial primary key NOT NULL,
    isbn_10 varchar(10) CHECK(isbn_10 ~ '^[0-9]*[0-9X]$'),
    isbn_13 varchar(13) check(isbn_13 ~ '^[0-9]*[0-9X]$') ,
    title varchar(100) NOT NULL,
    lang LANGUAGE NOT NULL
);

CREATE TABLE Owned(
    user_id integer references App_User(id) NOT NULL,
    book_id integer references Book(id) NOT NULL,
    primary key(user_id, book_id) ---format only allows 1 instance of a specific book from each user
);

CREATE TABLE Request(
    requester_user_id integer references App_User(id) NOT NULL,
    requested_user_id integer NOT NULL,
    requested_book_id integer NOT NULL,
    foreign key (requested_user_id, requested_book_id) references Owned(user_id, book_id),
    primary key(requester_user_id, requested_user_id, requested_book_id),
    date timestamp NOT NULL DEFAULT now(),
    duration integer NOT NULL --- request time in days
);


---Lend table: to allow for multiple lends of book, would require server to ensure no overlaps on the lend dates. Add requester_user_id as part of primary key
---plus ways to warn every user if return on book was delayed
--- primary key cannot be the same due to hibernate roadblocks. Starting to get convinced Hibernate is bugged in some way
CREATE TABLE Lend(
    user_id integer NOT NULL,
    book_id integer NOT NULL,
    foreign key (user_id, book_id) references Owned(user_id, book_id),
    requester_user_id integer references App_User(id) NOT NULL,
    primary key(user_id, book_id, requester_user_id),
    date timestamp NOT NULL DEFAULT now(), --- check others than timestamp
    return_limit timestamp NOT NULL --- check others than timestamp
);
