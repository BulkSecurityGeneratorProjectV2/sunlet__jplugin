package net.jplugin.ext.filesvr.svc;

/**
 *  ����ͼʵ�֣���ͼƬ(jpg��bmp��png��gif�ȵ�)��ʵ�ı����Ҫ�Ĵ�С
 */

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;

/*******************************************************************************
 * ����ͼ�ࣨͨ�ã� ��java���ܽ�jpg��bmp��png��gifͼƬ�ļ������еȱȻ�ǵȱȵĴ�Сת���� ����ʹ�÷���
 * compressPic(��ͼƬ·��,����СͼƬ·��,��ͼƬ�ļ���,����СͼƬ����,����СͼƬ���,����СͼƬ�߶�,�Ƿ�ȱ�����(Ĭ��Ϊtrue))
 */
public class PicCompressHelper {
	private File file = null; // �ļ�����
	private String inputDir; // ����ͼ·��
	private String outputDir; // ���ͼ·��
	private String inputFileName; // ����ͼ�ļ���
	private String outputFileName; // ���ͼ�ļ���
	private int outputWidth = 100; // Ĭ�����ͼƬ��
	private int outputHeight = 100; // Ĭ�����ͼƬ��
	private boolean proportion = true; // �Ƿ�ȱ����ű��(Ĭ��Ϊ�ȱ�����)

