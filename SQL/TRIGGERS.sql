DROP TRIGGER IF EXISTS restockCollection on collection;

create or replace function stocking()
returns trigger
as $$
declare
 addstock integer;
begin
--Add all order quantities from the last month together to get sum of all orders within the last month
select sum(quantity) into addstock
from orderdata join transfer on orderdata.transfer_id = transfer.transfer_id
where orderdata.ISBN = new.ISBN and (transfer_date between (current_date - interval '1' month) and (current_date + interval '1' day));
--call for an order with the new stuff
call buyfromPublisher(new.owner_username, new.isbn, addstock, new.price);
return new;
end;
$$ language plpgsql;

create trigger restockCollection after update of stock on collection
for each row
when (new.stock < 10)
execute procedure stocking();