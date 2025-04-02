package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.chainmatcher.IMatcherChainLink;
import com.amtgard.buildertraitscompletions.chainmatcher.matcher.GetterSetterPattern;
import com.amtgard.buildertraitscompletions.chainmatcher.matcher.StaticBuilderPattern;
import com.amtgard.buildertraitscompletions.chainmatcher.matcher.ToBuilderPattern;
import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.AbstractPipelineStage;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class DocumentPatternMatch extends AbstractPipelineStage {

    static List<IMatcherChainLink> matcherChain = ImmutableList.of(
        new StaticBuilderPattern(),
        new ToBuilderPattern(),
        new GetterSetterPattern()
    );

    @Override
    public Boolish execute(StageContext context) {
        for (IMatcherChainLink link : matcherChain) {
            try {
                Boolish<FqnString> match = link.match(context.getParameters());
                if (match.truthy()) {
                    context.setFqnString(match.get());
                    context.setMode(link.getBuilderMode());
                    return Boolish.maybe(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Boolish.falsey();
    }
}
