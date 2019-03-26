/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.springbasics.jdbctemplate;

/**
 *
 * @author mohit
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class MovieDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertMovie(Movie m) {
        String query = "INSERT INTO CATALOGUE VALUES('" + m.getTitle() + "','"
                + m.getLeadActor() + "','" + m.getLeadActress() + "','" + m.getType()
                + "')";
        return jdbcTemplate.update(query);
    }

    public int deleteMovie(Movie m) {
        String query = "delete from CATALOGUE where id='" + m.getId() + "' ";
        return jdbcTemplate.update(query);

    }

    public int createMovieTable(String ddl) {
        System.out.println("MovieDao.createMovieTable():");
        return jdbcTemplate.update(ddl);
    }

    public List<Movie> selectAllMovies() {
        System.out.println("MovieDao.selectAllMovies():");
        String SQL = "select * from CATALOGUE";
        List<Movie> ms = jdbcTemplate.query(SQL,
                new MovieMapper());
        return ms;

    }

    public class MovieMapper implements RowMapper<Movie> {

        public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
            Movie m = new Movie();
            
            m.setTitle(rs.getString("TITLE"));
            m.setLeadActor(rs.getString("LEAD_ACTOR"));
            m.setLeadActress(rs.getString("LEAD_ACCTRESS"));
            m.setType(rs.getString("TYPE"));

            return m;
        }
    }
}
