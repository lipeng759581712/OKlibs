/*
 * Copyright (c) 1998-2012 TENCENT Inc. All Rights Reserved.
 * 
 * FileName: ZipUtils.java
 * 
 * Description: 压缩与解压缩辅助类文件
 * 
 * History:
 * 1.0	devilxie	2012-09-05	Create
 */
package com.max.utils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * 压缩与解压缩工具类，提供了对目录、文件进行压缩与解压缩等方法
 * @author devilxie
 * @version 1.0
 */
public class ZipUtils
{
	public interface IFileFilter {
        boolean filter(File file);
	}

	/**
	 * 解压文件
	 * 
	 * @param zipPath 压缩包路径
	 * @param toPath 解压目录
	 * @param convertor 文件名字转换，null则文件为原始名称
	 * @throws IOException 如果出现读写错误，将抛出IO异常
	 */
	public static void unCompress(String zipPath, String toPath, FileNameConvertor convertor)
			throws IOException
	{
		File zipfile = new File(zipPath);
		if (!zipfile.exists())
			return;

		if (!toPath.endsWith("/"))
			toPath += "/";

		File destFile = new File(toPath);
		if (!destFile.exists())
		{
			boolean flag = destFile.mkdirs();
			if(!flag)
			{
				//TLog.e("ZipUtils", "mkdirs fail . path = "+toPath);
			}
		}

		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfile));
		ZipEntry entry = null;

		try
		{

			while ((entry = zis.getNextEntry()) != null)
			{
				if (entry.isDirectory())
				{
					File file = new File(toPath + entry.getName() + "/");
					boolean flag = file.mkdirs();
					if(!flag)
					{
						//TLog.e("Algorithm", "ZipUtils unCompress mkdirs fail . directory :"+file.getAbsolutePath());
					}
				}
				else
				{
					File file = new File(toPath + (convertor == null ? entry.getName() : convertor.convertName(entry.getName())));
					if (!file.getParentFile().exists())
					{
						if(!file.getParentFile().mkdirs())
						{
							//TLog.e("Algorithm", "ZipUtils unCompress mkdirs fail..");
						}
					}
						

					FileOutputStream fos = null;

					try
					{
						fos = new FileOutputStream(file);
						byte buf[] = new byte[1024];
						int len = -1;
						while ((len = zis.read(buf, 0, 1024)) != -1)
						{
							fos.write(buf, 0, len);
						}
					}
					finally
					{

						if (fos != null)
						{
							try
							{
								fos.close();
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}

		}
		finally
		{
			zis.close();
		}

	}

	/**
	 * 压缩单个文件,并将压缩输出文件置同层目录，以文件名+ ".zip"的形式命名
	 * 
	 * @param file 被压缩文件
	 */
	public static boolean compress(File file)
	{
		InputStream in = null;
		ZipOutputStream out = null;
		CheckedOutputStream cs = null;
		FileOutputStream f = null;
		try
		{
			String fileName = file.getName();
			if (fileName.indexOf(".") != -1)
				fileName = fileName.substring(0, fileName.indexOf("."));
			f = new FileOutputStream(file.getParent() + "/"
					+ fileName + ".zip");
			cs = new CheckedOutputStream(f, new Adler32());
			out = new ZipOutputStream(new BufferedOutputStream(
					cs));
			in = new FileInputStream(file);
			out.putNextEntry(new ZipEntry(file.getName()));
			int len = -1;
			byte buf[] = new byte[1024];
			while ((len = in.read(buf, 0, 1024)) != -1)
				out.write(buf, 0, len);
			out.closeEntry();
			return true;
		}
		catch (Exception e)
		{
			Log.e("error", "error", e);
			return false;
		} 
		finally 
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(cs);
			IOUtils.closeQuietly(f);
		}
	}

	/**
	 * 压缩文件夹，并将压缩输出文件置同层目录，以目录名 + ".zip"的形式命名
	 * 
	 * @param file 待压缩的目录文件
	 * @throws IOException 如果出现读写错误，将抛出IO异常
	 */
	public static String compressDir(File file, String zipFile, IFileFilter filter) throws IOException {
		String zipFilePath = zipFile;
		FileOutputStream f = null;
		CheckedOutputStream cs = null;
		ZipOutputStream out = null;
		try
		{
            if (zipFilePath == null || zipFilePath.length() <= 0) {
                zipFilePath = file.getParent() + file.getName() + ".zip";
            }

			f = new FileOutputStream(zipFilePath);
			cs = new CheckedOutputStream(f, new Adler32());
			out = new ZipOutputStream(new BufferedOutputStream(cs));
	
			compressDir(file, out, file.getAbsolutePath(), filter);
	
			out.flush();
		} finally 
		{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(cs);
			IOUtils.closeQuietly(f);
		}

		return zipFilePath;
	}

	/**
	 * 压缩文件夹递归调用方法
	 *
	 * @param srcFile
	 * @param out
	 * @param destPath
	 * @throws IOException 如果出现读写错误，将抛出IO异常
	 */
	private static void compressDir(File srcFile, ZipOutputStream out,
                                    String destPath, IFileFilter filter) throws IOException {
		InputStream in = null;
		if (srcFile.isDirectory()) {
			File subFile[] = srcFile.listFiles();
			if (subFile == null) {
				return;
			}

			for (int i = 0; i < subFile.length; i++) {
				compressDir(subFile[i], out, destPath, filter);
			}
		}else {
			try {
                if (filter != null && filter.filter(srcFile)){
                    return;
                }

				in = new FileInputStream(srcFile);
				String name = srcFile.getAbsolutePath().replace(destPath, "");
				if (name.startsWith("\\") || name.startsWith("/"))
					name = name.substring(1);
				ZipEntry entry = new ZipEntry(name);
				entry.setSize(srcFile.length());
				entry.setTime(srcFile.lastModified());
				out.putNextEntry(entry);
				int len = -1;
				byte buf[] = new byte[1024];
				while ((len = in.read(buf, 0, 1024)) != -1)
					out.write(buf, 0, len);
	
				out.closeEntry();
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
	}

	/**
	 * 文件名转换器
	 * <p/>
	 * 在使用{@link #unCompress(String, String, FileNameConvertor)}方法，
	 * 可传入自定义文件名转换器，这样生成的解压文件将采用转换后的名字，否则将采用原生名字
	 */
	public interface FileNameConvertor 
	{
		/**
		 * 将原始文件名字进行转换
		 */
		String convertName(String originName);
	}
	
	/**
	 * 对输入字节数组进行压缩，并获得压缩后的字节数组，可指定压缩范围
	 *
	 * @param uncompressBytes 待压缩字节数组
	 * @param offset          压缩偏移位置
	 * @param len             压缩长度
	 * @throws IOException 如果出现读写错误，将抛出IO异常
	 */
	public static byte[] compress (byte[] uncompressBytes, int offset, int len) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		compress(uncompressBytes, offset, len, bos);
		return bos.toByteArray();
	}

	private static void compress (byte[] srcBytes, int offset, int len, OutputStream os)
			throws IOException
	{
		DeflaterOutputStream dos = new DeflaterOutputStream(os);

		try
		{
			dos.write(srcBytes, offset, len);
		}
		finally
		{
			dos.close();
		}

	}

	/**
	 * 对输入字节数组进行解压，并获得解压后的字节数组，可指定解压范围
	 *
	 * @param compressBytes 待解压字节数组
	 * @param offset        解压偏移位置
	 * @param len           解压长度
	 * @throws IOException 如果出现读写错误，将抛出IO异常
	 */
	public static byte[] uncompress (byte[] compressBytes, int offset, int len) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (compressBytes.length > 0 && compressBytes.length >= offset + len)
		{
			uncompress(compressBytes, offset, len, bos);
		}
		return bos.toByteArray();
	}

	private static int uncompress (byte[] compressBytes, int offset, int len, OutputStream os)
	{
		InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(compressBytes,
				offset, len));

		byte[] chunk = new byte[2048];
		int count;
		int totalLen = 0;

		try
		{
			while ((count = iis.read(chunk)) >= 0)
			{
				os.write(chunk, 0, count);
				totalLen += count;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(iis);
		}

		return totalLen;
	}

	/**
	 * 解压字节数组，返回解压长度
	 *
	 * @param compressBytes 待解压字节数组
	 * @param out           用于放置解压结果的字节数组
	 * @return 当解压结果的长度小于out的长度时，out字节数组起始的一段会被置换成解压结果，并返回解压结果的长度；<p/>
	 * 		   当解压结果的长度大于或者等于out的长度时，out字节数组会被置换成解压结果的前面一部分，并返回out字节数组的长度
	 */
	public static int uncompress (byte[] compressBytes, byte[] out)
	{

		if (out == null || out.length == 0)
			return 0;

		InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(compressBytes));
		byte[] chunk = new byte[2048];
		int count = 0;
		int totalLen = 0;

		try
		{
			while (totalLen < out.length && (count = iis.read(chunk)) >= 0)
			{
				count = Math.min(count, out.length - totalLen);
				System.arraycopy(chunk, 0, out, totalLen, count);
				totalLen += count;
			}
		}
		catch (IOException e)
		{
		//	ALog.printStackTrace(e);
		}
		finally
		{
			try
			{
				iis.close();
			}
			catch (IOException e)
			{
			}
		}

		return totalLen;
	}
}

