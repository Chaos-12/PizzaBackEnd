package com.example.demo.infraestructure.imageRepository;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import com.example.demo.application.ImageApplication.ImageApplication.ImageCloudinaryRepository;
import com.example.demo.application.ImageApplication.ImageApplication.ImageDTO;
import com.example.demo.core.exceptions.RedisConnectionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@Repository
public class ImageCloudinaryRepositoryImp implements ImageCloudinaryRepository{
   // private final WebClient.Builder webClientBuilder;
    private final WebClient webClient;
    private final Map<String,String> cloudinaryVariables = new HashMap<String,String>();

    @Autowired
    public ImageCloudinaryRepositoryImp(WebClient.Builder webClientBuilder) {
        //this.webClientBuilder = webClientBuilder;
       // webClient = this.webClientBuilder.build();
       // webClient = WebClient.builder().baseUrl("https://api.cloudinary.com/v1_1/"+System.getenv("CloudName")).build();


       
       webClient = WebClient.builder().build();



        cloudinaryVariables.put("url", "https://api.cloudinary.com/v1_1/"+System.getenv("CloudName")+"/image/upload");
        cloudinaryVariables.put("apiKey", System.getenv("CloudAPIKey"));
        cloudinaryVariables.put("secretKey", System.getenv("CloudAPISecret"));
       // cloudinaryVariables.put("upload_preset", "qe991vfo");
    }    
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public String generateSignature(String public_id, long timestamp){
        StringBuilder builder = new StringBuilder();
        builder.append("public_id=");
        builder.append(public_id);
        builder.append("&timestamp=");
        builder.append(timestamp);
        builder.append(cloudinaryVariables.get("secretKey"));

        MessageDigest digest;
        String signature = new String(builder);
        try {
            digest = MessageDigest.getInstance("SHA-256");
           // signature = "public_id=106d32bc-e643-4047-be4b-ad949f632dc3&timestamp=16382031066wIrLobN-s8b6k7qhI26syd_Tc5A";
            byte[] encodedhash = digest.digest(signature.getBytes(StandardCharsets.UTF_8));
            String signatureString = bytesToHex(encodedhash);
            return signatureString; 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Mono<ImageDTO> saveImageCloudianary(ImageDTO image){
	Long timestamp = System.currentTimeMillis() / 100L;
        String signature = generateSignature(image.getId().toString(), timestamp);
       // URI url = UriComponentsBuilder.fromHttpUrl(cloudinaryVariables.get("url")).build().toUri();

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        
        builder.part("file", image.getContent()).filename("file");
        builder.part("api_key", cloudinaryVariables.get("apiKey"));
        builder.part("public_id", image.getId().toString());
        builder.part("timestamp", timestamp);
        builder.part("signature", signature);

        String result = webClient.post()
                        .uri("https://api.cloudinary.com/v1_1/dci77dznz/image/upload")
                	.contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(builder.build()))
                        .exchange()
                        .flatMap(response -> response.bodyToMono(String.class))
                        .flux()
                        .blockFirst();
         System.out.println("RESULT: " + result);
                 
        return Mono.just(image);
                
                /*
                 .onStatus(status -> 
                                status.value() == HttpStatus.METHOD_NOT_ALLOWED.value(), 
                            response -> 
                 .bodyToMono(ImageDTO.class);
                 */
                                               
    }        
}


        /*
        webClient.post()
                    .uri(uriBuilder -> uriBuilder
                                    .path(cloudinaryVariables.get("url"))
                                    .queryParam("file",image.getContent())
                                    .queryParam("upload_preset",cloudinaryVariables.get("upload_preset"))
                                    .queryParam("public_id",image.getId().toString())
                                    .build()
                    ).retrieve();
        */