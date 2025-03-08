--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: candidates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.candidates (
    id integer NOT NULL,
    party character varying(5) NOT NULL,
    name character varying(30) NOT NULL,
    nr_of_votes numeric DEFAULT 0
);


ALTER TABLE public.candidates OWNER TO postgres;

--
-- Name: candidates_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.candidates_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.candidates_id_seq OWNER TO postgres;

--
-- Name: candidates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.candidates_id_seq OWNED BY public.candidates.id;


--
-- Name: judete; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.judete (
    code character varying(2) NOT NULL,
    region character varying(2),
    serii character varying(40),
    CONSTRAINT check_digits CHECK (((code)::text ~ '^[0-9\.]+$'::text)),
    CONSTRAINT chk_county_only_chars CHECK (((region)::text ~ '^[A-Z]+$'::text))
);


ALTER TABLE public.judete OWNER TO postgres;

--
-- Name: login_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.login_logs (
    index integer NOT NULL,
    admin_id integer NOT NULL,
    login_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    ip_address character varying(45)
);


ALTER TABLE public.login_logs OWNER TO postgres;

--
-- Name: login_logs_index_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.login_logs_index_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.login_logs_index_seq OWNER TO postgres;

--
-- Name: login_logs_index_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.login_logs_index_seq OWNED BY public.login_logs.index;


