package com.alexis.itemservice.controller;

import com.alexis.commons.models.entity.Product;
import com.alexis.itemservice.models.Item;
import com.alexis.itemservice.service.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private Environment env;

    @Autowired
    private CircuitBreakerFactory cbFactory;

    @Autowired
    @Qualifier("serviceFeign")
    //@Qualifier("serviceRestTemplate")
    private ItemService itemService;

    @Value("${configuracion.texto}")
    private String texto;

    @GetMapping("/listar")
    public ResponseEntity<List<Item>> getAll(@RequestParam(name = "nombre", required = false) String nombre,
                                             @RequestHeader(name = "token-request", required = false) String token){

        System.out.println(nombre);
        System.out.println(token);
        List<Item> items = itemService.getAll();
        if(items.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/ver/{id}/cantidad/{quantity}")
    public Item getById(@PathVariable Integer id,@PathVariable Integer quantity){
        return cbFactory.create("items")
                .run( ()-> itemService.findById(id, quantity),
                        e -> metodoAlternativo(id, quantity, e));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
    @GetMapping("/ver2/{id}/cantidad/{quantity}")
    public Item detalle(@PathVariable Integer id,@PathVariable Integer quantity){
        return itemService.findById(id, quantity);
    }

    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2")
    @TimeLimiter(name = "items")
    @GetMapping("/ver3/{id}/cantidad/{quantity}")
    public CompletableFuture<Item> detalle2(@PathVariable Integer id, @PathVariable Integer quantity){
        return CompletableFuture.supplyAsync( ()-> itemService.findById(id, quantity));
    }

    public Item metodoAlternativo(Integer id, Integer quantity, Throwable e){

        logger.info(e.getMessage());

        Item item = new Item();
        Product product = new Product();

        item.setQuantity(quantity);
        product.setId(id);
        product.setName("Camara Sony");
        product.setPrice(500.0);
        item.setProduct(product);
        return item;
    }

    public CompletableFuture<Item> metodoAlternativo2(Integer id, Integer quantity, Throwable e){

        logger.info(e.getMessage());

        Item item = new Item();
        Product product = new Product();

        item.setQuantity(quantity);
        product.setId(id);
        product.setName("Cama Palomo");
        product.setPrice(500.0);
        item.setProduct(product);
        return CompletableFuture.supplyAsync( ()-> item);
    }

    @GetMapping("obtener-config")
    public ResponseEntity<?> obtenerConfiguracion(@Value("${server.port}") String puerto){

        logger.info(texto);

        Map<String, String> json= new HashMap<>();
        json.put("texto", texto);
        json.put("puerto", puerto);

        if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")){
            json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
            json.put("autor.email", env.getProperty("configuracion.autor.email"));
        }

        return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
    }


    @PostMapping("/crear")
    public Product crear(@RequestBody Product product){
        return itemService.save(product);
    }

    @PutMapping("/editar/{id}")
    public Product editar(@RequestBody Product product, @PathVariable Integer id){
        return itemService.update(product,id);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Integer id){
        itemService.delete(id);
    }
}
