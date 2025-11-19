package Repository;

import Application.Game;

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
                        profname varchar(50) not null,
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
                        isupgradingtroop boolean not null default false,
                        upgradingtroopid int default -1, -- unknown value if troop isn't upgrading
                        troopupgradefinishtime timestamp default current_timestamp, -- unknown value if troop isn't upgrading
                        CONSTRAINT fk_profid
                            FOREIGN KEY (profid)
                            REFERENCES data.profile (profid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        CONSTRAINT fk_upgradingtroopid
                            FOREIGN KEY (upgradingtroopid)
                            REFERENCES data.playertrooplvls (troopid)
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
                        isupgrading boolean not null default false,
                        upgradefinishtime timestamp default current_timestamp, -- unknown timestamp if not upgrading
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
                        pos int not null check (pos >= 0 and pos < 12), -- 12 is hardcoded since ik max 10 buildings
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
                IO.println("Failed to create Postgre Data Schema (if necessary). Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to create Postgre Data Schema (if necessary) likely interrupted: " + e2);
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
                IO.println("Failed to drop Postgre Data Schema (if exists). Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep to drop Postgre Data Schema (if exists) likely interrupted: " + e2);
                }
            }
        }
    }
    private void FillTables(){

    }

    @Override
    public float GetHoursSinceLastAttacked(int profID) {
        float hours = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "SELECT EXTRACT(EPOCH FROM (NOW() - lastattacked)) / 3600 AS hrs FROM data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    hours = (float) Math.min(24 * Game.MaxDaysTracked, rs.getDouble("hrs"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get hours since last attacked. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return hours;
    }
    @Override
    public float GetHoursSinceLastCollectedResources(int profID) {
        float hours = 0;

        try {
            String sql = "SELECT EXTRACT(EPOCH FROM (NOW() - lastcollected)) / 3600 AS hrs FROM data.player where profid=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                hours = (float) Math.min(24 * Game.MaxDaysTracked, rs.getDouble("hrs"));
            }
        } catch (Exception e) {
            IO.println("Failed to get hours since last collected resources. Try again: " + e);
        }

        return hours;
    }

    @Override
    public String GetPlayerName(int profID) {
        String name = "";
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select profname from data.profile where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    name = rs.getString("profname");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get profile name. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return name;
    }

    @Override
    public int GetGems(int profID) {
        int gems = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select gems from data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    gems = rs.getInt("gems");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get profile # gems. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return gems;
    }

    @Override
    public int GetTrophies(int profID) {
        int trophies = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select trophies from data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    trophies = rs.getInt("trophies");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get profile # trophies. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return trophies;
    }

    @Override
    public boolean GetTroopIsUpgrading(int profID) {
        boolean upgrading = false;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select isupgradingtroop from data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    upgrading = rs.getBoolean("isupgradingtroop");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get if troop is upgrading. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return upgrading;
    }

    @Override
    public int GetUpgradingTroopID(int profID) {
        int id = -1;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select upgradingtroopid from data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    id = rs.getInt("upgradingtroopid");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get ID of upgrading troop. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return id;
    }

    @Override
    public long GetTroopUpgradeTimeRemainingSeconds(int profID) {
        long remaining = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select troopupgradefinishtime - NOW() from data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    remaining = Math.max(0, rs.getLong("troopupgradefinishtime") / 1000);
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get time remaining of upgrading troop. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return remaining;
    }

}
