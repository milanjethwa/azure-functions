package com.function;

import com.microsoft.azure.functions.annotation.*;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Azure Blob trigger.
 */
public class AnalyzeImageBlobTriggerCosmosOut {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     * @throws IOException
     */
    @FunctionName("AnalyzeImageBlobTriggerCosmosOut")
    @StorageAccount("azuretutorialsjava29e89_STORAGE")
    public void run(
        @BlobTrigger(name = "content", path = "image-container/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        final ExecutionContext context
    ) throws IOException {
        context.getLogger().info("Java Blob trigger function processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
        
        String endpoint="https://cvservice1.cognitiveservices.azure.com/computervision/imageanalysis:analyze?api-version=2023-02-01-preview&features=Tags";
        String apiKey="";

        OkHttpClient client = new OkHttpClient();

        // Create the URL for the Computer Vision API
        HttpUrl apiUrl = HttpUrl.parse(endpoint)
            .newBuilder()
            .build();

        // Create the request body with the image data
        RequestBody requestBody = RequestBody.create(content,MediaType.parse("application/octet-stream"));

        // Create the HTTP request
        Request request = new Request.Builder()
            .url(apiUrl)
            .addHeader("Ocp-Apim-Subscription-Key", apiKey)
            .post(requestBody)
            .build();

        // Send the HTTP request
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Read and process the response data here
                String responseBody = response.body().string();
                context.getLogger().info(responseBody);
            } else {
                // Handle the error response
                String errorBody = response.body().string();
                context.getLogger().info(errorBody);
            }
        }
    }
}
