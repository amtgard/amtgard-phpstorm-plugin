package com.amtgard.buildertraitscompletions.pipeline;

import com.amtgard.buildertraitscompletions.provider.ProductionPipelineProvider;
import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.util.Boolish;

import java.util.List;

public class CompletionProductionPipeline {
    public static List<StageContext.CompletionStrategy> run(StageContext context) {
        Boolish result = Boolish.maybe(context);
        for (AbstractPipelineStage pipeline : ProductionPipelineProvider.providePipeline()) {
            if (result.truthy()) {
                result = pipeline.execute((StageContext) result.get());
            }
        }
        return (List<StageContext.CompletionStrategy>) result.truthy(c -> ((StageContext)c).getCompletions());
    }
}