--
-- Name: parties; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parties (
    code character varying(3) NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.parties OWNER TO postgres;

--
-- Name: registrations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.registrations (
    id integer NOT NULL,
    email character varying(100) NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    creation_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_valid CHECK (((email)::text ~~ '%_@__%.__%'::text)),
    CONSTRAINT first_name_length CHECK ((length((first_name)::text) >= 3)),
    CONSTRAINT last_name_length CHECK ((length((last_name)::text) >= 3)),
    CONSTRAINT password_length CHECK ((length((password)::text) >= 8)),
    CONSTRAINT password_like CHECK (((password)::text ~ '^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$'::text)),
    CONSTRAINT password_no_spaces CHECK (((password)::text !~~ '% %'::text)),
    CONSTRAINT username_length CHECK ((length((username)::text) >= 3)),
    CONSTRAINT username_no_spaces CHECK (((username)::text !~~ '% %'::text))
);


ALTER TABLE public.registrations OWNER TO postgres;

--
-- Name: registrations_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.registrations_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.registrations_id_seq OWNER TO postgres;

--
-- Name: registrations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.registrations_id_seq OWNED BY public.registrations.id;


--
-- Name: voters; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voters (
    index integer NOT NULL,
    cnp character varying(13) NOT NULL,
    seria character varying(2) NOT NULL,
    numar character varying(6) NOT NULL,
    judet character varying(2) NOT NULL,
    CONSTRAINT cnp_only_digits CHECK (((cnp)::text ~ '^[0-9]+$'::text)),
    CONSTRAINT judet_only_digits CHECK (((judet)::text ~ '^[0-9]+$'::text)),
    CONSTRAINT numar_only_digits CHECK (((numar)::text ~ '^[0-9]+$'::text)),
    CONSTRAINT "seria_only_A-Z" CHECK (((seria)::text ~ '^[A-Z]+$'::text))
);


ALTER TABLE public.voters OWNER TO postgres;

--
-- Name: voters_index_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.voters_index_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.voters_index_seq OWNER TO postgres;

--
-- Name: voters_index_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.voters_index_seq OWNED BY public.voters.index;


--
-- Name: votes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.votes (
    candidate_id integer NOT NULL,
    county_code character varying(2) NOT NULL,
    nr_of_votes integer DEFAULT 0
);


ALTER TABLE public.votes OWNER TO postgres;

--
-- Name: candidates id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidates ALTER COLUMN id SET DEFAULT nextval('public.candidates_id_seq'::regclass);


--
-- Name: login_logs index; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_logs ALTER COLUMN index SET DEFAULT nextval('public.login_logs_index_seq'::regclass);


--
-- Name: registrations id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registrations ALTER COLUMN id SET DEFAULT nextval('public.registrations_id_seq'::regclass);


--
-- Name: voters index; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voters ALTER COLUMN index SET DEFAULT nextval('public.voters_index_seq'::regclass);


--
-- Data for Name: candidates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.candidates (id, party, name, nr_of_votes) FROM stdin;
4	PNL	Nicolae-Ionel Ciuca	59644
3	PSD	Ion-Marcel Ciolacu	25962
2	AUR	George Nicolae-Simion	21022
1	USR	Elena Valerica-Lasconi	14492
\.


--
-- Data for Name: judete; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.judete (code, region, serii) FROM stdin;
01	AB	AX
02	AR	AR, ZR
03	AG	AS, AZ
04	BC	XC, ZC
05	BH	XH, ZH
06	BN	XB
07	BT	XT, ZT
08	BV	BV, ZV
09	BR	XR
10	BZ	XZ, ZB
11	CS	KS
12	CJ	KX, CJ
13	CT	KT, KZ
14	CV	KV
15	DB	DD, ZD
16	DJ	DX, DZ
17	GL	GL, ZL
18	GJ	GZ
19	HR	HR
20	HD	HD, XD
21	IL	SZ
22	IS	MX, MZ, IZ
23	IF	IF
24	MM	MM, XM
25	MH	MH
26	MS	ZS, MS
27	NT	NT, NZ
28	OT	OT, SL
29	PH	PH, PX, PK
30	SM	SM
31	SJ	SX
32	SB	SB, SR
33	SV	SV, XV
34	TR	TR
35	TM	TM, TZ
36	TL	TC
37	VS	VS, XS
38	VL	VX
39	VN	VN
40	B	DP, DR, DT, DX, RD, RR, RT, RX, RK, RZ
41	B	DP, DR, DT, DX, RD, RR, RT, RX, RK, RZ
42	B	DP, DR, DT, DX, RD, RR, RT, RX, RK, RZ
43	B	DP, DR, DT, DX, RD, RR, RT, RX, RK, RZ
44	B	DP, DR, DT, DX, RD, RR, RT, RX, RK, RZ
45	B	DP, DR, DT, DX, RD, RR, RT, RX, RK, RZ
46	B	DP, DR, DT, DX, RD, RR, RT, RX, RK, RZ
51	CL	KL
52	GR	GG
\.


--
-- Data for Name: login_logs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.login_logs (index, admin_id, login_time, ip_address) FROM stdin;
\.


--
-- Data for Name: parties; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parties (code, name) FROM stdin;
USR	Uniunea Salvati Romania
AUR	Alianta pentru Unirea Romanilor
PSD	Partidul Social Democrat
PNL	Partidul National Liberal
\.


--
-- Data for Name: registrations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.registrations (id, email, first_name, last_name, username, password, creation_date) FROM stdin;
1	dmcgrouther0@ihg.com	Danyette	McGrouther	dmcgrouther0	cZ2%(MrtIBB,Y	2024-08-09 21:41:01
2	smcturlough1@go.co	Simone	McTurlough	smcturlough1	qM2,,LfoSOSM$4*	2024-09-04 18:49:32
3	rmorlon2@yandex.ru	Raviv	Morlon	rmorlon2	pR4kQ?}J#	2024-03-13 22:16:10
4	lhindshaw3@blogspot.com	Lisbeth	Hindshaw	lhindshaw3	tK4W{>n%rePqz	2024-07-24 19:09:31
5	gbrassill4@gov.uk	Gill	Brassill	gbrassill4	gY7tiO8.y<	2024-01-30 10:21:23
6	htrice5@ifeng.com	Hussein	Trice	htrice5	mH9U?u5_0z	2024-10-27 08:39:07
7	sbraunroth6@guardian.co.uk	Sansone	Braunroth	sbraunroth6	bO4+FKW4%z	2024-10-16 20:41:51
8	dgiovannoni7@free.fr	Deana	Giovannoni	dgiovannoni7	mI0BqrD<&5q	2024-01-14 14:07:46
9	squennell8@unblog.fr	Say	Quennell	squennell8	kV6}wwv)clul	2024-04-18 06:18:53
10	cborham9@mlb.com	Charyl	Borham	cborham9	zD7mzep%	2024-11-20 03:48:56
\.


--
-- Data for Name: voters; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.voters (index, cnp, seria, numar, judet) FROM stdin;
4	5040404311234	SX	123123	31
5	5040401311234	SX	123123	31
6	5040401311232	SX	123123	31
7	5040401311237	SX	123123	31
\.


