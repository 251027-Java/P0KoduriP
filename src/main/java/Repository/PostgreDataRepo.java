package Repository;

import Application.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Abstracts.Building;
import Models.PaymentAccount;
import Models.Singletons.BuildingHandler;
import Service.RequirementService;
import Util.Time;

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
                            REFERENCES req.troop (troopid)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
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
                        CONSTRAINT fk_profid
                            FOREIGN KEY (profid)
                            REFERENCES data.profile (profid)
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
                        pos int not null check (pos >= 0 and pos < 12), -- 12 is hardcoded since ik max 12 buildings
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

    public int GetNumberOfProfiles(){
        int total = -1;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "SELECT COUNT(*) AS numprofs FROM data.profile;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    total = rs.getInt("numprofs");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get total number of profiles. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return total;
    }

    @Override
    public List<String> DisplayGameAccounts() {
        List<String> accounts = new ArrayList<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.profile";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    accounts.add(String.format("(%d) %s", rs.getInt("profid"), rs.getString("profname")));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to display game accounts. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return accounts;
    }

    @Override
    public List<Integer> GetGameAccountIDs() {
        List<Integer> accounts = new ArrayList<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.profile";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    accounts.add(rs.getInt("profid"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get game account IDs. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return accounts;
    }

    @Override
    public String GetCreationDate(int profID) {
        String date = "";

        try {
            String sql = "select datecreated from data.profile where profid=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                date = rs.getDate("datecreated").toString();
            }
        } catch (Exception e1) {
            IO.println("Failed to get creation date. Try again: " + e1);
            try {
                Thread.sleep(1000);
            } catch (Exception e2) {
                IO.println("Sleep likely interrupted: " + e2);
            }
        }

        return date;
    }

    @Override
    public float GetHoursSinceLastAttacked(int profID) {
        float hours = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "SELECT EXTRACT(EPOCH FROM (NOW() - lastattacked)) AS secs FROM data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    hours = Math.min(24 * Game.MaxDaysTracked, Time.GetTotalDecimalHours(rs.getLong("secs")));
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
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "SELECT EXTRACT(EPOCH FROM (NOW() - lastcollected)) AS secs FROM data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    hours = Math.min(24 * Game.MaxDaysTracked, Time.GetTotalDecimalHours(rs.getLong("secs")));
                }

                successfulInit = true;
            } catch (Exception e) {
                IO.println("Failed to get hours since last collected resources. Try again: " + e);
            }
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
    public boolean GetBuildingIsUpgrading(int profID, int buildID) {
        boolean upgrading = false;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select isupgrading from data.playerbuildinglvls where profid=? and buildid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                stmt.setInt(2, buildID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    upgrading = rs.getBoolean("isupgrading");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get if building is upgrading. Trying again...: " + e1);
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
                    if (rs.wasNull()) id = -1;
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
                String sql = "SELECT EXTRACT(EPOCH FROM (troopupgradefinishtime - NOW()) AS secs FROM data.player where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    remaining = Math.max(0, rs.getLong("secs"));
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

    @Override
    public long GetBuildingUpgradeTimeRemainingSeconds(int profID, int buildID) {
        long remaining = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "SELECT EXTRACT(EPOCH FROM (upgradefinishtime - NOW()) AS secs FROM data.playerbuildinglvls where profid=? and buildid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                stmt.setInt(2, buildID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    remaining = Math.max(0, rs.getLong("secs"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get time remaining of upgrading building. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return remaining;
    }

    @Override
    public List<PaymentAccount> GetPaymentAccounts(int profID) {
        List<PaymentAccount> accounts = new ArrayList<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.cc natural join data.ccprof where profid=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    PaymentAccount acct = new PaymentAccount(rs.getLong("cardno"), rs.getInt("expmo"),
                            rs.getInt("expyr"), rs.getInt("pin"), rs.getInt("balance"));
                    accounts.add(acct);
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get payment accounts. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return accounts;
    }

    @Override
    public Map<Integer, Integer> GetUserTroopLevels(int profID) {
        Map<Integer, Integer> troops = new HashMap<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.playertrooplvls where profid=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    troops.put(rs.getInt("troopid"), rs.getInt("level"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get player's troop levels. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return troops;
    }

    @Override
    public Map<Integer, Integer> GetPlayerBuildingLineup(int profID) {
        Map<Integer, Integer> lineup = new HashMap<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.playerbuildinglineup where profid=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    lineup.put(rs.getInt("buildid"), rs.getInt("pos"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get player's building lineup. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return lineup;
    }

    @Override
    public List<Building> GetPlayerBuildings(int profID) {
        List<Building> buildings = new ArrayList<>();
        RequirementService rServ = Game.getInstance().GetRequirementService();

        // the key is buildid
        List<Integer> buildIDs = new ArrayList<>();
        Map<Integer, Integer> levels = new HashMap<>();
        Map<Integer, Integer> buildingIDs = new HashMap<>();
        Map<Integer, Boolean> currentUpgrades = new HashMap<>();

        boolean successfulInit = false;
        while (!successfulInit) {
            try {
                String sql = "select * from data.playerbuildinglvls where profid=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("buildid");
                    buildIDs.add(id);
                    levels.put(id, rs.getInt("level"));
                    buildingIDs.put(id, rs.getInt("buildingid"));
                    currentUpgrades.put(id, rs.getBoolean("isupgrading"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get player's buildings. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        for (int buildID : buildIDs) {
            int level = levels.get(buildID);
            int buildingID = buildingIDs.get(buildID);
            boolean currentlyUpgrading = currentUpgrades.get(buildID);
            Building b = null;

            switch (BuildingHandler.GetBuildingTypeName(BuildingHandler.GetBuildingType(buildingID))){
                case "TH":
                    b = rServ.GetTownHall(buildID, level, buildingID, currentlyUpgrading);
                    break;
                case "camp":
                    b = rServ.GetArmyCamp(buildID, level, buildingID, currentlyUpgrading);
                    break;
                case "rax":
                    b = rServ.GetBarracks(buildID, level, buildingID, currentlyUpgrading);
                    break;
                case "lab":
                    b = rServ.GetLab(buildID, level, buildingID, currentlyUpgrading);
                    break;
                case "collector":
                    b = rServ.GetCollector(buildID, level, buildingID, currentlyUpgrading, GetResourceAmount(profID, buildID));
                    break;
                case "storage":
                    b = rServ.GetStorage(buildID, level, buildingID, currentlyUpgrading);
                    break;
                case "defense":
                    b = rServ.GetDefense(buildID, level, buildingID, currentlyUpgrading);
                    break;
                default:
                    IO.println("Got the default case (error) when switching through building type ID while getting player buildings.");
            }
            buildings.add(b);
        }

        return buildings;
    }

    @Override
    public int GetResourceAmount(int profID, int buildID) {
        int amt = 0;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select amount from data.playerresourcebuildings where profid=? and buildid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                stmt.setInt(2, buildID);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    amt = rs.getInt("amount");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get resource amount of buildID=" + buildID + ". Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return amt;
    }

    @Override
    public Map<Integer, Integer> GetPlayerArmy(int profID) {
        Map<Integer, Integer> army = new HashMap<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.playerarmy where profid=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    army.put(rs.getInt("troopid"), rs.getInt("count"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get player's army. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return army;
    }

    @Override
    public boolean GetPaymentAccountExists(long cardNo) {
        boolean exists = false;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.cc where cardno=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);

                exists = stmt.executeQuery().next();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get if card exists. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return exists;
    }

    @Override
    public boolean GetPaymentAccountExists(long cardNo, int expMo, int expYr, int pin) {
        boolean exists = false;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.cc where cardno=? and expmo=? and expyr=? and pin=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                stmt.setInt(2, expMo);
                stmt.setInt(3, expYr);
                stmt.setInt(4, pin);

                exists = stmt.executeQuery().next();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get if card exists. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return exists;
    }

    @Override
    public void AddNewCard(long cardNo, int expMo, int expYr, int pin) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "insert into data.cc values (?,?,?,?,0);";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                stmt.setInt(2, expMo);
                stmt.setInt(3, expYr);
                stmt.setInt(4, pin);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to add a new card. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }
    }

    @Override
    public void DeleteCard(long cardNo, int expMo, int expYr, int pin) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "delete from data.cc where cardno=? and expmo=? and expyr=? and pin=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                stmt.setInt(2, expMo);
                stmt.setInt(3, expYr);
                stmt.setInt(4, pin);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to delete card. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }
    }

    @Override
    public List<String> DisplayGameAccountsToAddToCard(long cardNo) {
        List<String> accounts = new ArrayList<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.profile where profid not in (select profid from data.ccprof where cardno=?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    accounts.add(String.format("(%d) %s", rs.getInt("profid"), rs.getString("profname")));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to display game accounts to add to payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return accounts;
    }

    @Override
    public List<String> DisplayGameAccountsToRemoveFromCard(long cardNo) {
        List<String> accounts = new ArrayList<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.profile natural join data.ccprof where cardno=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    accounts.add(String.format("(%d) %s", rs.getInt("profid"), rs.getString("profname")));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to display game accounts to remove from payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return accounts;
    }

    @Override
    public List<Integer> GetGameAccountIDsToAddToCard(long cardNo) {
        List<Integer> accounts = new ArrayList<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.profile where profid not in (select profid from data.ccprof where cardno=?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    accounts.add(rs.getInt("profid"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get game account IDs to add to payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return accounts;
    }

    @Override
    public List<Integer> GetGameAccountsIDsToRemoveFromCard(long cardNo) {
        List<Integer> accounts = new ArrayList<>();
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.profile natural join data.ccprof where cardno=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    accounts.add(rs.getInt("profid"));
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get game account IDs to remove from payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return accounts;
    }

    @Override
    public boolean GetAccountsAreConnected(long cardNo, int profID){
        boolean connected = false;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select * from data.ccprof where cardno=? and profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                stmt.setInt(2, profID);

                connected = stmt.executeQuery().next();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get if game and payment accounts are connected. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return connected;
    }

    @Override
    public void AddGameAccountToCard(long cardNo, int profID) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "insert into data.ccprof values (?,?);";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                stmt.setInt(2, profID);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to connect a game and payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }
    }

    @Override
    public void RemoveGameAccountFromCard(long cardNo, int profID) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "delete from data.ccprof where cardno=? and profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                stmt.setInt(2, profID);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to disconnect a game and payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }
    }

    @Override
    public void MakeDeposit(int deposit, long cardNo, int expMo, int expYr, int pin) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "update data.cc set balance = balance + ? where cardno=? and expmo=? and expyr=? and pin=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, deposit);
                stmt.setLong(2, cardNo);
                stmt.setInt(3, expMo);
                stmt.setInt(4, expYr);
                stmt.setInt(5, pin);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to deposit " + deposit + " into a payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }
    }

    @Override
    public void MakeWithdrawal(int withdrawal, long cardNo, int expMo, int expYr, int pin) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "update data.cc set balance = balance - ? where cardno=? and expmo=? and expyr=? and pin=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, withdrawal);
                stmt.setLong(2, cardNo);
                stmt.setInt(3, expMo);
                stmt.setInt(4, expYr);
                stmt.setInt(5, pin);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to withdraw " + withdrawal + " from a payment account. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }
    }

    @Override
    public int GetBalance(long cardNo, int expMo, int expYr, int pin) {
        int balance = -1;
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "select balance from data.cc where cardno=? and expmo=? and expyr=? and pin=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setLong(1, cardNo);
                stmt.setInt(2, expMo);
                stmt.setInt(3, expYr);
                stmt.setInt(4, pin);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()){
                    balance = rs.getInt("balance");
                }

                successfulInit = true;
            } catch (Exception e1) {
                IO.println("Failed to get payment account balance. Trying again...: " + e1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    IO.println("Sleep likely interrupted: " + e2);
                }
            }
        }

        return balance;
    }

    @Override
    public boolean CreateProfile(int profID, String name) {
        try {
            PreparedStatement stmt;
            String sql;
            conn.setAutoCommit(false);

            sql = "INSERT INTO data.profile VALUES (?, ?, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profID);
            stmt.setString(2, name);
            stmt.executeUpdate();

            sql = "INSERT INTO data.player VALUES (?, NOW(), NOW(), 1000, 0, false, null, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profID);
            stmt.executeUpdate();

            sql = "INSERT INTO data.playerbuildinglvls VALUES (?, 0, 1, 0, false, NOW())";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profID);
            stmt.executeUpdate();

            sql = "INSERT INTO data.playerbuildinglineup VALUES (?, 0, 0)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, profID);
            stmt.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e1) {
            IO.println("Failed to create a new account. Try again: " + e1);
            try {
                conn.rollback();
            } catch (Exception e2) {
                IO.println("Profile creation rollback failed: " + e2);
            }
            return false;
        }
        finally {
            boolean autoComm = false;
            while (!autoComm) {
                try {
                    conn.setAutoCommit(true);
                    autoComm = true;
                }
                catch (Exception e1){
                    IO.println("Failed to set autocommit to true. Trying again...: " + e1);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e2) {
                        IO.println("Sleep likely interrupted: " + e2);
                    }
                }
            }
        }
    }

    @Override
    public void UpdateCollectedResourcesTime(int profID) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "update data.player set lastcollected=NOW() where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, profID);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e) {
                IO.println("Failed to update time when last collected resources. Trying again...: " + e);
            }
        }
    }

    @Override
    public void SetResourceAmount(int profID, int buildID, int amount) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "update data.playerresourcebuildings set amount=? where profid=? and buildid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, amount);
                stmt.setInt(2, profID);
                stmt.setInt(3, buildID);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e) {
                IO.println("Failed to update resource amount of a resource building. Trying again...: " + e);
            }
        }
    }

    @Override
    public void SetGems(int profID, int gems) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "update data.player set gems=? where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, gems);
                stmt.setInt(2, profID);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e) {
                IO.println("Failed to update # gems. Trying again...: " + e);
            }
        }
    }

    @Override
    public void SetTrophies(int profID, int trophies) {
        boolean successfulInit = false;

        while (!successfulInit) {
            try {
                String sql = "update data.player set trophies=? where profid=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, trophies);
                stmt.setInt(2, profID);
                stmt.executeUpdate();

                successfulInit = true;
            } catch (Exception e) {
                IO.println("Failed to update # trophies. Trying again...: " + e);
            }
        }
    }

}
