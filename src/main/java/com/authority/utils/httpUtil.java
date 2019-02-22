package com.authority.utils;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;  
import org.apache.http.StatusLine;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.util.EntityUtils;  */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class httpUtil
{
    public static  String sendPost(String URL,String dataJson) throws Exception, IOException{/*
    	 StringBuffer buffer = new StringBuffer();
         InputStream instr = null;
         java.io.ByteArrayOutputStream out = null;
        try {
        	byte[] xmlData = dataJson.getBytes();
           
            URL url = new URL(URL);
            //URLConnection urlCon = url.openConnection();
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("content-Type", "application/json");
            urlCon.setRequestProperty("charset", "utf-8");
            urlCon.setRequestProperty("Content-length",String.valueOf(xmlData.length));
           
            System.out.println(String.valueOf(xmlData.length));
            DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());
//            printout.write(xmlData);
            printout.writeChars(dataJson);
            printout.flush();
            printout.close();
            instr = urlCon.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(instr));
            
            String line = "";
            while ((line = in.readLine()) != null){
              buffer.append(line);
            }
//            System.out.println(buffer.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                out.close();
                instr.close();
            } catch (Exception ex) {
            }
        }
    	return buffer.toString();
    */
    	 //创建连接             
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("connection", "keep-alive");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.connect();
        // POST请求 
         DataOutputStream out  = new DataOutputStream(connection.getOutputStream());
         String json = dataJson.toString();
         //out.writeChars(json); // 需要使用是 out.write(json.getBytes());
         out.write(json.getBytes());
         System.out.println(json);
         out.flush();
         // 读取响应 
         BufferedReader reader  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String lines;
         StringBuffer sb = new StringBuffer("");
         while ((lines = reader.readLine()) != null) {
             lines = new String(lines.getBytes(), "utf-8");
             sb.append(lines);
         }
        reader.close();
         // 断开连接 
         connection.disconnect();
		return sb.toString(); 
    
    }
    
    /** 
     * 向指定URL发送GET方法的请求 
     * @author cuijianbin
     * @param url 发送请求的URL 
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。 
     * @return URL 所代表远程资源的响应结果 
     */  
    public static String sendGet(String url, String param) {  
        String result = "";  
        BufferedReader in = null;  
        try {  
            //String urlNameString = url + "?" + param;  
            String urlNameString = url + "/" + param;  
            URL realUrl = new URL(urlNameString);  
            // 打开和URL之间的连接  
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性  
            connection.setRequestProperty("content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("accept", "*/*");  
            connection.setRequestProperty("connection", "Keep-Alive");  
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            // 建立实际的连接  
            connection.connect();  
            // 获取所有响应头字段  
            Map<String, List<String>> map = connection.getHeaderFields();  
            // 遍历所有的响应头字段  
            for (String key : map.keySet()) {  
                System.out.println(key + "--->" + map.get(key));  
            }  
            // 定义 BufferedReader输入流来读取URL的响应  
            InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");//解决读取数据乱码问题.因为InputStreamReader和BufferedReader都继承自Reader,而BufferedReader的构造器又是Reader.
            in = new BufferedReader(isr);
            String line=null;  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {  
            System.out.println("发送GET请求出现异常！" + e);  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输入流  
        finally {  
            try {  
                if (in != null) {  
                    in.close();  
                }  
            } catch (Exception e2) {  
                e2.printStackTrace();  
            }  
        }  
        return result;  
    }  
}
