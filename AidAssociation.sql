--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.1

-- Started on 2021-01-02 18:39:04

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "depremYardim";
--
-- TOC entry 3054 (class 1262 OID 32778)
-- Name: depremYardim; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "depremYardim" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Turkish_Turkey.1254';


ALTER DATABASE "depremYardim" OWNER TO postgres;

\connect "depremYardim"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 661 (class 1247 OID 40972)
-- Name: logistics_stok; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.logistics_stok AS (
	lo_from character varying(15),
	lo_item character varying(15),
	lo_date date,
	lo_plate character varying(12),
	lo_category character varying(10)
);


ALTER TYPE public.logistics_stok OWNER TO postgres;

--
-- TOC entry 214 (class 1255 OID 40979)
-- Name: liste_top_log(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.liste_top_log(co_to character varying) RETURNS public.logistics_stok[]
    LANGUAGE plpgsql
    AS $$
DECLARE
log_stok_cursor CURSOR FOR SELECT co_from,item_carry,departure_time,trucks_plate,category FROM logistics,stok WHERE co_to = destination AND item_name = item_carry ORDER BY departure_time LIMIT 1;
logs logistics_stok[];
i integer;
BEGIN
i:=1;
 FOR iterator IN log_stok_cursor LOOP
 logs[i] = iterator;
 i:=i+1;
 END LOOP;
 
 RETURN logs;
END;
$$;


ALTER FUNCTION public.liste_top_log(co_to character varying) OWNER TO postgres;

--
-- TOC entry 213 (class 1255 OID 40974)
-- Name: listele_logistics(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.listele_logistics(co_to character varying) RETURNS public.logistics_stok[]
    LANGUAGE plpgsql
    AS $$
DECLARE
log_stok_cursor CURSOR FOR SELECT co_from,item_carry,departure_time,trucks_plate,category FROM logistics,stok WHERE co_to = destination AND item_name = item_carry;
logs logistics_stok[];
i integer;
BEGIN
 i := 1;
 FOR iterator IN log_stok_cursor LOOP
 logs[i] = iterator;
 i := i + 1;
 END LOOP;
 RETURN logs;
END;
$$;


ALTER FUNCTION public.listele_logistics(co_to character varying) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 203 (class 1259 OID 32794)
-- Name: donor_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.donor_info (
    d_name character varying(15) NOT NULL,
    d_lastname character varying(15) NOT NULL,
    contact_number character(11),
    tc_no character(11) NOT NULL,
    last_time_donate date
);


ALTER TABLE public.donor_info OWNER TO postgres;

--
-- TOC entry 226 (class 1255 OID 40985)
-- Name: listele_top_donator(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.listele_top_donator(limit_num integer) RETURNS public.donor_info[]
    LANGUAGE plpgsql
    AS $$
DECLARE
donor_cursor CURSOR FOR SELECT d_name,d_lastname,contact_number,tc_no,last_time_donate FROM donor_info WHERE tc_no IN (SELECT donor_tcno FROM function_view);
di_d donor_info[];
i integer;
BEGIN
 i := 1;
 FOR iterator IN donor_cursor LOOP
 IF i < limit_num+1 THEN
 di_d[i] = iterator;
 END IF;
 i := i + 1;
 END LOOP;
 RETURN di_d;
END;
$$;


ALTER FUNCTION public.listele_top_donator(limit_num integer) OWNER TO postgres;

--
-- TOC entry 210 (class 1255 OID 32815)
-- Name: trigger_on_donor_donation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_on_donor_donation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
     UPDATE stok
	 SET item_quantity = item_quantity + new.quantity
	 WHERE item_name = new.item_name;
	 return new;
end; $$;


ALTER FUNCTION public.trigger_on_donor_donation() OWNER TO postgres;

--
-- TOC entry 211 (class 1255 OID 32817)
-- Name: trigger_on_donor_info(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_on_donor_info() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
     UPDATE donor_info
	 SET last_time_donate = new.donate_date
	 WHERE new.donor_tcno = tc_no;
     return new;
end; $$;


ALTER FUNCTION public.trigger_on_donor_info() OWNER TO postgres;

--
-- TOC entry 212 (class 1255 OID 32819)
-- Name: trigger_on_employee(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.trigger_on_employee() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
    insert into login_info
    select new.ssn,new.ssn;
    return new;
end; $$;


ALTER FUNCTION public.trigger_on_employee() OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 32784)
-- Name: donor_donation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.donor_donation (
    donor_donation_id integer NOT NULL,
    donate_date date,
    donor_tcno character(11) NOT NULL,
    item_name character varying(15),
    item_description text,
    quantity integer NOT NULL
);


ALTER TABLE public.donor_donation OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 32790)
-- Name: donor_donation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.donor_donation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.donor_donation_id_seq OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 32799)
-- Name: employee_volunteer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee_volunteer (
    ssn character(4) NOT NULL,
    mssn character(4),
    e_name character varying(15) NOT NULL,
    e_lastname character varying(15) NOT NULL,
    is_emp boolean,
    is_manager boolean DEFAULT false
);


ALTER TABLE public.employee_volunteer OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 40981)
-- Name: function_view; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.function_view AS
 SELECT donor_donation.donor_tcno,
    sum(donor_donation.quantity) AS sum
   FROM public.donor_donation
  GROUP BY donor_donation.donor_tcno, donor_donation.item_name
 HAVING ((donor_donation.item_name)::text = 'TL'::text)
  ORDER BY (sum(donor_donation.quantity)) DESC;


ALTER TABLE public.function_view OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 32810)
-- Name: logistics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.logistics (
    co_from character varying(15) NOT NULL,
    destination character varying(15) NOT NULL,
    trucks_plate character varying(12) NOT NULL,
    departure_time date,
    item_carry character varying(15),
    CONSTRAINT departure_bg_ck CHECK ((departure_time < '2022-01-01'::date)),
    CONSTRAINT departure_ck CHECK ((departure_time > '2020-01-01'::date))
);


