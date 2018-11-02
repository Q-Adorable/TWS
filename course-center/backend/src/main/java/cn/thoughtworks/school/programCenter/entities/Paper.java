package cn.thoughtworks.school.programCenter.entities;

import java.util.HashMap;
import java.util.List;

public class Paper {
    private List<Object> sections;
    private String paperId;
    private HashMap currentSection;
    private HashMap program;

    public List<Object> getSections() {
        return sections;
    }

    public void setSections(List<Object> sections) {
        this.sections = sections;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public HashMap getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(HashMap currentSection) {
        this.currentSection = currentSection;
    }

    public HashMap getProgram() {
        return program;
    }

    public void setProgram(HashMap program) {
        this.program = program;
    }
}
