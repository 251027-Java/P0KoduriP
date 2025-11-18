package Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
                    
                    DROP SCHEMA IF EXISTS public CASCADE;
                    
                    CREATE TABLE IF NOT EXISTS req.resource (
                        rid int primary key,
                        rname varchar(20) not null
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.buildingtype (
                        btid int primary key,
                        type varchar(20) not null
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.building (
                        buildingid int primary key,
                        buildingname varchar(20) not null,
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
                        troopname varchar(20) not null,
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
                        aclevel int primary key check (aclevel >= 1),
                        hp int not null check (hp > 0),
                        maxspace int not null check (maxspace >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.lab (
                        lablevel int primary key check (lablevel >= 1),
                        hp int not null check (hp > 0),
                        maxtrooplevel int not null check (maxtrooplevel >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.resgen (
                        resgenlevel int primary key check (resgenlevel >= 1),
                        hp int not null check (hp > 0),
                        perhr int not null check (perhr >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.resstore (
                        resstorelevel int primary key check (resstorelevel >= 1),
                        hp int not null check (hp > 0),
                        maxcap int not null check (maxcap >= 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.resheld (
                        buildingid int primary key,
                        rid int not null,
                        CONSTRAINT fk_buildingid
                            FOREIGN KEY (buildingid)
                            REFERENCES req.building (buildingid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        CONSTRAINT fk_rid
                            FOREIGN KEY (rid)
                            REFERENCES req.resource (rid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.cannon (
                        canlevel int primary key check (canlevel >= 1),
                        dmg int not null check (dmg >= 0),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.archtower (
                        atlevel int primary key check (atlevel >= 1),
                        dmg int not null check (dmg >= 0),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.rax (
                        raxlevel int primary key check (raxlevel >= 1),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.th (
                        thlevel int primary key check (thlevel >= 1),
                        hp int not null check (hp > 0)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.trooplevels (
                        troopid int,
                        trooplevel int check (trooplevel >= 1),
                        dmg int not null check (dmg >= 0),
                        hp int not null check (hp > 0),
                        upcost int not null check (upcost >= 0),
                        updays int not null check (updays >= 0),
                        uphrs int not null check (uphrs >= 0),
                        upmins int not null check (upmins >= 0),
                        upsecs int not null check (upsecs >= 0),
                        CONSTRAINT fk_troopid
                            FOREIGN KEY (troopid)
                            REFERENCES req.troop (troopid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        PRIMARY KEY (troopid, trooplevel)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.buildingupgrades (
                        btid int,
                        buildinglevel int check (buildinglevel >= 1),
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
                        PRIMARY KEY (btid, buildinglevel)
                    );
                    
                    CREATE TABLE IF NOT EXISTS req.nextcost (
                        thlevel int primary key,
                        nextcost int not null check (nextcost >= 0),
                        CONSTRAINT fk_thlevel
                            FOREIGN KEY (thlevel)
                            REFERENCES req.th (thlevel)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
                    );
                    """;

                stmt.execute(sql);
                IO.println("Successful creation (if necessary) of Postgre Req Schema.");
                successfulInit = true;

                if (resetSchema) FillTables();
            } catch (Exception e1) {
                IO.println("Failed to create Postgre Req Schema (if necessary). Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to create Postgre Req Schema (if necessary) likely interrupted: " + e2);
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
                IO.println("Failed to drop Postgre Req Schema (if exists). Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to drop Postgre Req Schema (if exists) likely interrupted: " + e2);
                }
            }
        }
    }
    private void FillTables(){
        boolean successfulInit = false;
        while (!successfulInit) {
            try (Statement stmt = conn.createStatement()) {
                String sql = """
                    INSERT INTO req.resource VALUES
                    (0, 'Gems'),
                    (1, 'Gold'),
                    (2, 'Elixir');
                    
                    INSERT INTO req.buildingtype VALUES
                    (0, 'TH'),
                    (1, 'camp'),
                    (2, 'rax'),
                    (3, 'lab'),
                    (4, 'collector'),
                    (5, 'storage'),
                    (6, 'defense');
                    
                    INSERT INTO req.building VALUES
                    (0, 'Town Hall', 0, 1),
                    (1, 'Army Camp', 1, 2),
                    (2, 'Barracks', 2, 2),
                    (3, 'Laboratory', 3, 2),
                    (4, 'Gold Mine', 4, 2),
                    (5, 'Elixir Collector', 4, 1),
                    (6, 'Gold Storage', 5, 2),
                    (7, 'Elixir Storage', 5, 1),
                    (8, 'Cannon', 6, 1),
                    (9, 'Archer Tower', 6, 1);
                    
                    INSERT INTO req.maxbuildings VALUES
                    (0, 1, 1, 1, 1),
                    (1, 1, 1, 1, 1),
                    (2, 1, 1, 1, 1),
                    (3, 0, 1, 1, 1),
                    (4, 1, 1, 1, 1),
                    (5, 1, 1, 1, 1),
                    (6, 1, 1, 2, 2);
                    
                    INSERT INTO req.maxbuildinglevel VALUES
                    (0, 2, 3, 4, 4),
                    (1, 1, 2, 3, 5),
                    (2, 1, 2, 3, 3),
                    (3, 0, 1, 2, 3),
                    (4, 1, 2, 5, 10),
                    (5, 1, 2, 5, 10),
                    (6, 1, 2, 4, 10);
                    
                    INSERT INTO req.gemvalue VALUES
                    ('time', 5), -- 5 minutes = 1 gem
                    ('resource', 1000); -- 1000 resource = 1 gem
                    
                    INSERT INTO req.gemshop VALUES
                    (1, 80),
                    (5, 500),
                    (10, 1200),
                    (20, 2500),
                    (50, 6500),
                    (100, 15000);
                    
                    INSERT INTO req.troop VALUES
                    (1, 'Barbarian', 20, 1, 20, 5, 1, 1, true, false),
                    (2, 'Archer', 30, 1, 20, 20, 2, 1.5, true, true),
                    (3, 'Giant', 500, 5, 12, 4, 3, 2, true, false);
                    
                    INSERT INTO req.defense VALUES
                    (8, false, 2, 60),
                    (9, true, 1, 30);
                    
                    INSERT INTO req.armycamp VALUES
                    (1, 500, 20),
                    (2, 700, 25),
                    (3, 1500, 50),
                    (4, 2500, 75),
                    (5, 4000, 100);
                    
                    INSERT INTO req.lab VALUES
                    (1, 750, 2),
                    (2, 1600, 5),
                    (3, 4000, 10);
                    
                    INSERT INTO req.resgen VALUES
                    (1, 600, 1000),
                    (2, 800, 1200),
                    (3, 1200, 1500),
                    (4, 1400, 1800),
                    (5, 1700, 2200),
                    (6, 2300, 2700),
                    (7, 2900, 3200),
                    (8, 3500, 3700),
                    (9, 4200, 4300),
                    (10, 5000, 5000);
                    
                    INSERT INTO req.resstore VALUES
                    (1, 1800, 20000),
                    (2, 2400, 30000),
                    (3, 3600, 50000),
                    (4, 4200, 75000),
                    (5, 5100, 100000),
                    (6, 6900, 200000),
                    (7, 8700, 300000),
                    (8, 10500, 500000),
                    (9, 12600, 750000),
                    (10, 15000, 1000000);
                    
                    INSERT INTO req.resheld VALUES
                    (4, 1),
                    (5, 2),
                    (6, 1),
                    (7, 2);
                    
                    INSERT INTO req.cannon VALUES
                    (1, 60, 4000),
                    (2, 80, 5000),
                    (3, 100, 6000),
                    (4, 120, 8000),
                    (5, 140, 10000),
                    (6, 170, 14000),
                    (7, 200, 18000),
                    (8, 240, 25000),
                    (9, 280, 32000),
                    (10, 350, 40000);
                    
                    INSERT INTO req.archtower VALUES
                    (1, 25, 3000),
                    (2, 35, 3800),
                    (3, 45, 4500),
                    (4, 55, 5800),
                    (5, 65, 8000),
                    (6, 75, 11000),
                    (7, 90, 14000),
                    (8, 110, 18000),
                    (9, 130, 25000),
                    (10, 160, 32000);
                    
                    INSERT INTO req.rax VALUES
                    (1, 500),
                    (2, 750),
                    (3, 2000);
                    
                    INSERT INTO req.th VALUES
                    (1, 8000),
                    (2, 10000),
                    (3, 20000),
                    (4, 100000);
                    
                    INSERT INTO req.trooplevels VALUES
                    (1, 1, 20, 50, 0, 0, 0, 0, 0),
                    (1, 2, 25, 60, 2000, 0, 0, 0, 10),
                    (1, 3, 30, 80, 4000, 0, 0, 0, 15),
                    (1, 4, 40, 100, 6000, 0, 0, 0, 20),
                    (1, 5, 50, 120, 10000, 0, 0, 0, 30),
                    (1, 6, 60, 140, 15000, 0, 0, 1, 0),
                    (1, 7, 80, 170, 20000, 0, 0, 5, 0),
                    (1, 8, 100, 200, 100000, 0, 2, 0, 0),
                    (1, 9, 140, 230, 400000, 3, 0, 0, 0),
                    (1, 10, 180, 300, 750000, 14, 0, 0, 0),
                    (2, 1, 35, 25, 0, 0, 0, 0, 0),
                    (2, 2, 40, 30, 2500, 0, 0, 0, 10),
                    (2, 3, 50, 40, 5000, 0, 0, 0, 15),
                    (2, 4, 60, 50, 8000, 0, 0, 0, 20),
                    (2, 5, 75, 60, 12000, 0, 0, 0, 30),
                    (2, 6, 90, 70, 18000, 0, 0, 1, 0),
                    (2, 7, 110, 85, 24000, 0, 0, 5, 0),
                    (2, 8, 140, 100, 120000, 0, 2, 0, 0),
                    (2, 9, 200, 115, 420069, 3, 0, 0, 0),
                    (2, 10, 300, 150, 800000, 14, 0, 0, 0),
                    (3, 1, 15, 250, 0, 0, 0, 0, 0),
                    (3, 2, 20, 300, 4000, 0, 0, 0, 10),
                    (3, 3, 25, 400, 8000, 0, 0, 0, 15),
                    (3, 4, 35, 500, 12000, 0, 0, 0, 20),
                    (3, 5, 45, 600, 20000, 0, 0, 0, 30),
                    (3, 6, 55, 700, 30000, 0, 0, 1, 0),
                    (3, 7, 75, 850, 50000, 0, 0, 5, 0),
                    (3, 8, 90, 1000, 200000, 0, 2, 0, 0),
                    (3, 9, 120, 1200, 500000, 3, 0, 0, 0),
                    (3, 10, 150, 1500, 1000000, 14, 0, 0, 0);
                    
                    INSERT INTO req.buildingupgrades VALUES
                    (0, 2, 20000, 0, 0, 0, 30),
                    (0, 3, 30000, 0, 0, 1, 0),
                    (0, 4, 100000, 0, 1, 0, 0),
                    (1, 1, 5, 0, 0, 0, 5),
                    (1, 2, 100, 0, 0, 0, 10),
                    (1, 3, 1000, 0, 0, 1, 0),
                    (1, 4, 100000, 0, 1, 0, 0),
                    (1, 5, 500000, 1, 0, 0, 0),
                    (2, 1, 10, 0, 0, 0, 10),
                    (2, 2, 500, 0, 0, 1, 0),
                    (2, 3, 10000, 0, 1, 0, 0),
                    (3, 1, 1000, 0, 0, 1, 0),
                    (3, 2, 20000, 0, 1, 0, 0),
                    (3, 3, 400000, 1, 0, 0, 0),
                    (4, 1, 0, 0, 0, 0, 5),
                    (4, 2, 10, 0, 0, 0, 10),
                    (4, 3, 100, 0, 0, 1, 0),
                    (4, 4, 1000, 0, 0, 5, 0),
                    (4, 5, 5000, 0, 0, 30, 0),
                    (4, 6, 10000, 0, 1, 0, 0),
                    (4, 7, 20000, 0, 2, 0, 0),
                    (4, 8, 30000, 0, 3, 0, 0),
                    (4, 9, 50000, 0, 6, 0, 0),
                    (4, 10, 100000, 0, 12, 0, 0),
                    (5, 1, 0, 0, 0, 0, 10),
                    (5, 2, 100, 0, 0, 0, 30),
                    (5, 3, 500, 0, 0, 2, 0),
                    (5, 4, 2000, 0, 0, 10, 0),
                    (5, 5, 10000, 0, 1, 0, 0),
                    (5, 6, 20000, 0, 2, 0, 0),
                    (5, 7, 50000, 0, 4, 0, 0),
                    (5, 8, 100000, 0, 6, 0, 0),
                    (5, 9, 200000, 0, 12, 0, 0),
                    (5, 10, 300000, 1, 0, 0, 0),
                    (6, 1, 10, 0, 0, 0, 5),
                    (6, 2, 100, 0, 0, 0, 20),
                    (6, 3, 1000, 0, 0, 2, 0),
                    (6, 4, 10000, 0, 0, 10, 0),
                    (6, 5, 50000, 0, 1, 0, 0),
                    (6, 6, 100000, 0, 4, 0, 0),
                    (6, 7, 200000, 0, 12, 0, 0),
                    (6, 8, 300000, 1, 0, 0, 0),
                    (6, 9, 500000, 3, 0, 0, 0),
                    (6, 10, 800000, 7, 0, 0, 0);
                    
                    INSERT INTO req.nextcost VALUES
                    (1, 2),
                    (2, 10),
                    (3, 50),
                    (4, 1000);
                    """;

                stmt.execute(sql);
                IO.println("Successful population of Postgre Req Schema.");
                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to populate Postgre Req Schema. Trying again...");
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to populate Postgre Req Schema likely interrupted.");
                }
            }
        }
    }

    @Override
    public Map<Integer, String> GetResourceInfo() {
        Map<Integer, String> resources = new HashMap<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from req.resource";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    resources.put(rs.getInt("rid"), rs.getString("rname"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get resource info. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return resources;
    }

    @Override
    public int GetValuePerGem(String value) {
        int val = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from req.gemvalue where type=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, value);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    val = rs.getInt("val");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get gem value (" + value + ") info. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return val;
    }

    @Override
    public Map<Integer, Integer> GetGemShopOptions() {
        Map<Integer, Integer> options = new HashMap<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from req.gemshop";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    options.put(rs.getInt("usd"), rs.getInt("numgems"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get gem shop info. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return options;
    }
}
