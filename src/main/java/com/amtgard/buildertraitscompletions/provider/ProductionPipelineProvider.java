package com.amtgard.buildertraitscompletions.provider;

import com.amtgard.buildertraitscompletions.pipeline.AbstractPipelineStage;
import com.amtgard.buildertraitscompletions.pipeline.stage.DocumentPatternMatch;
import com.amtgard.buildertraitscompletions.pipeline.stage.GetCompletionList;
import com.amtgard.buildertraitscompletions.pipeline.stage.IsAmtgardBuilder;
import com.amtgard.buildertraitscompletions.pipeline.stage.PredicateMatch;
import com.amtgard.buildertraitscompletions.pipeline.stage.PruneCompletions;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class ProductionPipelineProvider {
    static final List<AbstractPipelineStage> PIPELINE = ImmutableList.of(
        new PredicateMatch(),
        new DocumentPatternMatch(),
        new IsAmtgardBuilder(),
        new GetCompletionList(),
        new PruneCompletions()
    );

    public static List<AbstractPipelineStage> providePipeline() {
        return PIPELINE;
    }
}
