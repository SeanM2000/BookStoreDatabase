DROP view IF EXISTS allbooks;
DROP view IF EXISTS viewstore;
drop table orderdata;
drop table customer;
drop table restock;
drop table collection;
drop table author_of;
drop table transfer;
drop table book;
drop table publisher;
drop table owner;
drop table address;
drop table author;

/*
Author stores basic info about an author (name)
author_id is a serial primary key, because different authors will have the same name
	This will allow a customer/owner to distinguish between different authors based on their works
*/
create table author(
	ID SERIAL,
	name varchar(40) not null,
	primary key(ID)
);

/*
Address stores the addresses of various entities
	It assumes that the order is going somewhere in Canada, and that it is not delivering to
an apartment builing
address_id - a serial primary key, used to identify the address other places in the code
province - must conform to the Canada Post Internationally Approved Alpha Code,
ZIP -
*/
create table address(
	address_id SERIAL,
	street_address varchar(50) not null,
	city varchar(60),
	admin_area varchar(40), --region, state, province, territory
	country char(2) not null, --2 character ISO code
	postal_code varchar(7),
	primary key(address_id)
);

/*
Owner
*/
create table owner(
	username varchar(30),
	email varchar(40) unique check(email like '%_@__%.__%'),
	bank_num char(16), --Note: Should check for '%[^0-9]%'
	company_name varchar(40),
	primary key(username)
);

/*
Publisher stores information on book publishing companies
*/
create table publisher(
	email varchar(40) check(email like '%_@__%.__%'),
	name varchar(40) not null,
	bank_num char(16) not null, --Note: Should check for '%[^0-9]%'
	address_id integer not null,
	primary key(email),
	foreign key(address_id) references address(address_id)
);

/*
Book stores information about books
	- a book cannot be sold for over $9999.99
*/
create table book(
	ISBN 		numeric(13, 0),
	book_name 	varchar(100) not null,
	genre 		varchar(20),
	pagecount 	integer,
	email 		varchar(40) not null,
	royalty 	numeric(5,4) check(0<=royalty and royalty<1),
	price 		numeric(6, 2) check (price > 0),
	primary key(ISBN),
	foreign key(email) references publisher(email)
		on update cascade
);

/*
Transfer is used to track the payment from owners to the publishers, for both royalty payments and restocks
*/
create table transfer(
	transfer_id SERIAL,
	price numeric(8,2) check(price > 0),
	transfer_date date default(current_date),
	email varchar(40) not null,
	primary key(transfer_id),
	foreign key(email) references publisher(email)
		on update cascade
		on delete set null
);

/*
author_of associates authors with books allowing a book to have multiple authors
*/
create table author_of(
	ISBN numeric(13, 0),
	ID integer,
	primary key(ISBN, ID),
	foreign key(ISBN) references book(ISBN),
	foreign key(ID) references author(ID)
);

/*
Collection is a collection of a specific book by some owner, who is able to restock from the publisher and gain some 
	- a book cannot be sold for over $9999.99
	- book should not be able to be deleted but if somehow that happens I think it is safer to have this (people shouldn't be able to order NULL books)
	- obviously if the ISBN or owner_username would change they should still belong to the collections they are in
*/
create table collection(
	owner_username varchar(40),
	ISBN numeric(13, 0),
	stock integer default 0 check(stock>=0),
	price numeric(6, 2) default 0 check(price>=0),
	primary key(owner_username, ISBN),
	foreign key(owner_username) references owner(username)
		on update cascade,
	foreign key(ISBN) references book(ISBN)
		on update cascade
);

/*
Restock tracks the amount of books purchased at one time from a publisher (used to track expenditures)
*/
create table restock(
	restock_id SERIAL,
	owner_username varchar(30) not null,
	ISBN numeric(13, 0) not null,
	transfer_id integer, -- can be null if it costs 0 dollars to buy the books
	quantity integer check(quantity > 0),
	primary key(restock_id),
	foreign key(owner_username, ISBN) references collection(owner_username, ISBN),
	foreign key(transfer_id) references transfer(transfer_id)
);

/*
Customer stores customer info
	- A customer does not need to have their bank_num, billing_address_id, shipping_address_id because they can put that in when they order, these can be used as defaults
*/
create table customer(
	username varchar(30),
	name varchar(30),
	bank_num char(16), --check(bank_num like '%[^0-9]%')
	billing_address_id integer,
	shipping_address_id integer,
	primary key(username),
	foreign key(billing_address_id) references address(address_id)
		on update cascade,
	foreign key(shipping_address_id) references address(address_id)
		on update cascade
);

/*
Order stores information about orders
*/
create table orderdata(
	order_id SERIAL,
	customer_username varchar(20) not null,
	bank_num varchar(16) not null, --check(bank_num like '%[^0-9]%')
	billing_address_id integer not null,
	shipping_address_id integer not null,
	owner_username varchar(20) not null,
	ISBN numeric(13, 0) not null,
	quantity integer check(quantity > 0),
	price numeric(9, 2) check(price > 0),
	transfer_id integer not null,
	tracking smallint default 0 check(tracking in (0, 1, 2)),
	primary key(order_id),
	foreign key(customer_username) references customer(username)
		on update cascade
		on delete set null,
	foreign key(billing_address_id) references address(address_id),
	foreign key(shipping_address_id) references address(address_id),
	foreign key(owner_username, ISBN) references collection(owner_username, ISBN),
	foreign key(transfer_id) references transfer(transfer_id)
		on delete set null
);
