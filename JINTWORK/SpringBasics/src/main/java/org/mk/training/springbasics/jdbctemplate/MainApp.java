/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.springbasics.jdbctemplate;

/**
 *
 * @author mohit
 */
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("file:beans.xml");

        MovieDao dao = (MovieDao) ctx.getBean("edao");
        

        int status = dao.createMovieTable("CREATE TABLE CATALOGUE(TITLE VARCHAR(256), LEAD_ACTOR VARCHAR(256), LEAD_ACCTRESS VARCHAR(256), TYPE VARCHAR(20))");


        System.out.println(status);
        BufferedReader br = new BufferedReader(new FileReader("catalogue.txt"));

        try {
            while (true) {

                String title = br.readLine();
                if (title == null) {
                    break;
                }
                Movie m = new Movie(title, br.readLine(), br.readLine(), br.readLine());
                dao.insertMovie(m);
                br.readLine();
                // Read the termination line
            }
        } catch (EOFException e) {
        } finally {

            br.close();
        }
        
        System.out.println(dao.selectAllMovies());
        
        

    }
}
