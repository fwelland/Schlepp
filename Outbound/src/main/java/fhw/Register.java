package fhw;
import java.io.IOException;
import java.nio.file.FileSystems;
import javax.ejb.EJB; 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


@Path("/addDir") 
public class Register 
{    
    @EJB
    private DirMonitor dirMonitor; 
    
    public Register()
    {
    }

    @GET
    public String addDirectoryToMonitor(@QueryParam("dir") String pathToWatch)
    {
        String result = "nothing"; 
        if(null != pathToWatch && pathToWatch.length() > 0)
        {
            result = String.format("I got your [%s]",pathToWatch); 
            try 
            {
                java.nio.file.Path dir = FileSystems.getDefault().getPath(pathToWatch); 
                if(dir.toFile().isDirectory())
                {
                    dirMonitor.registerDirectory(dir);
                    result = String.format("regisitered [%s]",dir.toString());                     
                }
                else
                {
                    result = String.format("ERROR: [%s] is not a directory or doesn't exist",dir.toString());                     
                }
            }
            catch(IOException io)
            {
                result = String.format("ERROR: unable to register [%s] due to:  %s ",pathToWatch, io.getMessage());                     
            }
        }
        return(result); 
    }
}