package com.function;

import com.microsoft.azure.functions.annotation.*;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.tasks.io.BufferedImageSink;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.microsoft.azure.functions.*;


/**
 * Azure Functions with Azure Blob trigger.
 */
public class ImageThumbnailBlobTrigger {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     * @throws IOException
     */
    @FunctionName("ImageThumbnailBlobTrigger")
    @StorageAccount("azuretutorialsjava29e89_STORAGE")
    public void run(
        @BlobTrigger(name = "content", path = "image-container/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        @BlobOutput(name = "target", path = "image-output/{name}")OutputBinding<byte[] > outputItem,
        final ExecutionContext context
    ) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content);

            // Create a thumbnail
            ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
            Thumbnails.of(inputStream)
                .size(100, 100) // Specify the thumbnail size
                .outputFormat("jpg") // Specify the output format
                .toOutputStream(thumbnailStream);

            byte[] thumbnailBytes = thumbnailStream.toByteArray();

            // Store the thumbnail in the output blob
            outputItem.setValue(thumbnailBytes);
        
        context.getLogger().info("Java Blob trigger function processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
    }
}
