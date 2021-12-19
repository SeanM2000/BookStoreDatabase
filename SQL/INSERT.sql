delete from orderdata;
delete from restock;
delete from transfer;
delete from collection;
delete from customer;
delete from owner;
delete from author_of;
delete from book;
delete from publisher;
delete from address;
delete from author;
--Author INSERT
insert into author(name) values('F. Scott Fitzgerald');
insert into author(name) values('ONE');
insert into author(name) values('Yusuke Murata');
insert into author(name) values('Jim Davies');
insert into author(name) values('Mark Haddon');
insert into author(name) values('Tim Ferris');
insert into author(name) values('Sun Tzu');
insert into author(name) values('Daniel J. Leviten');
insert into author(name) values('Micheal Scott');
insert into author(name) values('Orson Scott Card');
insert into author(name) values('Malcolm Gladwell');
insert into author(name) values('Stephen Fry');

--Address INSERT
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(1, '973 Cove Island Terrace', 'Ottawa', 'ON', 'CA','K1V1R4');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(2, '151 Bermondsey Street', 'London', 'Greater London', 'UK','SE1 3HA');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(3, '166 King Street East', 'Toronto', 'ON', 'CA' ,'M5A 1J3');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(4, '120 Broadway', 'New York', 'NY', 'US', '10271');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(5, '320 Front Street West', 'Toronto', 'ON', 'CA','M5V 3B6');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(6, '3 Park Avenue', 'New York', 'NY', 'US','10016');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(7, '77 White Lion Street', 'London', 'Greater London', 'UK','N1 9PF');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(8, '175 Fifth Avenue', 'New York', 'NY', 'US', '10010');
insert into address(address_id, street_address, city, admin_area, country, postal_code) 
values(9, '1290 Avenue of the Americas', 'New York', 'NY', 'US', '10104');

--Publisher INSERT
insert into publisher(email, name, bank_num, address_id)
values('info@arcturuspublishing.com', 'Arcturus Publishing Limited' ,'6674784169911603', 2);
insert into publisher(email, name, bank_num, address_id)
values('info@simonandschuster.ca', 'Simon And Schuster' ,'4747101510130122', 3);
insert into publisher(email, name, bank_num, address_id)
values('info@stmartins.com', 'St. Martin’s Press' ,'7165141594597469', 4);
insert into publisher(email, name, bank_num, address_id)
values('contact@penguinrandomhouse.com', 'Penguin Random House' ,'5423064929101717', 5);
insert into publisher(email, name, bank_num, address_id)
values('contact@hmhco.com', 'Houghton Mifflin Harcourt' ,'0436344371742387', 6);
insert into publisher(email, name, bank_num, address_id)
values('enquiries@amberbooks.co.uk', 'Amber Books Limited' ,'2644082324907648', 7);
insert into publisher(email, name, bank_num, address_id)
values('webmaster@tor-forge.com', 'Tom Doherty Associates' ,'5185381189009991', 8);
insert into publisher(email, name, bank_num, address_id)
values('contact@hbgusa.com', 'Little, Brown and Company' ,'6277761783083824', 9);

--Book INSERT
insert into book (ISBN, book_name, genre, pagecount, email, royalty, price)
values(9781785996139, 'The Great Gatsby', 'Drama', 144, 'info@arcturuspublishing.com', 0.1, 10);
insert into book (ISBN, book_name, genre, email, royalty, price)
values(9781421585642, 'One Punch Man', 'Manga', 'info@simonandschuster.ca', 0.2, 5);
insert into book (ISBN, book_name, genre, email, royalty, price)
values(9781137279019, 'Riveted', 'Non-Fiction', 'info@stmartins.com', 0.12, 0.5);
insert into book (ISBN, book_name, genre, pagecount, email, royalty, price)
values(9780385659802, 'the curious incident of the dog in the night-time', 'Mystery', 226, 'contact@penguinrandomhouse.com', 0.2, 7.50);
insert into book (ISBN, book_name, pagecount, email, royalty, price)
values(9781328683786, 'Tools of Titans', 345, 'contact@hmhco.com', 0.0875, 15);
insert into book (ISBN, book_name, genre, email, royalty, price)
values(9781907446788, 'The Art of War', 'Historical', 'enquiries@amberbooks.co.uk', 0.12, 10);
insert into book (ISBN, book_name, genre, pagecount, email, royalty, price)
values(9780143189442, 'The Organized Mind', 'Non-Fiction', 498, 'contact@penguinrandomhouse.com', 0.17, 10);
insert into book (ISBN, book_name, genre, email, royalty, price)
values(9780385735322, 'The Necromancer: The Immortal Secrets of Nicholas Flamel', 'Fantasy', 'contact@penguinrandomhouse.com', 0.2, 7.85);
insert into book (ISBN, book_name, genre, pagecount, email, royalty, price)
values(9780765337320, 'Enders Game', 'Sci-Fi', 379, 'webmaster@tor-forge.com', 0.15, 8.50);
insert into book (ISBN, book_name, genre, pagecount, email, royalty, price)
values(9780316478526, 'Talking to Strangers', 'Historical', 386, 'contact@hbgusa.com', 0.15, 9.50);
insert into book (ISBN, book_name, genre, email, royalty, price)
values(9780718188740, 'Mythos', 'Historical', 'contact@penguinrandomhouse.com', 0.25, 9.20);

