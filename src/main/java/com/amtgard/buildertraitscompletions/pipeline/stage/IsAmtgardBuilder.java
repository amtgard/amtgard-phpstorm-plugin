package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.AbstractPipelineStage;
import com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil;
import com.amtgard.buildertraitscompletions.util.Boolish;

public class IsAmtgardBuilder extends AbstractPipelineStage {
    @Override
    public Boolish execute(StageContext context) {
        if (AmtgardBuilderUtil.hasAmtgardBuilderGetterTrait(context.getProject(), context.getFqnString().getFqn())) {
            return Boolish.maybe(context);
        }
        return Boolish.falsey();
    }
}