package ro.unibuc.hello.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.unibuc.hello.data.ListingRepository;
import ro.unibuc.hello.data.ProductRepository;
import ro.unibuc.hello.dto.Listing;
import ro.unibuc.hello.dto.Product;

import java.util.List;

@Controller
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private ProductRepository productRepository;

    @Timed(value = "post.listing.time", description = "Time taken to post a listing")
    @PostMapping("/post_listing")
    @ResponseBody
    public Listing postListing(@RequestBody Listing listing){
        listingRepository.save(listing);
        return listing;
    }

    @PostMapping("/bid_for_listing")
    @ResponseBody
    public Listing bidForListing(@RequestBody Listing listing, @RequestBody int price){
        if(price <= listing.currentPrice)
            System.out.println("Invalid price");
        else
            listing.setCurrentPrice(price);
        listingRepository.save(listing);
        return listing;
    }

    @Timed(value = "product.register.time", description = "Time taken to register product")
    @Counted(value = "produnct.register.counter", description = "Times a product has been registered")
    @PostMapping("/register_product")
    @ResponseBody
    public Product registerProduct(@RequestBody Product product){
        productRepository.save(product);
        return product;
    }

    @Timed(value = "product.return.time", description = "Time taken to return products")
    @GetMapping("/products")
    @ResponseBody
    public List<Product> getAllProducts(){return productRepository.findAll();}

    @Timed(value = "listing.return.time", description = "Time taken to return listings")
    @GetMapping("/listings")
    @ResponseBody
    public List<Listing> getAllListings(){
        return listingRepository.findAll();
    }
}
