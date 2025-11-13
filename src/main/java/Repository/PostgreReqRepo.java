package Repository;

import java.sql.*;

public class PostgreReqRepo implements IRequirementRepository{
    private final Connection conn;
    public PostgreReqRepo(Connection connection, boolean resetSchema){
        conn = connection;

        if (resetSchema) DropRepo();

        boolean successfulInit = false;
        while (!successfulInit) {
            try (Statement stmt = conn.createStatement()) {
                String[] thTables = {"maxbuildings", "maxbuildinglevel"};

                String sql = """
                    CREATE SCHEMA IF NOT EXISTS req;
                    
                    DROP SCHEMA IF EXISTS public;
                    
                    CREATE TABLE IF NOT EXISTS req.resource (
                        rid int primary key,
                        name varchar(20) not null
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.buildingtype (
                        btid int primary key,
                        type varchar(20) not null
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.building (
                        buildingid int primary key,
                        name varchar(20) not null,
                        btid int not null,
                        rid int not null,
                        CONSTRAINT fk_btid
                            FOREIGN KEY (btid)
                            REFERENCES req.buildingtype (btid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        CONSTRAINT fk_rid
                            FOREIGN KEY (rid)
                            REFERENCES req.resource (rid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
                    );
                    
                    """;

                sql += THTableString(thTables, 4);

                sql += """
                    CREATE TABLE IF NOT EXISTS req.gemvalue (
                        type varchar(20) primary key,
                        val int not null check (val >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.gemshop (
                        usd int primary key check (usd >= 0),
                        numgems int not null check (numgems >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.troop (
                        troopid int primary key,
                        name varchar(20) not null,
                        cost int not null check (cost >= 0),
                        space int not null check (space >= 0),
                        speed int not null check (speed >= 0),
                        range int not null check (range >= 0),
                        raxunlock int not null unique check (raxunlock >= 0),
                        hitspeed float not null check (hitspeed > 0),
                        ground boolean not null default true,
                        atkair boolean not null default false
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.defense (
                        buildingid int primary key,
                        atkair boolean not null default false,
                        hitspeed float not null check (hitspeed > 0),
                        range int not null check (range >= 0),
                        CONSTRAINT fk_buildingid
                            FOREIGN KEY (buildingid)
                            REFERENCES req.building (buildingid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.armycamp (
                        level int primary key check (level >= 1),
                        hp int not null check (hp > 0),
                        maxspace int not null check (maxspace >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.lab (
                        level int primary key check (level >= 1),
                        hp int not null check (hp > 0),
                        maxtrooplevel int not null check (maxtrooplevel >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.resgen (
                        level int primary key check (level >= 1),
                        hp int not null check (hp > 0),
                        perhr int not null check (perhr >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.resstore (
                        level int primary key check (level >= 1),
                        hp int not null check (hp > 0),
                        maxcap int not null check (maxcap >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.cannon (
                        level int primary key check (level >= 1),
                        dmg int not null check (dmg >= 0),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.archtower (
                        level int primary key check (level >= 1),
                        dmg int not null check (dmg >= 0),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.rax (
                        level int primary key check (level >= 1),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.th (
                        level int primary key check (level >= 1),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.trooplevels (
                        troopid int,
                        level int check (level >= 1),
                        dmg int not null check (dmg >= 0),
                        hp int not null check (hp > 0),
                        nextupcost int check (nextupcost >= 0),
                        nextupdays int check (nextupdays >= 0),
                        nextuphrs int check (nextuphrs >= 0),
                        nextupmins int check (nextupmins >= 0),
                        nextupsecs int check (nextupsecs >= 0),
                        CONSTRAINT fk_troopid
                            FOREIGN KEY (troopid)
                            REFERENCES req.troop (troopid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (troopid, level)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.buildingupgrades (
                        btid int,
                        level int check (level >= 1),
                        upcost int not null check (upcost >= 0),
                        updays int not null check (updays >= 0),
                        uphrs int not null check (uphrs >= 0),
                        upmins int not null check (upmins >= 0),
                        upsecs int not null check (upsecs >= 0),
                        CONSTRAINT fk_btid
                            FOREIGN KEY (btid)
                            REFERENCES req.buildingtype (btid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (btid, level)
                    );
                    """;

                stmt.execute(sql);
                IO.println("Successful creation (if necessary) of Postgre Req Schema.");
                successfulInit = true;

                if (resetSchema) FillTables();
            } catch (Exception e1) {
                IO.println("Failed to create Postgre Req Schema (if necessary). Trying again...");
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to create Postgre Req Schema (if necessary) likely interrupted.");
                }
            }
        }
    }
    private String THTableString(String[] tables, int maxTH){
        String create = "";
        for (String table : tables) {
            create += """
                CREATE TABLE IF NOT EXISTS req.%s (
                    btid int primary key,
                    max1 int check (max1 >= 0),
                """.formatted(table);
            for (int i=2; i<=maxTH; i++){
                create += """
                        max%d int check (max%d >= max%d),
                    """.formatted(i, i, i-1);
            }
            create += """
                    CONSTRAINT fk_btid
                        FOREIGN KEY (btid)
                        REFERENCES req.buildingType (btid)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
                
                """;
        }
        return create;
    }

    @Override
    public void DropRepo() {
        boolean successfulDrop = false;
        while (!successfulDrop) {
            try (Statement stmt = conn.createStatement()) {
                String sql = """
                    DROP SCHEMA IF EXISTS req CASCADE;
                    """;

                stmt.execute(sql);
                IO.println("Successful drop (if exists) of Postgre Req Schema.");
                successfulDrop = true;
            } catch (Exception e1) {
                IO.println("Failed to drop Postgre Req Schema (if exists). Trying again...");
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to drop Postgre Req Schema (if exists) likely interrupted.");
                }
            }
        }
    }
    private void FillTables(){

    }
}
