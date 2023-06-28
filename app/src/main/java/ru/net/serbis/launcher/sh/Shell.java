package ru.net.serbis.launcher.sh;

import java.io.*;
import java.util.*;
import ru.net.serbis.launcher.*;
import ru.net.serbis.launcher.help.*;

public class Shell
{
    public List<String> command(String... commands)
    {
        Process process = null;
        OutputStreamWriter output = null;
        BufferedReader input = null;
        BufferedReader error = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            output = new OutputStreamWriter(process.getOutputStream());
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            for (String command : commands)
            {
                Log.info(this, "command:" + command);
                output.write(command + "\n");
                output.flush();
            }

            output.write("exit\n");
            output.flush();
            process.waitFor();

            Log.error(this, error);
            return getResult(input);
        }
        catch (Throwable e)
        {
            Log.error(this, e);
            return Collections.<String>emptyList();
        }
        finally
        {
            Tools.close(input);
            Tools.close(error);
            Tools.close(output);
            if (process != null)
            {
                process.destroy();
            }
        }
    }

    private List<String> getResult(BufferedReader input) throws Exception
    {
        List<String> result = new ArrayList<String>();
        String line;
        while (input.ready() && (line = input.readLine()) != null)
        {
            result.add(line);
        }
        return result;
    }
    
    private static Boolean CHECK;
    
    public boolean check()
    {
        if (CHECK != null)
        {
            return CHECK;
        }
        CHECK = false;
        for(String result : command("echo 'ok'"))
        {
            if ("ok".equals(result))
            {
                CHECK = true;
                break;
            }
        }
        return CHECK;
    }
    
    public void stop(String packageName)
    {
        if (check())
        {
            command("pkill " + packageName);
        }
    }
}