	public PicCompressHelper() { // ��ʼ������
		inputDir = "";
		outputDir = "";
		inputFileName = "";
		outputFileName = "";
		outputWidth = 100;
		outputHeight = 100;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public void setOutputWidth(int outputWidth) {
		this.outputWidth = outputWidth;
	}

	public void setOutputHeight(int outputHeight) {
		this.outputHeight = outputHeight;
	}

	public void setWidthAndHeight(int width, int height) {
		this.outputWidth = width;
		this.outputHeight = height;
	}

	/*
	 * ���ͼƬ��С ������� String path ��ͼƬ·��
	 */
	public long getPicSize(String path) {
		file = new File(path);
		return file.length();
	}

	// ͼƬ����
	public void compressPic() {
		try {
			// ���Դ�ļ�
			file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				throw new RuntimeException("�ļ������ڣ�" + inputDir + inputFileName);
			}
			Image img = ImageIO.read(file);
			// �ж�ͼƬ��ʽ�Ƿ���ȷ
			if (img.getWidth(null) == -1) {
				System.out.println(" can't read,retry!" + "<BR>");
				throw new RuntimeException(" can't read,retry!" + inputDir
						+ inputFileName);
			} else {
				int newWidth;
				int newHeight;
				// �ж��Ƿ��ǵȱ�����
				if (this.proportion == true) {
					// Ϊ�ȱ����ż��������ͼƬ��ȼ��߶�
					double rate1 = ((double) img.getWidth(null))
							/ (double) outputWidth ;
					double rate2 = ((double) img.getHeight(null))
							/ (double) outputHeight ;
					// �������ű��ʴ�Ľ������ſ���
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = outputWidth; // �����ͼƬ���
					newHeight = outputHeight; // �����ͼƬ�߶�
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH �������㷨 ��������ͼƬ��ƽ���ȵ� ���ȼ����ٶȸ� ���ɵ�ͼƬ�����ȽϺ� ���ٶ���
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = null;
				try {
					saveImage(tag, outputDir + outputFileName);
					// out = new FileOutputStream(outputDir + outputFileName);
					// // JPEGImageEncoder������������ͼƬ���͵�ת��
					// JPEGImageEncoder encoder =
					// JPEGCodec.createJPEGEncoder(out);
					// encoder.encode(tag);
				} finally {
					closeQuitely(out);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException("ѹ��ʧ�ܣ�" + inputDir + inputFileName, ex);
		}
	}

	static void saveImage(BufferedImage dstImage, String dstName)
			throws IOException {
		String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
//		ImageIO.write(dstImage, /* "GIF" */formatName /* format desired */,
//				new File(dstName) /* target */);
		ImageIO.write(dstImage, /* "GIF" */"JPG" /* format desired */,
				new File(dstName) /* target */);
	}

	/**
	 * @param out
	 */
	private void closeQuitely(FileOutputStream s) {
		if (s != null) {
			try {
				s.close();
			} catch (Exception e) {
				ServiceFactory.getService(ILogService.class)
						.getLogger(PicCompressHelper.class.getName())
						.error("�ر����쳣", e);
			}
		}
	}

	public static void compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName) {
		PicCompressHelper pch = new PicCompressHelper();
		// ����ͼ·��
		pch.inputDir = inputDir;
		// ���ͼ·��
		pch.outputDir = outputDir;
		// ����ͼ�ļ���
		pch.inputFileName = inputFileName;
		// ���ͼ�ļ���
		pch.outputFileName = outputFileName;
		pch.compressPic();
	}

	public static void compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int width, int height,
			boolean gp) {
		PicCompressHelper pch = new PicCompressHelper();
		// ����ͼ·��
		pch.inputDir = inputDir;
		// ���ͼ·��
		pch.outputDir = outputDir;
		// ����ͼ�ļ���
		pch.inputFileName = inputFileName;
		// ���ͼ�ļ���
		pch.outputFileName = outputFileName;
		// ����ͼƬ����
		pch.setWidthAndHeight(width, height);
		// �Ƿ��ǵȱ����� ���
		pch.proportion = gp;
		pch.compressPic();
	}
	
	public static void compressSelf(String filename,int w,int h) {
		String dir = new File(filename).getParentFile().getAbsolutePath()+"/";
		String fname = new File(filename).getName();
		PicCompressHelper.compressPic(dir, dir,fname, fname, w, h, true);
	}

	// main����
	// compressPic(��ͼƬ·��,����СͼƬ·��,��ͼƬ�ļ���,����СͼƬ����,����СͼƬ���,����СͼƬ�߶�,�Ƿ�ȱ�����(Ĭ��Ϊtrue))
	public static void main(String[] arg) {
		PicCompressHelper mypic = new PicCompressHelper();
		System.out.println("�����ͼƬ��С��" + mypic.getPicSize("e:\\a.jpg") / 1024
				+ "KB");
		int count = 0; // ��¼ȫ��ͼƬѹ������ʱ��
		int start = (int) System.currentTimeMillis(); // ��ʼʱ��
		mypic.compressPic("e:\\", "e:\\", "a.jpg", "a1.jpg", 120, 120, true);
		int end = (int) System.currentTimeMillis(); // ����ʱ��
		int re = end - start; // ��ͼƬ���ɴ���ʱ��
	}
	
	public static void cutAndResizeImage(String src, String dest, int x, int y, int w,
			int h,int destWidth,int destHeight,int htmlImgWidth){
		
		if (htmlImgWidth>0){
			//px���������ؽ��л���
			int imgRealWidth = getImageWidth(src);
			if (imgRealWidth<=0){
				throw new RuntimeException("Can't get img width");
			}
			double rate = imgRealWidth*1.0 /htmlImgWidth;
			x = (int) Math.round(x * rate);
			y = (int) Math.round(y * rate);
			w = (int) Math.round(w * rate);
			h = (int) Math.round(h * rate);
		}
		
		cutImage(src,dest,x,y,w,h);
		PicCompressHelper.compressSelf(dest, destWidth, destHeight);
	}
	
	private static int getImageWidth(String file){
		Image img;
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// �ж�ͼƬ��ʽ�Ƿ���ȷ
		return img.getWidth(null);
	}

	public static void cutImage(String src, String dest, int x, int y, int w,
			int h){
		Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = null;
		ImageInputStream iis = null;
		try {
			in = new FileInputStream(src);
			iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, w, h);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "jpg", new File(dest));
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally {
			try{
				if (in!=null) in.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
}
