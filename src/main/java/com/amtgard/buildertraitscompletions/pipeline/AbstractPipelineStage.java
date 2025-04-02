package com.amtgard.buildertraitscompletions.pipeline;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.util.Boolish;

public abstract class AbstractPipelineStage {
    public Boolish execute(StageContext context) {
        return Boolish.maybe(context);
    }
}
