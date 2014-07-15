package fhw;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;


@Stateless
public class DirMonitor
{    
    public DirMonitor()
    {
        System.out.println("DirMonitor:Constructor"); 
    }
    
    @PostConstruct
    private void init()
    {
        System.out.println("DirMonitor:PostConstructor:init"); 
    }
    
    @Asynchronous
    public void watch(String fileSystemPath) 
            throws IOException            
    {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        while (true) 
        {
            WatchKey key = null;
            try {
                key = watcher.take();
                for (WatchEvent<?> event: key.pollEvents()) 
                {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE)
                    {
                        WatchEvent<Path> watchEvent = (WatchEvent<Path>)event;
                    }
                    else
                    {
                        continue;
                    }                    
                }
            } catch (InterruptedException e) 
            {
                e.printStackTrace();
                return;
            } 
            finally
            {
                if (key != null) 
                {
                    boolean valid = key.reset();
                    if (!valid) 
                    {
                        break;
                    } 
                }
            }
        }
    }        
}
