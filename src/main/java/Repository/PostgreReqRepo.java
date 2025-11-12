package Repository;

import java.sql.*;

public class PostgreReqRepo implements IRequirementRepository{
    private final Connection conn;
    public PostgreReqRepo(Connection connection){
        conn = connection;

        boolean successfulInit = false;
        while (!successfulInit) {
            try (Statement stmt = conn.createStatement()) {
                String sql = """
                        CREATE SCHEMA IF NOT EXISTS req;
                        
                        DROP SCHEMA IF EXISTS public;
                        """;

                stmt.execute(sql);
                IO.println("Successful creation (if necessary) of Postgre Req Schema.");
                successfulInit = true;
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
}
