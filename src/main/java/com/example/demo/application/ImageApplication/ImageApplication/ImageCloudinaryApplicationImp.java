package com.example.demo.application.ImageApplication.ImageApplication;

import com.example.demo.application.ImageApplication.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

@Service
public class ImageCloudinaryApplicationImp implements ImageCloudinaryApplication{
    private final WebClient.Builder webClientBuilder;
    private final WebClient webClient;
    private final String url;
    private final String apiKey;
    private final String apiSecret;

    @Autowired
    public ImageCloudinaryApplicationImp(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        webClient = this.webClientBuilder.build();
        url = "https://api.cloudinary.com/v1_1/"+System.getenv("CloudName")+"/image/upload";
        apiKey = System.getenv("CloudAPIKey");
        apiSecret = System.getenv("CloudAPISecret");
    }    
    public String getSignature(String url){
        String payload_to_sign = "file="+url;
        payload_to_sign = "&public_id="+url;
        payload_to_sign+="&timestamp="+System.currentTimeMillis();

        return  DigestUtils.sha1Hex(payload_to_sign + apiSecret);
    }

    public Mono<ImageDTO> saveImageCloudianary(byte[] image){
    webClient.post()
                .uri(uriBuilder -> uriBuilder
                                .path("https://api.cloudinary.com/v1_1/dci77dznz/image/upload")
                                .queryParam("file",image)
                                .queryParam("public_id",image)
                                .queryParam("timestamp", System.currentTimeMillis())
                                .queryParam("api_key",apiKey)
                                .queryParam("signature","asfsdf")
                                .build()
                ).retrieve();
                
    }
}
 
