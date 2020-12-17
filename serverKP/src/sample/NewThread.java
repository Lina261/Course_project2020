package sample;


import Data.WorkWithDB;

import java.net.*;
import java.io.*;
import java.sql.SQLException;

public class NewThread extends Thread {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private int number;

    NewThread(Socket socket, int count){
        client=socket;
        number=count;
    }

    public void run() {
        Integer choice;
       try{
            in=new BufferedReader(new InputStreamReader(client.getInputStream()));
            out=new PrintWriter(new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))),true);
            String dbName="mydb";
            //String url="jdbc:mysql://localhost:3306/mydb";
            String timeZone = "Europe/Minsk";
            String urlDB = "jdbc:mysql://localhost:3306/" + dbName + "?serverTimezone=" + timeZone;
            String login="root";
            String password="alina123";
            WorkWithDB work=WorkWithDB.getInstance(urlDB,login,password);
            MainServer project=new MainServer(in,out,work);
            while(true) {
                // try {
                String buffer = in.readLine();
                if (buffer.equals("END")) {
                    work.close();
                    System.out.println("Связь с базой данных прекращена");
                    break;
                }
                choice = Integer.parseInt(buffer);
                project.choice(choice);

            }
        } catch (Exception e) {
           System.out.println(e);
        }
        finally {

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
            System.out.println("Клиент "+number+" был отсоединен");
        }
    }}


