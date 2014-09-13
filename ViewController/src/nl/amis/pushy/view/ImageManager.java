package nl.amis.pushy.view;

import com.tangosol.coherence.config.unit.Seconds;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

import oracle.adf.view.rich.render.ClientEvent;

import static java.util.concurrent.TimeUnit.*;

public class ImageManager {
    private final List<String> imageList = new ArrayList<String>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private short currenSlideSelection;

    public ImageManager() {
        imageList.add("Chrysanthemum.jpg");
        imageList.add("Desert.jpg");
        imageList.add("Hydrangeas.jpg");
        imageList.add("Jellyfish.jpg");
        imageList.add("Koala.jpg");
        imageList.add("Lighthouse.jpg");
        imageList.add("Penguins.jpg");
        imageList.add("Tulips.jpg");
    }


    public void processImageSelection(ClientEvent clientEvent) {
        String image = (String) clientEvent.getParameters().get("imageName");
        SocketMediator.getSm().broadcast("IMAGE=" + image);
    }

    public void slideShow(ClientEvent clientEvent) {
        final Runnable slideForwarder = new Runnable() {
            public void run() {
                if (currenSlideSelection == imageList.size() - 1) {
                    currenSlideSelection = -1;
                }
                currenSlideSelection++;
                SocketMediator.getSm().broadcast("IMAGE=" + imageList.get(currenSlideSelection));
            }
        };
        final ScheduledFuture<?> slideshowHandle =
            scheduler.scheduleAtFixedRate(slideForwarder, 3, 3, TimeUnit.SECONDS);
        currenSlideSelection = 0;
        // after two minutes, stop the slideshow
        scheduler.schedule(new Runnable() {
            public void run() {
                slideshowHandle.cancel(true);
            }
        }, 2 * 60, SECONDS);
    }

    public List<String> getImages() {
        return imageList;
    }

}