--
-- Data for Name: votes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.votes (candidate_id, county_code, nr_of_votes) FROM stdin;
1	03	0
1	04	0
1	05	0
1	06	0
1	07	0
1	08	0
1	09	0
1	10	0
1	11	0
1	13	0
1	14	0
1	15	0
1	16	0
1	17	0
1	18	0
1	19	0
1	21	0
1	22	0
1	23	0
1	24	0
1	25	0
1	26	0
1	27	0
1	28	0
1	29	0
1	30	0
1	32	0
1	33	0
1	34	0
1	35	0
1	36	0
1	37	0
1	38	0
1	39	0
1	40	0
1	41	0
1	42	0
1	43	0
1	44	0
1	45	0
1	46	0
1	51	0
1	52	0
2	02	0
2	03	0
2	04	0
2	05	0
2	06	0
2	07	0
2	08	0
2	09	0
2	10	0
2	11	0
2	13	0
2	14	0
2	15	0
2	16	0
2	17	0
2	18	0
2	19	0
2	21	0
2	22	0
2	23	0
2	24	0
2	25	0
2	26	0
2	27	0
2	28	0
2	29	0
2	30	0
2	32	0
2	33	0
2	34	0
2	35	0
2	36	0
2	37	0
2	38	0
2	39	0
2	40	0
2	41	0
2	42	0
2	43	0
2	44	0
2	45	0
2	46	0
2	51	0
2	52	0
3	02	0
3	04	0
3	05	0
3	06	0
3	07	0
3	08	0
3	09	0
3	10	0
3	11	0
3	13	0
3	14	0
3	15	0
3	16	0
3	17	0
3	18	0
3	19	0
3	21	0
3	22	0
3	23	0
3	24	0
3	25	0
3	26	0
3	27	0
3	28	0
3	29	0
3	30	0
3	32	0
3	33	0
3	34	0
3	35	0
3	36	0
3	37	0
3	38	0
3	39	0
3	40	0
3	41	0
3	42	0
3	43	0
3	44	0
3	45	0
3	46	0
3	51	0
3	52	0
4	02	0
4	04	0
4	05	0
4	06	0
4	07	0
4	08	0
4	09	0
4	10	0
4	11	0
4	13	0
4	14	0
4	15	0
4	16	0
4	17	0
4	18	0
4	19	0
4	21	0
4	22	0
4	23	0
4	24	0
4	25	0
4	26	0
4	27	0
4	28	0
4	29	0
4	30	0
4	32	0
4	33	0
4	34	0
4	35	0
4	36	0
4	37	0
4	38	0
4	39	0
4	40	0
4	41	0
3	31	1236
2	01	2176
4	01	42912
3	01	4626
2	12	5212
4	12	10000
3	12	12362
4	42	0
4	43	0
4	44	0
4	45	0
4	46	0
1	02	2107
3	03	5020
4	03	2916
4	51	0
4	52	0
4	31	999
1	01	123
1	12	2142
4	20	2817
1	20	5918
3	20	2718
2	20	11182
2	31	2452
1	31	4202
\.


--
-- Name: candidates_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.candidates_id_seq', 4, true);


--
-- Name: login_logs_index_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.login_logs_index_seq', 89, true);


--
-- Name: registrations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.registrations_id_seq', 1, false);


--
-- Name: voters_index_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.voters_index_seq', 7, true);


--
-- Name: candidates candidates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidates
    ADD CONSTRAINT candidates_pkey PRIMARY KEY (id);


--
-- Name: judete county_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.judete
    ADD CONSTRAINT county_pkey PRIMARY KEY (code);


--
-- Name: login_logs login_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_logs
    ADD CONSTRAINT login_logs_pkey PRIMARY KEY (index);


--
-- Name: parties parties_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parties
    ADD CONSTRAINT parties_pkey PRIMARY KEY (code);


--
-- Name: registrations registrations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registrations
    ADD CONSTRAINT registrations_pkey PRIMARY KEY (id);


--
-- Name: registrations unique_username; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registrations
    ADD CONSTRAINT unique_username UNIQUE (username);


--
-- Name: voters voters_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voters
    ADD CONSTRAINT voters_pkey PRIMARY KEY (index);


--
-- Name: votes votes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes
    ADD CONSTRAINT votes_pkey PRIMARY KEY (candidate_id, county_code);


--
-- Name: candidates fk_candidates_party; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidates
    ADD CONSTRAINT fk_candidates_party FOREIGN KEY (party) REFERENCES public.parties(code);


--
-- Name: voters fk_voters_judet; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voters
    ADD CONSTRAINT fk_voters_judet FOREIGN KEY (judet) REFERENCES public.judete(code);


--
-- Name: login_logs login_logs_admin_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_logs
    ADD CONSTRAINT login_logs_admin_id_fkey FOREIGN KEY (admin_id) REFERENCES public.registrations(id) ON DELETE CASCADE;


--
-- Name: votes votes_candidate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes
    ADD CONSTRAINT votes_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES public.candidates(id);


--
-- Name: votes votes_county_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes
    ADD CONSTRAINT votes_county_code_fkey FOREIGN KEY (county_code) REFERENCES public.judete(code);


--
-- PostgreSQL database dump complete
--

