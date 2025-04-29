package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.pipeline.AbstractPipelineStage;
import com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;

public class GetCompletionList extends AbstractPipelineStage {
    static final String BUILD = "build";
    public static final String METHOD_BRACES = "()";
    static final String GETTER_PREFIX = "get";
    static final String SETTER_PREFIX = "set";

    @Override
    public Boolish execute(StageContext context) {
        appendBuilderCompletions(context);
        appendGetterCompletions(context);
        appendSetterCompletions(context);
        return Boolish.maybe(context);
    }

    @VisibleForTesting
    protected void appendBuilderCompletions(StageContext context) {
        if (context.getMode().equals(BuilderMode.BUILDER_MODE)) {
            if (AmtgardBuilderUtil.hasAmtgardBuilderTrait(context.getProject(), context.getFqnString().getFqn())) {
                for (String builderTrait : AmtgardBuilderUtil.getClassFieldList(context.getProject(), context.getFqnString())) {
                    context.getCompletions().add(StageContext.CompletionStrategy.build(builderTrait, METHOD_BRACES));
                }
                context.getCompletions().add(StageContext.CompletionStrategy.build(BUILD, METHOD_BRACES + ";"));
            }
        }
    }

    @VisibleForTesting
    protected void appendGetterCompletions(StageContext context) {
        if (context.getMode().equals(BuilderMode.GETTER_SETTER_MODE)) {
            if (AmtgardBuilderUtil.hasAmtgardBuilderGetterTrait(context.getProject(), context.getFqnString().getFqn())) {
                for (String builderTrait : AmtgardBuilderUtil.getClassFieldList(context.getProject(), context.getFqnString())) {
                    context.getCompletions().add(StageContext.CompletionStrategy.build(GETTER_PREFIX + StringUtils.capitalize(builderTrait), METHOD_BRACES));
                }
            }
        }
    }

    @VisibleForTesting
    protected void appendSetterCompletions(StageContext context) {
        if (context.getMode().equals(BuilderMode.GETTER_SETTER_MODE)) {
            if (AmtgardBuilderUtil.hasAmtgardBuilderSetterTrait(context.getProject(), context.getFqnString().getFqn())) {
                for (String builderTrait : AmtgardBuilderUtil.getClassFieldList(context.getProject(), context.getFqnString())) {
                    context.getCompletions().add(StageContext.CompletionStrategy.build(SETTER_PREFIX + StringUtils.capitalize(builderTrait), METHOD_BRACES));
                }
            }
        }
    }

}