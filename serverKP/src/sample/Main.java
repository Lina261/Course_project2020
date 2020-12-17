package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main  {

    public static void main(String[] args) {
        try{
            ServerSocket server=new ServerSocket(4000);
            int count=0;
            System.out.println("Подключение к серверу");
            while (true){
                count++;
                Socket client= server.accept();
                System.out.println("Клиент "+count+" успешно подключен");
                NewThread th=new NewThread(client,count);
                th.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
