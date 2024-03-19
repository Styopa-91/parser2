package com.parser.parser;

import java.util.List;

public class Oblast {
    String oblast;

    List<Region> regions;

    public Oblast(String oblast, List<Region> regions) {
        this.oblast = oblast;
        this.regions = regions;
    }

    public void setOblast(String oblast) {
        this.oblast = oblast;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public String getOblast() {
        return oblast;
    }

    public List<Region> getRegions() {
        return regions;
    }
}
