package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.*;

import com.microsoft.durabletask.*;
import com.microsoft.durabletask.azurefunctions.DurableActivityTrigger;
import com.microsoft.durabletask.azurefunctions.DurableClientContext;
import com.microsoft.durabletask.azurefunctions.DurableClientInput;
import com.microsoft.durabletask.azurefunctions.DurableOrchestrationTrigger;

public class DurableFunctionSample {

    /**
     * This HTTP-triggered function starts the orchestration.
     */
    @FunctionName("StartOrchestration")
    public HttpResponseMessage startOrchestration(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @DurableClientInput(name = "durableContext") DurableClientContext durableContext,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        DurableTaskClient client = durableContext.getClient();
        String instanceId = client.scheduleNewOrchestrationInstance("Cities");
        context.getLogger().info("Created new Java orchestration with instance ID = " + instanceId);

        String instanceQuestionOrchId = client.scheduleNewOrchestrationInstance("QuestionOrch");
        context.getLogger().info("Created new Java orchestration with instance ID = " + instanceQuestionOrchId);
        return durableContext.createCheckStatusResponse(request, instanceId);
    }
    
    /**
     * This is the orchestrator function, which can schedule activity functions, create durable timers,
     * or wait for external events in a way that's completely fault-tolerant.
     */
    @FunctionName("Cities")
    public String citiesOrchestrator(
            @DurableOrchestrationTrigger(name = "taskOrchestrationContext") TaskOrchestrationContext ctx) {
        String result = "";
        result += ctx.callActivity("Capitalize", "Tokyo", String.class).await() + ", ";
        result += ctx.callActivity("Capitalize", "London", String.class).await() + ", ";
        result += ctx.callActivity("Capitalize", "Seattle", String.class).await() + ", ";
        result += ctx.callActivity("Capitalize", "Austin", String.class).await()+ ", ";
        result += ctx.callActivity("Question", "How are you?", String.class).await()+ ", ";
        result += ctx.callActivity("Question", "What is your name?", String.class).await()+ ", ";
        result += ctx.callActivity("Question", "How much do I owe you?", String.class).await();
        return result;
    }

     @FunctionName("QuestionOrch")
    public String questionOrchestrator(
            @DurableOrchestrationTrigger(name = "taskOrchestrationContext") TaskOrchestrationContext ctx) {
        String result = "";
        result += ctx.callActivity("Question", "How are you?", String.class).await()+ ", ";
        result += ctx.callActivity("Question", "What is your name?", String.class).await()+ ", ";
        result += ctx.callActivity("Question", "How much do I owe you?", String.class).await();
        return result;
    }

    /**
     * This is the activity function that gets invoked by the orchestrator function.
     */
    @FunctionName("Capitalize")
    public String capitalize(@DurableActivityTrigger(name = "name") String name, final ExecutionContext context) {
        context.getLogger().info("Capitalizing: " + name);
        return name.toUpperCase();
    }

    /**
     * This is the activity function that gets invoked by the orchestrator function.
     */
    @FunctionName("Question")
    public String question(@DurableActivityTrigger(name = "questiontext") String questionText, final ExecutionContext context) {
        context.getLogger().info("Question: " + questionText);
        return "Hello " + questionText;
    }

}
