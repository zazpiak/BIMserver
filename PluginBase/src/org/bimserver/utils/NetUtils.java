package org.bimserver.utils;

/******************************************************************************
 * Copyright (C) 2009-2016  BIMserver.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see {@literal<http://www.gnu.org/licenses/>}.
 *****************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;

public class NetUtils {
	public static String getContent(URL url, int timeOut) throws IOException {
		URLConnection openConnection = url.openConnection();
		openConnection.setConnectTimeout(timeOut);
		openConnection.setReadTimeout(timeOut);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		InputStream in = openConnection.getInputStream();
		IOUtils.copy(in, byteArrayOutputStream);
		in.close();
		return new String(byteArrayOutputStream.toByteArray(), Charsets.UTF_8);
	}

	public static byte[] getContentAsBytes(URL url, int timeOut) throws IOException {
		URLConnection openConnection = url.openConnection();
		openConnection.setConnectTimeout(timeOut);
		openConnection.setReadTimeout(timeOut);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		InputStream in = openConnection.getInputStream();
		IOUtils.copy(in, byteArrayOutputStream);
		in.close();
		return byteArrayOutputStream.toByteArray();
	}
	
	public static void main(String[] args) {
		try {
			String content = getContent(new URL("https://berlotti.github.io/Elasstic/services.json"), 5000);
			System.out.println(content);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ObjectNode post(String url, ObjectNode objectNode) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			objectMapper.writeValue(out, objectNode);
			HttpPost post = new HttpPost(url);
			post.setEntity(new ByteArrayEntity(out.toByteArray(), ContentType.APPLICATION_JSON));
			CloseableHttpResponse httpResponse = httpclient.execute(post);
			ObjectNode response = objectMapper.readValue(httpResponse.getEntity().getContent(), ObjectNode.class);
			return response;
		} finally {
		    httpclient.close();
		}
	}
}
