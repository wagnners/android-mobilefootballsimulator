DROP TABLE IF EXISTS  campeonato CASCADE
;

DROP TABLE IF EXISTS  clube CASCADE
;

DROP TABLE IF EXISTS  estadio CASCADE
;

DROP TABLE IF EXISTS  jogador CASCADE
;

DROP TABLE IF EXISTS  jogo CASCADE
;

DROP TABLE IF EXISTS  loja CASCADE
;
CREATE TABLE  loja 
(
	 lojaId  INTEGER NOT NULL PRIMARY KEY
)
;
CREATE TABLE  campeonato 
(
	 estadioId  INTEGER NOT NULL PRIMARY KEY
)
;

CREATE TABLE  clube 
(
	 clubeId  INTEGER NOT NULL PRIMARY KEY,
	 forca  INTEGER,
	 nome  TEXT
)
;

CREATE TABLE  estadio 
(
	 estadioId  INTEGER NOT NULL PRIMARY KEY  ,
	 capacidade  INTEGER,
	 nome  TEXT,
	 precoEntrada  REAL,
	 precoExpansao  REAL,
	 clubeId  INTEGER NOT NULL,

	CONSTRAINT  FK_possui_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action
)
;

CREATE TABLE  jogador 
(
	 jogadorId  INTEGER NOT NULL PRIMARY KEY  ,
	 posicao  INTEGER,
	 jogando  INTEGER,
	 motivacao  INTEGER,
	 habilidade  INTEGER,
	 condicionamento  INTEGER,
	 nome  TEXT,
	 clubeId  INTEGER NOT NULL,
	 lojaId  INTEGER,

	CONSTRAINT  FK_pertence_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action,
	CONSTRAINT  FK_vende_jogador  FOREIGN KEY ( lojaId ) REFERENCES  loja  ( lojaId ) ON DELETE No Action ON UPDATE No Action
)
;

CREATE TABLE  jogo 
(
	 jogoId  INTEGER NOT NULL PRIMARY KEY,
	 golsLocal  INTEGER,
	 golsVisitante  INTEGER,
	 lucro  REAL,
	 vencedor  INTEGER,
	 estadioId  INTEGER,
	 clubeId  INTEGER,

	CONSTRAINT  FK_contem_jogo  FOREIGN KEY ( estadioId ) REFERENCES  campeonato  ( estadioId ) ON DELETE No Action ON UPDATE No Action,
	CONSTRAINT  FK_Visitante_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action,
	CONSTRAINT  FK_Local_clube  FOREIGN KEY ( clubeId ) REFERENCES  clube  ( clubeId ) ON DELETE No Action ON UPDATE No Action
)
;

