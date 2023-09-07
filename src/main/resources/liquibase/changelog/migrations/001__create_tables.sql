CREATE TABLE IF NOT EXISTS public."user" (
	id bigserial NOT NULL,
	username varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	CONSTRAINT uk_username UNIQUE (username),
	CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.file (
	id bigserial NOT NULL,
	"content" oid NOT NULL,
	"date" timestamp(6) NOT NULL,
	filename varchar(255) NOT NULL,
	"size" int8 NOT NULL,
	"type" varchar(255) NULL,
	user_id int8 NOT NULL,
	CONSTRAINT file_pkey PRIMARY KEY (id)
);