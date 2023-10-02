# azure-functions
Azure functions using Java

This repository has been created for hands-on experience with Azure Functions using Java. Several real-world examples have been developed using Azure Functions. These functions are ready for deployment and have also been tested on the Azure portal.

Examples:
1. Convert image to its thumbnail (ImageThumbnailBlobTrigger.java): Here azure function is triggered whenever images are placed in image-container blob and after resizing into thumbnail, images are pushed to image-output blob container.
2. Analyze images using Computer vision services(AnalyzeImageBlobTriggerCosmosOut.java): This function fetches images from image-container blob and sends it to Azure computer vision services to analyze the content of image like features, categories etc
3. Usage of Durable function (DurableFunctionSample.java): This function contains orchestration and activities to understand implementation of durable functions.
