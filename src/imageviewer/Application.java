package imageviewer;

import imageviewer.ui.console.ConsoleImageViewer;
import imageviewer.model.Dimension;
import imageviewer.model.Image;
import imageviewer.ui.swing.ImageViewer;
import imageviewer.persistence.ImageLoader;
import imageviewer.ui.swing.ApplicationFrame;
import imageviewer.controller.NextImageCommand;
import imageviewer.controller.PrevImageCommand;
import imageviewer.model.ProxyImage;
import imageviewer.model.RealImage;
import java.awt.event.ActionListener;

public class Application {

    public static void main(String[] args) {
        new Application().execute();
    }

    private void execute() {
        Image[] images = linkImages(createImages()); 
        ImageViewer viewer = createImageViewer(images[0]);
        createApplicationFrame(createCommands(viewer));
    }

    private Image[] createImages() {
        Image[] images = new Image[5];
        for (int i = 0; i < images.length; i++) {
            images[i] = createImage(i);
        }
        return images;
    }

    private Image createImage(final int index) {
        final int[] sizes = new int[] {200, 500, 400, 398, 100};
        return new ProxyImage(new ImageLoader() {
            @Override
            public Image load() {
                return new RealImage(new Dimension(sizes[index], sizes[index]));
            }
        });
    }

    private Image[] linkImages(Image[] images) {
        for (int i = 0; i < images.length; i++) {
            Image image = images[i];
            Image next = images[(i + 1) % images.length];
            Image prev = images[(i + images.length - 1) % images.length];
            image.setNext(next);
            image.setPrev(prev);
        }
        return images;
    }

    private ImageViewer createImageViewer(Image image) {
        ImageViewer viewer = new ConsoleImageViewer();
        viewer.setImage(image);
        return viewer;
    }

    private ApplicationFrame createApplicationFrame(ActionListener[] listeners) {
        return new ApplicationFrame(listeners);
    }

    private ActionListener[] createCommands(ImageViewer viewer) {
        return new ActionListener[] {
            new PrevImageCommand(viewer),
            new NextImageCommand(viewer)
        };
    }
    
}