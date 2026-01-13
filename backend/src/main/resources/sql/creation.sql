--- if is done by personal lending proximity to one another would be preferred
--- to research more specific locations, enum is probably not enough
CREATE type REGION_ENUM as ENUM(
 'England',
 'Portugal',
 'Spain',
 'France',
 'Germany',
 'Russia',
 'Japan',
 'Italy',
 'Israel',
 'Sweden',
 'USA',
 'China',
 'Brasil',
 'Equador'
);

--could be table instead of enum

CREATE TABLE Region(
    name varchar(50) primary key
);


CREATE TABLE App_User(
    id serial primary key NOT NULL,
    region varchar(50) references Region(name) NOT NULL,--reg REGION_ENUM NOT NULL,
    email varchar(70) UNIQUE NOT NULL,
    password varchar(256) NOT NULL
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

--- either isbn 10 or isbn_13 may be required to not be null
CREATE TABLE Book(
    id serial primary key NOT NULL,
    isbn_10 integer check(isbn_10 > 0 and isbn_10 < 9999999999),
    isbn_13 bigint check(isbn_13 > 0 and isbn_13 < 9999999999999), -- needs to be bigint for size
    title varchar(100) NOT NULL, --maybe not necessary if info found by isbn; also see size
    lang LANGUAGE NOT NULL
);

--- maybe add book genres

---user shared instances off books
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
    date timestamp NOT NULL DEFAULT now(), --- check others than timestamp
    duration integer NOT NULL --- request time? in days? to check
);

--- Accepted requests would go here
CREATE TABLE Lend(
    requester_user_id integer references App_User(id) NOT NULL,
    requested_user_id integer NOT NULL,
    requested_book_id integer NOT NULL,
    foreign key (requested_user_id, requested_book_id) references Owned(user_id, book_id),
    primary key(requester_user_id, requested_user_id, requested_book_id),
    date timestamp NOT NULL DEFAULT now(), --- check others than timestamp
    return_limit timestamp NOT NULL --- check others than timestamp
);