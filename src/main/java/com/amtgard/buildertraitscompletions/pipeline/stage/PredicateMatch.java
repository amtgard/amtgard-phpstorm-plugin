package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.AbstractPipelineStage;
import com.amtgard.buildertraitscompletions.util.Boolish;

public class PredicateMatch extends AbstractPipelineStage {
    @Override
    public Boolish execute(StageContext context) {
        if (context.getParameters().getPosition().getPrevSibling().textMatches("->")) {
            return Boolish.maybe(context);
        }
        return Boolish.falsey();
    }
}