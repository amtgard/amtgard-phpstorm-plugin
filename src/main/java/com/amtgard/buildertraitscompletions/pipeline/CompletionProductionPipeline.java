package com.amtgard.buildertraitscompletions.pipeline;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.stage.DocumentPatternMatch;
import com.amtgard.buildertraitscompletions.pipeline.stage.GetCompletionList;
import com.amtgard.buildertraitscompletions.pipeline.stage.IsAmtgardBuilder;
import com.amtgard.buildertraitscompletions.pipeline.stage.PredicateMatch;
import com.amtgard.buildertraitscompletions.pipeline.stage.PruneCompletions;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class CompletionProductionPipeline {
    public static final List<AbstractPipelineStage> pipeline = ImmutableList.of(
        new PredicateMatch(),
        new DocumentPatternMatch(),
        new IsAmtgardBuilder(),
        new GetCompletionList(),
        new PruneCompletions()
    );

    public static List<StageContext.CompletionStrategy> run(StageContext context) {
        Boolish result = Boolish.maybe(context);
        for (AbstractPipelineStage pipeline : pipeline) {
            if (result.truthy()) {
                result = pipeline.execute((StageContext) result.get());
            }
        }
        return (List<StageContext.CompletionStrategy>) result.truthy(c -> ((StageContext)c).getCompletions());
    }
}
