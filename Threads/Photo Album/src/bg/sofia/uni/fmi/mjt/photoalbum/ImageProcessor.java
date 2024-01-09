package bg.sofia.uni.fmi.mjt.photoalbum;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

public class ImageProcessor {

    private Queue<Image> resource;
    private String directory;

    public ImageProcessor() {
        this.resource = new ArrayDeque<>();
    }

    public Image loadImage(Path imagePath) {
        return Image.loadImage(imagePath);
    }

    public Image convertImage(Image image, String directory) {
        return Image.convertToBlackAndWhite(image, directory);
    }

    public synchronized void addImage(Image image) {
        resource.add(image);
        this.notifyAll();
    }

    public synchronized Image consumeImage() {
        while (resource.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        Image image = resource.peek();
        resource.remove();
        return image;
    }
}
