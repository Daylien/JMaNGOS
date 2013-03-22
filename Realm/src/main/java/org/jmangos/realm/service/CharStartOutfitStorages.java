/*******************************************************************************
 * Copyright (C) 2012 JMaNGOS <http://jmangos.org/>
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.jmangos.realm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jmangos.commons.dataholder.DataLoadService;
import org.jmangos.commons.entities.CharStartOutfitEntity;
import org.jmangos.commons.entities.pk.CharStartOutfitEntityPk;
import org.jmangos.commons.enums.Classes;
import org.jmangos.commons.enums.Gender;
import org.jmangos.commons.enums.Races;
import org.jmangos.world.services.CharStartOutfitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Class CharStartOutfitStorages.
 */
@Component
public class CharStartOutfitStorages
        implements DataLoadService<Map<CharStartOutfitEntityPk, CharStartOutfitEntity>> {

    /** The Constant log. */
    private static final Logger logger = LoggerFactory.getLogger(CharStartOutfitStorages.class);

    @Autowired
    CharStartOutfitService charStartOutfitService;

    /** The Player. */
    private Map<CharStartOutfitEntityPk, CharStartOutfitEntity> playerCSO =
            new HashMap<CharStartOutfitEntityPk, CharStartOutfitEntity>();

    /**
     * 
     * @see org.jmangos.commons.service.Service#start()
     */
    @PostConstruct
    @Override
    public void start() {

        // TEMPORARY NOT USED
        // load();
        logger.info("Loaded {} CharStartOutfits", this.playerCSO.size());
    }

    /**
     * 
     * @see org.jmangos.commons.service.Service#stop()
     */
    @Override
    public void stop() {

        // TODO Auto-generated method stub

    }

    /**
     * 
     * @see org.jmangos.commons.dataholder.DataLoadService#load()
     */
    @Override
    public Map<CharStartOutfitEntityPk, CharStartOutfitEntity> load() {

        final Map<CharStartOutfitEntityPk, CharStartOutfitEntity> map =
                new HashMap<CharStartOutfitEntityPk, CharStartOutfitEntity>();
        final List<CharStartOutfitEntity> infoList =
                this.charStartOutfitService.readCharStartOutfitEntities();
        for (final CharStartOutfitEntity сharStartOutfit : infoList) {
            map.put(сharStartOutfit.getCharStartOutfitEntityPk(), сharStartOutfit);
        }
        return this.playerCSO = map;
    }

    /**
     * 
     * @see org.jmangos.commons.dataholder.DataLoadService#reload()
     */
    @Override
    public void reload() {

        Map<CharStartOutfitEntityPk, CharStartOutfitEntity> tempPlayerCSO = load();
        this.playerCSO = tempPlayerCSO;
        tempPlayerCSO = null;
    }

    /**
     * 
     * @see org.jmangos.commons.dataholder.DataLoadService#save()
     */
    @Override
    public void save() {

        // TODO Auto-generated method stub

    }

    /**
     * 
     * @param clazz
     * @param race
     * @param gender
     * @return charStartOutfitEntity
     */
    public CharStartOutfitEntity get(final Classes clazz, final Races race, final Gender gender) {

        final CharStartOutfitEntityPk cl = new CharStartOutfitEntityPk();
        cl.setClazz(clazz);
        cl.setRace(race);
        cl.setGender(gender);
        if (this.playerCSO.containsKey(cl)) {
            return this.playerCSO.get(cl);
        } else {
            return null;
        }
    }

    /**
     * 
     * @see org.jmangos.commons.dataholder.DataLoadService#get()
     */
    @Override
    public Map<CharStartOutfitEntityPk, CharStartOutfitEntity> get() {

        // TODO Auto-generated method stub
        return null;
    }

}