--author_of
insert into author_of(ID, ISBN) 
select ID, 9781785996139
from author
where name = 'F. Scott Fitzgerald'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9781421585642
from author
where name = 'ONE'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9781421585642
from author
where name = 'Yusuke Murata'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9781137279019
from author
where name = 'Jim Davies'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9780385659802
from author
where name = 'Mark Haddon'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9781328683786
from author
where name = 'Tim Ferris'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9781907446788
from author
where name = 'Sun Tzu'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9780143189442
from author
where name = 'Daniel J. Leviten'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9780385735322
from author
where name = 'Micheal Scott'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9780765337320
from author
where name = 'Orson Scott Card'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9780316478526
from author
where name = 'Malcolm Gladwell'
limit 1;
insert into author_of(ID, ISBN) 
select ID, 9780718188740
from author
where name = 'Stephen Fry'
limit 1;

--Owner INSERT
insert into owner values('Sean', 'martinsean000@gmail.com', '1234567890123456', 'Sean Martin Inc');
insert into owner(username, email, bank_num, company_name) values('MrBezos', 'amazon@amazon.com', '0071072409873121', 'Amazon');
insert into owner(username, email, bank_num, company_name) values('Pig', 'pork@gmail.com', '2758692139129615', 'BarnsAndNobles');
insert into owner(username, email, bank_num) values('Carleton', 'carleton@carleton.ca', '7517650440629993');

--customer
insert into customer(username, bank_num)
values('Ian', '5318008133769');
insert into customer(username, bank_num)
values('Gregory', '8833569967842936');
insert into customer(username, bank_num)
values('Admin', '4999678465282779');
insert into customer(username, bank_num)
values('Yusuf', '6235722116665089');

--buying Books from a publisher INSERTS for COLLECTION, RESTOCK, TRANSFER
call buyfromPublisher('Sean', 9780765337320, 17, 19.99);
call buyfromPublisher('Sean', 9781785996139, 70, 21.99);
call buyfromPublisher('Sean', 9781421585642, 50, 25.50);
call buyfromPublisher('Sean', 9781421585642, 22, 17.99);
call buyfromPublisher('Sean', 9780718188740, 40, 24.99);
call buyfromPublisher('Sean', 9781907446788, 35, 21.99);
call buyfromPublisher('Sean', 9781328683786, 66, 39.99);
call buyfromPublisher('MrBezos', 9781785996139, 100, 17.99);
call buyfromPublisher('MrBezos', 9781907446788, 22, 23.50);
call buyfromPublisher('MrBezos', 9781328683786, 21, 22.00);
call buyfromPublisher('MrBezos', 9781907446788, 43, 32);
call buyfromPublisher('Pig', 9781907446788, 50, 22);
call buyfromPublisher('Pig', 9780385735322, 18, 30);
call buyfromPublisher('Sean', 9780385735322, 54, 21);
call buyfromPublisher('Pig', 9780385659802, 13, 32);
call buyfromPublisher('Pig', 9780143189442, 34, 22);
call buyfromPublisher('Carleton', 9781421585642, 43, 40);
call buyfromPublisher('Carleton', 9780385659802, 12, 40);
call buyfromPublisher('Carleton', 9780316478526, 32, 40);

--Buying Books as a customer from an owner INSERTS for Order, Transfer UPDATE for collection
call orderBook('Ian', 'Sean', 9780765337320, 7, 1, 1, '535073077340343'); -- stock: 10
call orderBook('Ian', 'Sean', 9780765337320, 7, 1, 1, '535073077340343'); -- stock: 3 -> 17
call orderBook('Ian', 'Sean', 9780765337320, 3, 1, 1, '535073077340343'); -- stock: 14

select setval(pg_get_serial_sequence('address', 'address_id'), (select max(address_id) from address)+1);
