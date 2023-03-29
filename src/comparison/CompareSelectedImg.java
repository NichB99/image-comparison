package comparison;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class CompareSelectedImg {

    public static void main(String[] args) throws InterruptedException {

        final JFrame parent = new JFrame();
        JButton button = new JButton();

        long start = 0;
        boolean controll = false;

        List<String> pathOriginal = new ArrayList<>();
        List<String> fileNamesOriginal = new ArrayList<>();

        List<String> pathControll = new ArrayList<>();
        List<String> fileNamesControll = new ArrayList<>();

        try (FileWriter writer = new FileWriter("results.txt")) {

            start = System.currentTimeMillis();

            JFrame frameOriginal = new JFrame("Choose Multiple Files Example");
            JFileChooser fileChooserOriginal = new JFileChooser("Original");
            fileChooserOriginal.setMultiSelectionEnabled(true);
            fileChooserOriginal.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValueOriginal = fileChooserOriginal.showOpenDialog(frameOriginal);

            if (returnValueOriginal == JFileChooser.APPROVE_OPTION) {

                File[] files = fileChooserOriginal.getSelectedFiles();
                for (File file : files) {

                    fileNamesOriginal.add(file.getName());
                    pathOriginal.add(file.getPath());
                }
            }

            JFrame frameControll = new JFrame("Choose Multiple Files Example");
            JFileChooser fileChooserControll = new JFileChooser();
            fileChooserControll.setMultiSelectionEnabled(true);
            fileChooserControll.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValueControll = fileChooserControll.showOpenDialog(frameControll);

            if (returnValueControll == JFileChooser.APPROVE_OPTION) {

                File[] files = fileChooserControll.getSelectedFiles();
                for (File file : files) {
                    fileNamesControll.add(file.getName());
                    pathControll.add(file.getPath());
                }
            }


            // Loop through each pair of images
            for (int i = 0; i < fileNamesControll.size(); i++) {

                for (int j = 0; j < fileNamesOriginal.size(); j++) {

                    // Load the images
                    BufferedImage image1 = ImageIO.read(new File(pathControll.get(i)));
                    BufferedImage image2 = ImageIO.read(new File(pathOriginal.get(j)));

                    // Compare the size of the images
                    if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {

                        System.out.println("The images  " + fileNamesControll.get(i) + "    and     " + fileNamesOriginal.get(j) + "     are not of the same size.");
                        continue;
                    }

                    // Create a new image to store the differences
                    BufferedImage diffImage = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);

                    // Loop through each pixel of the images and compare them
                    int diffCount = 0;
                    for (int y = 0; y < image1.getHeight(); y++) {

                        for (int x = 0; x < image1.getWidth(); x++) {

                            int pixel1 = image1.getRGB(x, y);
                            int pixel2 = image2.getRGB(x, y);
                            if (pixel1 != pixel2) {

                                // Highlight the difference in the diffImage
                                diffImage.setRGB(x, y, Color.RED.getRGB());
                                diffCount++;
                            }
                        }
                    }
                    // Calculate the percentage difference
                    double percentDiff = (double) diffCount / (image1.getWidth() * image1.getHeight()) * 100.0;
                    // Write the results to the output file
                    String result = fileNamesControll.get(i) + " vs " + fileNamesOriginal.get(j) + String.format(" - %.2f", percentDiff) + "%\n";
                    writer.write(result);
                }
            }
            System.out.println("Image comparison complete.");
        } catch (IOException exc) {

            controll = true;
            System.out.println("An error occurred: " + exc.getMessage());
            exc.printStackTrace();
        }
        long end = System.currentTimeMillis();
        String sec = (end - start) / 1000F + "    seconds";
        System.out.println(sec);
        if (!controll) {

            button.setText("Operation completed, closing in 5 seconds!");
            parent.add(button);
            parent.pack();
            parent.setVisible(true);
        }
        if (controll) {
            
            button.setText("An error occurred, closing in 5 seconds!");
            parent.add(button);
            parent.pack();
        }

        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }

}
