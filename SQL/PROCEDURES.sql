--buy a book from a publisher
create or replace procedure buyfrompublisher
(in_username varchar(40), in_ISBN numeric(13,0), in_qty integer, in_price numeric(6,2))
language plpgsql 
as $$
declare
 t_id integer;
begin
--If the collection already exists, add books into it, otherwise create a new collection
if ((in_username, in_ISBN) in (select owner_username, ISBN from collection)) then
	update collection
	set stock = stock+in_qty, price=in_price
	where in_username=owner_username and in_ISBN=ISBN;
else
	insert into collection(owner_username, ISBN, stock, price) 
	values(in_username, in_ISBN, in_qty, in_price);
end if;
--Create the transfer to the publisher (cost of books * # of books)
insert into transfer(email, price, transfer_date)
select book.email, book.price*in_qty, current_date
from book
where book.ISBN=in_ISBN
returning transfer_id into t_id;
--Log the restocking with this
insert into restock(owner_username, ISBN, quantity, transfer_id)
values(in_username, in_ISBN, in_qty, t_id);
end
$$;

--order book from collection (owner -> customer)
create or replace procedure orderbook
(cust_user varchar(40), own_user varchar(40), order_ISBN numeric(13,0),
in_qty integer, bill_id integer, ship_id int, in_bank_num char(16))
language plpgsql 
as $$
declare
 t_id integer;
begin
--Create the transfer to the publisher (cost of books for the owner * # of books * the royalty %)
insert into transfer(email, price, transfer_date)
select book.email, book.royalty*in_qty*collection.price, current_date
from book join collection on collection.ISBN = book.ISBN
where book.ISBN=order_ISBN and owner_username=own_user
returning transfer_id into t_id;
--Remove the books from the stock
update collection
set stock = stock-in_qty
where own_user=owner_username and order_ISBN=ISBN;
--create the order
insert into orderdata(customer_username, owner_username, ISBN, bank_num, quantity, price,
billing_address_id, shipping_address_id, transfer_id)
select cust_user, own_user, order_ISBN, in_bank_num,in_qty, collection.price*in_qty, bill_id, ship_id, t_id
from collection
where own_user=collection.owner_username and order_ISBN=collection.ISBN;
end
$$;