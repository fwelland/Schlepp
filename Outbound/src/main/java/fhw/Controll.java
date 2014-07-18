package fhw;
import java.io.IOException;
import javax.ejb.EJB; 
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/controll") 
public class Controll 
{    
    @EJB
    private DirMonitor dirMonitor; 
    
    public Controll()
    {
    }

    @GET
    @Path("start")
    public String start() 
    {
        String s = "started";
        try
        {
            dirMonitor.startWatching();
        }
        catch(IOException|InterruptedException ie)
        {
            s = String.format("failed to start watching:  " + ie.getMessage()); 
        }
        return(s); 
    }
    
    @GET
    @Path("stop")
    public String stop()
    {
        String s = "stopped";
        try
        {
            dirMonitor.stopWatching();
        }
        catch(IOException ioe)
        {
            s = String.format("failed to stop watching:  " + ioe.getMessage()); 
        }
        return(s); 
    }
}