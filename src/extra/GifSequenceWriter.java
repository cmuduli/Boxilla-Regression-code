package extra;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
/**
 * Class that will create a .gif file from a series of static images
 * @author Boxilla
 *
 */
public class GifSequenceWriter {

	protected ImageWriter writer;
	protected ImageWriteParam params;
	protected IIOMetadata metadata;

	public GifSequenceWriter(ImageOutputStream out, int imageType, int delay, boolean loop) throws IOException {
		writer = ImageIO.getImageWritersBySuffix("gif").next();
		params = writer.getDefaultWriteParam();

		ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
		metadata = writer.getDefaultImageMetadata(imageTypeSpecifier, params);

		configureRootMetadata(delay, loop);

		writer.setOutput(out);
		writer.prepareWriteSequence(null);
	}

	private void configureRootMetadata(int delay, boolean loop) throws IIOInvalidTreeException {
		String metaFormatName = metadata.getNativeMetadataFormatName();
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);

		IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
		graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
		graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay / 10));
		graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

		IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
		commentsNode.setAttribute("CommentExtension", "Created by: https://memorynotfound.com");

		IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
		IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
		child.setAttribute("applicationID", "NETSCAPE");
		child.setAttribute("authenticationCode", "2.0");

		int loopContinuously = loop ? 0 : 1;
		child.setUserObject(new byte[]{ 0x1, (byte) (loopContinuously & 0xFF), (byte) ((loopContinuously >> 8) & 0xFF)});
		appExtensionsNode.appendChild(child);
		metadata.setFromTree(metaFormatName, root);
	}

	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
		int nNodes = rootNode.getLength();
		for (int i = 0; i < nNodes; i++){
			if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
				return (IIOMetadataNode) rootNode.item(i);
			}
		}
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return(node);
	}

	private void writeToSequence(RenderedImage img) throws IOException {
		writer.writeToSequence(new IIOImage(img, null, metadata), params);
	}

	private void close() throws IOException {
		writer.endWriteSequence();
	}


	public static void createGif(ArrayList<String> list, String gifName) throws Exception {
		String dir = StartupTestCase2.gifLocation;
		BufferedImage first = ImageIO.read(new File(list.get(0)));
		ImageOutputStream output = new FileImageOutputStream(new File(dir + "\\" + gifName + ".gif"));

		GifSequenceWriter writer = new GifSequenceWriter(output, first.getType(), 700, true);
		writer.writeToSequence(first);
		ArrayList<File> fileList = new ArrayList<File>();

		for(int j = 1; j < list.size(); j++) {
			fileList.add(new File(list.get(j)));
		}
		for (File image : fileList) {
			BufferedImage next = ImageIO.read(image);
			writer.writeToSequence(next);
		}

		writer.close();
		output.close();
	}
	public static ArrayList<String> loadImages() {

		File[] files = new File("C:\\Test_Workstation\\SeleniumAutomation\\test-output\\Screenshots\\").listFiles();


		Arrays.sort(files, new Comparator<File>(){
			public int compare(File f1, File f2)
			{
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			} });

		ArrayList<String> newList = new ArrayList<String>();
		for(File f : files) {
			newList.add(f.getAbsolutePath());
		}
		Collections.sort(newList);
		for(String s : newList) {
			System.out.println(s);
		}
		return newList;
	}

	public static void main(String[]args) {
		GifSequenceWriter.loadImages();
	}
}
