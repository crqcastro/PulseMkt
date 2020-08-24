/*
SCHEMA
*/
drop database if exists pulsemkt;

create database pulsemkt;

use pulsemkt;

alter database `pulsemkt` default character set utf8 default collate utf8_unicode_ci;

/*
TABLES
*/
drop table if exists cart_products;

drop table if exists cart_paymethods;

drop table if exists users;

drop table if exists products;

drop table if exists carts;

drop table if exists paymethods;

drop table if exists delivery;

drop table if exists address;

create table address( addressid int not null auto_increment primary key,
address text not null,
addressnumber varchar(10),
addresscompl varchar(255),
city varchar(100) not null,
state varchar(2) not null) engine = innodb;

create table users( userid int not null auto_increment primary key,
username varchar(100) not null,
useremail varchar(255) not null,
usernumber varchar(14) not null,
userpwd varchar(255) not null,
userStatus char(1) not null default 'A',
addressid int not null,
userrole varchar(50) not null default 'USER',
foreign key (addressid) references address(addressid),
unique(useremail)) engine = InnoDB;

create table delivery( deliveryid int not null auto_increment primary key,
deliverytype int not null,
deliverydesc varchar(100) not null,
addressid int,
deliverystatus char(1) not null default 'A',
foreign key (addressid) references address(addressid)) engine = InnoDB;

create table products( productId int not null auto_increment primary key,
productBarcode varchar(13) not null unique,
productDescription varchar(100) not null,
productValue double not null default 0.0,
productImg varchar(255) default 'default.svg',
productStatus char(1) not null default 'A') engine = InnoDB;

create table carts( cartId int not null auto_increment primary key,
userId int not null,
deliveryid int,
enddate datetime,
cartstatus char(1) not null default 'A',
foreign key (deliveryid) references delivery(deliveryid),
foreign key (userId) references users(userId)) engine = InnoDB;

create table cart_products( cartId int not null,
productId int not null,
amount double not null default 1.0 ,
unitValue double not null default 0.0,
foreign key (cartId) references carts(cartId),
foreign key (productId) references products(productId),
check(amount >= 0)) engine = InnoDB;

create table paymethods( paymethodid int not null auto_increment primary key,
paymethoddescription varchar(50) not null unique,
paymethodstatus char(1) not null default 'A') engine = InnoDB;

create table cart_paymethods( cartId int not null,
paymethodid int not null,
amount double not null default 0.0,
cartpaystatus char(1) not null default 'A',
foreign key (cartId) references carts(cartId),
foreign key (paymethodid) references paymethods(paymethodid)) engine = InnoDB;

/*
EXEMPLE INSERT
*/
insert into paymethods (paymethoddescription) values ('Generic');

insert into products (productBarcode, productDescription, productValue) values ('789123456789', 'Produto de teste 1', 10.99), ('789126541789', 'Produto de teste 2', 10.99);

insert into address (address, addressnumber, addresscompl, city, state) values ('Av Daniel de la Touche', '123', 'Ao lado da BlackSwain', 'São Luis', 'MA');

insert into delivery (deliverytype, deliverydesc, addressid, deliverystatus) values (1, 'withdraw in store', null, 'A'), (2, 'Mateus Cohama', 1, 'A');

insert into users (username, useremail, usernumber, userpwd, userStatus, addressid, userrole) values('Admin', 'admin@pulsemkt.com', '99999999999', sha1('Admin'), 'A', 1, 'ADMINISTRATOR');

/*
TRIGGERS, FUNCTIONS E PROCEDURES
*/
drop trigger if exists trig_cart_pay_del;

drop trigger if exists trig_cart_pay_upd;

drop procedure if exists stp_finalize_order;

DELIMITER $$
create trigger trig_cart_pay_del 
before delete on pulsemkt.cart_paymethods for each row 
begin 
	if(old.cartpaystatus != 'A') then 
		signal sqlstate '45000' set message_text = 'it is not possible to exclude payments that are not active'; 
	end if; 
END$$
DELIMITER ;

DELIMITER $$
create trigger trig_cart_pay_upd 
before update on pulsemkt.cart_paymethods for each row 
begin 
	if(old.cartpaystatus != 'A') then 
		signal sqlstate '45000' set message_text = 'it is not possible to exclude payments that are not active'; 
    end if; 
END$$
DELIMITER ;

DELIMITER $$
create procedure stp_finalize_order() 

begin 
	declare done boolean default false;
	declare c1 cursor for
			select c.cartid from carts c where c.cartstatus = 'D'; 
	
	declare continue HANDLER for not found set done = true; 
	open c1; 
	
	cart:loop 
		if done then 
			leave cart; 
		end if;
		update carts set carts.cartstatus = 'F' where carts.cartId = c1.cartid; 
	end loop; 
	close c1; 
END$$
DELIMITER ;

/*
SCHEDULES
*/
drop event if exists update_issues_tree;

create event sch_finalize_carts
    on schedule
	every 20 second
    starts now()
    comment 'For teste, this run procedure who finalize every cart when then status is equals D'
    do call stp_finalize_order();

set global event_scheduler = on;    