ALTER TABLE public.logistics OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 32779)
-- Name: stok; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stok (
    category character varying(10) NOT NULL,
    item_name character varying(15) NOT NULL,
    item_quantity integer
);


ALTER TABLE public.stok OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 32843)
-- Name: log_view; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.log_view AS
 SELECT stok.item_name
   FROM public.stok
EXCEPT
 SELECT logistics.item_carry AS item_name
   FROM public.logistics;


ALTER TABLE public.log_view OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 32805)
-- Name: login_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.login_info (
    id character(4) NOT NULL,
    psswd character(4)
);


ALTER TABLE public.login_info OWNER TO postgres;

--
-- TOC entry 3043 (class 0 OID 32784)
-- Dependencies: 201
-- Data for Name: donor_donation; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (1, '2020-08-02', '15818159846', 'Kazak', 'Mor, Medium beden orgu', 5);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (2, '2020-07-10', '15818159846', 'TL', 'Türk lirası', 200);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (3, '2020-07-08', '29015075741', 'Battaniye', 'Beyaz,Çift kişilik', 25);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (4, '2020-12-19', '55812442442', 'Mont', 'Kırmızı,Large beden kapüşonlu', 15);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (5, '2020-07-14', '93616707267', 'Meşrubat', 'Su, 5Lt', 25);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (6, '2020-07-14', '93616707267', 'Gömlek', 'Kırmızı,Large beden yakalı', 10);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (7, '2020-07-30', '29015075741', 'Battaniye', 'Mor,Çift kişilik', 20);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (8, '2020-07-30', '93616707267', 'Gömlek', 'Beyaz,Medium Boy Yakalı', 2);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (9, '2020-07-30', '29015075741', 'Ayakkabı', 'Sarı,40 Numara spor', 15);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (10, '2020-07-30', '65863538745', 'Battaniye', 'Beyaz,Tek kişilik', 12);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (11, '2020-07-19', '15818159846', 'Kazak', 'Pembe, Medium beden orgu', 5);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (12, '2020-08-02', '86749076488', 'Pantolon', 'Jean, Small beden ', 1);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (13, '2020-08-02', '86749076488', 'Pantolon', 'Jean, Medium beden ', 1);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (14, '2020-08-02', '86749076488', 'Pantolon', 'Jean, Large beden ', 1);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (15, '2020-10-05', '29015075741', 'Gömlek', 'Sarı,Medium beden yakasız', 5);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (16, '2020-11-19', '71298190480', 'Gömlek', 'pembe, Large beden yakalı', 10);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (17, '2020-12-06', '29015075741', 'Ayakkabı', 'Beyaz,38 numara spor', 5);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (18, '2020-12-19', '28795147021', 'Gömlek', 'pembe, Large beden yakalı', 10);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (19, '2020-12-26', '99381544634', 'Pantolon', 'Siyah,Medium beden', 2);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (20, '2020-12-31', '71298190480', 'Pantolon', 'Kırmızı,Small beden', 5);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (21, '2020-07-15', '65863538745', 'TL', 'Türk Lirası', 50);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (22, '2020-09-12', '15818159846', 'TL', 'Türk lirası', 250);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (23, '2020-09-12', '16787093736', 'TL', 'Türk lirası', 100);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (24, '2020-10-16', '55812442442', 'TL', 'Türk Lirası', 550);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (25, '2020-10-26', '55812442442', 'TL', 'Türk Lirası', 10000);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (26, '2020-07-19', '86749076488', 'TL', 'Türk Lirası', 750);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (27, '2020-07-05', '93616707267', 'Dolar', 'Amerikan Dolari', 120);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (28, '2020-07-11', '71298190480', 'Dolar', 'Amerikan Doları', 100);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (29, '2020-07-22', '93616707267', 'Barbunya', 'Konserve yiyecek', 200);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (30, '2020-07-22', '93616707267', 'Pilaki', 'Konserve yiyecek', 100);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (31, '2020-12-21', '99381544634', 'TL', 'Türk lirası', 100);
INSERT INTO public.donor_donation (donor_donation_id, donate_date, donor_tcno, item_name, item_description, quantity) VALUES (32, '2020-12-31', '12345678987', 'Battaniye', 'Sicacik varya', 3);


