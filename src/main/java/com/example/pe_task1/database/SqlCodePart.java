package com.example.pe_task1.database;

public class SqlCodePart {

    private final String channelName;
    private final String databaseUserName;
    private final String tableName;

    public SqlCodePart(String channelName, String databaseUserName, String tableName) {
        this.channelName = channelName;
        this.databaseUserName = databaseUserName;
        this.tableName = tableName;
    }

    public String getSqlNotifyFunctionCode(){
        return "-- FUNCTION: public.table_update_notify()\n" +
                " \n" +
                "DROP FUNCTION IF EXISTS public.table_update_notify();\n" +
                " \n" +
                "CREATE FUNCTION public.table_update_notify()\n" +
                "    RETURNS trigger\n" +
                "    LANGUAGE 'plpgsql'\n" +
                "    COST 100\n" +
                "    VOLATILE NOT LEAKPROOF\n" +
                "AS $BODY$\n" +
                "DECLARE\n" +
                "  id bigint;" +
                "BEGIN\n" +
                "  IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN\n" +
                "    id = NEW.id;\n" +
                "  ELSE\n" +
                "    id = OLD.id;\n" +
                "  END IF;\n" +
                "  PERFORM pg_notify('" + channelName + "', json_build_object('table', TG_TABLE_NAME, 'id', id, 'type', TG_OP)::text);\n" +
                "  RETURN NEW;\n" +
                "END;\n" +
                " \n" +
                "$BODY$;\n" +
                " \n" +
                "ALTER FUNCTION public.table_update_notify()\n" +
                "    OWNER TO \"" + databaseUserName + "\";";
    }

    public String getSqlCreateTriggerCode(){
        return "-- Trigger: " + tableName + "_notify_update\n" +
                " \n" +
                "DROP TRIGGER IF EXISTS " + tableName + "_notify_update ON " + tableName + ";\n" +
                " \n" +
                "CREATE TRIGGER " + tableName + "_notify_update\n" +
                "    BEFORE INSERT OR DELETE OR UPDATE\n" +
                "    ON " + tableName + "\n" +
                "    FOR EACH ROW\n" +
                "    EXECUTE PROCEDURE public.table_update_notify();";
    }
}
