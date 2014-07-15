package fhw;

import javax.annotation.*;
import javax.ejb.*;

@Singleton
@Startup
public class Starter
{

    public Starter()
    {
        
    }
    
    @PostConstruct
    void postStructor()
    {
        System.out.println("@PostConstruction of the Starter....."); 
        
        
        System.out.println(".....@PostConstruct done."); 
    }
}



//@Singleton
//@Startup
//public class Initialiser {
//
//    @EJB
//    private FileSystemMonitor fileSystemMonitor;
//
//    @PostConstruct
//    public void init() {
//        String fileSystemPath = ....;
//        fileSystemMonitor.poll(fileSystemPath);
//    }
//
//}