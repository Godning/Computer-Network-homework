/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webserver;

/**
 *
 * @author Godning
 */
import java.io.*;
import java.net.*;
public class WebServer
{
    public static void main(String args[])
    {
        int i=1, PORT=9190;
        ServerSocket server=null;
        Socket client=null;
        try
        {
            server=new ServerSocket(PORT);
            System.out.println("Web Server is listening on port "+server.getLocalPort());
            for (;;)
            {
                client=server.accept();//接受客户机的连接请求
                new ConnectionThread(client,i).start();
                i++;
            }
        }
        catch (IOException e) {System.out.println(e);}
         }
 }
 /* ConnnectionThread类完成与一个Web浏览器的通信　*/
class ConnectionThread extends Thread
 {
    Socket client;//连接Web浏览器的socket字
    int counter;//计数器
    public ConnectionThread(Socket cl,int c)
    {
        client=cl;
        counter=c;
    }
    public void run()//线程体
    {
        try
        {
            String destIP=client.getInetAddress().toString();//客户机IP地址
            int destport=client.getPort();//客户机端口号
            System.out.println("Connection "+counter+":connected to "+destIP+" on port "+destport+".");
            PrintStream outstream=new PrintStream(client.getOutputStream());
            DataInputStream instream=new DataInputStream(client.getInputStream());
            String inline=instream.readLine();//读取Web浏览器提交的请求信息
            System.out.println("Received:"+inline);
            if  (getrequest(inline))
            {//如果是GET请求
            String filename=getfilename(inline);
            File file=new File(filename);
            if (file.exists())
            {//若文件存在，则将文件送给Web浏览器
                System.out.println(filename+" requested.");
                outstream.println("HTTP/1.0 200 OK");
                outstream.println("MIME_version:1.0");
                outstream.println("Content_Type:text/html");
                int len=(int)file.length();
                outstream.println("Content_Length:"+len);
                outstream.println("");
                sendfile(outstream,file);//发送文件
                outstream.flush();
            }else{//文件不存在时
                String msg1="<html><head><title>Not Found</title></head><body><h1>Error 404-file not found</h1></body></html>";
                outstream.println("HTTP/1.0 404 no found");
                outstream.println("Content_Type:text/html");
                outstream.println("Content_Length:"+msg1.length()+2);
                outstream.println("");
                outstream.println(msg1);
                outstream.flush();
            }
        }
        instream.close(); 
        outstream.close();
        long m1=1;//延时
        while (m1<11100000) {m1++;}
        client.close();
        }
        catch (IOException e)
        {
            System.out.println("Exception:"+e);
        }
 }
 /* 获取请求类型是否为“GET” */
boolean getrequest(String s)
 {
    if (s.length()>0)
    {
        if(s.substring(0,3).equalsIgnoreCase("GET")) 
            return true;
    }
    return false;
 }
 /* 获取要访问的文件名 */
String getfilename(String s)
{
    String f=s.substring(s.indexOf(' ')+1);
    f=f.substring(0,f.indexOf(' '));
    try
    {
        if (f.charAt(0)=='/')
        f=f.substring(1);
    }
    catch (StringIndexOutOfBoundsException e)
    {
        System.out.println("Exception:"+e);
    }
    if (f.equals("")) f="index.html";
        return f;
 }
 /*把指定文件发送给Web浏览器 */

void sendfile(PrintStream outs,File file)
{
    try
    {
        DataInputStream in=new DataInputStream(new FileInputStream(file));
        int len=(int)file.length();
        byte buf[]=new byte[len];
        in.readFully(buf);
        outs.write(buf,0,len);
        outs.flush();
        in.close();
    }
    catch(IOException e)
    {
        System.out.println("Error retrieving file.");
        System.exit(1);
    }
}