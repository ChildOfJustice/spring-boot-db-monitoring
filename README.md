the main SQL code:

insert into student (id, name, courses)
values(2, 'TestStudent', 'other');

delete from student where id = 1;




CREATE FUNCTION public.table_update_notify()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
DECLARE
  id bigint;
BEGIN
  IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
    id = NEW.id;
  ELSE
    id = OLD.id;
  END IF;
  PERFORM pg_notify('employee_channel', json_build_object('table', TG_TABLE_NAME, 'id', id, 'type', TG_OP)::text);
  RETURN NEW;
END;
 
$BODY$;
 
ALTER FUNCTION public.table_update_notify()
    OWNER TO "test-postgres";



CREATE TRIGGER employee_notify_update
    BEFORE INSERT OR DELETE OR UPDATE
    ON student
    FOR EACH ROW
    EXECUTE PROCEDURE public.table_update_notify();