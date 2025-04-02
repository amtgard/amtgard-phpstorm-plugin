package com.amtgard.buildertraitscompletions.util;

import com.amtgard.buildertraitscompletions.model.FqnString;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AmtgardBuilderUtil {
    public static final String BUILDER_TRAIT_ROOT = "\\Amtgard\\Traits\\Builder\\";
    public static final String BUILDER_TRAIT = "Builder";
    public static final String GETTER_TRAIT = "Getter";
    public static final String SETTER_TRAIT = "Setter";
    public static final String TO_BUILDER_TRAIT = "ToBuilder";

    private static final List<String> TRAITS = ImmutableList.of(BUILDER_TRAIT, GETTER_TRAIT, SETTER_TRAIT, TO_BUILDER_TRAIT);

    public static boolean hasAmtgardBuilderTrait(@NotNull Project project,
                                                 String fqnClassName,
                                                 String amtgardBuilderTrait) {
        Collection<PhpClass> classes = getClassesForFqn(project, fqnClassName);
        if (classes.size() == 1) {
            return Arrays.stream(classes.stream().findFirst().get().getTraitNames()).filter(t -> t.equals(BUILDER_TRAIT_ROOT + amtgardBuilderTrait)).count() == 1;
        }
        return false;
    }

    public static boolean hasAmtgardBuilderTrait(@NotNull Project project, String fqClassName) {
        for (String trait: TRAITS) {
            if (hasAmtgardBuilderTrait(project, fqClassName, trait)) return true;
        }
        return false;
    }

    public static boolean hasAmtgardBuilderBuilderTrait(@NotNull Project project, String fqClassName) {
        return hasAmtgardBuilderTrait(project, fqClassName, "Builder");
    }

    public static boolean hasAmtgardBuilderGetterTrait(@NotNull Project project, String fqClassName) {
        return hasAmtgardBuilderTrait(project, fqClassName, "Getter");
    }

    public static boolean hasAmtgardBuilderSetterTrait(@NotNull Project project, String fqClassName) {
        return hasAmtgardBuilderTrait(project, fqClassName, "Setter");
    }

    public static boolean hasAmtgardBuilderToBuilderTrait(@NotNull Project project, String fqClassName) {
        return hasAmtgardBuilderTrait(project, fqClassName, "ToBuilder");
    }

    static Collection<PhpClass> getClassesForFqn(@NotNull Project project, FqnString fqnString) {
        return PhpIndex.getInstance(project).getClassesByFQN(fqnString.getFqn());
    }

    static Collection<PhpClass> getClassesForFqn(@NotNull Project project, String fqnClassName) {
        return PhpIndex.getInstance(project).getClassesByFQN(fqnClassName);
    }

    public static List<String> getClassFieldList(@NotNull Project project, FqnString fqnString) {
        List<String> builderTraitCompletions = new ArrayList<>();
        for (PhpClass phpClass : getClassesForFqn(project, fqnString)) {
            for (Field field: phpClass.getFields()) {
                builderTraitCompletions.add(field.getName());
            }
        }
        return builderTraitCompletions;
    }


}
