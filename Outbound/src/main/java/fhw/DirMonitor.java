package fhw;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
@Lock(LockType.READ)        
public class DirMonitor
{
    private ReentrantLock lock = new ReentrantLock(false); 
    private WatchService watcher;
    private final Map<WatchKey,Path> watchKeys = new HashMap<>(); 
    private final List<Path> dirsToWatch = new ArrayList<>(); 
    
    public DirMonitor()
    {
        System.out.println("DirMonitor:Constructor");
    }    
    
    public void registerDirectory(Path dir) 
        throws IOException
    {
        dirsToWatch.add(dir); 
        if(null != watcher)
        {
            watchDir(dir); 
        }
    }    
    
    private void watchDir(Path dir) 
        throws IOException
    {
        WatchKey key = dir.register(watcher, ENTRY_CREATE);                
        watchKeys.put(key, dir);
    }
    
    public void stopWatching()
        throws IOException
    {        
        watcher.close();
        watcher = null; 
        watchKeys.clear();
    }    

    @Asynchronous
    public void startWatching()
            throws IOException, InterruptedException
    {   
        boolean b = lock.tryLock(1, TimeUnit.SECONDS); 
        if(b)
        {
            watcher = FileSystems.getDefault().newWatchService();
            for(Path d : dirsToWatch)
            {
                watchDir(d);
            }
            while (true)
            {
                WatchKey key = null;
                try
                {
                    key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents())
                    {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE)
                        {
                            WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                            System.out.println("Yeah I got an event!!!!");
                        }
                        else
                        {
                            continue;
                        }
                    }
                }
                catch (ClosedWatchServiceException cse)
                {
                    System.out.println("Someone called stop...I think...");
                    break; 
                }
                catch (InterruptedException e)
                {
                    System.out.println("interuppted exception!!!"); 
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
            lock.unlock();
        }  
        else
        {
            System.out.println("already polling FS..."); 
        }
    }                               
}
