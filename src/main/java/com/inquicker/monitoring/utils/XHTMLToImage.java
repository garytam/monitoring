package com.inquicker.monitoring.utils;

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright Tim Hutton and Berlin Brown <berlin dot brown at gmail.com> 2011
 *  
 * Tim Hutton is the original author, but a license not provided in source,
 * GPL was used for similar projects.  If Tim or anyone else has questions, please contact Berlin Brown.
 *
 * http://www.sq3.org.uk/Evolution/Squirm3/
 */ 

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;

import okhttp3.Response;



/**
 * Convert XHTML output to image using xhtmlrenderer
 * 
 * See:
 * http://flyingsaucerproject.github.com/flyingsaucer/r8/guide/users-guide-R8.html#xil_29
 * @author berlin
 *
 */
public class XHTMLToImage {
 
  
  /**
   * Convert XHTML output to image. 
   */
  public static void convertToImage(String inputFileName, String outputFileName) throws IOException {
    
    System.out.println("Calling convertToImage inputFilename=" + inputFileName + " outputFilename=" + outputFileName);
    int widthImage = 600;
    int heightImage = 800;
    
    // Generate an image from a file:
    final File f = new File(inputFileName);
    // can specify width alone, or width + height
    // constructing does not render; not until getImage() is called
    final Java2DRenderer renderer = new Java2DRenderer(f, widthImage, heightImage);

    // this renders and returns the image, which is stored in the J2R; will not
    // be re-rendered, calls to getImage() return the same instance
    final BufferedImage img = renderer.getImage();

    // write it out, full size, PNG
    // FSImageWriter instance can be reused for different images,
    // defaults to PNG
    final FSImageWriter imageWriter = new FSImageWriter();    
    // we can use the same writer, but at a different compression
    imageWriter.setWriteCompressionQuality(0.9f);
    imageWriter.write(img, outputFileName);
    System.out.println("Done with rendering");
  }



} // End of the class //