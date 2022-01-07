package com.nortal.mega.service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Building {

    @Getter @Setter private Long id;
    @Getter @Setter private String name;
    @Getter @Setter private String address;
    @Getter @Setter private String index;
    @Getter @Setter private String sectorCode;
    @Getter @Setter private Integer energyUnits;
    @Getter @Setter private Integer energyUnitMax;


    public boolean isValid() {
        if (!this.index.startsWith("NO")) {
            return false;
        }
        return energyUnitMax >= energyUnits;
    }
}
