package com.lab603.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

public class CopyUtil {

	@SuppressWarnings("unchecked")
	public static <T> List<T> deepCopyList(List<T> src) {
		List<T> dest = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			dest = (List<T>) in.readObject();
		} catch (IOException e) {
			System.err.println("IOException:" + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:" + e.getMessage());
		}
		return dest;
	}

	@SuppressWarnings("unchecked")
	public static <T> T clone(T src) {
		ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		T dist = null;
		try {
			out = new ObjectOutputStream(memoryBuffer);
			out.writeObject(src);
			out.flush();
			in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
			dist = (T) in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null)
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			if (in != null)
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
		return dist;
	}
}
