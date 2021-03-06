package com.projeto.starwars.service;

import com.projeto.starwars.model.Jedi;
import com.projeto.starwars.repository.JediRepositoryImpl;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class JediService {
    private static final Logger logger = LogManager.getLogger(JediService.class);

    private final JediRepositoryImpl jediRepositoryImpl;

    public JediService(JediRepositoryImpl jediRepositoryImpl) {
        this.jediRepositoryImpl = jediRepositoryImpl;
    }

    public Optional<Jedi> findById(int id){
        logger.info("Find Jedi with id: {}", id);

        return jediRepositoryImpl.findById(id);
    }

    public List<Jedi> findAll(){
        logger.info("Bring all the Jedis from the Galaxy");

        return jediRepositoryImpl.findAll();
    }

    public Jedi save(Jedi jedi){
        jedi.setVersion(1);

        logger.info("Update Jedi from system");

        return jediRepositoryImpl.save(jedi);
    }

    public boolean update(Jedi jedi){
        boolean updated = jediRepositoryImpl.update(jedi);

        return updated;
    }

    public boolean delete(int id){
        return jediRepositoryImpl.delete(id);
    }
}
