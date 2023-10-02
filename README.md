# azure-functions
Azure functions using Java

This repository is created to have hands on for Azure functions.  Some real world examples are developed using azure functions.
These functions are ready to deploy and also tested on azure portal.

Examples:
1. Convert image to its thumbnail (ImageThumbnailBlobTrigger.java): Here azure function is triggered whenever images are placed in image-container blob and after resizing into thumbnail, images are pushed to image-output blob container.
2. Analyze images using Computer vision services(AnalyzeImageBlobTriggerCosmosOut.java): This function fetches images from image-container blob and sends it to Azure computer vision services to analyze the content of image like features, categories etc
3. Usage of Durable function (DurableFunctionSample.java): This function contains orchestration and activities to understand implementation of durable functions.
