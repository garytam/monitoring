package com.inquicker.monitoring.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUntils {

	public static void saveFile(String content, String fileFullPath) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(fileFullPath);
			bw = new BufferedWriter(fw);
			bw.write(content);

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
				

			}

		}

	}
}