--
-- TOC entry 3045 (class 0 OID 32794)
-- Dependencies: 203
-- Data for Name: donor_info; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Serdar', 'Kara', '11826491050', '29015075741', '2020-12-06');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Azra', 'Eksi', '99071263422', '28795147021', '2020-12-19');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Efe', 'Babalı', '40989172072', '65863538745', '2020-07-15');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Ebru', 'Canberk', '6386291929 ', '15818159846', '2020-09-12');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Halil', 'Bozoglu', '44449148241', '16787093736', '2020-09-12');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Melek', 'Sabancı', '91232234443', '55812442442', '2020-10-26');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Goktug', 'Koksal', '03355991069', '86749076488', '2020-07-19');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Murat', 'Ayyildiz', '30426939241', '71298190480', '2020-07-11');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Metin', 'Dagli', '03492856169', '93616707267', '2020-07-22');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Kerem', 'Atik', '98911536437', '99381544634', '2020-12-21');
INSERT INTO public.donor_info (d_name, d_lastname, contact_number, tc_no, last_time_donate) VALUES ('Kimolaki', 'Kagusan', '12345678987', '12345678987', '2020-12-31');


--
-- TOC entry 3046 (class 0 OID 32799)
-- Dependencies: 204
-- Data for Name: employee_volunteer; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0001', '0001', 'Sercan', 'Aksoy', true, true);
INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0002', '0001', 'Yunus', 'Ayar', true, false);
INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0008', '0001', 'Göktuğ', 'Ayar', true, false);
INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0010', '0001', 'Ali', 'Biçim', true, false);
INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0013', '0001', 'Yusuf', 'Turhan', true, false);
INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0015', '0001', 'Ayris', 'Tadik', false, false);
INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0005', '0001', 'Göktuğ', 'Köksal', true, true);
INSERT INTO public.employee_volunteer (ssn, mssn, e_name, e_lastname, is_emp, is_manager) VALUES ('0003', '0001', 'Metin', 'Binbir', true, true);


--
-- TOC entry 3047 (class 0 OID 32805)
-- Dependencies: 205
-- Data for Name: login_info; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.login_info (id, psswd) VALUES ('0001', '0001');
INSERT INTO public.login_info (id, psswd) VALUES ('0008', '0008');
INSERT INTO public.login_info (id, psswd) VALUES ('0010', '0010');
INSERT INTO public.login_info (id, psswd) VALUES ('0013', '0013');
INSERT INTO public.login_info (id, psswd) VALUES ('0015', '0015');
INSERT INTO public.login_info (id, psswd) VALUES ('0005', '0005');
INSERT INTO public.login_info (id, psswd) VALUES ('0003', '0003');
INSERT INTO public.login_info (id, psswd) VALUES ('0002', 'gg11');


--
-- TOC entry 3048 (class 0 OID 32810)
-- Dependencies: 206
-- Data for Name: logistics; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Mardin', 'Kastamonu', '13 YHU 4245', '2020-05-16', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Antalya', 'Iğdır', '56 OSS 4552', '2020-06-11', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('İstanbul', 'Adıyaman', '34 KKA 3535', '2020-08-25', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Ankara', 'Istanbul', '06 AIA 4005', '2020-06-21', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Istanbul', 'Antalya', '34 IAI 2323', '2020-06-11', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Istanbul', 'Kilis', '34 IKI 0340', '2020-10-27', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Izmir', 'Agri', '35 AAA 4000', '2020-12-31', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Osmaniye', 'Burdur', '80 OBO 4245', '2020-12-20', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Ankara', 'Malatya', '06 KMK 0511', '2020-10-15', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Aydın', 'Edirne', '21 GOK 7444', '2020-06-03', 'Pantolon');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Kilis', 'Erzurum', '36 SQSQ 2442', '2020-09-24', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Istanbul', 'Izmir', '34 III 4245', '2020-12-31', 'Ayakkabı');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('İzmir', 'Trabzon', '36 HKA 4125', '2020-01-17', 'Su 5Lt');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Ankara', 'Izmir', '06 AAA 4245', '2020-10-25', 'Gömlek');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Mardin', 'Izmir', '47 MMM 0520', '2020-08-14', 'Barbunya');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Manisa', 'Izmir', '48 ATK 0099', '2020-05-10', 'Battaniye');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('İzmir', 'Istanbul', '51 HUH 4444', '2020-11-23', 'Su 1.5Lt');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Trabzon', 'Istanbul', '81 HQA 3939', '2020-07-28', 'TL');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Mardin', 'Izmir', '65 BTA 9261', '2020-07-10', 'Pilaki');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Adıyaman', 'Istanbul', '34 ATA 2982', '2020-08-02', 'Dolar');
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Istanbul', 'Izmir', '34 IIA 0305', '2020-01-17', NULL);
INSERT INTO public.logistics (co_from, destination, trucks_plate, departure_time, item_carry) VALUES ('Izmir', 'Kastamonu', '35 YOK 5555', '2021-02-02', NULL);


