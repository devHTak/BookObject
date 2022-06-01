package com.review.cloudNative.chap06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/customers/{id}/photo")
@Slf4j
public class CustomerProfilePhotoRestController {

    private File root;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerProfilePhotoRestController(@Value("${upload.dir:${user.home}/images}") String uploadDir
            , CustomerRepository customerRepository) {
        this.root = new File(uploadDir);
        this.customerRepository = customerRepository;

        Assert.isTrue(this.root.exists() || this.root.mkdirs(),
                String.format("The path %s must exists.", this.root.getAbsolutePath()));
    }

    @GetMapping
    ResponseEntity<Resource> read(@PathVariable Long id) {
        return this.customerRepository.findById(id)
                .map(customer -> {
                    File file = this.fileFor(customer);
                    Assert.isTrue(file.exists(), String.format("file not found: %s", file.getAbsolutePath()));

                    Resource fileSystemResource = new FileSystemResource(file);
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                            .body(fileSystemResource);
                }).orElseThrow(() -> new CustomerNotFoundException());
    }

    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.POST})
    Callable<ResponseEntity<?>> write(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception{
        return () -> customerRepository.findById(id)
                .map(customer -> {
                    File fileForCustomer = fileFor(customer);
                    try(InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(fileForCustomer)) {
                        FileCopyUtils.copy(in, out);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(id).toUri();
                    return ResponseEntity.created(uri).build();
                }).orElseThrow(() -> new CustomerNotFoundException());
    }

    private File fileFor(Customer customer) {
        return new File(this.root, Long.toString(customer.getId()));
    }
}
