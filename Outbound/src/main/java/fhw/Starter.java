package fhw;
import javax.annotation.*;
import javax.ejb.*;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;


@Path("/addDir")
@Stateless
@ApplicationPath("schlepp")
public class Starter 
    extends Application
{
    public Starter()
    {
        System.out.println("constructor...."); 
    }
    
    @PostConstruct
    void postStructor()
    {
        System.out.println("@PostConstruction of the Starter....."); 
                
        System.out.println(".....@PostConstruct done."); 
    }
    
    @GET
    public String addDirectoryToMonitor(@QueryParam("dir") String pathToWatch)
    {
        System.out.println( String.format("I have been called with [%s]", pathToWatch)); 
        String result = "nothing"; 
        if(null != pathToWatch && pathToWatch.length() > 0)
        {
            result = String.format("I got your [%s]",pathToWatch); 
        }
        return(result); 
    }
}