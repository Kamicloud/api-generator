package com.kamicloud.generator.writers.components.php;

import com.kamicloud.generator.interfaces.CombinerInterface;

public class ClassAttributeCombiner implements CombinerInterface {
    private ClassCombiner classCombiner;
    private String name;
    private String access;


    public ClassAttributeCombiner(ClassCombiner classCombiner, String name, String access) {
        this.classCombiner = classCombiner;
        this.access = access == null ? "public" : access;
        this.name = name;

        if (!classCombiner.getAttributes().contains(this)) {
            classCombiner.addAttribute(this);
        }
    }

    @Override
    public String toString() {
        return "    " + access + " $" + name + ";\n";
    }

    public ClassCombiner getClassCombiner() {
        return classCombiner;
    }

    @Override
    public boolean equals(Object obj) {
        return ((ClassAttributeCombiner) obj).name.equals(this.name);
    }
}
