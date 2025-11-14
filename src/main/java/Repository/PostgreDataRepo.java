package Repository;

import java.sql.*;

public class PostgreDataRepo implements IDataRepository{
    private final Connection conn;
    public PostgreDataRepo(Connection connection, boolean resetSchema){
        conn = connection;

        if (resetSchema) DropRepo();

        boolean successfulInit = false;
        while (!successfulInit) {
            try (Statement stmt = conn.createStatement()) {
                String sql = """
                    CREATE SCHEMA IF NOT EXISTS data;
                    
                    DROP SCHEMA IF EXISTS public CASCADE;
                    
                    CREATE TABLE IF NOT EXISTS data.cc (
                        cardno bigint primary key check (cardno >= 0 and cardno <= 9999999999999999),
                        expmo int not null check (expmo >= 1 and expmo <= 12),
                        expyr int not null,
                        pin int not null check (pin >= 0 and pin <= 999),
                        balance int not null default 0 check (balance >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.profile (
                        profid int primary key,
                        name varchar(50) not null,
                        datecreated timestamp not null default current_timestamp
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.ccprof (
                        cardno bigint,
                        profid int,
                        CONSTRAINT fk_cardno
                            FOREIGN KEY (cardno)
                            REFERENCES data.cc (cardno)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        CONSTRAINT fk_profid
                            FOREIGN KEY (profid)
                            REFERENCES data.profile (profid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (cardno, profid)
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.player (
                        profid int primary key,
                        lastattacked timestamp not null default current_timestamp, -- the last time the player was attacked
                        lastcollected timestamp not null default current_timestamp,
                        gems int not null default 0 check (gems >= 0),
                        trophies int not null default 0 check (trophies >= 0),
                        CONSTRAINT fk_profid
                            FOREIGN KEY (profid)
                            REFERENCES data.profile (profid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.playertrooplvls (
                        profid int,
                        troopid int,
                        level int not null check (level >= 1),
                        CONSTRAINT fk_troopid
                            FOREIGN KEY (troopid)
                            REFERENCES req.troop (troopid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        CONSTRAINT fk_profid
                            FOREIGN KEY (profid)
                            REFERENCES data.profile (profid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (troopid, profid)
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.playerbuildinglvls (
                        profid int,
                        buildid int,
                        level int not null check (level >= 1),
                        buildingid int not null,
                        CONSTRAINT fk_buildingid
                            FOREIGN KEY (buildingid)
                            REFERENCES req.building (buildingid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (buildid, profid)
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.playerresourcebuildings (
                        profid int,
                        buildid int,
                        amount int not null check (amount >= 0),
                        CONSTRAINT fk_profbuildid
                            FOREIGN KEY (profid, buildid)
                            REFERENCES data.playerbuildinglvls (profid, buildid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (buildid, profid)
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.playerarmy (
                        profid int,
                        troopid int,
                        count int not null check (count >= 0),
                        CONSTRAINT fk_proftroopid
                            FOREIGN KEY (profid, troopid)
                            REFERENCES data.playertrooplvls (profid, troopid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (troopid, profid)
                    );
                    
                    CREATE TABLE IF NOT EXISTS data.playerbuildinglineup (
                        profid int,
                        buildid int,
                        pos int not null check (pos >= 0 and pos < 10), -- 10 is hardcoded since ik max 10 buildings
                        CONSTRAINT fk_profbuildid
                            FOREIGN KEY (profid, buildid)
                            REFERENCES data.playerbuildinglvls (profid, buildid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (buildid, profid),
                        UNIQUE (profid, pos)
                    );
                    """;

                stmt.execute(sql);
                IO.println("Successful creation (if necessary) of Postgre Data Schema.");
                successfulInit = true;

                if (resetSchema) FillTables();
            } catch (Exception e1) {
                IO.println("Failed to create Postgre Data Schema (if necessary). Trying again...");
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to create Postgre Data Schema (if necessary) likely interrupted.");
                }
            }
        }
    }

    @Override
    public void DropRepo() {
        boolean successfulDrop = false;
        while (!successfulDrop) {
            try (Statement stmt = conn.createStatement()) {
                String sql = """
                    DROP SCHEMA IF EXISTS data CASCADE;
                    """;

                stmt.execute(sql);
                IO.println("Successful drop (if exists) of Postgre Data Schema.");
                successfulDrop = true;
            } catch (Exception e1) {
                IO.println("Failed to drop Postgre Data Schema (if exists). Trying again...");
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to drop Postgre Data Schema (if exists) likely interrupted.");
                }
            }
        }
    }
    private void FillTables(){

    }
}
