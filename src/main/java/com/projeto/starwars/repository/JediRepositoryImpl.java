package com.projeto.starwars.repository;

import com.projeto.starwars.model.Jedi;
import com.projeto.starwars.service.JediService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JediRepositoryImpl implements JediRepository{
    private static final Logger logger = LogManager.getLogger(JediService.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JediRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource datasource){
        this.jdbcTemplate = jdbcTemplate;

        this.simpleJdbcInsert = new SimpleJdbcInsert(datasource)
                .withTableName("jedis")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Jedi> findById(int id) {
        try {
            Jedi jedi = jdbcTemplate.queryForObject("SELECT * FROM jedis WHERE id = ?",
                    new Object[]{id},
                    (rs, rowNumber) ->{
                        Jedi p = new Jedi();
                        p.setId(rs.getInt("id"));
                        p.setName(rs.getString("name"));
                        p.setStrength(rs.getInt("strength"));
                        p.setVersion(rs.getInt("version"));
                        return p;
                    });
            return Optional.of(jedi);
        } catch (EmptyResultDataAccessException e){
            return  Optional.empty();
        }
    }

    @Override
    public List<Jedi> findAll() {
        return jdbcTemplate.query("SELECT * FROM jedis",
                (rs, rowNumber) ->{
                    Jedi jedi = new Jedi();
                    jedi.setId(rs.getInt("id"));
                    jedi.setName(rs.getString("name"));
                    jedi.setStrength(rs.getInt("strength"));
                    jedi.setVersion(rs.getInt("version"));
                    return jedi;
                });
    }

    @Override
    public boolean update(Jedi jedi) {
        return jdbcTemplate.update("UPDATE jedis SET name = ?, strength = ?, version = ? WHERE id = ?",
                jedi.getName(),
                jedi.getStrength(),
                jedi.getVersion(),
                jedi.getId()) == 1;
    }


    @Override
    public Jedi save(Jedi jedi) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", jedi.getName());
        parameters.put("strength", jedi.getStrength());
        parameters.put("version", jedi.getVersion());

        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        logger.info("Inserting Jedi into database, generated id is: {}", newId);

        jedi.setId((int) newId);
        return jedi;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM jedis where id = ?", id) == 1;
    }
}