--
-- TOC entry 3042 (class 0 OID 32779)
-- Dependencies: 200
-- Data for Name: stok; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Icecek', 'Su 5Lt', 0);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Icecek', 'Su 1.5Lt', 0);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Icecek', 'Mesrubat(Lt)', 0);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Kiyafet', 'Mont', 15);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Kiyafet', 'Kazak', 10);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Kiyafet', 'Ayakkabı', 20);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Kiyafet', 'Gömlek', 37);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Kiyafet', 'Pantolon', 10);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Para', 'Dolar', 220);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Konserve', 'Barbunya', 200);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Konserve', 'Pilaki', 100);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Para', 'TL', 12000);
INSERT INTO public.stok (category, item_name, item_quantity) VALUES ('Barınma', 'Battaniye', 60);


--
-- TOC entry 3055 (class 0 OID 0)
-- Dependencies: 202
-- Name: donor_donation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.donor_donation_id_seq', 32, true);


--
-- TOC entry 2895 (class 2606 OID 32793)
-- Name: donor_donation donor_donation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.donor_donation
    ADD CONSTRAINT donor_donation_pkey PRIMARY KEY (donor_donation_id);


--
-- TOC entry 2897 (class 2606 OID 32798)
-- Name: donor_info donor_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.donor_info
    ADD CONSTRAINT donor_info_pkey PRIMARY KEY (tc_no);


--
-- TOC entry 2899 (class 2606 OID 32804)
-- Name: employee_volunteer employee_volunteer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_volunteer
    ADD CONSTRAINT employee_volunteer_pkey PRIMARY KEY (ssn);


--
-- TOC entry 2902 (class 2606 OID 32809)
-- Name: login_info login_pk_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_info
    ADD CONSTRAINT login_pk_id PRIMARY KEY (id);


--
-- TOC entry 2904 (class 2606 OID 32814)
-- Name: logistics logistics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.logistics
    ADD CONSTRAINT logistics_pkey PRIMARY KEY (trucks_plate);


--
-- TOC entry 2893 (class 2606 OID 32783)
-- Name: stok stok_pk_cat_nam; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stok
    ADD CONSTRAINT stok_pk_cat_nam PRIMARY KEY (category, item_name);


--
-- TOC entry 2900 (class 1259 OID 32831)
-- Name: fki_employee_volunteer_fkey_mssn; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_volunteer_fkey_mssn ON public.employee_volunteer USING btree (mssn);


--
-- TOC entry 2907 (class 2620 OID 32816)
-- Name: donor_donation trigger_on_donor_donation; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_on_donor_donation AFTER INSERT ON public.donor_donation FOR EACH ROW EXECUTE FUNCTION public.trigger_on_donor_donation();


--
-- TOC entry 2908 (class 2620 OID 32818)
-- Name: donor_donation trigger_on_donor_info; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_on_donor_info AFTER INSERT ON public.donor_donation FOR EACH ROW EXECUTE FUNCTION public.trigger_on_donor_info();


--
-- TOC entry 2909 (class 2620 OID 32820)
-- Name: employee_volunteer trigger_on_employee; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_on_employee AFTER INSERT ON public.employee_volunteer FOR EACH ROW EXECUTE FUNCTION public.trigger_on_employee();


--
-- TOC entry 2905 (class 2606 OID 32821)
-- Name: employee_volunteer fk_emp_col_mssn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee_volunteer
    ADD CONSTRAINT fk_emp_col_mssn FOREIGN KEY (mssn) REFERENCES public.employee_volunteer(ssn) ON DELETE SET NULL;


--
-- TOC entry 2906 (class 2606 OID 32826)
-- Name: login_info login_fk_id_delete; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_info
    ADD CONSTRAINT login_fk_id_delete FOREIGN KEY (id) REFERENCES public.employee_volunteer(ssn) ON DELETE CASCADE;


-- Completed on 2021-01-02 18:39:04

--
-- PostgreSQL database dump complete
--

