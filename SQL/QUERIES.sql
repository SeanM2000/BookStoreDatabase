DROP view IF EXISTS allbooks;
DROP view IF EXISTS viewstore;

create view allbooks as
select book.ISBN, author.name as author, book.book_name as name, publisher.name as pub_name, book.price, book.royalty
from ((book join author_of on book.isbn=author_of.isbn) join author on author_of.ID=author.ID) join publisher on publisher.email=book.email;

create view viewstore as
select owner_username, book.book_name, collection.ISBN, collection.price, collection.stock, publisher.email as pub_email
from ((collection join book on collection.ISBN=book.ISBN) join publisher on book.email=publisher.email);

/*
--Simple Book Info: ISBN, authors, bookname, publisher email
select book.ISBN, author.name, book.book_name, book.email
from (book join author_of on book.isbn=author_of.isbn) join author on author_of.ID=author.ID;

--Search for books with generic search term(must set to lowercase)
select book.ISBN, author.name, book.book_name, book.email
from (book join author_of on book.isbn=author_of.isbn) join author on author_of.ID=author.ID
where lower(book.book_name) like '%tools%';*/