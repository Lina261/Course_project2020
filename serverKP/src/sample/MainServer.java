package sample;

import Data.WorkWithDB;
import com.mysql.cj.protocol.Resultset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainServer {
    private BufferedReader in;
    private PrintWriter out;
    private WorkWithDB work;


    public MainServer(BufferedReader in, PrintWriter out, WorkWithDB work) {
        this.in = in;
        this.out = out;
        this.work = work;
    }


    public void choice(int choice) throws IOException, SQLException {
        switch (choice){
            case 1:
                this.entrance();
                break;
            case 2:
                this.registration();
                break;
            case 3:
                this.initUser();
                break;
            case 4:
                this.deleteUser();
                break;

            case 5:
                this.editUser();
                break;

            case 6:
                this.adminFactory();
                break;

            case 7:
                this.addFactory();
                break;
            case 8:
                this.showFeatures();
                break;
            case 9:
                this.showFactoryWithCountry();
                break;

            case 10:
                this.initCountry();
                break;

            case 11:
                this.deleteFactory();
                break;
            case 12:
                this.editFactory();
                break;
          case 13:
                this.findFactory();
                break;
            case 14:
                this.setFeatures();
                break;
            case 15:
                this.showCountry();
                break;
            case 16:
                this.makeEfficiency();
                break;
            case 17:
                this.pieChart();
                break;
            case 18:
                this.graphic();
                break;
            case 19:
                this.otchet();
                break;
        }
}

    private void otchet() throws IOException, SQLException{
        System.out.println("ОТЧЕТ");

        Integer idFactory = Integer.parseInt(in.readLine());
        ResultSet report=work.select("SELECT\n" +
                "    f.factoryName,\n" +
                "    f.directorSurname,\n" +
                "    f.factoryAddress,\n" +
                "    f2.proceeds,\n" +
                "    f2.assets,\n" +
                "    f2.equity,\n" +
                "    f2.bacon,\n" +
                "    r.result\n" +
                "FROM factory f\n" +
                "INNER JOIN features f2 on f.idFactory = f2.idFactory\n" +
                "INNER JOIN result r on f2.idFeature = r.idFeature\n" +
                "WHERE f.idFactory=" + idFactory
        );;


        if (report.next()) {
            out.println(report.getString("factoryName"));
            out.println(report.getString("directorSurname"));
            out.println(report.getString("factoryAddress"));
            out.println(report.getString("proceeds"));
            out.println(report.getString("assets"));
            out.println(report.getString("equity"));
            out.println(report.getString("bacon"));
            out.println(report.getString("result"));
        } else {
            out.println(0);
            out.println(0);
            out.println(0);
            out.println(0);
            out.println(0);
            out.println(0);
            out.println(0);
            out.println(0);
        }
    }

    private void graphic() throws IOException, SQLException {
        System.out.println("ГРАФИК");

        Integer idFactory = Integer.parseInt(in.readLine());
        ResultSet count_res=work.select("SELECT\n" +
                "    COUNT(f.factoryName) `count`\n" +
                "FROM factory f\n" +
                "LEFT JOIN features f2 on f.idFactory = f2.idFactory\n" +
                "LEFT JOIN result r on f2.idFeature = r.idFeature\n" +
                "WHERE f.idCountry = " + idFactory);;

        if (count_res.next()) {
            out.println(count_res.getString("count"));
        } else {
            out.println(0);
        }

        ResultSet res;
        res = work.select("SELECT\n" +
                "    f.factoryName,\n" +
                "    r.result\n" +
                "FROM factory f\n" +
                "LEFT JOIN features f2 on f.idFactory = f2.idFactory\n" +
                "LEFT JOIN result r on f2.idFeature = r.idFeature\n" +
                "WHERE f.idCountry = " + idFactory);
        while(res.next()) {
            out.println(res.getString("factoryName"));
            if (res.getString("result")== null) {
                out.println(0);
            } else {
                out.println(res.getString("result"));
            }

        }


    }

    private void pieChart() throws IOException, SQLException {
        System.out.println("ДИАГРАММА");

        ResultSet country_count = work.select("SELECT COUNT(*) `count` FROM country;");
        if (country_count.next()) {
            out.println(Integer.parseInt(country_count.getString("count")));
        } else {
            out.println(0);
        }

        ResultSet country_res=work.select("SELECT COUNT(f.idCountry) `count`, countryName\n" +
                "FROM factory f\n" +
                "INNER JOIN country c on f.idCountry = c.idCountry\n" +
                "GROUP BY f.idCountry;");;


        while (country_res.next()) {
            out.println(country_res.getString("countryName"));
            out.println(country_res.getString("count"));
        }
    }

    private void makeEfficiency() throws IOException, SQLException{
        System.out.println("РАСЧЕТ РЕНТАБЕЛЬНОСТИ");

        Integer idFactory = Integer.parseInt(in.readLine());
        ResultSet calc = work.select(
                "SELECT\n" +
                    "    *\n" +
                    "FROM features\n" +
                    "WHERE idFactory="+idFactory
        );
        Double result = 0.0;
        Integer idfeature = 0;
        while (calc.next()) {
            Double bacon = Double.parseDouble(calc.getString("bacon"));
            Double assets = Double.parseDouble(calc.getString("assets"));
            Double equity = Double.parseDouble(calc.getString("equity"));
            Double proceeds = Double.parseDouble(calc.getString("proceeds"));
            result = (bacon/proceeds)*(proceeds/assets)*(assets/equity);
            idfeature = Integer.parseInt(calc.getString("idFeature"));
        }

        work.insert("INSERT INTO result (idFeature, result) VALUES ("+idfeature+","+result+");");

    }

    private void showCountry() throws SQLException {
        System.out.println("СТРАНЫ");

        ResultSet count;
        count = work.select("SELECT COUNT(*) `count` FROM country ");

        if(count.next()) {
            out.println(count.getString("count")); // Отправить кол-во пользователей
        } else {
            out.println(0);
        }

        ResultSet country;
       country= work.select("SELECT * FROM country");


        while (country.next()) {
            out.println(country.getString("idCountry"));
            out.println(country.getString("countryName"));

        }
    }

    private void setFeatures() throws IOException {
        Integer idfactory=Integer.parseInt(in.readLine());
        Double proceeds=Double.parseDouble(in.readLine());
        Double assets=Double.parseDouble(in.readLine());
        Double equity=Double.parseDouble(in.readLine());
        Double bacon=Double.parseDouble(in.readLine());
        work.insert("INSERT INTO `features` (idFactory, proceeds,assets,equity, bacon) VALUES ('"+idfactory+"','"+proceeds+"','"+assets+"','"+equity+"', " +bacon+ ")");



    }

    private void initCountry() throws SQLException, IOException{
        ResultSet count;
        count = work.select("SELECT COUNT(*) `count` FROM country");

        if(count.next()) {
            out.println(count.getString("count")); // Отправить кол-во пользователей
        } else {
            out.println(0); // Отправить кол-во пользователей
        }

        ResultSet res;
        res = work.select("SELECT * FROM country");
        while (res.next()) {
            out.println(res.getString("idCountry"));
            out.println(res.getString("countryName"));

        }

    }

    private void showFactoryWithCountry() throws IOException, SQLException {
            System.out.println("ПРЕДПРИЯТИЯ ПО СТРАНЕ");
            Integer idCountry= Integer.parseInt(in.readLine());

            ResultSet count;
            count = work.select("SELECT COUNT(*) `count` FROM factory WHERE idCountry = " + idCountry);

            if(count.next()) {
                out.println(count.getString("count")); // Отправить кол-во
            } else {
                out.println(0);
            }
            ResultSet res=work.select("SELECT\n" +
                    "   f.idFactory,\n" +
                    "    f.factoryName,\n" +
                    "    f.factoryAddress,\n" +
                    "    f.directorSurname,\n" +
                    "    c.countryName,\n" +
                    "   u.login\n" +
                    "FROM factory f\n" +
                    "INNER JOIN users u USING(idUser)\n" +
                    "INNER JOIN country c on f.idCountry = c.idCountry\n" +
                    "WHERE f.idCountry = "+idCountry);;


            while (res.next()) {
                out.println(res.getString("idFactory"));
                out.println(res.getString("factoryName"));
                out.println(res.getString("factoryAddress"));
                out.println(res.getString("directorSurname"));
                out.println(res.getString("countryName"));
                out.println(res.getString("login"));

            }

        }


    private void showFeatures() throws IOException, SQLException {
        System.out.println("ПОКАЗАТЕЛИ");
        Integer idFactory = Integer.parseInt(in.readLine());

        ResultSet count;
        count = work.select("SELECT COUNT(*) `count` FROM features WHERE idFactory = " + idFactory);

        if(count.next()) {
            out.println(count.getString("count")); // Отправить кол-во пользователей
        } else {
            out.println(0);
        }

        ResultSet features=work.select("SELECT\n" +
                "   fe.idFeature,\n" +
                "    f.factoryName,\n" +
                "    fe.proceeds,\n" +
                "    fe.assets,\n" +
                "    fe.equity,\n" +
                "    fe.bacon\n" +

                "FROM features fe\n" +
                "INNER JOIN factory f on fe.idFactory= f.idFactory\n" +
                "WHERE f.idFactory = "+idFactory);;


        while (features.next()) {
            out.println(features.getString("idFeature"));
            out.println(features.getString("factoryName"));
            out.println(features.getString("proceeds"));
            out.println(features.getString("assets"));
            out.println(features.getString("equity"));
            out.println(features.getString("bacon"));

        }

    }

    private void initUser() throws IOException, SQLException {
        System.out.println("ПОЛЬЗОВАТЕЛЬ");
        ResultSet count;
        count = work.select("SELECT COUNT(*) `count` FROM users");
        if(count.next()) {
            out.println(count.getString("count")); // Отправить кол-во пользователей
        } else {
            out.println(0);
        }

        ResultSet res;
        res = work.select("SELECT * FROM users");
        while (res.next()) {
            out.println(res.getString("idUser"));
            out.println(res.getString("name"));
            out.println(res.getString("login"));
            out.println(res.getString("password"));
            out.println(res.getString("role"));
        }
    }

    private void registration() throws IOException {
        System.out.println("РЕГИСТРАЦИЯ");
        String name = in.readLine();
        String login = in.readLine();
        String password = in.readLine();
        Integer role = Integer.parseInt(in.readLine());
        try {
            work.insert("INSERT INTO `users` (name, login, password, role) VALUES " +
                    "('" +name+ "', '" +login+ "','" +password+ "'," +role+ ");");
            System.out.println("New User was added");
        } catch(Exception ex) {
            out.println("Error in added User");
        }
    }

    private void entrance() throws IOException, SQLException {
        System.out.println("ВХОД");
        String login = in.readLine();
        String password = in.readLine();


         ResultSet res = work.select("SELECT * FROM users WHERE login='" +login+ "' AND password='" +password+ "'");
        if (res.next()) {
            out.println("ok");
            out.println(res.getString("idUser"));
            out.println(res.getString("name"));
            out.println(res.getString("role"));
        } else {
            out.println("error");
        }
    }




    private void deleteUser() throws IOException {
        System.out.println("УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ");
        Integer id = Integer.parseInt(in.readLine());
        work.delete("DELETE FROM users WHERE idUser=" + id);
    }

    private void editUser() throws IOException{
        System.out.println("РЕДАКТИРОВАНИЕ ПОЛЬЗОВАТЕЛЯ");
        Integer idUser = Integer.parseInt(in.readLine());

        String name = in.readLine();
        String login = in.readLine();
        String password = in.readLine();
        Integer role = Integer.parseInt(in.readLine());

            work.insert("UPDATE `users` SET  name='"+name+"', login='"+login+"', password='"+password+"', role="+role+
                    " WHERE idUser="+idUser);
            System.out.println("Пользователь отредактирован");


    }

    private void adminFactory() throws SQLException{
        System.out.println("ПРЕДПРИЯТИЯ");

        ResultSet count;
        count = work.select("SELECT COUNT(*) `count` FROM factory");

        if(count.next()) {
            out.println(count.getString("count")); // Отправить кол-во пользователей
        } else {
            out.println(0);
        }

        ResultSet res=work.select("SELECT\n" +
                "    f.idFactory,\n" +
                "    f.factoryName,\n" +
                "    f.factoryAddress,\n"+
                "    f.directorSurname,\n" +
                "    c.countryName,\n"+
                "    u.login\n" +
                "FROM factory f\n" +
                "INNER JOIN country c USING(idCountry)\n" +
                "INNER JOIN users u USING(idUser);");;

        while (res.next()) {
            out.println(res.getString("idFactory"));
            out.println(res.getString("factoryName"));
            out.println(res.getString("factoryAddress"));
            out.println(res.getString("directorSurname"));
            out.println(res.getString("countryName"));
            out.println(res.getString("login"));
        }
    }



    private void deleteFactory() throws IOException, SQLException {
        System.out.println("УДАЛЕНИЕ ПРЕДПРИЯТИЯ");
        Integer id = Integer.parseInt(in.readLine());

        work.delete("DELETE FROM factory WHERE idFactory = " + id);

    }

    private void addFactory() throws IOException {
        System.out.println("ДОБАВЛЕНИЕ ПРЕДПРИЯТИЯ");
        String factoryName = in.readLine();
        String factoryAddress = in.readLine();
        String directorSurname = in.readLine();
        Integer idCountry = Integer.parseInt(in.readLine());
        Integer idUser = Integer.parseInt(in.readLine());

        work.insert("INSERT INTO `factory` (factoryName,directorSurname,factoryAddress, idCountry, idUser) VALUES ('"+factoryName+"','"+directorSurname+"','"+factoryAddress+"', " +idCountry+ ", "+idUser + ")");
    }



    private void editFactory() throws IOException, SQLException {
        System.out.println("РЕДАКТИРОВАНИЕ ПРЕДПРИЯТИЯ");

        Integer idFactory = Integer.parseInt(in.readLine());
        String factoryName = in.readLine();
        String factoryAddress=in.readLine();
        String directorSurname=in.readLine();
        Integer idCountry = Integer.parseInt(in.readLine());


        work.insert("UPDATE `factory` SET factoryName='"+factoryName+"', directorSurname = '"+directorSurname+"', factoryAddress='"+factoryAddress+"', idCountry = "+idCountry+" WHERE idFactory="+idFactory);
    }


    private void findFactory() throws IOException, SQLException{
        System.out.println("ПОИСК");


        String searchQuery = in.readLine();

        ResultSet count;
        count = work.select("SELECT\n" +
                "    COUNT(f.idFactory) `count`\n" +
                "FROM factory f\n" +
                "     INNER JOIN country c on f.idCountry = c.idCountry\n" +
                "WHERE LOWER(f.factoryName) LIKE '%"+searchQuery+"%';");
        if (count.next()) {
            out.println(count.getString("count"));
        } else {
            out.println(0);
        }
        ResultSet res=work.select("SELECT\n" +
                "    f.idFactory,\n" +
                "    f.factoryName,\n" +
                "    f.factoryAddress,\n" +
                "    f.directorSurname,\n" +
                "    c.countryName,\n" +
                "    u.login\n" +
                "FROM factory f\n" +
                "INNER JOIN country c USING(idCountry)\n" +
                "INNER JOIN users u USING(idUser)\n"+
                "WHERE  LOWER(f.factoryName) LIKE '%"+searchQuery+"%';");;

        while (res.next()) {
            out.println(res.getString("idFactory"));
            out.println(res.getString("factoryName"));
            out.println(res.getString("factoryAddress"));
            out.println(res.getString("directorSurname"));
            out.println(res.getString("countryName"));
            out.println(res.getString("login"));
        }
    }




}
