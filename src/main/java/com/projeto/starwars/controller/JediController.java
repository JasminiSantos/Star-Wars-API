package com.projeto.starwars.controller;

import com.projeto.starwars.model.Jedi;
import com.projeto.starwars.service.JediService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/jedis")
@CrossOrigin("*")
public class JediController {
    private static final Logger logger = LogManager.getLogger(JediService.class);

    private final JediService jediService;

    public JediController(JediService jediService) {
        this.jediService = jediService;
    }

    @GetMapping("/jedi")
    public ResponseEntity<List<Jedi>> getJedis(){
        List<Jedi> jedis = jediService.findAll();
        return ResponseEntity.ok().body(jedis);
    }

    @GetMapping("/jedi/{id}")
    public ResponseEntity<?> getJedi(@PathVariable int id){
        return jediService.findById(id)
                .map(jedi -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(jedi.getVersion()))
                                .location(new URI("/jedi/"+ jedi.getId()))
                                .body(jedi);
                    }catch (URISyntaxException e){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/jedi")
    public ResponseEntity<Jedi> saveJedi(@RequestBody Jedi jedi){
        Jedi newJedi = jediService.save(jedi);

        try {
            return ResponseEntity
                    .created(new URI("/jedi/"+ newJedi.getId()))
                    .eTag(Integer.toString(jedi.getVersion()))
                    .body(newJedi);
        }catch (URISyntaxException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/jedi/{id}")
    public ResponseEntity<Void> deleteJedi(@PathVariable int id){
        jediService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/jedi")
    public ResponseEntity<Jedi> updateJedi(@RequestBody Jedi jedi){
        jediService.update(jedi);
        return ResponseEntity.ok().body(jedi);
    }
}
